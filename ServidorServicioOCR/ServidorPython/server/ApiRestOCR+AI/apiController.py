from flask import Blueprint, request, jsonify
from werkzeug.exceptions import RequestEntityTooLarge
from typing import Dict, Any, Tuple

class ApiController:
    def __init__(self, ocr_service, chat_service, utils, demo_mode=False, ocr_local=True):
        """
        Controlador para los endpoints de la API.
        
        :param ocr_service: Instancia de OcrService
        :param chat_service: Instancia de ChatService
        :param utils: Instancia de Utils
        :param demo_mode: Modo demo activo/inactivo
        :param ocr_local: Usar OCR local (True) o de OpenAI (False)
        """
        self.ocr_service = ocr_service
        self.chat_service = chat_service
        self.utils = utils
        self.DEMO_MODE = demo_mode
        self.OCR_LOCAL = ocr_local
        self.blueprint = Blueprint('api', __name__)
        
        self._register_error_handlers()
        self._register_routes()

    def _register_error_handlers(self):
        """Registra manejadores de errores."""
        
        @self.blueprint.errorhandler(RequestEntityTooLarge)
        def handle_file_too_large(e):
            self.utils.logger.warning("Archivo demasiado grande rechazado")
            return jsonify({"error": "Archivo demasiado grande. Máximo permitido: 10MB"}), 413

    def _register_routes(self):
        """Registra todos los endpoints."""
        self.blueprint.route("/ocr", methods=["POST"])(self.utils.require_api_key(self.endpoint_ocr_image))
        self.blueprint.route("/ocr-file", methods=["POST"])(self.utils.require_api_key(self.endpoint_ocr_archivo))
        self.blueprint.route("/aichat", methods=["POST"])(self.utils.require_api_key(self.endpoint_chat_service))
        self.blueprint.route("/status", methods=["GET"])(self.endpoint_status_server)

    def endpoint_ocr_image(self) -> Tuple[Dict[str, Any], int]:
        """Endpoint para procesar imágenes con OCR."""
        try:
            file = request.files.get('file')
            if not file or not self.utils.allowed_file(file.filename):
                return {"error": "Tipo de archivo no permitido"}, 400

            if self.DEMO_MODE:
                return self.utils.simulador_respuesta_demo(), 200

            if self.OCR_LOCAL:
                resultado = self.ocr_service.procesar_ocr_imagen_local(file)
            else:
                resultado = self.ocr_service.procesar_ocr_imagen_openai(file)
            
            return resultado, 200
            
        except ConnectionError as ce:
            return {"error": str(ce)}, 503
        except Exception as e:
            self.utils.logger.error("Error en /ocr", exc_info=True)
            return {"error": "Error interno al procesar imagen OCR"}, 500

    def endpoint_ocr_archivo(self) -> Tuple[Dict[str, Any], int]:
        """Endpoint para procesar archivos (PDF/texto)."""
        try:
            file = request.files.get('file')
            if not file or not self.utils.allowed_file(file.filename):
                return {"error": "Tipo de archivo no permitido"}, 400
            if self.DEMO_MODE:
                return self.utils.simulador_respuesta_demo(), 200
            
            resultado = self.ocr_service.procesar_ocr_archivo(file)
            return resultado, 200
            
        except ConnectionError as ce:
            return {"error": str(ce)}, 503
        except Exception as e:
            self.utils.logger.error("Error en /ocr-file", exc_info=True)
            return {"error": "Error interno al procesar archivo OCR"}, 500

    def endpoint_chat_service(self) -> Tuple[Dict[str, Any], int]:
        """Endpoint para procesar texto del chat."""
        try:
            data = request.get_json(silent=True)
            if not data:
                return {"error": "JSON inválido o no enviado"}, 400
            
            mensaje = data.get("mensaje")
            if not mensaje:
                return {"error": "Debe enviar un campo 'mensaje' en JSON"}, 400
            
            
            if self.DEMO_MODE:
                return {"respuesta": "Bienvenido a Gesthor, Modo demo activado"}, 200
            
            resultado = self.chat_service.responder_chat(mensaje)
            return resultado, 200
        
        except ConnectionError as ce:
            return {"error": str(ce)}, 503
        except Exception as e:
            self.utils.logger.error("Error en /aichat", exc_info=True)
            return {"error": "Error interno al generar respuesta"}, 500


    def endpoint_status_server(self) -> Tuple[Dict[str, Any], int]:
        """Endpoint de estado del servicio."""
        response = {
            "statusServer": True,
            "demo": not self.DEMO_MODE,
            "ocrLocal": self.OCR_LOCAL
        }
        return response, 200