/* Populate tables */
INSERT INTO usuarios (id, email, nombre, password, fecha_nacimiento, administrador, bloqueado) VALUES('1', 'ana.garcia@gmail.com', 'Ana García', '12345678', '2001-02-10', 'false', 'false');
INSERT INTO tareas (id, titulo, prioridad, usuario_id) VALUES('1', 'Lavar coche', '1', '1');
INSERT INTO tareas (id, titulo, prioridad, usuario_id) VALUES('2', 'Renovar DNI', '1', '1');

INSERT INTO equipos (id, nombre, administrador_id) VALUES('1', 'Proyecto P1', '1');
INSERT INTO equipo_usuario (fk_equipo, fk_usuario) VALUES('1', '1');
INSERT INTO equipos (id, nombre, administrador_id) VALUES('2', 'Proyecto P3', '1');