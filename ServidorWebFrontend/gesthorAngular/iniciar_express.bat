@echo off
setlocal

:: Título en consola
title Gesthor - Compilar y lanzar servidor

:: Paso 1: Eliminar carpeta dist si existe
echo Eliminando carpeta dist anterior...
rmdir /s /q dist

:: Paso 2: Compilar Angular en modo producción
echo Compilando Angular en modo producción...
call npm run build -- --configuration=production
IF ERRORLEVEL 1 (
  echo Error durante la compilación. Abortando.
  pause
  exit /b 1
)

:: Paso 3: Iniciar el servidor Express
echo Iniciando servidor Express...
node server.js

endlocal
pause
