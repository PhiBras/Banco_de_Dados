-- Nível 1 - Básico (SELECT)
-- 1. Liste todos os clientes da tabela customer.
select * from film;
-- 2. Mostre apenas o nome e sobrenome dos clientes.
select first_name, last_name from customer;
-- 3. Liste os filmes com duração maior que 120 minutos.
select * from film where length > 120;
-- 4. Mostre todos os filmes lançados no ano de 2006.
select * from film where release_year = 2006;
-- 5. Liste os clientes ativos (active = 1).
select * from customer where active = 1;

-- Nível 2 - JOIN
-- 6. Liste todos os aluguéis com o nome do cliente.
SELECT r.rental_id, c.first_name, c.last_name FROM rental r INNER JOIN customer c ON r.customer_id = c.customer_id;
-- 7. Liste os pagamentos com nome do cliente e valor pago.
SELECT p.payment_id, c.first_name, c.last_name, p.amount FROM payment p INNER JOIN customer c  ON p.customer_id = c.customer_id;
-- 8. Liste os filmes alugados com o nome do cliente.
SELECT f.title, c.first_name, c.last_name FROM rental r INNER JOIN customer c ON r.customer_id = c.customer_id INNER JOIN inventory i ON r.inventory_id = i.inventory_id INNER JOIN film f ON i.film_id = f.film_id;
-- 9. Liste todos os funcion ́arios e suas lojas.
SELECT s.first_name, s.last_name, st.store_id FROM staff s INNER JOIN store st  ON s.store_id = st.store_id;
-- 10. Liste todos os filmes com sua categoria.
SELECT f.title, c.name AS category FROM film f INNER JOIN film_category fc ON f.film_id = fc.film_id INNER JOIN category c ON fc.category_id = c.category_id;

-- N ́ıvel 3 - Agrega ̧c ̃ao
-- 11. Quantos clientes existem?
SELECT COUNT(*) AS total_clientes FROM customer;
-- 12. Qual o valor total de pagamentos realizados?
SELECT SUM(amount) AS total_pago FROM payment;
-- 13. Qual o valor m ́edio dos pagamentos?
SELECT AVG(amount) AS media_pagamentos FROM payment;
-- 14. Quantos filmes existem por categoria?
SELECT c.name, COUNT(*) AS total_filmes FROM category c INNER JOIN film_category fc ON c.category_id = fc.category_id GROUP BY c.name;
-- 15. Qual cliente fez mais alugu ́eis?
SELECT c.first_name, c.last_name, COUNT(r.rental_id) AS total
FROM customer c
INNER JOIN rental r ON c.customer_id = r.customer_id
GROUP BY c.customer_id
ORDER BY total DESC
LIMIT 1;

-- N ́ıvel 4 - Avan ̧cado
-- 16. Liste o total gasto por cliente.
SELECT c.first_name, c.last_name, SUM(p.amount) AS total_gasto
FROM customer c
INNER JOIN payment p ON c.customer_id = p.customer_id
GROUP BY c.customer_id;
-- 17. Liste os 5 clientes que mais gastaram.
SELECT c.first_name, c.last_name, SUM(p.amount) AS total_gasto
FROM customer c
INNER JOIN payment p ON c.customer_id = p.customer_id
GROUP BY c.customer_id
ORDER BY total_gasto DESC
LIMIT 5;
-- 18. Liste os filmes mais alugados.
SELECT f.title, COUNT(*) AS total_alugueis
FROM rental r
INNER JOIN inventory i ON r.inventory_id = i.inventory_id
INNER JOIN film f ON i.film_id = f.film_id
GROUP BY f.title
ORDER BY total_alugueis DESC;
-- 19. Para cada categoria, mostre:
SELECT 
    c.name,
    COUNT(DISTINCT f.film_id) AS total_filmes,
    COUNT(r.rental_id) AS total_alugueis
FROM category c
INNER JOIN film_category fc ON c.category_id = fc.category_id
INNER JOIN film f ON fc.film_id = f.film_id
LEFT JOIN inventory i ON f.film_id = i.film_id
LEFT JOIN rental r ON i.inventory_id = r.inventory_id
GROUP BY c.name;
SELECT 
    c.first_name,
    c.last_name,
    f.title,
    cat.name AS categoria,
    p.amount
FROM payment p
INNER JOIN customer c ON p.customer_id = c.customer_id
INNER JOIN rental r ON p.rental_id = r.rental_id
INNER JOIN inventory i ON r.inventory_id = i.inventory_id
INNER JOIN film f ON i.film_id = f.film_id
INNER JOIN film_category fc ON f.film_id = fc.film_id
INNER JOIN category cat ON fc.category_id = cat.category_id;

-- N ́ıvel 5 - Desafios
-- 21. Liste clientes que nunca alugaram nada.
SELECT c.first_name, c.last_name
FROM customer c
LEFT JOIN rental r ON c.customer_id = r.customer_id
WHERE r.rental_id IS NULL;
-- 22. Liste filmes que nunca foram alugados.
SELECT f.title
FROM film f
LEFT JOIN inventory i ON f.film_id = i.film_id
LEFT JOIN rental r ON i.inventory_id = r.inventory_id
WHERE r.rental_id IS NULL;
-- 23. Para cada cliente, mostre:
SELECT 
    c.first_name,
    c.last_name,
    COUNT(r.rental_id) AS total_alugueis,
    SUM(p.amount) AS total_gasto,
    AVG(p.amount) AS ticket_medio
FROM customer c
LEFT JOIN rental r ON c.customer_id = r.customer_id
LEFT JOIN payment p ON c.customer_id = p.customer_id
GROUP BY c.customer_id;
-- 24. Liste o dia com mais alugu ́eis.
SELECT DATE(rental_date) AS dia, COUNT(*) AS total
FROM rental
GROUP BY dia
ORDER BY total DESC
LIMIT 1;
-- 25. Liste:
SELECT 
    c.first_name,
    c.last_name,
    COUNT(r.rental_id) AS total_alugueis,
    cat.name AS categoria_favorita
FROM customer c
INNER JOIN rental r ON c.customer_id = r.customer_id
INNER JOIN inventory i ON r.inventory_id = i.inventory_id
INNER JOIN film f ON i.film_id = f.film_id
INNER JOIN film_category fc ON f.film_id = fc.film_id
INNER JOIN category cat ON fc.category_id = cat.category_id
GROUP BY c.customer_id, cat.name
HAVING COUNT(r.rental_id) = (
    SELECT MAX(total)
    FROM (
        SELECT COUNT(*) AS total
        FROM rental r2
        INNER JOIN inventory i2 ON r2.inventory_id = i2.inventory_id
        INNER JOIN film_category fc2 ON i2.film_id = fc2.film_id
        WHERE r2.customer_id = c.customer_id
        GROUP BY fc2.category_id
    ) sub
);
