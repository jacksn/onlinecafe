DROP TABLE CoffeeOrderItem IF EXISTS;
DROP TABLE CoffeeOrder IF EXISTS;
DROP TABLE COFFEETYPE IF EXISTS;
DROP TABLE Configuration IF EXISTS;

CREATE TABLE COFFEETYPE (
  id        INTEGER GENERATED BY DEFAULT AS IDENTITY (
  START WITH 1 ),
  type_name VARCHAR(200) NOT NULL,
  price     DOUBLE       NOT NULL,
  disabled  CHAR(1),
  PRIMARY KEY (id)
);

CREATE INDEX CT_I
  ON COFFEETYPE (
    id ASC
  );

CREATE TABLE CoffeeOrder (
  id               INTEGER GENERATED BY DEFAULT AS IDENTITY (
  START WITH 1 ),
  order_date       DATETIME     NOT NULL,
  name             VARCHAR(100),
  delivery_address VARCHAR(200) NOT NULL,
  cost             DOUBLE,
  PRIMARY KEY (id)
);

CREATE INDEX CO_I1
  ON CoffeeOrder (
    id ASC
  );

CREATE TABLE CoffeeOrderItem (
  id       INTEGER GENERATED BY DEFAULT AS IDENTITY (
  START WITH 1 ),
  type_id  INT NOT NULL,
  order_id INT NOT NULL,
  quantity INT,
  PRIMARY KEY (id)
);

CREATE INDEX COI_I
  ON CoffeeOrderItem (
    order_id ASC
  );

CREATE INDEX COI_3
  ON CoffeeOrderItem (
    type_id ASC
  );

ALTER TABLE CoffeeOrderItem
  ADD CONSTRAINT COI_CO FOREIGN KEY (order_id)
REFERENCES CoffeeOrder (id)
  ON DELETE CASCADE;

ALTER TABLE CoffeeOrderItem
  ADD CONSTRAINT COI_CT FOREIGN KEY (type_id)
REFERENCES COFFEETYPE (id)
  ON DELETE CASCADE;

CREATE TABLE Configuration (
  id    VARCHAR(20) NOT NULL,
  value VARCHAR(30),
  PRIMARY KEY (id)
);