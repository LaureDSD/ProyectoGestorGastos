import os
from flask import Flask, request, jsonify
from werkzeug.utils import secure_filename
from dotenv import dotenv_values

# Cargar variables de entorno desde .env
env = dotenv_values()

# Inicializar Flask
app = Flask(__name__)

# Cargar API Key
API_KEY = env.get("API_KEY", "default_api_key")

# Middleware simple de autenticación por API Key
def require_api_key(func):
    def wrapper(*args, **kwargs):
        auth_header = request.headers.get("Authorization", "")
        if auth_header.startswith("Bearer "):
            token = auth_header.replace("Bearer ", "")
            if token == API_KEY:
                return func(*args, **kwargs)
        return jsonify({"error": "Unauthorized"}), 401
    wrapper.__name__ = func.__name__
    return wrapper

# ========== ENDPOINT 1: Procesamiento de imagen OCR ==========

    # ========== ENDPOINT OCR IMAGEN ==========

@app.route("/ocr", methods=["POST"])
@require_api_key
def ocr():
    try:
        if 'file' not in request.files:
            return jsonify({"error": "No se encontró archivo"}), 400

        file = request.files['file']
        if file.filename == '':
            return jsonify({"error": "Nombre de archivo vacío"}), 400

        resultado = procesar_ocr_imagen(file)
        return jsonify(resultado), 200

    except ValueError as e:
        return jsonify({"error": str(e)}), 400
    except Exception as e:
        return jsonify({"error": "Error interno al procesar imagen OCR"}), 500
    
    # ========== ENDPOINT OCR ARCHIVO ==========

@app.route("/ocr-file", methods=["POST"])
@require_api_key
def ocr_archivo():
    try:
        if 'file' not in request.files:
            return jsonify({"error": "No se encontró archivo"}), 400

        file = request.files['file']
        if file.filename == '':
            return jsonify({"error": "Nombre de archivo vacío"}), 400

        resultado = procesar_ocr_archivo(file)
        return jsonify(resultado), 200

    except ValueError as e:
        return jsonify({"error": str(e)}), 400
    except Exception as e:
        return jsonify({"error": "Error interno al procesar archivo OCR"}), 500



# ========== ENDPOINT 2: Comunicación con IA ==========

@app.route("/aichat", methods=["POST"])
@require_api_key
def conversar():
    try:
        data = request.get_json()
        if not data or "mensaje" not in data:
            return jsonify({"error": "Debe enviar un campo 'mensaje' en JSON"}), 400

        mensaje = data["mensaje"]
        resultado = responder_chat(mensaje)
        return jsonify(resultado), 200

    except ValueError as e:
        return jsonify({"error": str(e)}), 400
    except Exception as e:
        return jsonify({"error": "Error interno al generar respuesta"}), 500


# ========== ENDPOINT 3: Información del servidor ==========

@app.route("/status", methods=["GET"])
def health():
    return jsonify({"status": "ok"}), 200

# ===================== FUNCIONES AUXILIARES =====================

def procesar_ocr_imagen(file):

    if not file:
        raise ValueError("Archivo inválido o no proporcionado.")

    # Simulación de OCR
    return {
        "establecimiento": "Supermercado ABC",
        "fecha": "2025-05-13",
        "hora": "17:30",
        "total": 3500.0,
        "iva": 21,
        "categoria": 2,
        "articulos": [
            {
                "nombre": "Coca-Cola",
                "precio": 1500,
                "cantidad": 2,
                "subtotal": 3000
            },
            {
                "nombre": "Pan",
                "precio": 500,
                "cantidad": 1,
                "subtotal": 500
            }
        ],
        "confianza": 0.95
    }


def procesar_ocr_archivo(file):

    if not file:
        raise ValueError("Archivo inválido o no proporcionado.")

    return {
        "tipo": "archivo",
        "contenido_extraido": "Este es el contenido simulado de un archivo.",
        "paginas": 3,
        "confianza": 0.88
    }


def responder_chat(mensaje: str):

    if not mensaje:
        raise ValueError("Mensaje vacío.")

    return {
        "respuesta": f"Recibido: '{mensaje}'. Esta es una respuesta simulada."
    }



# ========== INICIO DEL SERVIDOR ==========
if __name__ == "__main__":
    app.run(debug=True, port=5000)
