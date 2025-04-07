from flask import Flask, request, jsonify
from werkzeug.utils import secure_filename
import pytesseract
import pdf2image
import cv2
import numpy as np
import re
from datetime import datetime
import os

# Configuracións globais
IDIOMA_OCR = "spa"
IVA_POR_DEFECTO = 0.21

app = Flask(__name__)

# Configuración para permitir la carga de archivos
app.config['UPLOAD_FOLDER'] = 'uploads'
app.config['ALLOWED_EXTENSIONS'] = {'png', 'jpg', 'jpeg', 'pdf'}

# Función para verificar que el archivo tenga una extensión válida
def allowed_file(filename):
    return '.' in filename and filename.rsplit('.', 1)[1].lower() in app.config['ALLOWED_EXTENSIONS']

# Función para extraer texto de archivos (imágenes o PDFs)
def extraer_texto(contenido, tipo, idioma=IDIOMA_OCR):
    if tipo == 'application/pdf':
        imagenes = pdf2image.convert_from_bytes(contenido)
        return "\n".join(pytesseract.image_to_string(img, lang=idioma) for img in imagenes)
    else:
        img = cv2.imdecode(np.frombuffer(contenido, np.uint8), cv2.IMREAD_COLOR)
        if img is None:
            raise Exception("Imagen no válida")
        gris = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
        _, thresh = cv2.threshold(gris, 0, 255, cv2.THRESH_BINARY + cv2.THRESH_OTSU)
        return pytesseract.image_to_string(thresh, lang=idioma)

# Función para procesar la factura
def procesar_factura(texto, iva=IVA_POR_DEFECTO):
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

# Función para procesar el ticket
def procesar_ticket(texto):
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

# Ruta para procesar el archivo (factura o ticket)
@app.route('/procesar', methods=['POST'])
def procesar():
    try:
        if 'file' not in request.files:
            raise Exception("No se ha enviado ningún archivo")

        archivo = request.files['file']
        es_factura = request.form.get('es_factura', 'False').lower() == 'true'

        if archivo.filename == '' or not allowed_file(archivo.filename):
            raise Exception("Archivo no válido")

        contenido = archivo.read()
        texto = extraer_texto(contenido, archivo.content_type)

        if es_factura:
            datos = procesar_factura(texto)
            if not datos:
                return jsonify({"exito": False, "error": "No es una factura válida"}), 400
        else:
            datos = procesar_ticket(texto)
            if not datos:
                return jsonify({"exito": False, "error": "No es un ticket válido"}), 400

        return jsonify({
            "exito": True,
            "datos": datos
        })

    except Exception as e:
        return jsonify({"exito": False, "error": str(e)}), 500

if __name__ == "__main__":
    # Crear la carpeta de subida si no existe
    if not os.path.exists(app.config['UPLOAD_FOLDER']):
        os.makedirs(app.config['UPLOAD_FOLDER'])

    app.run(host='0.0.0.0', port=8000)
