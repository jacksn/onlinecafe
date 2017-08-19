package test.onlinecafe.repository;

public final class CoffeeOrderQueryStrings {
    static final String SELECT_ALL_QUERY = "SELECT " +
            "  coffeeorder.id AS order_id," +
            "  order_date," +
            "  name," +
            "  delivery_address," +
            "  cost," +
            "  coffeeorderitem.id AS order_item_id, " +
            "  coffeetype.id AS type_id," +
            "  coffeetype.type_name, " +
            "  coffeetype.disabled, " +
            "  coffeetype.price, " +
            "  coffeeorderitem.quantity " +
            "FROM coffeeorder " +
            "  LEFT JOIN coffeeorderitem " +
            "    ON coffeeorder.id = coffeeorderitem.order_id " +
            "  JOIN coffeetype ON coffeeorderitem.type_id = coffeetype.id " +
            "ORDER BY coffeeorder.id, coffeeorderitem.id";

    static final String SELECT_QUERY = "SELECT " +
            "  coffeeorder.id AS order_id, " +
            "  order_date, " +
            "  name, " +
            "  delivery_address, " +
            "  cost, " +
            "  coffeeorderitem.id AS order_item_id, " +
            "  coffeetype.id AS type_id, " +
            "  coffeetype.type_name, " +
            "  coffeetype.disabled, " +
            "  coffeetype.price, " +
            "  coffeeorderitem.quantity " +
            "FROM coffeeorder " +
            "  LEFT JOIN coffeeorderitem " +
            "    ON coffeeorder.id = coffeeorderitem.order_id " +
            "  JOIN coffeetype ON coffeeorderitem.type_id = coffeetype.id\n" +
            "WHERE order_id = ? " +
            "ORDER BY coffeeorderitem.id";

    static final String INSERT_ORDER_QUERY = "INSERT INTO CoffeeOrder (order_date, name, delivery_address, cost) " +
            "VALUES (?, ?, ?, ?)";
    static final String INSERT_ORDER_ITEMS_QUERY = "INSERT INTO CoffeeOrderItem (type_id, order_id, quantity) VALUES (?, ?, ?)";

    static final String DELETE_ORDER_QUERY = "DELETE FROM CoffeeOrder WHERE id=?";

    private CoffeeOrderQueryStrings() {
    }
}
