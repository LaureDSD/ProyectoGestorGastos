import base64
from flask import jsonify, request
from functools import wraps
import logging
import json
from logging.handlers import RotatingFileHandler
from models import Producto, TicketResponse

class Utils:
    def __init__(self, allowed_extensions=None, api_key=None, log_file="app.log"):
        """
        Inicializa la clase Utils con configuraciones.
        
        :param allowed_extensions: Set de extensiones permitidas
        :param api_key: API key para autenticación
        :param log_file: Ruta del archivo de log
        """
        self.EXTENSIONS = allowed_extensions or {'png', 'jpg', 'jpeg', 'pdf', 'txt'}
        self.LOCAL_API_KEY = api_key
        
        self.logger = logging.getLogger("app_logger")
        self.logger.setLevel(logging.INFO)
        handler = RotatingFileHandler(log_file, maxBytes=5*1024*1024, backupCount=3)
        handler.setFormatter(logging.Formatter('%(asctime)s - %(levelname)s - %(message)s'))
        self.logger.addHandler(handler)

    def allowed_file(self, filename):
        """Verifica si la extensión del archivo está permitida."""
        return '.' in filename and filename.rsplit('.', 1)[1].lower() in self.EXTENSIONS

    def require_api_key(self, func):
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
                if token == self.LOCAL_API_KEY:
                    return func(*args, **kwargs)
            return jsonify({"error": "Unauthorized"}), 401
        return wrapper

    def simulador_respuesta_json(self, texto_json):
            """
            Procesa un texto JSON y lo convierte en respuesta estructurada.
            """
            try:
                datos = json.loads(texto_json)
                productos = [Producto(**p) for p in datos.get("productos", [])]
                total = datos.get("total", 0.0)
                return jsonify(TicketResponse(productos=productos, total=total, texto_original=texto_json).dict())
            except Exception as e:
                self.logger.error(f"Error procesando respuesta del modelo: {str(e)}")
                return jsonify({"error": f"Error procesando la respuesta del modelo: {str(e)}"}), 500

    @staticmethod
    def simulador_respuesta_demo():
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
