use academia;

create table Alunos(
matrícula int primary key,
nome varchar(100) not null,
telefone varchar (20) unique not null
);

create table Instrutores(
id int auto_increment primary key,
nome varchar(100) not null,
especialidade varchar (100) not null
);

create table Treinos(
id int auto_increment primary key,
nomeTreino varchar(100) not null,
nivelDificuldade varchar(100) not null,
matricula int not null,
idInstrutor int not null,
foreign key (matricula) references Alunos(matrícula),
foreign key(idInstrutor) references Instrutores(id)
);

insert into Alunos (matrícula, nome, telefone) values
(12345, "Pedro", "+55 (71) 1 1234-1234"),
(12346, "João", "+55 (71) 2 1234-1234"),
(12347, "Paulo", "+55 (71) 3 1234-1234"),
(12348, "Matheus", "+55 (71) 4 1234-1234");

insert into Instrutores (nome, especialidade) values
("Jonas", "Musculação"),
("Marcus", "Dança"),
("Carlos", "Box");

insert into Treinos (nomeTreino, nivelDificuldade, matricula, idInstrutor) value
("Full Body", "Alto", 12345, 1),
("Superirores", "Médio", 12346, 3),
("Inferiores", "Médio", 12347, 2),
("Pacote completo", "Extremo", 12348, 3);

select * from Alunos;
select * from Instrutores;
select * from Treinos;
