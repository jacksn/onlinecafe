--
-- Изменяйте типы для тестовой БД.
-- Для MySQL юзайте InnoDB
--
-- !!!: Сохраняйте регистр в мэппингах для Hibernate (на mysql под linux это важно).
--

DROP TABLE IF EXISTS CoffeeOrderItem;
DROP TABLE IF EXISTS CoffeeOrder;
DROP TABLE IF EXISTS CoffeeType;
DROP TABLE IF EXISTS Configuration;
--
-- Сорт кофе
--
CREATE TABLE CoffeeType (
  id        INT          NOT NULL AUTO_INCREMENT, -- pk
  type_name VARCHAR(200) NOT NULL, -- название
  price     DOUBLE       NOT NULL, -- цена
  disabled  CHAR(1), -- если disabled = 'Y', то не показывать данный сорт в списке доступных сортов
  PRIMARY KEY (id)
);
-- ) type=InnoDB;

CREATE INDEX CT_I
  ON CoffeeType (
    id ASC
  );

--
-- Заказ
--
CREATE TABLE CoffeeOrder (
  id               INT          NOT NULL AUTO_INCREMENT, -- pk
  order_date       DATETIME     NOT NULL, -- время заказа
  name             VARCHAR(100), -- имя заказчика
  delivery_address VARCHAR(200) NOT NULL, -- куда доставлять
  cost             DOUBLE, -- сколко стоит (алгоритм подсчёта может поменяться, поэтому надо хранить стоимость)
  PRIMARY KEY (id)
);
-- ) type=InnoDB;

CREATE INDEX CO_I1
  ON CoffeeOrder (
    id ASC
  );

--
-- Одна позиция заказа
--
CREATE TABLE CoffeeOrderItem (
  id       INT NOT NULL AUTO_INCREMENT, -- pk
  type_id  INT NOT NULL, -- сорт кофе
  order_id INT NOT NULL, -- к какому заказу принадлежит
  quantity INT, -- сколько чашек
  PRIMARY KEY (id)
);
-- ) type=InnoDB;

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
REFERENCES CoffeeOrder (id);


ALTER TABLE CoffeeOrderItem
  ADD CONSTRAINT COI_CT FOREIGN KEY (type_id)
REFERENCES CoffeeType (id);

--
-- Конфигурация
--
CREATE TABLE Configuration (
  id    VARCHAR(20) NOT NULL, -- pk, название свойства
  value VARCHAR(30), -- значение
  PRIMARY KEY (id)
);
-- ) type=InnoDB;
