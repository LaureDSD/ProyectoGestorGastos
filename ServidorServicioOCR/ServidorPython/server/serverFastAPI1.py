"""
Servicio OCR GesThor
====================

Este módulo implementa unha API REST utilizando FastAPI para ofrecer servizos de OCR,
procesando arquivos de entrada (PDFs ou imaxes) e extraendo texto mediante Tesseract OCR.

Tecnoloxías empregadas:
- **FastAPI**: Framework para crear APIs REST de alto rendemento.
- **Tesseract OCR**: Extrae texto de imaxes de tickets e facturas.
- **pdf2image**: Converte arquivos PDF a imaxes para o procesamento OCR.
- **OpenCV**: Mellora e procesa imaxes para optimizar a extracción de datos.
- **NumPy**: Facilita o procesado de datos numéricos e imaxes.
- **re**: Identifica e valida patróns de texto extraídos.
- **Sphinx**: Documenta a API REST a partir dos docstrings (para xeración automática de documentación).
"""

from fastapi import FastAPI, UploadFile, Form, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from typing import Optional, List, Dict
import pytesseract
import pdf2image
import cv2
import numpy as np
import re
from datetime import datetime
import uvicorn

# Configuracións globais
pytesseract.pytesseract.tesseract_cmd = r"C:\Program Files\Tesseract-OCR\tesseract.exe"
IDIOMA_OCR = "spa"
IVA_POR_DEFECTO = 0.21

app = FastAPI(title="Servicio OCR GesThor")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_methods=["*"],
    allow_headers=["*"],
)

class FacturaResponse(BaseModel):
    """
    Modelo de resposta para o procesamento de facturas.

    :param proveedor: Nome do proveedor.
    :param nif: Número de identificación fiscal (opcional).
    :param fecha: Data da factura.
    :param numero: Número da factura.
    :param base: Valor base da factura.
    :param iva: Valor do IVA aplicado.
    :param total: Total da factura.
    :param lineas: Lista de liñas (detalles) da factura.
    :param confianza: Nivel de confianza na extracción do texto.
    """
    proveedor: str
    nif: Optional[str]
    fecha: str
    numero: str
    base: float
    iva: float
    total: float
    lineas: List[Dict[str, str]]
    confianza: float

class TicketResponse(BaseModel):
    """
    Modelo de resposta para o procesamento de tickets.

    :param establecimiento: Nome do establecemento.
    :param fecha: Data do ticket (opcional).
    :param hora: Hora do ticket (opcional).
    :param total: Total do ticket.
    :param articulos: Lista de articulos do ticket.
    :param confianza: Nivel de confianza na extracción do texto.
    """
    establecimiento: str
    fecha: Optional[str]
    hora: Optional[str]
    total: float
    articulos: List[Dict[str, str]]
    confianza: float

async def extraer_texto(contenido: bytes, tipo: str, idioma: str = IDIOMA_OCR) -> str:
    """
    Extrae texto dun arquivo utilizando OCR.

    :param contenido: Contido do arquivo en bytes.
    :param tipo: Tipo MIME do arquivo (ex.: 'application/pdf' ou outro tipo de imaxe).
    :param idioma: Idioma para o OCR (por defecto "spa").
    :return: Texto extraído do arquivo.
    :raises HTTPException: Se a imaxe non pode ser decodificada.
    """
    if tipo == 'application/pdf':
        imagenes = pdf2image.convert_from_bytes(contenido)
        return "\n".join(pytesseract.image_to_string(img, lang=idioma) for img in imagenes)
    else:
        img = cv2.imdecode(np.frombuffer(contenido, np.uint8), cv2.IMREAD_COLOR)
        if img is None:
            raise HTTPException(400, "Imagen no válida")
        gris = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
        _, thresh = cv2.threshold(gris, 0, 255, cv2.THRESH_BINARY + cv2.THRESH_OTSU)
        return pytesseract.image_to_string(thresh, lang=idioma)

def procesar_factura(texto: str, iva: float = IVA_POR_DEFECTO) -> Optional[Dict]:
    """
    Procesa o texto dunha factura para extraer datos básicos.

    :param texto: Texto extraído do documento.
    :param iva: Porcentaxe de IVA a aplicar (por defecto 0.21).
    :return: Diccionario cos datos da factura ou None se non se detecta o total.
    """
    total_match = re.search(r"(?i)total\s*[:=]?\s*(\d+,\d{2})", texto)
    if not total_match:
        return None

    total = float(total_match.group(1).replace(',', '.'))
    iva_val = total * iva
    base = total - iva_val

    # Buscar os artículos
    articulos = []
    articulo_match = re.findall(r"(\d+)\s*([\w\s]+)\s*(\d+,\d{2})", texto)
    for match in articulo_match:
        articulos.append({
            "nombre": match[1].strip(),
            "categoria": ["desconocida"]  # Aquí podrías afinar con categorías específicas si hay patrones.
        })

    return {
        "proveedor": "Proveedor no identificado",
        "nif": None,
        "fecha": datetime.now().strftime("%d/%m/%Y"),
        "numero": "N/A",
        "base": round(base, 2),
        "iva": round(iva_val, 2),
        "total": round(total, 2),
        "lineas": articulos,
        "confianza": 0.8
    }

def procesar_ticket(texto: str) -> Optional[Dict]:
    """
    Procesa o texto dun ticket para extraer datos básicos.

    :param texto: Texto extraído do documento.
    :return: Diccionario cos datos do ticket ou None se non se detecta o total.
    """
    total_match = re.search(r"(?i)total\s*[:=]?\s*(\d+,\d{2})", texto)
    if not total_match:
        return None

    # Buscar artículos
    articulos = []
    articulo_match = re.findall(r"(\d+)\s*([\w\s]+)\s*(\d+,\d{2})", texto)
    for match in articulo_match:
        articulos.append({
            "nombre": match[1].strip(),
            "categoria": ["desconocida"]
        })

    return {
        "establecimiento": "Establecimiento no identificado",
        "fecha": None,
        "hora": None,
        "total": float(total_match.group(1).replace(',', '.')),
        "articulos": articulos,
        "confianza": 0.8
    }

@app.post("/procesar")
async def procesar(
    archivo: UploadFile,
    es_factura: bool = Form(False)
):
    """
    Endpoint para procesar un arquivo cargado (factura ou ticket), extraer o texto mediante OCR
    e retornar os datos procesados.

    :param archivo: Arquivo cargado polo usuario.
    :param es_factura: Indica se o archivo é unha factura (True) ou un ticket (False).
    :return: Diccionario con o resultado da operación e os datos extraídos.
    :raises HTTPException: Se ocorre un erro durante o proceso ou se o documento non é válido.
    """
    try:
        contenido = await archivo.read()
        texto = await extraer_texto(contenido, archivo.content_type)
        
        if es_factura:
            datos = procesar_factura(texto)
            if not datos:
                raise HTTPException(400, "Non es unha factura válida")
            respuesta = FacturaResponse(**datos)
        else:
            datos = procesar_ticket(texto)
            if not datos:
                raise HTTPException(400, "Non es un ticket válido")
            respuesta = TicketResponse(**datos)
        
        return {
            "exito": True,
            "datos": respuesta
        }
        
    except Exception as e:
        raise HTTPException(500, f"Error: {str(e)}")

if __name__ == "__main__":
    """
    Execución principal do servidor FastAPI.
    """
    puerto = 5000
    host = "localhost"
    uvicorn.run(app, host=host, port=puerto)
