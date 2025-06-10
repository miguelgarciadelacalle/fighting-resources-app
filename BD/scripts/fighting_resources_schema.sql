DROP DATABASE IF EXISTS fighting_resources_db;
CREATE DATABASE fighting_resources_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE fighting_resources_db;

CREATE TABLE juegos (
    id INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    desarrollador VARCHAR(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    lanzamiento DATE DEFAULT NULL,
    imagen_base64 LONGTEXT COLLATE utf8mb4_unicode_ci,
    PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE personajes (
    id INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    imagen_base64 LONGTEXT COLLATE utf8mb4_unicode_ci,
    id_juego INT DEFAULT NULL,
    PRIMARY KEY (id),
    KEY (id_juego),
    CONSTRAINT fk_personaje_juego FOREIGN KEY (id_juego)
        REFERENCES juegos (id)
        ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE movimientos (
    id INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    imagen_base64 LONGTEXT COLLATE utf8mb4_unicode_ci,
    damage INT DEFAULT NULL,
    startup INT DEFAULT NULL,
    active INT DEFAULT NULL,
    recovery INT DEFAULT NULL,
    rec_hit INT DEFAULT NULL,
    rec_block INT DEFAULT NULL,
    cancel VARCHAR(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    properties VARCHAR(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    id_personaje INT NOT NULL,
    PRIMARY KEY (id),
    KEY (id_personaje),
    CONSTRAINT fk_movimiento_personaje FOREIGN KEY (id_personaje)
        REFERENCES personajes (id)
        ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE usuarios (
    id INT NOT NULL AUTO_INCREMENT,
    nombre_usuario VARCHAR(50) COLLATE utf8mb4_unicode_ci NOT NULL,
    password_hash VARCHAR(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY (nombre_usuario)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE usuarios_juegos (
    id_usuario INT NOT NULL,
    id_juego INT NOT NULL,
    PRIMARY KEY (id_usuario, id_juego),
    KEY (id_juego),
    CONSTRAINT fk_uj_usuario FOREIGN KEY (id_usuario)
        REFERENCES usuarios (id)
        ON DELETE CASCADE,
    CONSTRAINT fk_uj_juego FOREIGN KEY (id_juego)
        REFERENCES juegos (id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;