CREATE DATABASE superstore;

USE superstore;

CREATE TABLE items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    category VARCHAR(50),
    name VARCHAR(100),
    price DOUBLE
);

CREATE TABLE orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    item_name VARCHAR(100),
    quantity INT,
    total DOUBLE,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert sample data into items
INSERT INTO items (category, name, price) VALUES 
('Food', 'Burger', 200),
('Food', 'Pizza', 500),
('Food', 'Fries', 150),
('Grocery', 'Rice', 120),
('Grocery', 'Oil', 250),
('Grocery', 'Sugar', 90),
('Drinks', 'Coke', 60),
('Drinks', 'Juice', 100),
('Drinks', 'Water', 40),
('Stationery', 'Pen', 20),
('Stationery', 'Notebook', 50),
('Stationery', 'Pencil', 10);
