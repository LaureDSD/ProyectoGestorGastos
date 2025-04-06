SELECT * FROM gestor_bd.tickets;

-- Insertar tickets (sin especificar ID)
INSERT INTO tickets (user_id, categoria_id, fecha_compra, total, created_at, productosjson)
VALUES
-- Tickets para admin
(1, 1, '2023-05-10 10:15:30', 1299.99, '2023-05-10 12:15:00', '[{"nombre":"Laptop","categoria":"Electrónica","cantidad":1,"precio":1299.99}]'),
(1, 2, '2023-05-15 14:30:00', 89.50, '2023-05-15 16:30:00', '[{"nombre":"Zapatos","categoria":"Ropa","cantidad":1,"precio":89.50}]'),

-- Tickets para user
(2, 1, '2023-05-12 13:25:00', 199.99, '2023-05-12 15:25:00', '[{"nombre":"Auriculares","categoria":"Electrónica","cantidad":1,"precio":199.99}]'),
(2, 3, '2023-05-18 19:15:00', 45.75, '2023-05-18 21:15:00', '[{"nombre":"Supermercado","categoria":"Alimentos","cantidad":1,"precio":45.75}]');