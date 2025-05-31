# Spring Boot API Server (GestorAPI v1.7)

[![Ask DeepWiki](https://deepwiki.com/badge.svg)](https://deepwiki.com/LaureDSD/ProyectoGestorGastos)
![Java](https://img.shields.io/badge/Java-17%2B-blue)
![SpringBoot](https://img.shields.io/badge/Spring_Boot-3.1%2B-green)
![MySQL](https://img.shields.io/badge/MySQL-8.0%2B-orange)

## Configuración del Entorno

### Descripción
#### GestorAPI es una aplicación Spring Boot que proporciona una API REST con autenticación JWT, integración OAuth2 (Google/GitHub), gestión de usuarios y gastos con operaciones CRUD. Incluye:

1. Autenticación segura con JWT
2. Login mediante Google y GitHub
3. Subida de archivos
4. Integración con servicios externos
5. Documentación Swagger/OpenAPI

### Requisitos previos
1. **Java JDK 22 (Amazon Corretto 22.0.2.9+)** - [Descargar Amazon Corretto](https://aws.amazon.com/es/corretto/)
2. **Maven 3.8+** - [Descargar Maven](https://maven.apache.org/)
3. **MySQL 8.0+** - [Descargar MySQL](https://dev.mysql.com/downloads/)
4. **WiX Toolset v3.11** (para generación de ejecutable) - [Descargar WiX](https://wixtoolset.org/releases/)


### Descargas
- [Servidor GestorAPI v1.7 .exe](https://drive.google.com/file/d/1T7f3toidAd0Mjr0jBmVy2YtdnSNpFF4M/view?usp=drive_link)
- [Servidor GestorAPI v1.7 .zip](https://drive.google.com/file/d/1mc7uOl0mLF4TUfh0LuIB4_u8zjn_DpHe/view?usp=drive_link)
- [Servidor GestorAPI v1.7 .jar](https://drive.google.com/file/d/1gG8EVKGudYv_ffAmS4XQmcnM8zKPDFvX/view?usp=drive_link) (Para ejecutar el .jar descargado, solo siga los pasos 1 y 3 )

## Configuración inicial

#### Configuracionde la base de datos (MySQL)

```bash
CREATE DATABASE gestor_bd;
CREATE USER 'gestor_user'@'localhost' IDENTIFIED BY 'Password123!';
GRANT ALL PRIVILEGES ON gestor_bd.* TO 'gestor_user'@'localhost'; 
FLUSH PRIVILEGES; 
```

## Variables del servidor:

| Variable                      | Descripción                                      |
| ----------------------------- | ------------------------------------------------ |
| `SPRING_APP_NAME`             | Nombre de la aplicación                          |
| `SPRING_DB_URL`               | URL de conexión a la base de datos MySQL         |
| `SPRING_DB_USERNAME`          | Usuario de la base de datos                      |
| `SPRING_DB_PASSWORD`          | Contraseña de la base de datos                   |
| `SPRING_HIBERNATE_DDL`        | Modo de actualización de esquema (`update`)      |
| `SPRING_HIBERNATE_SHOW_SQL`   | Mostrar consultas SQL en consola (`true/false`)  |
| `SPRING_JWT_SECRET`           | Clave secreta para JWT                           |
| `SPRING_JWT_EXPIRATION`       | Minutos de validez del token JWT                 |
| `SPRING_GOOGLE_CLIENT_ID`     | ID cliente para login con Google                 |
| `SPRING_GOOGLE_CLIENT_SECRET` | Secreto cliente de Google                        |
| `SPRING_GOOGLE_REDIRECT_URI`  | URI de redirección de Google                     |
| `SPRING_GITHUB_CLIENT_ID`     | ID cliente para login con GitHub                 |
| `SPRING_GITHUB_CLIENT_SECRET` | Secreto cliente de GitHub                        |
| `SPRING_GITHUB_REDIRECT_URI`  | URI de redirección de GitHub                     |
| `SPRING_CORS_ALLOWED_ORIGINS` | Orígenes permitidos para CORS                    |
| `SPRING_OAUTH2_REDIRECT_URI`  | URI de redirección post-login                    |
| `SPRING_PYTHON_SERVER_URL`    | URL del servidor Python OCR/IA                   |
| `SPRING_PYTHON_API_KEY`       | API key del servidor OCR                         |
| `SPRING_UPLOAD_DIR`           | Directorio donde guardar archivos subidos        |
| `SPRING_MAX_FILE_SIZE`        | Tamaño máximo de archivo                         |
| `SPRING_MAX_REQUEST_SIZE`     | Tamaño máximo de petición                        |
| `SPRING_LOG_SECURITY_LEVEL`   | Nivel de logs de seguridad                       |
| `SPRING_LOGIN_MAX_FAILED`     | Intentos máximos de login fallido                |
| `SPRING_LOGIN_BLOCK_DURATION` | Tiempo de bloqueo en minutos                     |
| `SPRING_LOGIN_DELETE_LOG`     | Eliminar logs de login tras éxito (`true/false`) |


## Configuraciones Basicas de variables:

#### Variables de Seguridad

| Variable                           | Descripción                               | Ejemplo                                |
|-----------------------------------|-------------------------------------------|----------------------------------------|
| `app.jwt.secret`                  | Clave secreta para firmar JWT             | `MiClaveSuperSegura123!@#`             |
| `app.jwt.expiration.minutes`      | Minutos de validez del token              | `1440`                                 |

#### Conexión a base de datos

| Variable                           | Descripción                               | Ejemplo                                |
|-----------------------------------|-------------------------------------------|----------------------------------------|
| `spring.datasource.url`           | URL de conexión JDBC                      | `jdbc:mysql://localhost:3306/gestor_bd`|
| `spring.datasource.username`      | Usuario MySQL                             | `root`                                 |
| `spring.datasource.password`      | Contraseña MySQL                          | `abc123.`                              |

#### OAuth2 (Google & GitHub)

| Variable                                                               | Ejemplo                                                    |
|------------------------------------------------------------------------|------------------------------------------------------------|
| `spring.security.oauth2.client.registration.google.client-id`         | `123.apps.googleusercontent.com`                          |
| `spring.security.oauth2.client.registration.google.client-secret`     | `clave_google`                                             |
| `spring.security.oauth2.client.registration.github.client-id`         | `github_client_id`                                         |
| `spring.security.oauth2.client.registration.github.client-secret`     | `github_client_secret`                                     |

#### Servidor Python externo

| Variable                   | Descripción                                       | Ejemplo                         |
|---------------------------|---------------------------------------------------|---------------------------------|
| `python.server.url`       | URL del backend Python externo                    | `http://localhost:5000`        |
| `python.server.apiKey`    | API key para validar peticiones al servidor Python| `apikey-compartida123`         |



### Para usar Google o GitHub como proveedores OAuth:

- Registra tu app en Google Developer Console y GitHub Developer.

- Agrega client-id y client-secret en el application.properties.

- Configura redirect-uri a http://localhost:8080/login/oauth2/code/google o GitHub según corresponda.

- Agregar clabe de usuario de google y key , igual para git en el archivo properties.

- La clave de la api debe ser igual en el server python y spring

- Configua las rutas a los diferentes servidores.

#### OAuth2 - Google
- spring.security.oauth2.client.registration.google.client-id=TU_CLIENT_ID
- spring.security.oauth2.client.registration.google.client-secret=TU_CLIENT_SECRET

#### OAuth2 - GitHub
- spring.security.oauth2.client.registration.github.client-id=TU_CLIENT_ID
- spring.security.oauth2.client.registration.github.client-secret=TU_CLIENT_SECRET

### Carga de variables de entorno permanentes (Recomendado instalador).
- Recuerda poner tus claves, usar set para sesión y ejecución en consola.

```Bash
    @echo off
    REM ---------- SPRING VARIABLES PERMANENTES ----------
    setx SPRING_APP_NAME GestorAPI
    setx SPRING_DB_URL jdbc:mysql://localhost:3306/gestor_bd
    setx SPRING_DB_USERNAME root
    setx SPRING_DB_PASSWORD abc123.
    setx SPRING_HIBERNATE_DDL update
    setx SPRING_HIBERNATE_SHOW_SQL true
    setx SPRING_JWT_SECRET [REEMPLAZAR_CON_CLAVE]
    setx SPRING_JWT_EXPIRATION 1440
    setx SPRING_GOOGLE_CLIENT_ID [REEMPLAZAR_CON_ID]
    setx SPRING_GOOGLE_CLIENT_SECRET [REEMPLAZAR_CON_SECRET]
    setx SPRING_GOOGLE_REDIRECT_URI http://localhost:8080/login/oauth2/code/google
    setx SPRING_GITHUB_CLIENT_ID [REEMPLAZAR_CON_ID]
    setx SPRING_GITHUB_CLIENT_SECRET [REEMPLAZAR_CON_SECRET]
    setx SPRING_GITHUB_REDIRECT_URI http://localhost:8080/login/oauth2/code/github
    setx SPRING_CORS_ALLOWED_ORIGINS http://localhost:4200
    setx SPRING_OAUTH2_REDIRECT_URI http://localhost:4200/oauth2/redirect
    setx SPRING_PYTHON_SERVER_URL http://localhost:5000
    setx SPRING_PYTHON_API_KEY CREAR_API_KEY
    setx SPRING_UPLOAD_DIR C:/uploads/
    setx SPRING_MAX_FILE_SIZE 5MB
    setx SPRING_MAX_REQUEST_SIZE 5MB
    setx SPRING_LOG_SECURITY_LEVEL DEBUG
    setx SPRING_LOGIN_MAX_FAILED 5
    setx SPRING_LOGIN_BLOCK_DURATION 30
    setx SPRING_LOGIN_DELETE_LOG false
    pause
```

## Instalación del ejecutable .exe (Recomendado)

- Solo necesita las variables de sistema

- Ejecuta el instalador generado (GestorAPI.exe)

- Se instalará por defecto en:

       C:\Program Files (x86)\GestorAPI\

- Abre el acceso directo para iniciar el backend como servicio.

- Accede a http://localhost:8080 para utilizar la app.

## Clonar el repositorio:

```bash
git clone [URL_DEL_REPOSITORIO]
```
#### Instalar dependencias con Maven:

```bash
mvn clean install
```

## Ejecución como .Jar

- Directorio de generación del .jar
```
    GestorAPI/
    ├── src/
    ├── target/
    │   ├── GestorAPI-0.0.1-SNAPSHOT.jar #Se puede portar
    ├── .gitignore
    ├── pom.xml
    └── README.md
```
1. Necesita tener instalado el JSK

   - [Descargar Amazon Corretto](https://aws.amazon.com/es/corretto/)
   

2. Ejecutar en Desarrollo
   ```bash
   mvn spring-boot:run
   ```
2. Ejecutar JAR Compilado
   ```bash
   java -jar target/GestorAPI-0.0.1-SNAPSHOT.jar
   ```
3. Ejecutar con Perfil de Producción
   ```bash
   java -jar target/GestorAPI-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
   ```


- 
## Rutas del servidor:

- http://localhost:8080/swagger-ui/index.html
- http://localhost:8080/ & http://localhost:8080/auth/login acceso a login de dashboard(Correo y contrasena)

## Credenciales de acceso:

| Rol    | Usuario               | Contraseña |
|--------|------------------------|------------|
| Admin  | admin@gesthor.com      | admin      |
| Usuario| user@gesthor.com       | user       |

## Variables del archivo poperties
    Al lado del .jar o ejecuable, puedes poner un .porperties con los valores que te interesa modifcar.

    # --------- APLICACIÓN
    spring.application.name=${SPRING_APP_NAME:GestorAPI}
    spring.main.banner-mode=console
    spring.main.allow-circular-references=false
    
    # --------- BANNER
    spring.banner.location=classpath:banner.txt
    spring.banner.charset=UTF-8
    spring.banner.image.ansi=ENABLED
    spring.banner.image.margin=2
    spring.banner.image.invert=false
    
    # Variables para banner.txt
    application.version=@project.version@
    application.name=@project.name@
    
    # --------- BASE DE DATOS (MySQL)
    spring.datasource.url=${SPRING_DB_URL:jdbc:mysql://localhost:3306/gestor_bd}
    spring.datasource.username=${SPRING_DB_USERNAME:root}
    spring.datasource.password=${SPRING_DB_PASSWORD:abc123.}
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
    
    # --------- JPA / HIBERNATE
    spring.jpa.hibernate.ddl-auto=${SPRING_HIBERNATE_DDL:update}
    spring.jpa.show-sql=${SPRING_HIBERNATE_SHOW_SQL:false}
    
    # --------- SEGURIDAD: JWT
    app.jwt.secret=${SPRING_JWT_SECRET:TU_KEY}
    app.jwt.expiration.minutes=${SPRING_JWT_EXPIRATION:1440}
    
    # --------- SEGURIDAD: OAuth2
    ## Google
    spring.security.oauth2.client.registration.google.client-id=${SPRING_GOOGLE_CLIENT_ID}
    spring.security.oauth2.client.registration.google.client-secret=${SPRING_GOOGLE_CLIENT_SECRET}
    spring.security.oauth2.client.registration.google.scope=profile,email
    spring.security.oauth2.client.registration.google.redirect-uri=${SPRING_GOOGLE_REDIRECT_URI:http://localhost:8080/login/oauth2/code/google}
    
    ## GitHub
    spring.security.oauth2.client.registration.github.client-id=${SPRING_GITHUB_CLIENT_ID}
    spring.security.oauth2.client.registration.github.client-secret=${SPRING_GITHUB_CLIENT_SECRET}
    spring.security.oauth2.client.registration.github.scope=read:user,user:email
    spring.security.oauth2.client.registration.github.redirect-uri=${SPRING_GITHUB_REDIRECT_URI:http://localhost:8080/login/oauth2/code/github}
    
    # --------- THYMELEAF
    spring.thymeleaf.prefix=classpath:/templates/
    spring.thymeleaf.suffix=.html
    spring.thymeleaf.cache=false
    
    # --------- CORS & REDIRECCIONES
    app.cors.allowed-origins=${SPRING_CORS_ALLOWED_ORIGINS:http://localhost:4200}
    app.oauth2.redirectUri=${SPRING_OAUTH2_REDIRECT_URI:http://localhost:4200/oauth2/redirect}
    
    # --------- INTEGRACIÓN CON API EXTERNA (Python)
    python.server.url=${SPRING_PYTHON_SERVER_URL:http://localhost:5000}
    python.server.apiKey=${SPRING_PYTHON_API_KEY:CREAR_API_KEY}
    
    # --------- SUBIDA DE ARCHIVOS
    file.upload-dir=${SPRING_UPLOAD_DIR:C:/uploads/}
    spring.servlet.multipart.enabled=true
    spring.servlet.multipart.max-file-size=${SPRING_MAX_FILE_SIZE:5MB}
    spring.servlet.multipart.max-request-size=${SPRING_MAX_REQUEST_SIZE:5MB}
    
    # --------- ARCHIVOS ESTÁTICOS
    spring.mvc.static-path-pattern=/static/**
    spring.web.resources.static-locations=classpath:/static/
    
    # --------- LOGGING
    logging.level.org.springframework.security=${SPRING_LOG_SECURITY_LEVEL:DEBUG}
    # logging.level.org.hibernate.SQL=${SPRING_LOG_SQL_LEVEL:DEBUG}
    
    # --------- SEGURIDAD PERSONALIZADA
    attemp.login.max.failed=${SPRING_LOGIN_MAX_FAILED:5}
    attemp.login.block.duration=${SPRING_LOGIN_BLOCK_DURATION:30}
    attemp.login.delete.log=${SPRING_LOGIN_DELETE_LOG:false}


## Instalación de WiX Toolset
- Descargar WiX Toolset v3.11 desde [https://wixtoolset.org/releases/]
- Ejecutar el instalador
- Asegurarse que la ruta C:\Program Files (x86)\WiX Toolset v3.11\bin\ esté en el PATH
- Configuración Recomendada para Producción
- Base de Datos:
- Cambiar spring.jpa.hibernate.ddl-auto a validate
- Usar un usuario con permisos 


#### Asegúrese de agregar a su PATH tras instalar Wix:

            set PATH=%PATH%;C:\Program Files (x86)\WiX Toolset v3.11\bin\

### Construcción del Proyecto
 - 1. Compilar el proyecto

            mvn clean package

- 2. Verificar el JAR generado

            jar tf GestorAPI-0.0.1-SNAPSHOT.jar | findstr Application.class

### Generación de Ejecutable Windows
 - Indica las rutas input y output
 - Indica el jar a cargar
 - Especifca formato de jecutable .exe
 - Indica que la aplicación use modo consola.
 - Usa el iniciador del .jar main

        jpackage ^
          --input C:\ruta\al\directorio\input ^
          --name GestorAPI ^
          --main-jar GestorAPI-0.0.1-SNAPSHOT.jar ^
          --main-class org.springframework.boot.loader.JarLauncher ^
          --type exe ^
          --win-console ^
          --dest C:\ruta\al\directorio\output

 


## Seguridad:
-  Generar una nueva clave JWT segura:

        En la carpeta del proyecto, en scripts, se encuentran progrmas utiles entre ellos 2 para generar claves.
- Evitar exponer claves publicamente y limitar acceso al servidor.
- Configurar conexion segura.

## Uploads:

- Usar un directorio fuera del proyecto (ej: C:/uploads)

- Configurar permisos adecuados

## Solución de Problemas Comunes

#### Error de conexión a BD
  - Verifique que el servicio MySQL esté corriendo
  - Revise credenciales en application.properties o variables de sistema

#### Problemas con OAuth2
  - Verifique que los client IDs y secrets sean correctos
  - Asegúrese que las URIs de redirección coincidan exactamente


## Notas Finales

- Necesitas tenre instalado el JDK Amazon Coretto 22.0.2 y Lombok
- Las dependecias se instalan desde archivo pom.xml
- En el proeycto en el inicializador se encuentra las credenciales de acceso para admin, se pueden crear usuarios default.

