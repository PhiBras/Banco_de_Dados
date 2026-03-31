use empresa;

create table funcionario(
id int auto_increment primary key,
nome varchar(100) not null,
salario numeric(10,2)not null,
departamento varchar (100) not null,
create_at timestamp default current_timestamp,
update_at timestamp default current_timestamp on update current_timestamp
);

insert into funcionario (nome, salario, departamento) values
('Carlos', 3000, 'TI'),
('Maria', 4500, 'RH'),
('João', 5000, 'TI'),
('Ana', 3500, 'Financeiro'),
('Pedro', 2800, 'RH');

-- Liste todos os funcionários
select * from funcionario;

-- Mostre a quantidade de funcionários.
select count(*) from funcionario;

-- Liste apenas os funcionários do departamento TI.
select * from funcionario where departamento = 'TI';

-- Mostre a quantidade de funcionários do departamento TI.
select count(*) from funcionario where departamento = 'TI';

-- Liste os funcionários com salário maior que 4000.
select * from funcionario where salario > 4000;

-- Mostre a quantidade de funcionários com salário maior que 4000
select count(*) from funcionario where salario > 4000;

-- Adicione um novo funcionário: Nome: Juliana, Salário: 4200, Departamento: TI
insert into funcionario (nome, salario, departamento) values
('Juliana', 4200, 'TI');

-- Aumente o salário de Carlos para 3500.
update funcionario set salario = 3500 where id = 1;

-- Aumente o salário de todos do RH em 10%.
update funcionario set salario = salario * 1.1 where departamento = 'RH';

-- Remova o funcionário chamado Pedro.
delete from funcionario where id = 5;

-- Liste todos os funcionários ordenados pelo salário (maior → menor).
select * from funcionario order by salario desc;

-- Mostre: Média salarial
select avg(salario) from funcionario;
-- Maior salário
select max(salario) from funcionario;
-- Menor salário
select min(salario) from funcionario;

-- Aumente em 5% o salário apenas dos funcionários do TI que ganham menos de 4000.
update funcionario set salario = salario * 1.05 where departamento = 'TI' and salario < 4000;
