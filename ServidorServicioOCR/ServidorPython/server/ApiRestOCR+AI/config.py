import os
from dotenv import load_dotenv
from typing import Set

class Config:
    """Clase de configuración centralizada."""

    @classmethod
    def load_from_env(cls):
        """Carga la configuración desde variables de entorno priorizando las del sistema."""
        load_dotenv()

        def get_env_var(key, default=None):
            # Primero intenta del sistema, luego del .env (a través de getenv)
            val = os.environ.get(key)
            if val is not None:
                return val
            return os.getenv(key, default)

        cls.DEMO_MODE = get_env_var("FLASK_DEMO_MODE", "false").lower() == "true"
        cls.OCR_LOCAL = get_env_var("FLASK_OCR_LOCAL", "true").lower() == "true"
        cls.TESSERACT_CMD = get_env_var("TESSERACT_CMD", r"C:\Program Files\Tesseract-OCR\tesseract.exe")
        cls.LOCAL_API_KEY = get_env_var("FLASK_LOCAL_API_KEY")
        cls.MODEL = get_env_var("FLASK_MODEL", "gpt-3.5-turbo")
        cls.EXTENSIONS = set(get_env_var("FLASK_EXTENSIONS", "jpg,jpeg,png,pdf,txt").split(","))
        cls.MAX_CONTENT_LENGTH_MB = int(get_env_var("FLASK_MAX_CONTENT_LENGTH_MB", 10))
        cls.HOST = get_env_var("FLASK_HOST", "0.0.0.0")
        cls.PORT = int(get_env_var("FLASK_PORT", 5000))
        cls.DEBUG_MODE = get_env_var("FLASK_DEBUG_MODE", "false").lower() == "true"

        cls.OPENAI_API_KEY = get_env_var("FLASK_OPENAI_API_KEY")

        if not cls.OPENAI_API_KEY:
            raise ValueError("OPENAI_API_KEY no está configurada")
