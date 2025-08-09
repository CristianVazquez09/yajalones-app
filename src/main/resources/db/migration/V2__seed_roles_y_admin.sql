INSERT INTO rol (id_rol, name, description)
VALUES
    (1,'ADMIN', 'Acceso completo al sistema'),
    (2,'TRABAJADOR', 'Acceso limitado');


INSERT INTO usuario (id_usuario, nombre_usuario, password)
VALUES
    (1,'Yajalon', '$2a$12$QRYAZdRggteM0gOCoWI8EO6DBjZzB6qhyWyhXS61LHYUcEQaKZBje'),
    (2,'Tuxtla', '$2a$12$8jboFZoalTn0e.RMDafUwedJxc0NrVTkWTt5eE5CbAE09UOk0VsJS');

INSERT INTO usuario_rol(id_usuario, id_rol)
VALUES
    (1,2),
    (2,2);