import pytesseract
from PIL import Image, ImageEnhance, ImageFilter
import numpy as np
import cv2

pytesseract.pytesseract.tesseract_cmd = r"C:\Program Files\Tesseract-OCR\tesseract.exe"
img = Image.open("Tests/ticket-zara-1.jpg")
img_gray = img.convert('L')
enhancer = ImageEnhance.Contrast(img_gray)
img_contrast = enhancer.enhance(2)
img_sharp = img_contrast.filter(ImageFilter.SHARPEN)
img_np = np.array(img_sharp)
img_bin = cv2.threshold(img_np, 0, 255, cv2.THRESH_BINARY + cv2.THRESH_OTSU)[1]
img_bin_pil = Image.fromarray(img_bin)
custom_config = r'--oem 3 --psm 6' 
texto = pytesseract.image_to_string(img_bin_pil, config=custom_config)
print(texto)
