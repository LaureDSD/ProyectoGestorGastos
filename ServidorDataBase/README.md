![MySQL](https://img.shields.io/badge/MySQL-8.0%2B-orange)

# Instalación y Configuración de MySQL para el Proyecto Gesthor
Este documento guía paso a paso la instalación de MySQL y la creación de la base de datos necesaria para ejecutar el sistema Gesthor.

##  Requisitos Previos
Antes de comenzar, asegúrate de tener lo siguiente:

1. Sistema operativo: Windows, macOS o Linux

2. Acceso a una terminal o consola de comandos

3. Permisos de administrador (para instalar software)

## Pasos para instalación:

### Opción 1: Windows / macOS

1. [Descargar MySQL](https://dev.mysql.com/downloads/installer/)

2. Ejecuta el instalador y selecciona MySQL Server
- MySQL Workbench (opcional, pero recomendado para uso gráfico)
3. Durante la instalación:
- Elige modo de configuración "Developer Default" o "Server only"
4. Establece una contraseña para el usuario root
5. Finaliza la instalación.

### Opción 2: Linux (Debian/Ubuntu)

1. Descargar e isnalar :
```bash
sudo apt update
sudo apt install mysql-server
sudo mysql_secure_installation
Durante la instalación, se te pedirá que establezcas una contraseña para root.
```
2. Acceder a MySQL
- Abre una terminal (o Workbench) y conéctate a MySQL como usuario root:

```bash
mysql -u root -p
```
- Introduce la contraseña que configuraste en el paso anterior.

3. : Crear la Base de Datos gestor_bd
- Una vez dentro del cliente MySQL, puedes copiar y pegar el siguiente script SQL completo para crear la base de datos y todas las tablas necesarias:

## Puedes usar MySQL Workbench o guardar este contenido en un archivo .sql y ejecutarlo con:

```bash
mysql -u root -p < gestor_bd_schema.sql
```
```sql
-- Crear el esquema
CREATE SCHEMA IF NOT EXISTS `gestor_bd` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE `gestor_bd`;

-- Crear tablas
CREATE TABLE IF NOT EXISTS `categorias` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `created_at` DATETIME(6) NULL DEFAULT NULL,
  `description` VARCHAR(255) NULL DEFAULT NULL,
  `iva` DOUBLE NOT NULL,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  `updated_at` DATETIME(6) NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `usuarios` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `active` BIT(1) NOT NULL,
  `budget_monthly` DOUBLE NOT NULL,
  `created_at` DATETIME(6) NULL DEFAULT NULL,
  `email` VARCHAR(255) NULL DEFAULT NULL,
  `image_url` VARCHAR(255) NULL DEFAULT NULL,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  `notice_expense` DOUBLE NOT NULL,
  `password` VARCHAR(255) NULL DEFAULT NULL,
  `provider` ENUM('GITHUB', 'GOOGLE', 'LOCAL') NULL DEFAULT NULL,
  `provider_id` VARCHAR(255) NULL DEFAULT NULL,
  `role` TINYINT NULL DEFAULT NULL,
  `updated_at` DATETIME(6) NULL DEFAULT NULL,
  `username` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_username` (`username`),
  UNIQUE KEY `UK_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `gastos` (
  `spent_id` BIGINT NOT NULL AUTO_INCREMENT,
  `created_at` DATETIME(6) NULL DEFAULT NULL,
  `description` VARCHAR(255) NULL DEFAULT NULL,
  `expense_date` DATETIME(6) NULL DEFAULT NULL,
  `icon` VARCHAR(255) NULL DEFAULT NULL,
  `iva` DOUBLE NOT NULL,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  `total` DOUBLE NOT NULL,
  `tipo` ENUM('FACTURA', 'GASTO_GENERICO', 'SUBSCRIPCION', 'TICKET', 'TRANSFERENCIA') NOT NULL,
  `updated_at` DATETIME(6) NULL DEFAULT NULL,
  `categoria_id` BIGINT DEFAULT NULL,
  `user_id` BIGINT NOT NULL,
  PRIMARY KEY (`spent_id`),
  KEY `fk_categoria` (`categoria_id`),
  KEY `fk_usuario` (`user_id`),
  CONSTRAINT `fk_categoria` FOREIGN KEY (`categoria_id`) REFERENCES `categorias` (`id`),
  CONSTRAINT `fk_usuario` FOREIGN KEY (`user_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `subscripciones` (
  `accumulate` DOUBLE NOT NULL,
  `activa` BIT(1) NOT NULL,
  `end` DATETIME(6) DEFAULT NULL,
  `interval_time` INT NOT NULL,
  `restart_day` INT NOT NULL,
  `start` DATETIME(6) NOT NULL,
  `spent_id` BIGINT NOT NULL,
  PRIMARY KEY (`spent_id`),
  CONSTRAINT `fk_subscripcion_gasto` FOREIGN KEY (`spent_id`) REFERENCES `gastos` (`spent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `tickets` (
  `productsjson` JSON DEFAULT NULL,
  `store` VARCHAR(255) DEFAULT NULL,
  `spent_id` BIGINT NOT NULL,
  PRIMARY KEY (`spent_id`),
  CONSTRAINT `fk_ticket_gasto` FOREIGN KEY (`spent_id`) REFERENCES `gastos` (`spent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
```


4. Verifica que las tablas se hayan creado correctamente
- Puedes listar las tablas de la base de datos con:

``` sql
USE gestor_bd;
SHOW TABLES;
```

- Deberías ver: 
```
categorias, usuarios, gastos, subscripciones, tickets
```
## Consejos adicionales
- Usa MySQL Workbench para ver y modificar los datos fácilmente.
- Asegúrate de que tu aplicación Spring/Python apunte a esta base de datos (gestor_bd) y tenga permisos de acceso.
- No olvides hacer backups regulares de esta base si estás en producción.

## Soporte
Si encuentras errores al importar o necesitas ayuda con la configuración, contáctanos por GitHub o vía email.
- https://github.com/LaureDSD/ProyectoGestorGastos

