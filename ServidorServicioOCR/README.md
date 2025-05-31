# OCR + AI Ticket Processing Service (SOCRAI v0.0.2)

[![Ask DeepWiki](https://deepwiki.com/badge.svg)](https://deepwiki.com/LaureDSD/ProyectoGestorGastos)
![Python](https://img.shields.io/badge/Python-3.8%2B-blue)
![Flask](https://img.shields.io/badge/Flask-2.0%2B-green)
![Tesseract](https://img.shields.io/badge/Tesseract-OCR-orange)

Este proyecto proporciona un servicio REST que combina OCR (Reconocimiento Óptico de Caracteres) con inteligencia artificial para procesar tickets digitales. El sistema permite cargar tickets mediante fotos o documentos, aunque actualmente la funcionalidad de carga de tickets digitales presenta algunas limitaciones.

## Limitaciones:
- La carga de ticket digital esta limitada **No reconoce todos los formatos**
- Subir fotos puede fallar - intenta almenos 3 vezes

## Requisitos:

- Windows OS
- Python 3.8+
- Tesseract OCR instalado en `C:\Archivos de Programa`
  - Download from [UB-Mannheim/tesseract](https://github.com/UB-Mannheim/tesseract/wiki)
  - Incluido en la carpeta del proyecto

## Descargas:
- [Descargar SOCRAI v0.0.2 .exe](https://drive.google.com/file/d/1HqVd1Lb9uN0B6H4frqGTzn98RgdsHBh-/view?usp=drive_link)
- [Descargar SOCRAI v0.0.2 .zip](https://drive.google.com/file/d/1dg4yfwz9AY8-QUIp7C-ivSOXKJjzWI4u/view?usp=drive_link)

## Instalación:

### 1. Instalar dependencias (No necesario para ejecutable)
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

#### Variables de entorno clave
| Variable               | Descripción                     | Valor por defecto       |
|-----------------------|--------------------------------|-------------------------|
| FLASK_DEMO_MODE             | Modo demostración              | false                   |
| FLASK_OCR_LOCAL             | Usar OCR local                 | true                    |
| FLASK_MODEL                 | Modelo de OpenAI a usar        | gpt-3.5-turbo           |
| FLASK_EXTENSIONS            | Formatos de archivo aceptados  | jpg,jpeg,png,pdf,txt,webp |
| FLASK_MAX_CONTENT_LENGTH_MB | Tamaño máximo de archivo (MB)  | 10                      |
| FLASK_HOST                  | Host del servidor              | 0.0.0.0                 |
| FLASK_PORT                  | Puerto del servidor            | 5000                    |
| FLASK_DEBUG_MODE            | Modo depuración                | true                    |
| FLASK_LOCAL_API_KEY         | API key para autenticación     | CREAR_API_KEY           |
| FLASK_OPENAI_API_KEY        | API key de OpenAI              | Requerida               |


#### Option A: Variables de sistema SETX CMD (Windows , Recomendada para ejecutable)
 - Para temporal usar 'set' en vez de 'setx' antes de lanzar exe o main


    setx FLASK_DEMO_MODE false
    setx FLASK_OCR_LOCAL true
    setx FLASK_MODEL "gpt-3.5-turbo"
    setx FLASK_EXTENSIONS "jpg,jpeg,png,pdf,txt,webp,jpeg"
    setx FLASK_MAX_CONTENT_LENGTH_MB 10
    setx FLASK_HOST 0.0.0.0
    setx FLASK_PORT 5000
    setx FLASK_DEBUG_MODE true
    setx FLASK_LOCAL_API_KEY "your_api_key_here"
    setx FLASK_OPENAI_API_KEY "your_openai_key_here"


#### Option B: Usar archivo .env 

Crea o usa `.env` en la raiz del directorio con lo siguiente:


    FLASK_DEMO_MODE=false
    FLASK_DEBUG_MODE=true
    FLASK_OCR_LOCAL=true
    FLASK_EXTENSIONS=jpg,jpeg,png,pdf,txt,webp
    FLASK_MAX_CONTENT_LENGTH_MB=10
    FLASK_MODEL=gpt-3.5-turbo
    FLASK_HOST=0.0.0.0
    FLASK_PORT=5000
    FLASK_LOCAL_API_KEY=your_api_key_here
    FLASK_OPENAI_API_KEY=your_openai_key_here


# Ejecución del servidor
## Método 1: Usando el ejecutable
 - Navegue a la carpeta dist (Localización de los ejecutables generados) o arriba en Descargas
 - Ejecute el archivo  SOCRAI_vx.x.x.exe 
 - Si no usas variables de sistema asegurate de agregar el .env al nivel del ejecutable.
 - El ejecutable puedes moverlo a donde quieras mientras tenga variables de sistema o .env

## Método 2: Desde código fuente
 - (Opcional) Active el entorno virtual: .\venv\Scripts\activate
 - Ejecute el servidor: python main.py
 

## Documentación
 #### Para generar la documentación:
 - Acceda a la carpeta docs
 - Ejecute el comando sphinx-build -b html source/ _build/html
 - La documentación estará disponible en docs/_build/html/index.html

## Endpoints

### POST /ocr
 - Descripción: Procesa una imagen (JPG, PNG, etc.) mediante OCR (local o remoto con OpenAI).
 - Parámetros:
```
file: archivo de imagen en multipart/form-data.
 ```
 - Cabecera requerida:
```
  x-api-key: clave de autenticación configurada como FLASK_LOCAL_API_KEY.
```
- Respuesta:
```
200 OK: Resultado del OCR en formato JSON.

400 Bad Request: Si falta el archivo o no tiene extensión permitida.

503 Service Unavailable: Si falla la conexión (por ejemplo, con OpenAI).

500 Internal Server Error: Si ocurre un error inesperado.

Demo mode: Devuelve una respuesta simulada.
```

### POST /ocr-file
- Descripción: Procesa un archivo PDF o de texto usando OCR.

- Parámetros:
```
file: archivo PDF o TXT en multipart/form-data.
```
- Cabecera requerida:
```
x-api-key: clave de autenticación.
```
-Respuesta:
```
200 OK: Resultado procesado del archivo en JSON.

400 Bad Request: Si falta el archivo o no está permitido.

503 Service Unavailable: Si hay problemas de conexión.

500 Internal Server Error: Si ocurre un error durante el procesamiento.

Demo mode: Devuelve una respuesta simulada.
```
### POST /aichat
-Descripción: Genera una respuesta de IA a un mensaje de texto.

-Parámetros (JSON):
```
{
"mensaje": "Texto que deseas enviar a la IA"
}
```
- Cabecera requerida:
```
x-api-key: clave de autenticación.
```
- Respuesta:
```
200 OK: Devuelve la respuesta generada por la IA, ejemplo:
{ "respuesta": "Texto de respuesta generada" }

400 Bad Request: Si no se envía un JSON válido o el campo mensaje está ausente.

503 Service Unavailable: Si falla la conexión con el modelo.

500 Internal Server Error: Si hay un error interno.

Demo mode: Devuelve una respuesta fija tipo:
{ "respuesta": "Bienvenido a Gesthor, Modo demo activado" }
```
### GET /status
- Descripción: Verifica si el servidor está funcionando.

- Parámetros: Ninguno.

- Respuesta:
```
{
"statusServer": true,
"demo": false,         // o true si está en modo demo
"ocrLocal": true       // o false si usa OpenAI para OCR
}
Código HTTP: 200 OK.
```

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


