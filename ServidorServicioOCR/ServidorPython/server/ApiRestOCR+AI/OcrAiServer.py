"""
Módulo principal del servidor de procesamiento de tickets con OCR e IA.

Este servidor Flask proporciona endpoints para:

- Procesamiento de imágenes de tickets usando OCR (local o con OpenAI)
- Procesamiento de archivos de texto/PDF de tickets
- Interacción con modelos de IA para análisis de tickets
- Chat asistido por IA

Configuración:

- Se utiliza dotenv para variables de entorno
- Soporta autenticación por API key
- Incluye logging y manejo de errores
"""


import json
import base64
from io import BytesIO
from typing import List, Optional
from flask import Flask, request, jsonify

from dotenv import load_dotenv, dotenv_values
from pydantic import BaseModel
from PIL import Image

import openai  # Importa la biblioteca OpenAI para interactuar con la API de OpenAI.

from functools import wraps
import logging
from logging.handlers import RotatingFileHandler
from werkzeug.exceptions import RequestEntityTooLarge

import pytesseract
from PIL import Image, ImageEnhance, ImageFilter
import numpy as np
import cv2

import mimetypes

# ===================== CONFIGURACIÓN =====================

load_dotenv()
env = dotenv_values()

DEMO_MODE = env.get("DEMO_MODE", "false").lower() == "true"

OCR_LOCAL = env.get("OCR_LOCAL", "True").lower() == "true"
"""bool: Determina si se usa OCR local (Tesseract) o remoto (OpenAI Vision)"""

TESSERACT_CMD = r"C:\Program Files\Tesseract-OCR\tesseract.exe"
"""str: Ruta al ejecutable de Tesseract OCR (solo Windows)"""

LOCAL_API_KEY = env.get("LOCAL_API_KEY")
"""str: API key para autenticación de endpoints"""

MODEL = env.get("MODEL", "gpt-3.5-turbo")
"""str: Modelo de OpenAI a utilizar (por defecto gpt-3.5-turbo)"""

openai.api_key = env.get("OPENAI_API_KEY","NO-KEY")
"""str: API key para servicios de OpenAI"""

app = Flask(__name__)
"""Flask: Aplicación principal del servidor"""

max_mb = int(env.get("MAX_CONTENT_LENGTH_MB", 10))
app.config['MAX_CONTENT_LENGTH'] = max_mb * 1024 * 1024



EXTENSIONS = env.get("EXTENSIONS") 

# ===================== LOGGING =====================

logger = logging.getLogger("app_logger")
"""logging.Logger: Logger principal de la aplicación"""

logger.setLevel(logging.INFO)
handler = RotatingFileHandler("app.log", maxBytes=5 * 1024 * 1024, backupCount=3)
handler.setFormatter(logging.Formatter('%(asctime)s - %(levelname)s - %(message)s'))
logger.addHandler(handler)

# ===================== MODELOS PYDANTIC =====================

class Producto(BaseModel):
    """
    Representa un producto en un ticket de compra.

    :param nombre: Nombre del producto.
    :type nombre: str
    :param precio: Precio unitario del producto.
    :type precio: float
    :param cantidad: Cantidad comprada del producto.
    :type cantidad: float
    :param subtotal: Precio total por el producto (precio * cantidad).
    :type subtotal: float
    """
    nombre: str
    precio: float
    cantidad: float
    subtotal: float


class TicketResponse(BaseModel):
    """
    Representa la respuesta estructurada para un ticket de compra.

    :param establecimiento: Nombre del establecimiento donde se realizó la compra.
    :type establecimiento: str
    :param fecha: Fecha del ticket (formato string, opcional).
    :type fecha: Optional[str]
    :param hora: Hora del ticket (formato string, opcional).
    :type hora: Optional[str]
    :param total: Total pagado en el ticket.
    :type total: float
    :param iva: Monto de IVA pagado.
    :type iva: float
    :param categoria: Categoría del ticket.
    :type categoria: str
    :param articulos: Lista de productos comprados.
    :type articulos: List[Producto]
    :param confianza: Nivel de confianza en el procesamiento del ticket (entre 0 y 1).
    :type confianza: float
    """
    establecimiento: str
    fecha: Optional[str]
    hora: Optional[str]
    total: float
    iva: float
    categoria: str
    articulos: List[Producto]
    confianza: float


# ===================== DECORADORES =====================

def require_api_key(func):
    """
    Decorador para requerir autenticación mediante API key en el header Authorization.

    :param func: Función a decorar.
    :type func: function
    :returns: Función decorada que verifica el token de autorización antes de llamar a la función original.
    :rtype: function
    :raises werkzeug.exceptions.Unauthorized: Retorna una respuesta 401 si no se proporciona un token válido.
    """
    @wraps(func)
    def wrapper(*args, **kwargs):
        auth_header = request.headers.get("Authorization", "")
        if auth_header.startswith("Bearer "):
            token = auth_header.replace("Bearer ", "")
            if token == LOCAL_API_KEY:
                return func(*args, **kwargs)
        return jsonify({"error": "Unauthorized"}), 401
    return wrapper

# ===================== FUNCIONES AUXILIARES =====================

def simulador_respuesta():
    """
    Genera una respuesta simulada de ticket para pruebas.

    :return: Diccionario con la estructura de un ticket de ejemplo.
    :rtype: dict
    """
    return {
        "articulos": [{
            "cantidad": 5.5,
            "nombre": "DEMO_TEST",
            "precio": 4.4,
            "subtotal": 3.3
        }],
        "categoria": 1,
        "confianza": 0.99,
        "establecimiento": "DEMO_TEST",
        "fecha": "2023-10-26",
        "hora": "18:16",
        "iva": 22.22,
        "total": 111.11
    }

def allowed_file(filename):
    return '.' in filename and filename.rsplit('.', 1)[1].lower() in EXTENSIONS
    

def procesar_ocr_imagen_local(file):
    """
    Procesa una imagen de ticket usando OCR local (Tesseract).

    :param file: Archivo de imagen a procesar.
    :type file: FileStorage o similar

    :return: Ticket estructurado según TicketResponse.
    :rtype: dict

    :raises RuntimeError: Si falla el procesamiento OCR.
    """

    try:
        # Abre la imagen desde el stream del archivo (Flask o archivo directo)
        img = Image.open(file).convert('RGB')

        # Preprocesamiento: Escala de grises, contraste, nitidez
        img_gray = img.convert('L')
        enhancer = ImageEnhance.Contrast(img_gray)
        img_contrast = enhancer.enhance(2)
        img_sharp = img_contrast.filter(ImageFilter.SHARPEN)

        # Convertir a array de NumPy y aplicar binarización
        img_np = np.array(img_sharp)
        _, img_bin = cv2.threshold(img_np, 0, 255, cv2.THRESH_BINARY + cv2.THRESH_OTSU)

        # Convertir de nuevo a PIL para OCR
        img_bin_pil = Image.fromarray(img_bin)

        # Configuración personalizada de Tesseract
        custom_config = r'--oem 3 --psm 6'  # OEM 3: automático, PSM 6: bloques de texto

        # OCR
        texto = pytesseract.image_to_string(img_bin_pil, config=custom_config)

        logger.info("Texto extraído correctamente de imagen OCR.")

        response = openai.ChatCompletion.create(
            model=MODEL,
            messages=[
                {"role": "system", "content": (
                    "Eres un experto en tickets. Extrae esta estructura exacta como JSON:\n"
                    "{ establecimiento: str, fecha: str, hora: str, total: float, iva: float, categoria: str,\n"
                    "  articulos: [ { nombre: str, precio: float, cantidad: float, subtotal: float } ], confianza: float }"
                )},
                {"role": "user", "content": f"Texto OCR:\n{texto.strip()}\nDevuélveme solo JSON estructurado."}
            ],
            temperature=0.7,
        )


        json_text = response['choices'][0]['message']['content']  # corregido
        parsed = json.loads(json_text)
        ticket = TicketResponse(**parsed)
        ticket.categoria = 1 #Temporal
        return ticket.dict()
    
    except Exception as e:
        logger.error("Error al procesar OCR imagen", exc_info=True)
        raise RuntimeError(f"Error al procesar OCR imagen: {e}") from e

def procesar_ocr_imagen_openai(file):
    """
    Procesa una imagen de ticket usando la API de OpenAI Vision.

    :param file: Archivo de imagen a procesar.
    :type file: FileStorage o similar

    :return: Ticket estructurado según TicketResponse.
    :rtype: dict

    :raises ConnectionError: Si falla la conexión con OpenAI.
    :raises RuntimeError: Si falla el procesamiento del OCR o la conversión del resultado.
    """
    try:
        img = Image.open(file.stream).convert("RGB")
        buffer = BytesIO()
        img.save(buffer, format="JPEG")
        img_b64 = base64.b64encode(buffer.getvalue()).decode()

        response = openai.ChatCompletion.create(
            model=MODEL, 
            messages=[
                {"role": "system", "content": (
                    "Eres un experto en tickets. Extrae esta estructura exacta como JSON:\n"
                    "{ establecimiento: str, fecha: str, hora: str, total: float, iva: float, categoria: str,\n"
                    "  articulos: [ { nombre: str, precio: float, cantidad: float, subtotal: float } ], confianza: float }"
                )},
                {"role": "user", "content": [
                    {"type": "text", "text": "Devuélveme solo JSON estructurado."},
                    {"type": "image_url", "image_url": {"url": f"data:image/jpeg;base64,{img_b64}"}}
                ]}
            ],
            temperature=0.7,
        )

        json_text = response['choices'][0]['message']['content']  # corregido
        parsed = json.loads(json_text)
        ticket = TicketResponse(**parsed)
        ticket.categoria = 1 #Temporal
        return ticket.dict()

    except (RateLimitError) as e:
        logger.error("Error de conexión con OpenAI", exc_info=True)
        raise ConnectionError("No se pudo conectar con el servicio de IA, inténtalo más tarde.") from e
    except Exception as e:
        logger.error("Error al procesar OCR imagen", exc_info=True)
        raise RuntimeError(f"Error al procesar OCR imagen: {e}") from e


def procesar_ocr_archivo(file):
    """
    Procesa un archivo de texto o PDF de ticket utilizando la API de OpenAI.

    :param file: Archivo a procesar (PDF o texto).
    :type file: FileStorage o similar

    :return: Ticket estructurado según TicketResponse.
    :rtype: dict

    :raises ConnectionError: Si falla la conexión con OpenAI.
    :raises RuntimeError: Si falla el procesamiento o la conversión del resultado.
    """
    try:
        content_type = mimetypes.guess_type(file.filename)[0]
        
        if content_type == 'application/pdf':
            from PyPDF2 import PdfReader
            reader = PdfReader(file)
            contenido = "\n".join(page.extract_text() or '' for page in reader.pages)
        else:
            contenido = file.read().decode('utf-8', errors='ignore')

        response = openai.ChatCompletion.create(
            model=MODEL, 
            messages=[
                {"role": "system", "content": (
                    "Eres un experto en tickets. Extrae esta estructura exacta como JSON:\n"
                    "{ establecimiento: str, fecha: str, hora: str, total: float, iva: float, categoria: str,\n"
                    "  articulos: [ { nombre: str, precio: float, cantidad: float, subtotal: float } ], confianza: float }"
                )},
                {"role": "user", "content": contenido}
            ],
            temperature=0.7,
            max_tokens=1000
        )

        json_text = response['choices'][0]['message']['content']  # corregido
        parsed = json.loads(json_text)
        ticket = TicketResponse(**parsed)
        ticket.categoria = 1 #Temporal
        return ticket.dict()

    except (RateLimitError, APIError) as e:
        logger.error("Error de conexión con OpenAI", exc_info=True)
        raise ConnectionError("No se pudo conectar con el servicio de IA, inténtalo más tarde.") from e
    except Exception as e:
        logger.error("Error al procesar OCR archivo", exc_info=True)
        raise RuntimeError(f"Error al procesar OCR archivo: {e}") from e


def responder_chat(mensaje: str):
    """
    Genera una respuesta de chat usando IA.

    :param mensaje: Mensaje del usuario.
    :type mensaje: str

    :return: Respuesta generada con clave "respuesta".
    :rtype: dict

    :raises ConnectionError: Si falla la conexión con OpenAI.
    :raises RuntimeError: Si ocurre un error durante el procesamiento.
    """
    try:
        response = openai.ChatCompletion.create(
            model="gpt-3.5-turbo", 
            messages=[
                {"role": "system", "content": "Eres un asistente útil y claro, trabajas para gesthor , te encargas de cubrir las necesidades finacieras de los clientes en España, dandoles infomacion sobre precios en euros y recomendaciones ."},
                {"role": "user", "content": mensaje}
            ],
            temperature=0.7,
            max_tokens=500
        )
        return {"respuesta": response['choices'][0]['message']['content']}
    except (RateLimitError) as e:
        logger.error("Error de conexión con OpenAI", exc_info=True)
        raise ConnectionError("No se pudo conectar con el servicio de IA, inténtalo más tarde.") from e
    except Exception as e:
        logger.error("Error al generar respuesta de chat", exc_info=True)
        raise RuntimeError(f"Error al generar respuesta de chat: {e}") from e
    


# ===================== ENDPOINTS =====================

@app.errorhandler(RequestEntityTooLarge)
def handle_file_too_large(e):
    """
    Manejador para archivos que exceden el tamaño máximo permitido.

    :param e: Excepción capturada para archivo demasiado grande.
    :type e: RequestEntityTooLarge

    :return: Mensaje de error y código HTTP 413 (Payload Too Large).
    :rtype: tuple[dict, int]
    """
    logger.warning("Archivo demasiado grande rechazado")
    return jsonify({"error": "Archivo demasiado grande. Máximo permitido: 10MB"}), 413

@app.route("/ocr", methods=["POST"])
@require_api_key
def endpoint_ocr_image():
    """
    Endpoint para procesar imágenes de tickets mediante OCR.

    Procesa una imagen enviada en el campo 'file' y devuelve la información
    extraída del ticket en formato estructurado.

    :return: JSON con los datos del ticket o un mensaje de error.
    :rtype: Response (JSON)

    :raises 400: Si no se envía un archivo válido.
    :raises 401: Si no se proporciona una API key válida.
    :raises 413: Si el archivo excede el tamaño máximo permitido.
    :raises 500: Error interno del servidor.
    :raises 503: Servicio no disponible por fallo de conexión con OpenAI.
    """
    try:
        file = request.files.get('file')
        if not file or not allowed_file(file.filename):
            return jsonify({"error": "Tipo de archivo no permitido"}), 400

        if DEMO_MODE:
            return jsonify(simulador_respuesta()), 200

        if OCR_LOCAL:
            resultado = procesar_ocr_imagen_local(file)
        else:
            resultado = procesar_ocr_imagen_openai(file)
        
        return jsonify(resultado), 200
    except ConnectionError as ce:
        return jsonify({"error": str(ce)}), 503
    except Exception as e:
        logger.error("Error en /ocr", exc_info=True)
        return jsonify({"error": "Error interno al procesar imagen OCR"}), 500

@app.route("/ocr-file", methods=["POST"])
@require_api_key
def endpoint_ocr_archivo():
    """
    Endpoint para procesamiento de archivos de tickets (PDF o texto).

    Procesa un archivo enviado en el campo 'file' y extrae la información
    del ticket en formato estructurado.

    :return: JSON con los datos del ticket o un mensaje de error.
    :rtype: Response (JSON)

    :raises 400: Si no se envía un archivo válido.
    :raises 401: Si no se proporciona una API key válida.
    :raises 413: Si el archivo excede el tamaño máximo permitido.
    :raises 500: Error interno del servidor.
    :raises 503: Servicio no disponible por fallo de conexión con OpenAI.
    """
    try:
        file = request.files.get('file')
        if not file or not allowed_file(file.filename):
            return jsonify({"error": "Tipo de archivo no permitido"}), 400

        if DEMO_MODE:
            return jsonify(simulador_respuesta()), 200  
        
        resultado = procesar_ocr_archivo(file)
        return jsonify(resultado), 200
    except ConnectionError as ce:
        return jsonify({"error": str(ce)}), 503
    except Exception as e:
        logger.error("Error en /ocr-file", exc_info=True)
        return jsonify({"error": "Error interno al procesar archivo OCR"}), 500

@app.route("/aichat", methods=["POST"])
@require_api_key
def endpoint_Chat_Service():
    """
    Endpoint para interactuar con el asistente de IA.

    Recibe un JSON con un campo 'mensaje' y devuelve una respuesta generada
    por el asistente de inteligencia artificial.

    :return: JSON con la respuesta del asistente o mensaje de error.
    :rtype: Response (JSON)

    :raises 400: Si no se proporciona el campo 'mensaje' en el JSON.
    :raises 401: Si no se proporciona una API key válida.
    :raises 500: Error interno del servidor.
    :raises 503: Servicio no disponible por fallo de conexión con OpenAI.
    """
    try:
        data = request.get_json()
        mensaje = data.get("mensaje")
        if not mensaje:
            return jsonify({"error": "Debe enviar un campo 'mensaje' en JSON"}), 400
        
        if DEMO_MODE:
            response_text = {
                "respuesta": "Bienvenido a Gesthor, Modo demo activado"
            }
            return jsonify(response_text), 200
        
        resultado = responder_chat(mensaje)
        return jsonify(resultado), 200
    except ConnectionError as ce:
        return jsonify({"error": str(ce)}), 503
    except Exception as e:
        logger.error("Error en /aichat", exc_info=True)
        return jsonify({"error": "Error interno al generar respuesta"}), 500

@app.route("/status", methods=["GET"])
def endpoin_tStatus_Server():
    """
    Endpoint de estado del servicio.

    Devuelve un JSON con el estado actual del servicio para verificar que está activo.

    :return: Estado del servicio en formato JSON.
    :rtype: Response (JSON con clave "status")

    :statuscode 200: Servicio activo y funcionando correctamente.
    """
    return jsonify({"status": "ok"}), 200


# ===================== INICIO =====================
if __name__ == "__main__":
    """
    Punto de entrada principal para iniciar el servidor Flask.

    Configura la ruta del ejecutable de Tesseract para OCR local
    y lanza la aplicación en modo debug en el puerto 5000.
    """
    pytesseract.pytesseract.tesseract_cmd = TESSERACT_CMD
    app.run(debug=True, port=5000)
