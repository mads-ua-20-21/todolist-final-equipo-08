/* Populate tables */
INSERT INTO usuarios (id, email, nombre, password, fecha_nacimiento, administrador, bloqueado) VALUES('1', 'domingo@ua', 'Domingo Gallardo', '123', '2001-02-10', 'false', 'false');

INSERT INTO tareas (id, titulo, prioridad, usuario_id, estado, proyecto_id) VALUES('1', 'Lavar coche', '1', '1', '0',null);
INSERT INTO tareas (id, titulo, prioridad, usuario_id, estado, proyecto_id) VALUES('2', 'Renovar DNI', '1', '1', '0',null);

INSERT INTO usuarios (id, email, nombre, password, fecha_nacimiento, administrador, bloqueado) VALUES('2', 'ana.garcia@gmail.com', 'Ana García', '12345678', '2001-02-10', 'false', 'false');
INSERT INTO tareas (id, titulo, prioridad, usuario_id, estado, proyecto_id) VALUES('3', 'Lavar coche', '1', '2', '0',null);
INSERT INTO tareas (id, titulo, prioridad, usuario_id, estado, proyecto_id) VALUES('4', 'Renovar DNI', '1', '2', '0',null);

INSERT INTO usuarios (id, email, nombre, password, fecha_nacimiento, administrador, bloqueado) VALUES('3', 'admin@admin', 'Admin', 'admin', '1900-01-01', 'true', 'false');

INSERT INTO equipos (id, nombre, administrador_id) VALUES('1', 'Proyecto P1', '1');
INSERT INTO equipo_usuario (fk_equipo, fk_usuario) VALUES('1', '1');
INSERT INTO equipo_usuario (fk_equipo, fk_usuario) VALUES('1', '2');

INSERT INTO equipos (id, nombre, administrador_id) VALUES('2', 'Proyecto P3', '2');
INSERT INTO equipo_usuario (fk_equipo, fk_usuario) VALUES('2', '2');

INSERT INTO proyectos (id, nombre, equipo_id) VALUES ('1','Proyecto MADS','1');
INSERT INTO proyectos (id, nombre, equipo_id) VALUES ('2','Proyecto ADI','2');
INSERT INTO tareas (id, titulo, prioridad, usuario_id, estado, proyecto_id) VALUES('5', 'Crear mockups', '1', '1', '0', '1');
INSERT INTO tareas (id, titulo, prioridad, usuario_id, estado, proyecto_id) VALUES('6', 'Implementar Tests', '1', '1', '0','1');

INSERT INTO comentarios (id, mensaje, usuario_id, tarea_id) VALUES ('1','Comentario de prueba','1','5');