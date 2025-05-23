import os
from dotenv import load_dotenv
from typing import Set

class Config:
    """Clase de configuración centralizada."""
    
    @classmethod
    def load_from_env(cls):
        """Carga la configuración desde variables de entorno."""
        load_dotenv()
        
        cls.DEMO_MODE = os.getenv("DEMO_MODE", "false").lower() == "true"
        cls.OCR_LOCAL = os.getenv("OCR_LOCAL", "true").lower() == "true"
        cls.TESSERACT_CMD = os.getenv("TESSERACT_CMD", r"C:\Program Files\Tesseract-OCR\tesseract.exe")
        cls.LOCAL_API_KEY = os.getenv("LOCAL_API_KEY")
        cls.MODEL = os.getenv("MODEL", "gpt-3.5-turbo")
        cls.OPENAI_API_KEY = os.getenv("OPENAI_API_KEY")
        cls.EXTENSIONS = set(os.getenv("EXTENSIONS", "jpg,jpeg,png,pdf,txt").split(","))
        cls.MAX_CONTENT_LENGTH_MB = int(os.getenv("MAX_CONTENT_LENGTH_MB", 10))
        cls.HOST = os.getenv("HOST", "0.0.0.0")
        cls.PORT = int(os.getenv("PORT", 5000))
        cls.DEBUG_MODE = os.getenv("DEBUG_MODE", "false").lower() == "true"
        
        if not cls.OPENAI_API_KEY:
            raise ValueError("OPENAI_API_KEY no está configurada")