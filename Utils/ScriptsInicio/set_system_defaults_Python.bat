@echo off
REM Variables permanentes - afectan futuras sesiones del sistema
setx FLASK_DEMO_MODE true
setx FLASK_OCR_LOCAL true
setx FLASK_MODEL "gpt-3.5-turbo"
setx FLASK_EXTENSIONS "jpg,jpeg,png,pdf,txt,webp,jpeg"
setx FLASK_MAX_CONTENT_LENGTH_MB 10
setx FLASK_HOST 0.0.0.0
setx FLASK_PORT 5000
setx FLASK_DEBUG_MODE true
setx FLASK_LOCAL_API_KEY "CREAR_API_KEY"
setx FLASK_OPENAI_API_KEY "KEY"

echo Variables de entorno permanentes configuradas. Reinicia la consola para que surtan efecto.
pause
