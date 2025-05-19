import json
import base64
from io import BytesIO
from typing import List, Optional
from flask import Flask, request, jsonify
from dotenv import load_dotenv, dotenv_values
from pydantic import BaseModel
from PIL import Image

import openai  # Importa la biblioteca OpenAI para interactuar con la API de OpenAI.
import os  # Importa el módulo os para interactuar con el sistema operativo
from datetime import datetime # Improtamos esta funcion para transformar las fechas de creación

from functools import wraps
from flasgger import Swagger
import logging
from logging.handlers import RotatingFileHandler
from werkzeug.exceptions import RequestEntityTooLarge

import pytesseract
from PIL import Image, ImageEnhance, ImageFilter
import numpy as np
import cv2

# ===================== CONFIGURACIÓN =====================

load_dotenv()
env = dotenv_values()

config = dotenv_values(".env")

OCR_LOCAL = config.get("OCR_LOCAL", "True").lower() == "true"
TESSERACT_CMD = config.get("TESSERACT_CMD")
LOCAL_API_KEY = config.get("LOCAL_API_KEY")
OPENAI_API_KEY = config.get("OPENAI_API_KEY")
# Llamar a la API para listar los modelos
models = openai.Model.list()
# Imprimir los modelos disponibles
for model in models['data']:
    print(model['id'])
    
    
app = Flask(__name__)
max_mb = int(env.get("MAX_CONTENT_LENGTH_MB", 10))
app.config['MAX_CONTENT_LENGTH'] = max_mb * 1024 * 1024

swagger = Swagger(app, config={
    "headers": [],
    "specs": [
        {
            "endpoint": 'apispec_1',
            "route": '/swagger.json',
            "rule_filter": lambda rule: True,
            "model_filter": lambda tag: True,
        }
    ],
    "static_url_path": "/flasgger_static",
    "swagger_ui": True,
    "specs_route": "/docs/"
}, template={
    "swagger": "2.0",
    "info": {
        "title": "OCR & IA Ticket API",
        "description": "API para procesar tickets con OCR e interactuar con IA usando GPT-4.",
        "version": "1.0.0"
    },
    "basePath": "/",
    "securityDefinitions": {
        "BearerAuth": {
            "type": "http",
            "scheme": "bearer",
            "bearerFormat": "JWT"
        }
    },
    "security": [{"BearerAuth": []}],
    "definitions": {
        "Producto": {
            "type": "object",
            "properties": {
                "nombre": {"type": "string"},
                "precio": {"type": "number"},
                "cantidad": {"type": "string"},
                "subtotal": {"type": "number"}
            }
        },
        "TicketResponse": {
            "type": "object",
            "properties": {
                "establecimiento": {"type": "string"},
                "fecha": {"type": "string"},
                "hora": {"type": "string"},
                "total": {"type": "number"},
                "iva": {"type": "number"},
                "categoria": {"type": "string"},
                "articulos": {
                    "type": "array",
                    "items": {"$ref": "#/definitions/Producto"}
                },
                "confianza": {"type": "number"}
            }
        },
        "RespuestaChat": {
            "type": "object",
            "properties": {
                "respuesta": {"type": "string"}
            }
        }
    }
})

# ===================== LOGGING =====================

logger = logging.getLogger("app_logger")
logger.setLevel(logging.INFO)
handler = RotatingFileHandler("app.log", maxBytes=5 * 1024 * 1024, backupCount=3)
handler.setFormatter(logging.Formatter('%(asctime)s - %(levelname)s - %(message)s'))
logger.addHandler(handler)

# ===================== MODELOS =====================

class Producto(BaseModel):
    nombre: str
    precio: float
    cantidad: str
    subtotal: float

class TicketResponse(BaseModel):
    establecimiento: str
    fecha: Optional[str]
    hora: Optional[str]
    total: float
    iva: float
    categoria: str
    articulos: List[Producto]
    confianza: float

# ===================== AUTENTICACIÓN =====================

def require_api_key(func):
    @wraps(func)
    def wrapper(*args, **kwargs):
        auth_header = request.headers.get("Authorization", "")
        if auth_header.startswith("Bearer "):
            token = auth_header.replace("Bearer ", "")
            if token == LOCAL_API_KEY:
                return func(*args, **kwargs)
            print(LOCAL_API_KEY)
        return jsonify({"error": "Unauthorized"}), 401
    return wrapper

# ===================== FUNCIONES AUXILIARES =====================

def procesar_ocr_imagen_openai(file):
    try:
        img = Image.open(file.stream).convert("RGB")
        buffer = BytesIO()
        img.save(buffer, format="JPEG")
        img_b64 = base64.b64encode(buffer.getvalue()).decode()

        response = openai.ChatCompletion.create(
            model="gpt-3.5-turbo", 
            messages=[
                {"role": "system", "content": (
                    "Eres un experto en tickets. Extrae esta estructura exacta como JSON:\n"
                    "{ establecimiento: str, fecha: str, hora: str, total: float, iva: float, categoria: str,\n"
                    "  articulos: [ { nombre: str, precio: float, cantidad: str, subtotal: float } ], confianza: float }"
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
        return ticket.dict()

    except (RateLimitError) as e:
        logger.error("Error de conexión con OpenAI", exc_info=True)
        raise ConnectionError("No se pudo conectar con el servicio de IA, inténtalo más tarde.") from e
    except Exception as e:
        logger.error("Error al procesar OCR imagen", exc_info=True)
        raise RuntimeError(f"Error al procesar OCR imagen: {e}") from e


def procesar_ocr_archivo(file):
    try:
        contenido = file.read().decode('utf-8', errors='ignore')

        response = openai.ChatCompletion.create(
            model="gpt-3.5-turbo", 
            messages=[
                {"role": "system", "content": (
                    "Eres un experto en tickets. Extrae esta estructura exacta como JSON:\n"
                    "{ establecimiento: str, fecha: str, hora: str, total: float, iva: float, categoria: str,\n"
                    "  articulos: [ { nombre: str, precio: float, cantidad: str, subtotal: float } ], confianza: float }"
                )},
                {"role": "user", "content": contenido}
            ],
            temperature=0.7,
            max_tokens=1000
        )

        json_text = response['choices'][0]['message']['content']  # corregido
        parsed = json.loads(json_text)
        ticket = TicketResponse(**parsed)
        return ticket.dict()

    except (RateLimitError, APIError) as e:
        logger.error("Error de conexión con OpenAI", exc_info=True)
        raise ConnectionError("No se pudo conectar con el servicio de IA, inténtalo más tarde.") from e
    except Exception as e:
        logger.error("Error al procesar OCR archivo", exc_info=True)
        raise RuntimeError(f"Error al procesar OCR archivo: {e}") from e


def responder_chat(mensaje: str):
    try:
        response = openai.ChatCompletion.create(
            model="gpt-3.5-turbo", 
            messages=[
                {"role": "system", "content": "Eres un asistente útil y claro."},
                {"role": "user", "content": mensaje}
            ],
            temperature=0.7,
            max_tokens=500
        )
        return {"respuesta": response['choices'][0]['message']['content']}  # corregido
    except (RateLimitError) as e:
        logger.error("Error de conexión con OpenAI", exc_info=True)
        raise ConnectionError("No se pudo conectar con el servicio de IA, inténtalo más tarde.") from e
    except Exception as e:
        logger.error("Error al generar respuesta de chat", exc_info=True)
        raise RuntimeError(f"Error al generar respuesta de chat: {e}") from e
    

def procesar_ocr_imagen_local(file):
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
        return texto.strip()
    
    
    
    
    

    except Exception as e:
        logger.error("Error al procesar OCR imagen", exc_info=True)
        raise RuntimeError(f"Error al procesar OCR imagen: {e}") from e

        


# ===================== ENDPOINTS =====================

@app.errorhandler(RequestEntityTooLarge)
def handle_file_too_large(e):
    logger.warning("Archivo demasiado grande rechazado")
    return jsonify({"error": "Archivo demasiado grande. Máximo permitido: 10MB"}), 413

@app.route("/ocr", methods=["POST"])
@require_api_key
def ocr():
    try:
        file = request.files.get('file')
        if not file or file.filename == '':
            return jsonify({"error": "Archivo inválido"}), 400
        
        if OCR_LOCAL:
            resultado = procesar_ocr_imagen_openai(file)
        else:
            resultado = procesar_ocr_imagen_local(file)
        
        return jsonify(resultado), 200
    except ConnectionError as ce:
        return jsonify({"error": str(ce)}), 503
    except Exception as e:
        logger.error("Error en /ocr", exc_info=True)
        return jsonify({"error": "Error interno al procesar imagen OCR"}), 500

@app.route("/ocr-file", methods=["POST"])
@require_api_key
def ocr_archivo():
    try:
        file = request.files.get('file')
        if not file or file.filename == '':
            return jsonify({"error": "Archivo inválido"}), 400
        resultado = procesar_ocr_archivo(file)
        return jsonify(resultado), 200
    except ConnectionError as ce:
        return jsonify({"error": str(ce)}), 503
    except Exception as e:
        logger.error("Error en /ocr-file", exc_info=True)
        return jsonify({"error": "Error interno al procesar archivo OCR"}), 500

@app.route("/aichat", methods=["POST"])
@require_api_key
def conversar():
    try:
        data = request.get_json()
        mensaje = data.get("mensaje")
        if not mensaje:
            return jsonify({"error": "Debe enviar un campo 'mensaje' en JSON"}), 400
        resultado = responder_chat(mensaje)
        return jsonify(resultado), 200
    except ConnectionError as ce:
        return jsonify({"error": str(ce)}), 503
    except Exception as e:
        logger.error("Error en /aichat", exc_info=True)
        return jsonify({"error": "Error interno al generar respuesta"}), 500

@app.route("/status", methods=["GET"])
def health():
    return jsonify({"status": "ok"}), 200

# ===================== INICIO =====================
if __name__ == "__main__":
    pytesseract.pytesseract.tesseract_cmd = TESSERACT_CMD
    app.run(debug=True, port=5000)
