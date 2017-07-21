DELETE FROM coffeeorderitem;
DELETE FROM coffeeorder;
DELETE FROM coffeetype;

INSERT INTO CoffeeType (id, type_name, price, disabled) VALUES
  (NULL, 'Caffè Americano', 3.25, 'N'),
  (NULL, 'Caffè Latte', 3.75, 'N'),
  (NULL, 'Caffè Mocha', 4.00, 'N'),
  (NULL, 'Cappuccino', 3.50, 'N'),
  (NULL, 'Caramel Macchiato', 6.00, 'Y');

INSERT INTO CoffeeOrder (id, order_date, name, delivery_address, cost) VALUES
  (NULL, '2017-07-01 09:00', 'John Smith', 'John\'s address', 25.50),
  (NULL, '2017-07-01 09:01', 'Jane Smith', 'Jane\'s address', 11.00),
  (NULL, '2017-07-02 18:00', 'John Smith', 'John\'s address', 7.00);

INSERT INTO CoffeeOrderItem (id, type_id, order_id, quantity) VALUES
  (NULL, 1, 1, 2),
  (NULL, 4, 1, 2),
  (NULL, 5, 1, 2),
  (NULL, 2, 2, 2),
  (NULL, 4, 2, 1),
  (NULL, 1, 3, 1),
  (NULL, 2, 3, 1);