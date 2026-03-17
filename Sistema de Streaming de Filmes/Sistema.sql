use streaming_de_filmes;

create table filmes (
id int auto_increment primary key,
titulo varchar(150) not null,
genero varchar(100) not null,
anoLançamento int not null
);

create table atores(
id int auto_increment primary key,
nomeCompleto varchar(255) not null,
idFilme int not null,
foreign key (idFilme) references filmes (id)
);

create table usuario(
id int auto_increment primary key,
nome varchar(100) not null,
email varchar(250) unique not null
);

create table avaliações(
nota numeric(4,2) not null,
comentario text not null,
idUsuario int,
idFilme int,
foreign key (idUsuario) references usuario (id),
foreign key (idFilme) references filmes (id)
);

insert into filmes (titulo, genero, anoLançamento) values
("filme 1", "ação", 2026);

insert into atores (nomeCompleto, idFilme) values
("Pedro Alveres Cabral", 1);

insert into usuario(nome, email) values
("Pedro", "pedro@gmail.com");

insert into avaliações (nota, comentario, idUsuario, idFilme) value
(10, "excelente filme assitirei de novo", 1, 1);

select * from avaliações;
