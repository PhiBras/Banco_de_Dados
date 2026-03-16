use hospital;

create table Pacientes(
cpf char(14) primary key not null,
nomeCompleto varchar (255) not null,
dataNascimanto date not null,
telefone char (20)  unique not null
);

create table Médicos(
id int auto_increment primary key,
nome varchar (100) not null,
especialidade varchar (255) not null
);

create table Consultas(
idPaciente char(14) not null,
idMedico int not null,
dataConsulta date not null,
diagnóstico varchar(255) not null,
foreign key (idPaciente) references Pacientes(cpf),
foreign key (idMedico) references Médicos (id)
);

insert into Pacientes (cpf, nomeCompleto, dataNascimanto, telefone) values
("123.123.123-11", "Pedro", "2002-03-16", "+55 (71) 1 1234-1234"),
("123.123.123-12", "João", "2003-04-11", "+55 (71) 2 1234-1234"),
("123.123.123-13", "Paulo", "2000-07-02", "+55 (71) 3 1234-1234"),
("123.123.123-14", "Marcos", "2003-08-07", "+55 (71) 4 1234-1234"),
("123.123.123-15", "Guilherme", "2001-06-27", "+55 (71) 5 1234-1234"),
("123.123.123-16", "Lucas", "2004-01-20", "+55 (71) 6 1234-1234");

insert into Médicos (nome, especialidade) values
("Eduardo", "Dentista"),
("Matheus", "Cardiologista"),
("Edvaldo", "Fisioterapeuta"),
("Gilberto", "Psicologo");

insert into Consultas (idPaciente, idMedico, dataConsulta, diagnóstico) values
("123.123.123-11", 4, "2026-03-16", "Doença"),
("123.123.123-12", 1, "2026-03-16", "Doença"),
("123.123.123-12", 1, "2026-03-16", "Doença"),
("123.123.123-14", 3, "2026-03-16", "Doença"),
("123.123.123-16", 2, "2026-03-16", "Doença"),
("123.123.123-16", 2, "2026-03-16", "Doença");

select * from Pacientes;
select * from Médicos;
select * from Consultas;
