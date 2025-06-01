#Para integrar SSL en el servidor, Proyecto.

Necesitas tener instalado OpenSSL (3.5.0)

Achivo properties:

server.ssl.enabled=true
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=tu_contraseña #123456
server.ssl.key-store-type=PKCS12
server.ssl.key-alias=gestorapi
server.port=8443

Ejecutar Script en PowerShell.

Copiar keystore.p12 a resources.

Importar certificados.

Ya tienes SSL.
