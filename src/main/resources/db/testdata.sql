DELETE FROM coffeeorderitem;
DELETE FROM coffeeorder;
DELETE FROM coffeetype;

INSERT INTO CoffeeType (id, type_name, price, disabled) VALUES
  (NULL, 'Caffè Americano', 1.00, 'N'),
  (NULL, 'Caffè Latte', 2.00, 'N'),
  (NULL, 'Caffè Mocha', 3.00, 'N'),
  (NULL, 'Cappuccino', 3.00, 'N'),
  (NULL, 'Caramel Macchiato', 5.00, 'Y');

INSERT INTO CoffeeOrder (id, order_date, name, delivery_address, cost) VALUES
  (NULL, '2017-07-01 09:00', 'John Smith', 'John\'s address', 9.00),
  (NULL, '2017-07-01 09:01', 'Jane Smith', 'Jane\'s address', 5.00),
  (NULL, '2017-07-02 18:00', 'John Smith', 'John\'s address', 3.00);

INSERT INTO CoffeeOrderItem (id, type_id, order_id, quantity) VALUES
  (NULL, 1, 1, 2),
  (NULL, 4, 1, 2),
  (NULL, 5, 1, 2),
  (NULL, 2, 2, 2),
  (NULL, 4, 2, 1),
  (NULL, 1, 3, 1),
  (NULL, 2, 3, 1);