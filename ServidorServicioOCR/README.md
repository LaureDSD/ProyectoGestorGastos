# OCR + AI Ticket Processing Service (SOCRAI)

![Python](https://img.shields.io/badge/Python-3.8%2B-blue)
![Flask](https://img.shields.io/badge/Flask-2.0%2B-green)
![Tesseract](https://img.shields.io/badge/Tesseract-OCR-orange)

Este proyecto proporciona un servicio REST que combina OCR (Reconocimiento Óptico de Caracteres) con inteligencia artificial para procesar tickets digitales. El sistema permite cargar tickets mediante fotos o documentos, aunque actualmente la funcionalidad de carga de tickets digitales presenta algunas limitaciones.

## Limitaciones
- La carga de ticket digital esat limitada **No reconoce todos los formatos**
- Subir fotos puede fallar - intenta almenos 3 vezes

## Requisitos

- Windows OS
- Python 3.8+
- Tesseract OCR instalado en `C:\Archivos de Programa`
  - Download from [UB-Mannheim/tesseract](https://github.com/UB-Mannheim/tesseract/wiki)
  - Incluido en la carpeta del proyecto

## Instalación

### 1. Instalar dependdencias


#### Dependencias:
- Flask
- python-dotenv
- pydantic
- Pillow
- openai==0.28.0 (Compatible)
- Werkzeug
- pytz
- flasgger
- ratelimit
- cachetools
- requests
- pytesseract
- opencv-python
- numpy
- pdf2image
- PyPDF2
- flask-cors
- waitress


    pip install -r requirements.txt
    # OR
    pip install --no-cache-dir -r requirements.txt


### 2 Configuración del entorno

## Variables de entorno clave
| Variable               | Descripción                     | Valor por defecto       |
|-----------------------|--------------------------------|-------------------------|
| DEMO_MODE             | Modo demostración              | false                   |
| OCR_LOCAL             | Usar OCR local                 | true                    |
| MODEL                 | Modelo de OpenAI a usar        | gpt-3.5-turbo           |
| EXTENSIONS            | Formatos de archivo aceptados  | jpg,jpeg,png,pdf,txt,webp |
| MAX_CONTENT_LENGTH_MB | Tamaño máximo de archivo (MB)  | 10                      |
| HOST                  | Host del servidor              | 0.0.0.0                 |
| PORT                  | Puerto del servidor            | 5000                    |
| DEBUG_MODE            | Modo depuración                | true                    |
| LOCAL_API_KEY         | API key para autenticación     | CREAR_API_KEY           |
| OPENAI_API_KEY        | API key de OpenAI              | Requerida               |

You can configure environment variables in three ways:

### Option A: Variables de sistema SETX CMD (Windows)
 - Para temporal usar set en consola antes de lanzar exe o main


    setx DEMO_MODE false
    setx OCR_LOCAL true
    setx MODEL "gpt-3.5-turbo"
    setx EXTENSIONS "jpg,jpeg,png,pdf,txt,webp,jpeg"
    setx MAX_CONTENT_LENGTH_MB 10
    setx HOST 0.0.0.0
    setx PORT 5000
    setx DEBUG_MODE true
    setx LOCAL_API_KEY "your_api_key_here"
    setx OPENAI_API_KEY "your_openai_key_here"


### Option B: Usar archivo .env 

Crea o usa `.env` en la raiz del directorio con lo siguiente:


    DEMO_MODE=false
    DEBUG_MODE=true
    OCR_LOCAL=true
    EXTENSIONS=jpg,jpeg,png,pdf,txt,webp
    MAX_CONTENT_LENGTH_MB=10
    MODEL=gpt-3.5-turbo
    HOST=0.0.0.0
    PORT=5000
    LOCAL_API_KEY=your_api_key_here
    OPENAI_API_KEY=your_openai_key_here


# Ejecución del servidor
## Método 1: Usando el ejecutable
 - Navegue a la carpeta dist
 - Ejecute el archivo main.exe

## Método 2: Desde código fuente
 - (Opcional) Active el entorno virtual: .\venv\Scripts\activate
 - Ejecute el servidor: python main.py

## Documentación
 #### Para generar la documentación:
 - Acceda a la carpeta docs
 - Ejecute el comando sphinx-build -b html source/ _build/html
 - La documentación estará disponible en docs/_build/html/index.html

## Notas importantes
 - La carga de tickets por foto puede fallar - se recomienda intentarlo varias veces
 - Tesseract debe estar instalado en C:\Archivos de Programa
 - Para desarrollo, puede usar el modo debug activando DEBUG_MODE=true
 - El servidor se ejecuta por defecto en http://0.0.0.0:5000

## Construir ejecutable
 - Para crear un nuevo ejecutable:
 - Ejecute el comando pyinstaller --onefile main.py
 - El ejecutable se generará en la carpeta dist.


## Solución de problemas
 - Si hay problemas con Tesseract, reinstale desde UB-Mannheim/tesseract
 - Para actualizar dependencias, ejecute pip install --no-cache-dir -r requirements.txt
 - En caso de errores, active DEBUG_MODE=true para más información

## Adicional
 - En la carpeta del sevidor entraras , la version anterior, en carpeta old.
 - Se agrego el instalador de tesseract en la carpeta del proyecto.
 - En caso de fallo, cuentas con la version original del servidor en old.
 - Dara fallo al no tener clave de OPENAI en el fichero env.
 - Para probar que responde pon el modo demo.
 - Para usar la ia, usa modo local con modo demo desactivado.

#### IMPORTANTE: GPT3.5 no permite archivos, es necesario tener activo ocr local,cob gpt mas modernos si permite usar sin OCR local yes cuando si fincionala carga directa de imagenes y archivos..

#### ATENCION: Carga de tickets digitales no esta fucnional del todo. La carga de tickets por foto tiene tasa de fallo, prueba mas de 1 vez...

#### OBLIGATORIO INSTALAR TESSERCT EN C/aRCHIVOS DE PROGRAMA
https://github.com/UB-Mannheim/tesseract/wiki

#### PARA ACTUALIZAR SERVIDOR, ACCEDE AL ENV (Desde carpeta ApiRestOCR+AI):
 - venv\Scripts\activate
 - Desde docs (Carpeta docs):
 - sphinx-build -b html source/ _build/html

#### NO OLVIDES:
 - pip install --no-cache-dir -r requirements.txt
 
#### PARA USAR EL SERVIDOR, LANZA EL SERVER DESDE LA CARPETA CON PYTHON (NO OBLIGATORIO ESTAR EN EL VENV).
 - .\venv\Scripts\actívate
 - sphinx-build -b html source/ _build/html
 - pyinstaller --onefile main.py


