create table aluno(
matricula varchar(255) primary key not null,
nome_completo varchar(255) not null,
curso varchar (255) not null,
email varchar(255) unique not null
);

create table professores(
id_professor varchar(14) not null primary key,
nome_completo varchar(255) not null,
área_especialização varchar(255) not null
);

create table disciplinas(
codigo_unico int primary key not null,
nome_disciplina varchar(254) not null,
carga_horária varchar(10) not null,
id_professor varchar(14) not null,
foreign key (id_professor) references professores (id_professor)
);

create table matriculas(
id_matricula int primary key not null,
semestre varchar(255) not null,
codigo_unico int unique not null,
matricula varchar(255),
foreign key (matricula) references aluno (matricula)
);

insert into aluno (matricula, nome_completo, curso,email) values ("1","Paulo", "programaçao","paulo@gmail.com");
insert into professores (id_professor, nome_completo, área_especialização) values ("6","João","programação");
insert into disciplinas (codigo_unico, nome_disciplina, carga_horária, id_professor) value ("12345", "Programação", "90h", "6");
insert into matriculas(id_matricula, semestre, codigo_unico, matricula) values ("1","primeiro", "1234", "1");

select * from aluno;
select * from professores;
select * from disciplinas;
select * from matriculas;
