DELETE FROM coffeeorderitem;
DELETE FROM coffeeorder;
DELETE FROM coffeetype;

INSERT INTO CoffeeType (id, type_name, price, disabled) VALUES
  (NULL, 'Caffè Americano', 3.25, 'N'),   -- 1
  (NULL, 'Caffè Latte', 3.75, 'N'),       -- 2
  (NULL, 'Caffè Mocha', 4.00, 'N'),       -- 3
  (NULL, 'Cappuccino', 3.50, 'N'),        -- 4
  (NULL, 'Caramel Macchiato', 6.00, 'Y'); -- 5

INSERT INTO CoffeeOrder (id, order_date, name, delivery_address, cost) VALUES
  (NULL, '2017-07-01 09:00', 'John Smith', 'John\'s address', 3.25), -- 1
  (NULL, '2017-07-01 09:01', 'Jane Smith', 'Jane\'s address', 3.25), -- 2
  (NULL, '2017-07-01 18:00', 'John Smith', 'John\'s address', 3.25), -- 3
  (NULL, '2017-07-02 09:00', 'Jane Smith', 'Jane\'s address', 3.25), -- 4
  (NULL, '2017-07-02 09:02', 'John Smith', 'John\'s address', 3.25); -- 5

INSERT INTO CoffeeOrderItem (id, type_id, order_id, quantity) VALUES
  (NULL, 4, 1, 2),
  (NULL, 2, 2, 2),
  (NULL, 1, 1, 2),
  (NULL, 4, 1, 2),
  (NULL, 5, 1, 2),
  (NULL, 4, 2, 1),
  (NULL, 1, 3, 2),
  (NULL, 2, 3, 2),
  (NULL, 1, 4, 1),
  (NULL, 2, 4, 2),
  (NULL, 3, 4, 1)
;
