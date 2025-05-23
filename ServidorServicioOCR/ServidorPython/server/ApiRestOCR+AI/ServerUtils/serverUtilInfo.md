# Actualizamos el contenido para quitar las etiquetas ```bash y usar comentarios en su lugar
documentation_guide_clean = """
# 🧾 Guía para Generar la Documentación con Sphinx

Este documento describe el flujo completo para generar y actualizar la documentación del proyecto utilizando **Sphinx**.

---

## 📦 Instalación de dependencias

Asegúrate de tener instalado Sphinx y los plugins necesarios:

# pip install sphinx sphinx-autodoc-typehints sphinx-rtd-theme

Si tu proyecto tiene un archivo `requirements.txt`, puedes instalar todo lo necesario con:

# pip install --no-cache-dir -r requirements.txt

---

## 🚀 Inicialización de Sphinx (solo una vez)

> Ya realizado, solo se incluye como referencia.

# sphinx-quickstart docs

---

## 🔄 Generar archivos automáticos de documentación

> Estos comandos generan automáticamente los archivos `.rst` a partir del código fuente.

# sphinx-apidoc -o docs/source .
# sphinx-apidoc -o docs . --force

---

## 🧹 Limpiar documentación previa

Borra la carpeta `build` para una compilación limpia:

# PowerShell
# Remove-Item -Recurse -Force docs/build

# Unix/macOS
# rm -rf docs/build

---

## 🔧 Generar la documentación HTML

Desde la carpeta raíz del proyecto, ejecuta:

# python -m sphinx build docs/source docs/build

> Asegúrate de que las rutas `source` y `build` coincidan con la estructura de tu proyecto.

---

## ✅ Comprobaciones

Verifica que tu aplicación se importe correctamente:

# python -c "from OcrAiServer import app; print(dir(app))"

---

## 📂 Estructura esperada de carpetas

project/
│
├── OcrAiServer/          # Código fuente
├── docs/
│   ├── build/            # Salida HTML generada
│   ├── source/           # Archivos .rst
│   └── conf.py           # Configuración de Sphinx
├── requirements.txt
└── ...

---

## 🌐 Acceder a la documentación generada

Abre en tu navegador el archivo:

docs/build/index.html
"""

# Guardar el archivo en formato Markdown
output_path = Path("docs/DOCUMENTATION_GUIDE.md")
output_path.write_text(documentation_guide_clean.strip(), encoding="utf-8")

