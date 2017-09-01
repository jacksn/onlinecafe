package test.onlinecafe.repository.jdbc;

public final class CoffeeOrderQueryStrings {
    public static final String SELECT_ALL_QUERY = "SELECT " +
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

    public static final String SELECT_QUERY = "SELECT " +
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

    public static final String INSERT_ORDER_QUERY = "INSERT INTO CoffeeOrder (order_date, name, delivery_address, cost) " +
            "VALUES (?, ?, ?, ?)";
    public static final String INSERT_ORDER_ITEMS_QUERY = "INSERT INTO CoffeeOrderItem (type_id, order_id, quantity) VALUES (?, ?, ?)";

    public static final String DELETE_ORDER_QUERY = "DELETE FROM CoffeeOrder WHERE id=?";

    private CoffeeOrderQueryStrings() {
    }
}
