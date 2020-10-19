/* Populate tables */
INSERT INTO usuarios (id, email, nombre, password, fecha_nacimiento, administrador, bloqueado) VALUES('1', 'ana.garcia@gmail.com', 'Ana García', '12345678', '2001-02-10', 'false', 'false');
INSERT INTO tareas (id, titulo, usuario_id) VALUES('1', 'Lavar coche', '1');
INSERT INTO tareas (id, titulo, usuario_id) VALUES('2', 'Renovar DNI', '1');
