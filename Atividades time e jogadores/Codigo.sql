use atividades_time_e_jogadores;

create table jogador (
    id int auto_increment primary key,
    nome varchar(100),
    numero_camisa int
);

describe jogador;

alter table jogador add altura decimal(3,2),
add data_de_nascimento date;

describe jogador;

drop table jogador;

show tables;

create table jogador (
    id int auto_increment primary key,
    nome varchar(100),
    numero_camisa int,
    altura decimal(3,2),
    data_de_nascimento date
);
 
insert into jogador (nome, numero_camisa, altura, data_de_nascimento) values
('Neymar', 10, 1.75, '1992-02-05'),
('Messi', 30, 1.70, '1987-06-24'),
('Cristiano Ronaldo', 7, 1.87, '1985-02-05'),
('Mbappe', 9, 1.78, '1998-12-20'),
('Haaland', 11, 1.94, '2000-07-21'),
('Modric', 8, 1.72, '1985-09-09'),
('Vinicius Jr', 20, 1.76, '2000-07-12'),
('Rodrygo', 21, 1.74, '2001-01-09'),
('Salah', 22, 1.75, '1992-06-15'),
('Kane', 18, 1.88, '1993-07-28');

truncate table jogador;

alter table jogador modify numero_camisa varchar(3);

alter table jogador add constraint unique_camisa unique (numero_camisa);

alter table jogador change data_de_nascimento data_nsc date;

insert into jogador (nome, numero_camisa, altura, data_nsc) values
('Neymar', '10', 1.75, '1992-02-05'),
('Messi', '30', 1.70, '1987-06-24'),
('Cristiano Ronaldo', '7', 1.87, '1985-02-05'),
('Mbappe', '9', 1.78, '1998-12-20'),
('Haaland', '11', 1.94, '2000-07-21'),
('Modric', '8', 1.72, '1985-09-09'),
('Vinicius Jr', '20', 1.76, '2000-07-12'),
('Rodrygo', '21', 1.74, '2001-01-09'),
('Salah', '22', 1.75, '1992-06-15'),
('Kane', '18', 1.88, '1993-07-28');

create table time(
    id int auto_increment primary key,
    nome_time varchar(100) not null
);

insert into time (nome_time) values
('Barcelona'),
('Real Madrid'),
('PSG'),
('Manchester City');

alter table jogador add id_time int;

alter table jogador add constraint fk_time
foreign key (id_time) references time(id);

alter table jogador
drop index unique_camisa;

alter table jogador add constraint unique_camisa_time unique(numero_camisa, id_time);
