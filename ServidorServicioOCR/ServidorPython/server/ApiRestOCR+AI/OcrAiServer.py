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
@app.route("/ocr", methods=["POST"])
@require_api_key
def ocr():
    if 'file' not in request.files:
        return jsonify({"error": "No se encontró archivo"}), 400

    file = request.files['file']
    if file.filename == '':
        return jsonify({"error": "Nombre de archivo vacío"}), 400

    #filename = secure_filename(file.filename)
    #save_path = os.path.join("uploads", filename)
    #os.makedirs("uploads", exist_ok=True)
    #file.save(save_path)

    # Simulación de procesamiento OCR
    resultado = {
        "establecimiento": "Supermercado ABC",
        "fecha": "2025-05-13",
        "hora": "17:30",
        "total": 3500.0,
        "category": 2,
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

    #return jsonify({"error": "OCR fallido"}), 422
    return jsonify(resultado), 200

# ========== ENDPOINT 2: Comunicación con IA ==========
@app.route("/conversar", methods=["POST"])
@require_api_key
def conversar():
    data = request.get_json()
    if not data or "mensaje" not in data:
        return jsonify({"error": "Debe enviar un campo 'mensaje' en JSON"}), 400

    mensaje = data["mensaje"]
    respuesta = f"Recibido: '{mensaje}'. Esta es una respuesta simulada."

    return jsonify({"respuesta": respuesta}), 200

# ========== INICIO DEL SERVIDOR ==========
if __name__ == "__main__":
    app.run(debug=True, port=5000)
