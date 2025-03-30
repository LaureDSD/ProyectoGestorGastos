from fastapi import FastAPI, UploadFile, Form, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from typing import Optional, List, Dict
import pytesseract
import pdf2image
import cv2
import numpy as np
import boto3
import re
from datetime import datetime
import uvicorn

app = FastAPI(title="Servicio OCR GesThor")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_methods=["*"],
    allow_headers=["*"],
)

class FacturaResponse(BaseModel):
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
    establecimiento: str
    fecha: Optional[str]
    hora: Optional[str]
    total: float
    articulos: List[Dict[str, str]]
    confianza: float

def subir_s3(contenido: bytes, nombre: str, bucket: str, prefijo: str, aws_key: str, aws_secret: str) -> str:
    s3 = boto3.client('s3', aws_access_key_id=aws_key, aws_secret_access_key=aws_secret)
    clave = f"{prefijo}{datetime.now().strftime('%Y%m%d')}/{nombre}"
    s3.put_object(Bucket=bucket, Key=clave, Body=contenido)
    return f"https://{bucket}.s3.amazonaws.com/{clave}"

async def extraer_texto(contenido: bytes, tipo: str, idioma: str = "spa") -> str:
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

def procesar_factura(texto: str, iva: float = 0.21) -> Optional[Dict]:
    total_match = re.search(r"(?i)total\s*[:=]?\s*(\d+,\d{2})", texto)
    if not total_match:
        return None
    
    total = float(total_match.group(1).replace(',', '.'))
    iva_val = total * iva
    base = total - iva_val
    
    return {
        "proveedor": "Proveedor no identificado",
        "nif": None,
        "fecha": datetime.now().strftime("%d/%m/%Y"),
        "numero": "N/A",
        "base": round(base, 2),
        "iva": round(iva_val, 2),
        "total": round(total, 2),
        "lineas": [],
        "confianza": 0.8
    }

def procesar_ticket(texto: str) -> Optional[Dict]:
    total_match = re.search(r"(?i)total\s*[:=]?\s*(\d+,\d{2})", texto)
    if not total_match:
        return None
    
    return {
        "establecimiento": "Establecimiento no identificado",
        "fecha": None,
        "hora": None,
        "total": float(total_match.group(1).replace(',', '.')),
        "articulos": [],
        "confianza": 0.8
    }

@app.post("/procesar")
async def procesar(
    archivo: UploadFile,
    es_factura: bool = Form(False),
    bucket: str = Form("gesthor-ocr"),
    prefijo: str = Form("procesados/"),
    aws_key: str = Form("tu_key"),
    aws_secret: str = Form("tu_secret")
):
    try:
        contenido = await archivo.read()
        texto = await extraer_texto(contenido, archivo.content_type)
        
        if es_factura:
            datos = procesar_factura(texto)
            if not datos:
                raise HTTPException(400, "No es una factura válida")
            respuesta = FacturaResponse(**datos)
        else:
            datos = procesar_ticket(texto)
            if not datos:
                raise HTTPException(400, "No es un ticket válido")
            respuesta = TicketResponse(**datos)
        
        url = subir_s3(contenido, archivo.filename, bucket, prefijo, aws_key, aws_secret)
        
        return {
            "exito": True,
            "datos": respuesta,
            "url_archivo": url
        }
        
    except Exception as e:
        raise HTTPException(500, f"Error: {str(e)}")

if __name__ == "__main__":
    # Configuración principal
    puerto = 8000
    host = "0.0.0.0"
    debug = True
    idioma_ocr = "spa"
    iva_por_defecto = 0.21
    
    uvicorn.run(app, host=host, port=puerto)