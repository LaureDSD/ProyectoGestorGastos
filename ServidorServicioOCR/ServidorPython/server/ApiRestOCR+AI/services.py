import io
import pytesseract
import cv2
import numpy as np
import json
import mimetypes
from PIL import Image, ImageEnhance, ImageFilter
from io import BytesIO
from openai.error import RateLimitError, APIError
from models import Producto, TicketResponse
from typing import Optional, Dict, Any
import openai
from PyPDF2 import PdfReader


class OcrService:
    def __init__(self, openai_api_key: str, model: str = "gpt-3.5-turbo", logger=None):
        """
        Inicializa el servicio OCR con configuración.
        
        :param openai_api_key: Clave API de OpenAI
        :param model: Modelo de OpenAI a usar
        :param logger: Instancia de logger (opcional)
        """
        self.model = model
        self.logger = logger
        import openai
        openai.api_key = openai_api_key

    def _preprocesar_imagen(self, img: Image.Image) -> Image.Image:
        """Preprocesamiento de imagen para OCR"""
        img_gray = img.convert('L')
        enhancer = ImageEnhance.Contrast(img_gray)
        img_contrast = enhancer.enhance(2)
        img_sharp = img_contrast.filter(ImageFilter.SHARPEN)
        
        img_np = np.array(img_sharp)
        _, img_bin = cv2.threshold(img_np, 0, 255, cv2.THRESH_BINARY + cv2.THRESH_OTSU)
        return Image.fromarray(img_bin)

    def _procesar_texto_con_openai(self, texto: str) -> Dict[str, Any]:
        """Envía texto a OpenAI para estructuración"""
        response = openai.ChatCompletion.create(
            model=self.model,
            messages=[
                {
                    "role": "system",
                    "content": (
                        "Eres un experto en tickets. Extrae esta estructura exacta como JSON:\n"
                        "{ establecimiento: str, fecha: str, hora: str, total: float, iva: float, categoria: int,\n"
                        "  articulos: [ { nombre: str, precio: float, cantidad: float, subtotal: float } ], confianza: float }"
                    )
                },
                {
                    "role": "user",
                    "content": f"Texto OCR:\n{texto.strip()}\nDevuélveme solo JSON estructurado."
                }
            ],
            temperature=0.7,
        )
        return json.loads(response['choices'][0]['message']['content'])

    def procesar_ocr_imagen_local(self, file) -> Dict[str, Any]:
        """
        Procesa imagen con Tesseract + OpenAI.
        
        :param file: Archivo de imagen (FileStorage o similar)
        :return: Diccionario con datos estructurados del ticket
        """
        try:
            img = Image.open(file).convert('RGB')
            img_procesada = self._preprocesar_imagen(img)
            
            texto = pytesseract.image_to_string(
                img_procesada,
                config=r'--oem 3 --psm 6'
            )
            
            if self.logger:
                self.logger.info("Texto extraído correctamente de imagen OCR.")
            
            parsed = self._procesar_texto_con_openai(texto)
            ticket = TicketResponse(**parsed)
            ticket.categoria = 1  # Temporal
            return ticket.model_dump()
            
        except Exception as e:
            if self.logger:
                self.logger.error("Error al procesar OCR imagen", exc_info=True)
            raise RuntimeError(f"Error al procesar OCR imagen: {e}") from e

    def procesar_ocr_imagen_openai(self, file) -> Dict[str, Any]:
        """Procesa imagen directamente con OpenAI Vision"""
        try:
            img = Image.open(file.stream).convert("RGB")
            buffer = BytesIO()
            img.save(buffer, format="JPEG")
            img_b64 = base64.b64encode(buffer.getvalue()).decode()

            response = openai.ChatCompletion.create(
                model=self.model,
                messages=[
                    {
                        "role": "system",
                        "content": (
                            "Eres un experto en tickets. Extrae esta estructura exacta como JSON:\n"
                            "{ establecimiento: str, fecha: str, hora: str, total: float, iva: float, categoria: int,\n"
                            "  articulos: [ { nombre: str, precio: float, cantidad: float, subtotal: float } ], confianza: float }"
                        )
                    },
                    {
                        "role": "user",
                        "content": [
                            {"type": "text", "text": "Devuélveme solo JSON estructurado."},
                            {"type": "image_url", "image_url": {"url": f"data:image/jpeg;base64,{img_b64}"}}
                        ]
                    }
                ],
                temperature=0.7,
            )

            parsed = json.loads(response['choices'][0]['message']['content'])
            ticket = TicketResponse(**parsed)
            ticket.categoria = 1  # Temporal
            return ticket.model_dump()

        except RateLimitError as e:
            if self.logger:
                self.logger.error("Error de conexión con OpenAI", exc_info=True)
            raise ConnectionError("No se pudo conectar con el servicio de IA, inténtalo más tarde.") from e
        except Exception as e:
            if self.logger:
                self.logger.error("Error al procesar OCR imagen", exc_info=True)
            raise RuntimeError(f"Error al procesar OCR imagen: {e}") from e

    def procesar_ocr_archivo(self, file) -> Dict[str, Any]:
        """Procesa archivos PDF o de texto"""
        try:
            content_type = mimetypes.guess_type(file.filename)[0]
            
            if content_type == 'application/pdf':
                
                reader = PdfReader(file)
                contenido = "\n".join(page.extract_text() or '' for page in reader.pages)
            else:
                contenido = file.read().decode('utf-8', errors='ignore')

            response = openai.ChatCompletion.create(
                model=self.model,
                messages=[
                    {
                        "role": "system",
                        "content": (
                            "Eres un experto en tickets. Extrae esta estructura exacta como JSON:\n"
                            "{ establecimiento: str, fecha: str, hora: str, total: float, iva: float, categoria: int,\n"
                            "  articulos: [ { nombre: str, precio: float, cantidad: float, subtotal: float } ], confianza: float }"
                        )
                    },
                    {"role": "user", "content": contenido}
                ],
                temperature=0.7,
                max_tokens=1000
            )

            parsed = json.loads(response['choices'][0]['message']['content'])
            ticket = TicketResponse(**parsed)
            ticket.categoria = 1  # Temporal
            return ticket.model_dump()

        except (RateLimitError, APIError) as e:
            if self.logger:
                self.logger.error("Error de conexión con OpenAI", exc_info=True)
            raise ConnectionError("No se pudo conectar con el servicio de IA, inténtalo más tarde.") from e
        except Exception as e:
            if self.logger:
                self.logger.error("Error al procesar OCR archivo", exc_info=True)
            raise RuntimeError(f"Error al procesar OCR archivo: {e}") from e


class ChatService:
    def __init__(self, openai_api_key: str, model: str = "gpt-3.5-turbo", logger=None):
        """
        Servicio para manejar el chat con IA.
        
        :param openai_api_key: Clave API de OpenAI
        :param model: Modelo de OpenAI a usar
        :param logger: Instancia de logger (opcional)
        """
        self.model = model
        self.logger = logger
        import openai
        openai.api_key = openai_api_key


    def responder_chat(self, mensaje: str) -> Dict[str, str]:
        """
        Genera respuesta de chat usando IA.
        
        :param mensaje: Mensaje del usuario
        :return: Diccionario con la respuesta
        """
        print("Mensaje recibido:", mensaje)  # Agrega este print para depura
        try:
            response = openai.ChatCompletion.create(
                model=self.model,
                messages=[
                    {
                        "role": "system",
                        "content": (
                            "Eres un asistente útil y claro, trabajas para gesthor, "
                            "te encargas de cubrir las necesidades financieras de los clientes "
                            "en España, dándoles información sobre precios en euros y recomendaciones."
                        )
                    },
                    {"role": "user", "content": mensaje}
                ],
                temperature=0.7,
                max_tokens=500
            )
            
            return {"respuesta": response['choices'][0]['message']['content']}
            
        except RateLimitError as e:
            if self.logger:
                self.logger.error("Error de conexión con OpenAI", exc_info=True)
            raise ConnectionError("No se pudo conectar con el servicio de IA, inténtalo más tarde.") from e
        except Exception as e:
            if self.logger:
                self.logger.error("Error al generar respuesta de chat", exc_info=True)
            raise RuntimeError(f"Error al generar respuesta de chat: {e}") from e