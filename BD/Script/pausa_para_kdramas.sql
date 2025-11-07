show databases;
drop database kdramasdb;

show tables;


-- Crear base de datos
CREATE DATABASE IF NOT EXISTS kdramasdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE kdramasdb;

-- 1. Usuarios
CREATE TABLE usuarios (
    id_usuario BIGINT AUTO_INCREMENT PRIMARY KEY,                
    firebase_uid VARCHAR(255) NOT NULL UNIQUE,                   
    nombre VARCHAR(100) NOT NULL,                              
    email VARCHAR(150) NOT NULL UNIQUE,                          
    foto_perfil VARCHAR(255) DEFAULT 'https://example.com/default-profile.jpg',
    fecha_registro DATE NOT NULL,                                
    CONSTRAINT UK_firebase_uid UNIQUE (firebase_uid)
);

-- 2. Roles de usuario (colección embebida en entidad Usuario)
CREATE TABLE usuario_roles (
    usuario_id BIGINT NOT NULL,
    rol VARCHAR(50) NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id_usuario) ON DELETE CASCADE
);

-- 3. K-Dramas (nombre sin guion para evitar problemas)
CREATE TABLE kdramas (
    id_kdrama BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo_kdrama VARCHAR(100) NOT NULL,
    anio DATE,                                                   
    genero VARCHAR(50),
    actores_principales VARCHAR(255),
    sinopsis TEXT,
    imagen_url VARCHAR(255),
    fecha_registro DATE,
    usuario_id BIGINT NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id_usuario) ON DELETE CASCADE
);

-- 4. Episodios
CREATE TABLE episodios (
    id_episodio BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_kdrama BIGINT NOT NULL,
    titulo_episodio VARCHAR(100),
    numero_episodio INT,
    duracion_min INT,
    sinopsis TEXT,
    fecha_emision DATE,
    FOREIGN KEY (id_kdrama) REFERENCES kdramas(id_kdrama) ON DELETE CASCADE
);

-- 5. Géneros
CREATE TABLE kdrama_genero (
    id_genero BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre_genero VARCHAR(50) UNIQUE NOT NULL
);

-- 6. Relación K-Drama ↔ Género
CREATE TABLE kdrama_genero_rel (
    id_kdrama BIGINT NOT NULL,
    id_genero BIGINT NOT NULL,
    PRIMARY KEY (id_kdrama, id_genero),
    FOREIGN KEY (id_kdrama) REFERENCES kdramas(id_kdrama) ON DELETE CASCADE,
    FOREIGN KEY (id_genero) REFERENCES kdrama_genero(id_genero) ON DELETE CASCADE
);

-- 7. Valoraciones
CREATE TABLE valoraciones (
    id_valoracion BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_usuario BIGINT NOT NULL,
    id_kdrama BIGINT NOT NULL,
    puntuacion INT CHECK (puntuacion BETWEEN 1 AND 5),
    comentario TEXT,
    fecha_valoracion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (id_kdrama) REFERENCES kdramas(id_kdrama) ON DELETE CASCADE
);

-- 8. Seguimientos
CREATE TABLE seguimientos (
    id_seguimiento BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_usuario BIGINT NOT NULL,
    id_kdrama BIGINT NOT NULL,
    estado ENUM('viendo','finalizado','pendiente'),
    progreso INT,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (id_kdrama) REFERENCES kdramas(id_kdrama) ON DELETE CASCADE
);

-- 9. Comentarios
CREATE TABLE comentarios (
    id_comentario BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_usuario BIGINT NOT NULL,
    id_kdrama BIGINT NOT NULL,
    texto TEXT NOT NULL,
    fecha_comentario TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (id_kdrama) REFERENCES kdramas(id_kdrama) ON DELETE CASCADE
);



-- Creación de usuario admin.
INSERT INTO usuarios (firebase_uid, nombre, email, foto_perfil, fecha_registro)
VALUES (
    'Lnhj3wais1M8h4gzFu0Q2egxEFj2', -- UID FIREBASE(Antes crear en firebase y copiar el uid)
    'Administrador General',
    'admin@admin.es',
    'https://example.com/admin-profile.jpg',
    CURDATE()
);

SELECT id_usuario 
FROM usuarios 
WHERE firebase_uid = 'Lnhj3wais1M8h4gzFu0Q2egxEFj2';

INSERT INTO usuario_roles (usuario_id, rol)
VALUES (7, 'ADMIN');


SELECT * FROM usuarios WHERE firebase_uid = 'Lnhj3wais1M8h4gzFu0Q2egxEFj2';
SELECT * FROM usuario_roles WHERE usuario_id = 7;


Select * from usuarios;

delete from usuarios where id_usuario=6;


