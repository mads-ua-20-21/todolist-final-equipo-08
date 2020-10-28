/* Populate tables */
INSERT INTO usuarios (id, email, nombre, password, fecha_nacimiento, administrador, bloqueado) VALUES('1', 'domingo@ua', 'Domingo Gallardo', '123', '2001-02-10', 'false', 'false');
INSERT INTO tareas (id, titulo, usuario_id) VALUES('1', 'Lavar coche', '1');
INSERT INTO tareas (id, titulo, usuario_id) VALUES('2', 'Renovar DNI', '1');

INSERT INTO usuarios (id, email, nombre, password, fecha_nacimiento, administrador, bloqueado) VALUES('2', 'ana.garcia@gmail.com', 'Ana García', '12345678', '2001-02-10', 'false', 'false');
INSERT INTO tareas (id, titulo, usuario_id) VALUES('3', 'Lavar coche', '2');
INSERT INTO tareas (id, titulo, usuario_id) VALUES('4', 'Renovar DNI', '2');

INSERT INTO equipos (id, nombre) VALUES('1', 'Proyecto P1');
INSERT INTO equipos (id, nombre) VALUES('2', 'Proyecto P3');