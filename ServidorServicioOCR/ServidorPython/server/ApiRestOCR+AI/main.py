"""
Módulo principal del servidor de procesamiento de tickets con OCR e IA.

Este servidor Flask proporciona endpoints para:

- Procesamiento de imágenes de tickets usando OCR (local o con OpenAI)
- Procesamiento de archivos de texto/PDF de tickets
- Interacción con modelos de IA para análisis de tickets
- Chat asistido por IA
"""

import os
import logging
from logging.handlers import RotatingFileHandler
from dotenv import dotenv_values, load_dotenv
from flask import Flask
from flask_cors import CORS
import pytesseract
import openai



# Importaciones de clases propias
from utils import Utils
from services import OcrService, ChatService
from apiController import ApiController
from config import Config



def configure_app() -> Flask:
    """Configura y retorna la aplicación Flask."""
    app = Flask(__name__)
    CORS(app)
    
    # Configuración básica de Flask
    app.config['ALLOWED_EXTENSIONS'] = Config.EXTENSIONS
    app.config['MAX_CONTENT_LENGTH'] = Config.MAX_CONTENT_LENGTH_MB * 1024 * 1024

    
    
    return app

def setup_logging(app: Flask) -> None:
    """Configura el sistema de logging."""
    if not os.path.exists('logs'):
        os.makedirs('logs')
    
    handler = RotatingFileHandler('logs/server.log', maxBytes=10*1024*1024, backupCount=3)
    handler.setFormatter(logging.Formatter(
        '%(asctime)s - %(name)s - %(levelname)s - %(message)s'
    ))
    
    app.logger.addHandler(handler)
    app.logger.setLevel(logging.INFO)

def initialize_services() -> tuple:
    """Inicializa y retorna los servicios principales."""
    
   
    
    utils = Utils(
        allowed_extensions=Config.EXTENSIONS,
        api_key=Config.LOCAL_API_KEY,
        log_file='logs/utils.log'
    )
    
    ocr_service = OcrService(
        openai_api_key=Config.OPENAI_API_KEY,
        model=Config.MODEL,
        logger=utils.logger
    )
    
    chat_service = ChatService(
        openai_api_key=Config.OPENAI_API_KEY,
        model=Config.MODEL,
        logger=utils.logger
    )
    
    return utils, ocr_service, chat_service

def create_api_controller(utils, ocr_service, chat_service) -> ApiController:
    """Crea y configura el controlador de API."""
    return ApiController(
        ocr_service=ocr_service,
        chat_service=chat_service,
        utils=utils,
        demo_mode=Config.DEMO_MODE,
        ocr_local=Config.OCR_LOCAL
    )

def main():
    """Función principal de entrada."""
    # Cargar configuración
    load_dotenv()
    Config.load_from_env()
    
    # Configurar aplicación Flask
    app = configure_app()
    setup_logging(app)
    
    # Configurar Tesseract
    pytesseract.pytesseract.tesseract_cmd = Config.TESSERACT_CMD
    
    # Configurar OpenAI
    openai.api_key = Config.OPENAI_API_KEY
    
    # Inicializar servicios
    utils, ocr_service, chat_service = initialize_services()
    
    # Crear controlador API
    api_controller = create_api_controller(utils, ocr_service, chat_service)
    
    # Registrar blueprints
    app.register_blueprint(api_controller.blueprint, url_prefix='/api')
    
    # Mostrar información de configuración
    app.logger.info(f"Iniciando servidor en modo {'DEMO' if Config.DEMO_MODE else 'PRODUCCIÓN'}")
    app.logger.info(f"Usando OCR {'LOCAL' if Config.OCR_LOCAL else 'OPENAI VISION'}")
    app.logger.info(f"Modelo OpenAI: {Config.MODEL}")
    
    return app

if __name__ == "__main__":
    app = main()
    app.run(
        host=Config.HOST,
        port=Config.PORT,
        debug=Config.DEBUG_MODE,
        threaded=True
    )