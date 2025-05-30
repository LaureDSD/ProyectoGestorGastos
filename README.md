# **GesThor - Gestor de Gastos con OCR e IA para Recomendacións**

[![Ask DeepWiki](https://deepwiki.com/badge.svg)](https://deepwiki.com/LaureDSD/ProyectoGestorGastos)
![Java](https://img.shields.io/badge/Java-17%2B-blue)
![SpringBoot](https://img.shields.io/badge/Spring_Boot-3.1%2B-green)
![MySQL](https://img.shields.io/badge/MySQL-8.0%2B-orange)
![Python](https://img.shields.io/badge/Python-3.8%2B-blue)
![Flask](https://img.shields.io/badge/Flask-2.0%2B-green)
![Tesseract](https://img.shields.io/badge/Tesseract-OCR-orange)

## **Objetivos**

- **Gestión de gastos**: Los usuarios pueden registrar, visualizar y organizar sus gastos por categorías, días, meses y años.
- **OCR**: Escaneo y extracción de datos de tickets y facturas con tecnología OCR (Tesseract) para obtener información precisa de los productos y precios.
- **Recomendaciones IA**: El sistema de IA analiza el historial de gastos del usuario y sugiere formas de ahorro o lugares para comprar productos.
- **Chatbot integrado**: Para ofrecer recomendaciones, responder preguntas y proporcionar información adicional.
- **Interfaz personalizable**: Los usuarios pueden elegir entre un tema claro u oscuro para adaptar la experiencia a sus preferencias.
- **Seguridad robusta**: Autenticación de usuarios mediante tokens JWT y cifrado para garantizar la privacidad y seguridad de los datos.
- **Sincronización de datos**: Acceso a la información desde cualquier dispositivo, manteniendo los datos actualizados y sincronizados.

## **Tecnologías utilizadas**

- **Frontend**:
  - **Angular**: Framework para el desarrollo de la interfaz de usuario.
  - **Bootstrap**: Biblioteca de CSS para diseño responsivo y atractivo.
  - **TypeScript**: Lenguaje de programación para el frontend.

- **Backend**:
  - **Spring Boot**: Framework para la gestión del backend, operaciones CRUD y autenticación de usuarios.
  - **Python**: Utilizado para procesar el OCR, realizar análisis de datos y ofrecer recomendaciones mediante IA.
  - **MySQL**: Base de datos relacional para almacenar los datos de los usuarios y los tickets.
  - **JWT (JSON Web Token)**: Para la autenticación y autorización de usuarios.

- **OCR**:
  - **Tesseract OCR**: Herramienta para el reconocimiento óptico de caracteres en imágenes y documentos.

- **Almacenamiento**:
  - **Almacenamiento interno**: Los tickets y facturas escaneados se almacenan internamente en el servidor para su gestión y posterior acceso.
  - **Posibilidad de expansion a almacenaminto y seguridad en la nube.
    
- **Control de versions**:
  - **GitHub
