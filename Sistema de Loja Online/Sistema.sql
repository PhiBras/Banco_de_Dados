use loja_online;

create table clientes(
id int auto_increment primary key,
nome varchar (100) not null,
email varchar (255) unique not null,
telefone char(20) unique not null
);

create table produtos(
id int auto_increment primary key,
nome_Produto varchar(255) not null,
preço float not null,
qtdEstoque int not null
);

create table pedidos(
numero_identificador int auto_increment primary key,
data_Pedido date not null,
idCliente int not null,
idProduto int not null,
qtdComprada int not null,
foreign key(idProduto) references produtos(id),
foreign key(idCliente) references clientes(id)
);

drop table pedidos;

insert into clientes (nome, email, telefone) values 
("Pedro", "pedro@gmail.com", "+12 (12) 1 1234-5678"),
("João", "joao@gmail.com", "+12 (12) 1 2234-5678"),
("Paulo", "paulo@gmail.com", "+12 (12) 1 3234-5678"),
("Guilherme", "guilherme@gmail.com", "+12 (12) 1 4234-5678"),
("Marcos", "marcos@gmail.com", "+12 (12) 1 5234-5678"),
("Maycon", "maycon@gmail.com", "+12 (12) 1 6234-5678");

insert into produtos (nome_Produto, preço, qtdEstoque) values 
("produto 1", 10.50, 2000),
("produto 2", 15.39, 1500),
("produto 3", 5.99, 5000),
("produto 4", 30.00, 750),
("produto 5", 11.22, 1750),
("produto 6", 1.50, 10000);

insert into pedidos (data_Pedido, idCliente, idProduto, qtdComprada) values
("2026-03-16", 1, 5, 20),
("2026-03-16", 2, 5, 10),
("2026-03-16", 3, 4, 50),
("2026-03-16", 4, 4, 22),
("2026-03-16", 5, 3, 39),
("2026-03-16", 6, 3, 89);

truncate table pedidos;

select * from clientes;
select * from produtos;
select * from pedidos;
