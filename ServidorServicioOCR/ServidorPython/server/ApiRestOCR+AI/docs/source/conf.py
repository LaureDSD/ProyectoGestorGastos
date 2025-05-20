import os
import sys
from datetime import datetime

# Asegúrate que esta ruta apunta al directorio donde está tu módulo Python
sys.path.insert(0, os.path.abspath('../..'))

project = 'ApiRestOCR+AI'
copyright = f'{datetime.now().year}, LaureanoDeSousaDias'
author = 'LaureanoDeSousaDias'
release = '3.0.1'

extensions = [
    'sphinx.ext.autodoc',
    'sphinx.ext.napoleon',
    'sphinx.ext.viewcode',
    'sphinx.ext.autosummary',
    'sphinx_autodoc_typehints',
]

# Configuración importante para autodoc
autodoc_default_options = {
    'members': True,
    'member-order': 'bysource',
    'special-members': '__init__',
    'undoc-members': True,
    'exclude-members': '__weakref__'
}

# Configuración para type hints
always_document_param_types = True
typehints_fully_qualified = False

templates_path = ['_templates']
exclude_patterns = []

# Idioma español
language = 'es'

# Configuración del tema
html_theme = 'sphinx_rtd_theme'
html_static_path = ['_static']

# Evita la creación automática de archivos .rst redundantes
autosummary_generate = False
autosummary_generate_overwrite = False

# Configuración para evitar duplicados
autodoc_default_options = {
    'inherited-members': False,
    'no-inherited-members': True
}