import pytesseract
from PIL import Image, ImageEnhance
import spacy
import json
import cv2
import numpy as np
import datetime

# Clase de preprocesamiento de imagen
class PreprocesadorImagen:
    def __init__(self, ruta_imagen):
        self.img = Image.open(ruta_imagen)

    def mejorar_contraste(self):
        # Convertir la imagen a escala de grises
        img_gray = self.img.convert('L')

        # Aumentar el contraste
        enhancer = ImageEnhance.Contrast(img_gray)
        img_enhanced = enhancer.enhance(2.0)

        # Convertir la imagen de PIL a formato de OpenCV para procesamiento adicional
        img_cv = np.array(img_enhanced)

        # Suavizado para reducir ruido
        img_cv = cv2.GaussianBlur(img_cv, (5, 5), 0)

        # Umbralización para convertir en blanco y negro (OCR más preciso)
        img_cv = cv2.threshold(img_cv, 0, 255, cv2.THRESH_BINARY + cv2.THRESH_OTSU)[1]

        return img_cv

# Clase de extracción de texto usando OCR
class ExtractorTextoOCR:
    def __init__(self, imagen_cv):
        # Configuración de Tesseract (si es necesario)
        pytesseract.pytesseract.tesseract_cmd = r"C:\Program Files\Tesseract-OCR\tesseract.exe"  # Cambia la ruta si es necesario
        self.imagen_cv = imagen_cv

    def extraer_texto(self):
        # Usar OCR para extraer el texto
        custom_config = r'--oem 3 --psm 6'
        texto = pytesseract.image_to_string(self.imagen_cv, config=custom_config)
        return texto

# Clase de procesamiento de texto y extracción de entidades con NLP
class ProcesadorTexto:
    def __init__(self, texto_extraido):
        self.texto = texto_extraido
        self.nlp = spacy.load("es_core_news_sm")  # Usar un modelo preentrenado en español

    def extraer_entidades(self):
        # Procesar el texto con spaCy para extraer las entidades
        doc = self.nlp(self.texto)

        entidades = {'tienda': '', 'fecha': '', 'hora': '', 'total': '', 'iva': '', 'articulos': [], 'metodo_pago': '', 'cambio': '', 'confianza': 1.0}
        
        for ent in doc.ents:
            if ent.label_ == 'ORG' and not entidades['tienda']:
                entidades['tienda'] = ent.text
            if ent.label_ == 'DATE' and not entidades['fecha']:
                entidades['fecha'] = ent.text
            if ent.label_ == 'TIME' and not entidades['hora']:
                entidades['hora'] = ent.text
            if ent.label_ == 'MONEY' and not entidades['total']:
                entidades['total'] = ent.text
            if 'IVA' in ent.text and not entidades['iva']:
                entidades['iva'] = ent.text
            if 'EFECTIVO' in ent.text and not entidades['metodo_pago']:
                entidades['metodo_pago'] = 'Efectivo'
            if 'TARJETA' in ent.text and not entidades['metodo_pago']:
                entidades['metodo_pago'] = 'Tarjeta'
            if 'CAMBIO' in ent.text and not entidades['cambio']:
                entidades['cambio'] = ent.text
            # Procesar artículos y categorías
            if 'BOTIN' in ent.text:  # Este es solo un ejemplo, necesitarás más lógica para categorías
                articulo = {'nombre': ent.text, 'categoria': ['ropa']}
                entidades['articulos'].append(articulo)

        return entidades

# Clase de validación de datos extraídos
class ValidadorDatos:
    @staticmethod
    def validar_datos(datos):
        # Comprobar que el total no sea mayor que el efectivo
        if float(datos['total'].replace(',', '.')) > float(datos['cambio'].replace(',', '.')):
            return False
        # Validar que la fecha sea válida
        try:
            datetime.datetime.strptime(datos['fecha'], '%d/%m/%Y')
        except ValueError:
            return False
        return True

# Clase principal que integra todo el flujo
class ExtraccionDatosTicket:
    def __init__(self, ruta_imagen):
        self.ruta_imagen = ruta_imagen

    def ejecutar(self):
        # Paso 1: Preprocesamiento de la imagen
        preprocesador = PreprocesadorImagen(self.ruta_imagen)
        imagen_cv = preprocesador.mejorar_contraste()

        # Paso 2: Extraer texto utilizando OCR
        extractor_ocr = ExtractorTextoOCR(imagen_cv)
        texto_extraido = extractor_ocr.extraer_texto()

        # Paso 3: Procesar texto para extraer entidades clave
        procesador_texto = ProcesadorTexto(texto_extraido)
        datos_extraidos = procesador_texto.extraer_entidades()

        # Paso 4: Validar los datos extraídos
        if ValidadorDatos.validar_datos(datos_extraidos):
            return datos_extraidos
        else:
            return {'error': 'Datos inválidos o inconsistentes.'}

# Ejemplo de uso
if __name__ == '__main__':
    ruta_imagen = 'ticket.jpg'  # Aquí pones la ruta del ticket a procesar
    extraccion = ExtraccionDatosTicket(ruta_imagen)
    resultado = extraccion.ejecutar()
    
    # Mostrar el resultado
    print(json.dumps(resultado, indent=4))
