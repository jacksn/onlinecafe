DROP TABLE IF EXISTS CoffeeOrderItem;
DROP TABLE IF EXISTS CoffeeOrder;
DROP TABLE IF EXISTS CoffeeType;
DROP TABLE IF EXISTS Configuration;
DROP SEQUENCE IF EXISTS ct_seq;
DROP SEQUENCE IF EXISTS co_seq;
DROP SEQUENCE IF EXISTS coi_seq;

CREATE SEQUENCE ct_seq START 1;

CREATE TABLE CoffeeType (
  id        INTEGER PRIMARY KEY DEFAULT nextval('ct_seq'),
  type_name VARCHAR(200)     NOT NULL,
  price     DOUBLE PRECISION NOT NULL,
  disabled  CHAR(1)
);

CREATE SEQUENCE co_seq START 1;

CREATE TABLE CoffeeOrder (
  id       INTEGER PRIMARY KEY DEFAULT nextval( 'co_seq'),
  order_date       TIMESTAMP    NOT NULL DEFAULT now(),
  name             VARCHAR(100),
  delivery_address VARCHAR(200) NOT NULL,
  cost             DOUBLE PRECISION
);

CREATE SEQUENCE coi_seq START 1;

CREATE TABLE CoffeeOrderItem (
  id       INTEGER PRIMARY KEY DEFAULT nextval('coi_seq'),
  type_id INT  NOT NULL REFERENCES CoffeeType (id) ON DELETE CASCADE,
  order_id INT NOT NULL REFERENCES CoffeeOrder (id) ON DELETE CASCADE,
  quantity INT
);

CREATE TABLE Configuration (
  id    VARCHAR(20) PRIMARY KEY NOT NULL,
  value VARCHAR(30)
);
