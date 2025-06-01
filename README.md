# **GesThor - Gestor de Gastos con OCR e IA**

[![Ask DeepWiki](https://deepwiki.com/badge.svg)](https://deepwiki.com/LaureDSD/ProyectoGestorGastos)
![Java](https://img.shields.io/badge/Java-17%2B-blue)
![SpringBoot](https://img.shields.io/badge/Spring_Boot-3.1%2B-green)
![MySQL](https://img.shields.io/badge/MySQL-8.0%2B-orange)
![Python](https://img.shields.io/badge/Python-3.8%2B-blue)
![Flask](https://img.shields.io/badge/Flask-2.0%2B-green)
![Tesseract](https://img.shields.io/badge/Tesseract-OCR-orange)
![Angular](https://img.shields.io/badge/Angular-19.2.0-red?logo=angular)
![Bootstrap](https://img.shields.io/badge/Bootstrap-5.3.5-purple?logo=bootstrap)
![Express](https://img.shields.io/badge/Express-4.18.2-black?logo=express)
![Node.js](https://img.shields.io/badge/Node.js-18-green?logo=node.js)
![FontAwesome](https://img.shields.io/badge/FontAwesome-6.7.2-blue?logo=fontawesome)
![Chart.js](https://img.shields.io/badge/Chart.js-4.4.9-orange?logo=chartdotjs)

## **Autor**
- Laurenao De Sousa Dias

## **Objetivos**

- **Gestión de gastos**: Los usuarios pueden registrar, visualizar y organizar sus gastos por categorías, días, meses y años.
- **OCR**: Escaneo y extracción de datos de tickets y facturas con tecnología OCR (Tesseract) para obtener información precisa de los productos y precios.
- **Chatbot integrado**: Para ofrecer recomendaciones, responder preguntas y proporcionar información adicional.
- **Seguridad robusta**: Autenticación de usuarios mediante tokens JWT y cifrado para garantizar la privacidad y seguridad de los datos.
- **Sincronización de datos**: Acceso a la información desde cualquier dispositivo, manteniendo los datos actualizados y sincronizados.


## Estructura y documentación
- [Documentación técnica](https://github.com/LaureDSD/ProyectoGestorGastos/tree/main/Documentacion/ManualT%C3%A9cnico)
- [Memoria 30/05/25](https://github.com/LaureDSD/ProyectoGestorGastos/blob/main/Documentacion/Memoria%20Completa%20Laureano%20De%20Sousa%20Dias%20Gesthor.pdf)
- [Dificultades 30/05/25](https://github.com/LaureDSD/ProyectoGestorGastos/blob/main/Documentacion/Memoria%20Dificulatdes%20Laureano%20de%20Sousa%20Dias%20.pdf)
- [Alcance 30/05/25](https://github.com/LaureDSD/ProyectoGestorGastos/blob/main/Documentacion/Dfinici%C3%B3n%20de%20alcance%20Laureano%20De%20Sousa%20Dias.pdf)
- [Planos PUML](https://github.com/LaureDSD/ProyectoGestorGastos/tree/main/Documentacion/Archivos%20PUML)
- [Planos EER](https://github.com/LaureDSD/ProyectoGestorGastos/tree/main/Documentacion/Planos%20EER)
- [Documentacion Tecnica Autogenerada (DeepWiki)](https://deepwiki.com/LaureDSD/ProyectoGestorGastos)

## **Tecnologías utilizadas**

### **Manual MySQL**
  - [Manual instalación](https://github.com/LaureDSD/ProyectoGestorGastos/tree/main/ServidorDataBase)


### **Frontend Angular**:
  - [Acceso al servidor Angular](https://github.com/LaureDSD/ProyectoGestorGastos/tree/main/ServidorWebFrontend)
  - [Documentación técnica](https://github.com/LaureDSD/ProyectoGestorGastos/tree/main/Documentacion/ManualT%C3%A9cnico/ServidorAngular)
  - [Manual de usuario](https://github.com/LaureDSD/ProyectoGestorGastos/blob/main/Documentacion/ManualDeUsuario/Manual%20De%20Usuario%20GFA.pdf)
  - **Angular**: Framework para el desarrollo de la interfaz de usuario.
  - **Bootstrap**: Biblioteca de CSS para diseño responsivo y atractivo.
  - **TypeScript**: Lenguaje de programación para el frontend.

### **Backend Spring**:
- [Acceso al servidor Spring](https://github.com/LaureDSD/ProyectoGestorGastos/tree/main/ServidorServicioAPI)
- [Documentación técnica](https://github.com/LaureDSD/ProyectoGestorGastos/tree/main/Documentacion/ManualT%C3%A9cnico/ServidorSpring)
- [Manual de usuario](https://github.com/LaureDSD/ProyectoGestorGastos/blob/main/Documentacion/ManualDeUsuario/Manual%20De%20Usuario%20SAPI.pdf)
- **Spring Boot**: Framework para la gestión del backend, operaciones CRUD y autenticación de usuarios.
- **Python**: Utilizado para procesar el OCR, realizar análisis de datos y ofrecer recomendaciones mediante IA.
- **MySQL**: Base de datos relacional para almacenar los datos de los usuarios y los tickets.
- **JWT (JSON Web Token)**: Para la autenticación y autorización de usuarios.
- **Swagger**: Documenatación de endpoints del servidor y pruebas.

### **OCR Flask**:
- [Acceso al servidor Flask](https://github.com/LaureDSD/ProyectoGestorGastos/tree/main/ServidorServicioOCR)
- [Documentación técnica](https://github.com/LaureDSD/ProyectoGestorGastos/tree/main/Documentacion/ManualT%C3%A9cnico/ServidorFlask)
- [Manual de usuario](https://github.com/LaureDSD/ProyectoGestorGastos/blob/main/Documentacion/ManualDeUsuario/Manual%20De%20Usuario%20SOCRAI.pdf)
- **Tesseract OCR**: Herramienta para el reconocimiento óptico de caracteres en imágenes y documentos.
- **ChatGPT 3.5**: Bot de erespuesta autonoma y interprete de datos.
- **Sphynx**: Documentacion de clases del servidor.

### **Utiles**

- Se agregaron utiles y sus documentaciones de ayuda.
- Scripts de arranque rapido para servidores [Probar..](https://github.com/LaureDSD/ProyectoGestorGastos/tree/main/Utils/ScriptsInicio)
- Manual de integracion de SSL y script automatico [Ver..](https://github.com/LaureDSD/ProyectoGestorGastos/tree/main/Utils/ssl)
- Manualde lanzamiento en XAMPP u otros, archivo .htaccess. [Ver..](https://github.com/LaureDSD/ProyectoGestorGastos/tree/main/Utils/WebServers)

### **Almacenamiento**:
- **Almacenamiento interno**: Los diferenstes archivos gastos y perfiles usuarios se almacenan internamente en el servidor para su gestión y posterior acceso.

## Descargar Repositorio
```bash
git clone https://github.com/LaureDSD/ProyectoGestorGastos.git
```