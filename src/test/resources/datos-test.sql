/* Populate tables */
INSERT INTO usuarios (id, email, nombre, password, fecha_nacimiento, administrador, bloqueado) VALUES('1', 'ana.garcia@gmail.com', 'Ana García', '12345678', '2001-02-10', 'false', 'false');
INSERT INTO usuarios (id, email, nombre, password, fecha_nacimiento, administrador, bloqueado) VALUES('2', 'raul@gmail.com', 'Raul García', '12345678', '2001-02-10', 'false', 'false');
INSERT INTO tareas (id, titulo, prioridad, usuario_id, proyecto_id) VALUES('1', 'Lavar coche', '1', '1',null);
INSERT INTO tareas (id, titulo, prioridad, usuario_id, proyecto_id) VALUES('2', 'Renovar DNI', '1', '1',null);

INSERT INTO equipos (id, nombre, administrador_id) VALUES('1', 'Proyecto P1', '1');
INSERT INTO equipo_usuario (fk_equipo, fk_usuario) VALUES('1', '1');
INSERT INTO equipos (id, nombre, administrador_id) VALUES('2', 'Proyecto P3', '1');
INSERT INTO equipos (id, nombre, administrador_id) VALUES('3', 'Proyecto P4', '2');
INSERT INTO equipo_usuario (fk_equipo, fk_usuario) VALUES('3', '2');

INSERT INTO proyectos (id, nombre, equipo_id) VALUES ('1','Proyecto MADS','2');
INSERT INTO proyectos (id, nombre, equipo_id) VALUES ('2','Proyecto ADI', '2');
INSERT INTO tareas (id, titulo, prioridad, usuario_id, proyecto_id) VALUES('3', 'Crear mockups', '1', '1','1');
INSERT INTO tareas (id, titulo, prioridad, usuario_id, proyecto_id) VALUES('4', 'Implementar Tests', '1', '1','1');
INSERT INTO proyectos (id, nombre, equipo_id) VALUES ('3','Proyecto DCA','3');
INSERT INTO proyectos (id, nombre, equipo_id) VALUES ('4','Proyecto IW', '3');