import pytesseract
from PIL import Image
import re


pytesseract.pytesseract.tesseract_cmd = r"C:\Program Files\Tesseract-OCR\tesseract.exe"
img = Image.open("Tests/ticket-zara-1.jpg")
custom_config = r'--oem 3 --psm 6'
texto = pytesseract.image_to_string(img, config=custom_config)
print(texto)
def extraer_datos(ticket_text):

    datos = {}
    tienda = re.search(r'(ZARA|tienda).*', ticket_text)
    if tienda:
        datos['tienda'] = tienda.group(0)

    fecha_hora = re.search(r'(\d{2}/\d{2}/\d{4}) (\d{2}:\d{2})', ticket_text)
    if fecha_hora:
        datos['fecha'] = fecha_hora.group(1)
        datos['hora'] = fecha_hora.group(2)

    total = re.search(r'Total.*?(\d+,\d{2})', ticket_text)
    if total:
        datos['total'] = total.group(1)

    iva = re.search(r'IVA\s*(\d+,\d{2})', ticket_text)
    if iva:
        datos['iva'] = iva.group(1)

    articulos = []
    for articulo in re.findall(r'(\d+)\s*(\w+[\w\s]+)\s*(\d+,\d{2})', ticket_text):
        articulos.append({
            'nombre': articulo[1],
            'categoria': ['item']
        })
    datos['articulos'] = articulos

    cambio = re.search(r'Cambio\s*EUR\s*(\d+,\d{2})', ticket_text)
    if cambio:
        datos['cambio'] = cambio.group(1)

    if "Efectivo" in ticket_text:
        datos['metodo_pago'] = 'Efectivo'
    elif "Tarjeta" in ticket_text:
        datos['metodo_pago'] = 'Tarjeta'

    datos['confianza'] = 0.9

    return datos

datos_ticket = extraer_datos(texto)

print(datos_ticket)
