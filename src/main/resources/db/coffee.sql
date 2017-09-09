DROP TABLE IF EXISTS CoffeeOrderItem;
DROP TABLE IF EXISTS CoffeeOrder;
DROP TABLE IF EXISTS CoffeeType;
DROP TABLE IF EXISTS Configuration;

CREATE TABLE CoffeeType (
  id        INT           NOT NULL AUTO_INCREMENT,
  type_name VARCHAR(200)  NOT NULL,
  price     DECIMAL(10,2) NOT NULL,
  disabled  CHAR(1),
  PRIMARY KEY (id)
)
  ENGINE = InnoDB;

CREATE INDEX CT_I
  ON CoffeeType (
    id ASC
  );

CREATE TABLE CoffeeOrder (
  id               INT          NOT NULL AUTO_INCREMENT,
  order_date       DATETIME     NOT NULL,
  name             VARCHAR(100),
  delivery_address VARCHAR(200) NOT NULL,
  cost             DECIMAL(10,2),
  PRIMARY KEY (id)
)
  ENGINE = InnoDB;

CREATE INDEX CO_I1
  ON CoffeeOrder (
    id ASC
  );

CREATE TABLE CoffeeOrderItem (
  id       INT NOT NULL AUTO_INCREMENT,
  type_id  INT NOT NULL,
  order_id INT NOT NULL,
  quantity INT,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB;

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
REFERENCES CoffeeType (id)
  ON DELETE CASCADE;

CREATE TABLE Configuration (
  id    VARCHAR(20) NOT NULL,
  value VARCHAR(30),
  PRIMARY KEY (id)
)
  ENGINE = InnoDB;
