DELETE FROM coffeeorderitem;
DELETE FROM coffeeorder;
DELETE FROM coffeetype;

INSERT INTO CoffeeType (type_name, price, disabled) VALUES
  ('Caffè Americano', 1.00, 'N'),
  ('Caffè Latte', 2.00, 'N'),
  ('Caffè Mocha', 3.00, 'N'),
  ('Cappuccino', 3.00, 'N'),
  ('Caramel Macchiato', 5.00, 'Y');

INSERT INTO CoffeeOrder (order_date, name, delivery_address, cost) VALUES
  ('2017-07-01 09:00:00', 'John Smith', 'John''s address', 9.00),
  ('2017-07-01 09:01:00', 'Jane Smith', 'Jane''s address', 5.00),
  ('2017-07-02 18:00:00', 'John Smith', 'John''s address', 3.00);

INSERT INTO CoffeeOrderItem (type_id, order_id, quantity) VALUES
  (1, 1, 2),
  (4, 1, 2),
  (5, 1, 2),
  (2, 2, 2),
  (4, 2, 1),
  (1, 3, 1),
  (2, 3, 1);

INSERT INTO Configuration (id, value) VALUES
  ('n', '5'),
  ('x', '10'),
  ('m', '2');