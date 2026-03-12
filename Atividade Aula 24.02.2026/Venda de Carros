use atividade_aula_24_02_2026;

create table clientes(
id int auto_increment primary key not null,
cpf char(14) unique not null,
nome varchar(100) not null,
email varchar(254) unique not null
);

create table carros(
id int auto_increment primary key not null,
marca varchar(200) not null,
modelo varchar(200) not null,
ano char(4) not null,
cor varchar(100) not null,
preço varchar(255) not null,
id_cliente int,
foreign key (id_cliente) references clientes(id)
);

insert into clientes (cpf, nome, email) values ("123.123.123-12", "Pedro Paulo Portela", "PPPP@gmail.com");
insert into carros (marca, modelo, ano, cor, preço, id_cliente) value ("marca 1", "modelo 1", "2026", "prata", "9999","1");

select * from clientes;
select * from carros;
