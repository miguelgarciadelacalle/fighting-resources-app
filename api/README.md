Fighting Resources - API REST
=============================

Este es el servicio de backend del proyecto "Fighting Resources".

------------------------------------------------------------

Tecnologías utilizadas
----------------------

- Java 21
- Spring Boot
- Spring Data JPA
- Spring Web
- MySQL
- Lombok
- Maven
- JSON

------------------------------------------------------------

Funcionalidad
-------------

La API REST proporciona acceso centralizado a los datos de la aplicación.

Gestión de:

- Usuarios
- Juegos
- Personajes
- Movimientos
- Relación UsuariosJuegos

------------------------------------------------------------

Seguridad
---------

- Autenticación mediante nombre de usuario y contraseña.
- Contraseñas almacenadas cifradas (hash).
- Operaciones CRUD protegidas.

------------------------------------------------------------

Configuración y ejecución
-------------------------

1. Crear base de datos MySQL fighting_resources_db con el script fighting_resources_schema.sql.
2. Configurar archivo application.properties:

spring.datasource.url=jdbc:mysql://IP:3306/fighting_resources_db
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña

Normalmente se tiene que ejecutar en el equipo que esté corriendo la BD.

3. Ejecutar el servicio.

4. La API REST estará disponible en:

http://IP:8080/api

------------------------------------------------------------

Pruebas
-------

- Probado desde Postman y mediante las aplicaciones cliente (escritorio y móvil).
- Soporta consultas sobre las tablas maestras y la relación N-M.

------------------------------------------------------------

Autor
-----

Miguel García De La Calle
Ciclo: desarrollo de aplicaciones multiplataforma (DAM)
Curso: 2023–2025

------------------------------------------------------------