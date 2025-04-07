from flask import Flask, request, jsonify
import pytesseract
from PIL import Image

app = Flask(__name__)

@app.route('/process-ocr', methods=['POST'])
def process_ocr():
    file = request.files['file']
    
    # Procesa la imagen con pytesseract (OCR)
    image = Image.open(file)
    text = pytesseract.image_to_string(image)
    
    # Devuelve el resultado en formato JSON
    result = {"ocrText": text}
    
    return jsonify(result)

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
