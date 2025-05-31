# Gesthor Frontend Angular (GFA v0.1)

[![Ask DeepWiki](https://deepwiki.com/badge.svg)](https://deepwiki.com/LaureDSD/ProyectoGestorGastos)
![Angular](https://img.shields.io/badge/Angular-19.2.0-red?logo=angular)
![Bootstrap](https://img.shields.io/badge/Bootstrap-5.3.5-purple?logo=bootstrap)
![Express](https://img.shields.io/badge/Express-4.18.2-black?logo=express)
![Node.js](https://img.shields.io/badge/Node.js-18-green?logo=node.js)
![FontAwesome](https://img.shields.io/badge/FontAwesome-6.7.2-blue?logo=fontawesome)
![Chart.js](https://img.shields.io/badge/Chart.js-4.4.9-orange?logo=chartdotjs)

---

## Descripción

Proyecto Gesthor Angular para gestión de gastos. Frontend desarrollado con Angular 19, backend API separado.  
Incluye autenticación Google y GitHub, gráficos, y conexión mediante API REST.

---

### Requisitos:
- Node.js v18 o superior
- Acceso a Internet para instalar dependencias

### Descargas

[GFA v0.1 EasyUse .zip](https://drive.google.com/file/d/1FZJiu5xbdG1lI8NlbOcATBWTCgy24tnr/view?usp=drive_link)

### Dependencias del Proyecto:
```
npm install 
```

- @angular/common@^19.2.0 \
- @angular/compiler@^19.2.0 \
- @angular/core@^19.2.0 \
- @angular/forms@^19.2.0 \
- @angular/platform-browser@^19.2.0 \
- @angular/platform-browser-dynamic@^19.2.0 \
- @angular/router@^19.2.0 \
- @fortawesome/fontawesome-free@^6.7.2 \
- bootstrap@^5.3.5 \
- chart.js@^4.4.9 \
- jspdf@^3.0.1 \
- jspdf-autotable@^5.0.2 \
- ng2-charts@^4.1.1 \
- rxjs@~7.8.0 \
- tslib@^2.3.0 \
- zone.js@~0.15.0\
- express@4.18.2


---
## Variables para frontend (Proyecto)

Solo se requiere configurar:

```ts
export const environment = {
  production: true,
  apiUrl: 'http://localhost:8080/api' // URL base del backend API
};
```

## Acceder al frontend

Home público: http://localhost:4200/public/home

Login: http://localhost:4200/login


---

##  Accesos al sistema

### Usuarios de prueba:

| Rol    | Usuario               | Contraseña |
|--------|------------------------|------------|
| Admin  | admin@gesthor.com      | admin      |
| Usuario| user@gesthor.com       | user       |


### Uso de autenticación externa

- El backend soporta login vía Google y GitHub OAuth2.
- Debe configurarse el backend con las claves correspondientes.

## Despliege del servidor
1. Descomprimir el ZIP en una carpeta.
2. Contendrá:
   - Carpeta gesthor-angular/browser/ con los archivos compilados del frontend
   - Archivo server.js para servir el frontend con Express 
   - Archivo start-server.bat para instalar dependencias y lanzar el servidor

3. Doble clic sobre `start_server.bat`:
   - Si es la **primera vez**, instalará `express` y abrirá el servidor en otra terminal.
   - Si ya está instalado, lo lanzará directamente.

4. Abrir navegador en: http://localhost:4200

### Notas adicionales
- El .bat se encarga de instalar dependencias necesarias y ejecutar el servidor Express.
- No es necesario compilar manualmente el frontend, ya viene precompilado en la carpeta browser.
