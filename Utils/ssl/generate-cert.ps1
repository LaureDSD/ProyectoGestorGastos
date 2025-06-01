# Nombre de archivo para configuración OpenSSL
$configFile = "openssl-san.cnf"

# Contenido del archivo de configuración OpenSSL
$configContent = @"
[req]
default_bits       = 2048
prompt             = no
default_md         = sha256
req_extensions     = req_ext
distinguished_name = dn

[dn]
CN = Laurenao De Sousa Dias
OU = Gesthor
O = Gesthor
L = Ourense
ST = Ourense
C = ES

[req_ext]
subjectAltName = @alt_names

[alt_names]
DNS.1 = localhost
IP.1 = 127.0.0.1
"@

# Guarda la configuración en el archivo
$configContent | Out-File -Encoding ascii $configFile

# Generar clave privada y certificado autofirmado con SAN
openssl req -x509 -nodes -days 3650 -newkey rsa:2048 -keyout key.pem -out cert.pem -config $configFile

# Preguntar la contraseña para el keystore PKCS12
$keystorePass = Read-Host -AsSecureString "Introduce contraseña para el keystore PKCS12"

# Convertir la contraseña segura a texto plano para OpenSSL
$ptr = [System.Runtime.InteropServices.Marshal]::SecureStringToBSTR($keystorePass)
$plainPass = [System.Runtime.InteropServices.Marshal]::PtrToStringBSTR($ptr)

# Generar keystore PKCS12 para Java/Spring Boot
openssl pkcs12 -export -in cert.pem -inkey key.pem -out keystore.p12 -name gestorapi -passout pass:$plainPass

# Limpieza memoria
[System.Runtime.InteropServices.Marshal]::ZeroFreeBSTR($ptr)

Write-Host "Certificado y keystore.p12 generados correctamente."
Write-Host "Usa key para keystore.p12"
