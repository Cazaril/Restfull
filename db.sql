CREATE TABLE Usuarios (
ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
Nombre VARCHAR(50) NOT NULL,
Pass INTEGER,
PRIMARY KEY (ID)
);

INSERT INTO USUARIOS (Nombre, Pass) VALUES ('Eugenio Gonzalo Jimenez', 0001);
INSERT INTO USUARIOS (Nombre, Pass) VALUES ('Santiago Cervantes Reus', 0002);


CREATE TABLE Calendarios (
ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
Propietario INTEGER NOT NULL,
Nombre VARCHAR(50) NOT NULL,
Publico BOOLEAN,
PRIMARY KEY (ID),
FOREIGN KEY (Propietario) REFERENCES Usuarios(ID)
);

INSERT INTO CALENDARIOS (Propietario, Nombre, Publico) VALUES (1, 'Calendario1', true);
INSERT INTO CALENDARIOS (Propietario, Nombre, Publico) VALUES (2, 'Calendario2', true);
INSERT INTO CALENDARIOS (Propietario, Nombre, Publico) VALUES (1, 'Calendario3', false);


CREATE TABLE Citas (
ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
IDCalendario INTEGER NOT NULL,
Descripcion VARCHAR(50),
Fecha DATE NOT NULL,
PRIMARY KEY (ID),
FOREIGN KEY (IDCalendario) REFERENCES Calendarios(ID)
);

INSERT INTO CITAS (IDCalendario, Descripcion, Fecha) VALUES (1, 'Mi cumpleaños', '1990-02-09');
INSERT INTO CITAS (IDCalendario, Descripcion, Fecha) VALUES (2, 'Entrega practica Rest', '2015-07-07');
INSERT INTO CITAS (IDCalendario, Descripcion, Fecha) VALUES (3, 'Vacaciones', '2015-07-16');