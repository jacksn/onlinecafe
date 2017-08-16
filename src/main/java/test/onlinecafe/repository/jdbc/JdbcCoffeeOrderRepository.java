package test.onlinecafe.repository.jdbc;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import test.onlinecafe.model.CoffeeOrder;
import test.onlinecafe.model.CoffeeOrderItem;
import test.onlinecafe.model.CoffeeType;
import test.onlinecafe.repository.CoffeeOrderRepository;
import test.onlinecafe.util.exception.DataAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static test.onlinecafe.repository.jdbc.CoffeeOrderQueryStrings.*;

@Profile("repo-jdbc")
@Repository
public class JdbcCoffeeOrderRepository implements CoffeeOrderRepository {
    private static final Logger log = LoggerFactory.getLogger(JdbcCoffeeOrderRepository.class);

    private DataSource dataSource;

    public JdbcCoffeeOrderRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private static void fillStatementParameters(CoffeeOrder order, PreparedStatement statement) throws SQLException {
        statement.setTimestamp(1, Timestamp.valueOf(order.getOrderDate()));
        statement.setString(2, order.getName());
        statement.setString(3, order.getDeliveryAddress());
        statement.setDouble(4, order.getCost());
    }

    private static void saveCoffeeOrderItems(int orderId, List<CoffeeOrderItem> orderItems, PreparedStatement orderItemsStatement) throws SQLException {
        for (CoffeeOrderItem orderItem : orderItems) {
            orderItemsStatement.setInt(1, orderItem.getCoffeeType().getId());
            orderItemsStatement.setInt(2, orderId);
            orderItemsStatement.setInt(3, orderItem.getQuantity());
            orderItemsStatement.addBatch();
        }
        orderItemsStatement.executeBatch();
        ResultSet resultSet = orderItemsStatement.getGeneratedKeys();
        List<Integer> ids = new ArrayList<>();
        while (resultSet.next()) {
            ids.add(resultSet.getInt(1));
        }
        if (ids.size() != orderItems.size()) {
            throw new SQLException("Create of CoffeeOrderItems failed, obtained CoffeeOrderItems ID's count " +
                    "differs from CoffeeOrderItems count.");
        }
        for (int i = 0; i < orderItems.size(); i++) {
            orderItems.get(i).setId(ids.get(i));
        }
    }

    private static List<CoffeeOrder> getCoffeeOrders(ResultSet resultSet) throws SQLException {
        Map<Integer, CoffeeOrder> ordersMap = new HashMap<>();
        while (resultSet.next()) {
            int coffeeOrderId = resultSet.getInt("order_id");
            CoffeeOrder order = ordersMap.get(coffeeOrderId);
            if (order == null) {
                order = new CoffeeOrder(coffeeOrderId,
                        resultSet.getTimestamp("order_date").toLocalDateTime(),
                        resultSet.getString("name"),
                        resultSet.getString("delivery_address"),
                        new ArrayList<>(),
                        resultSet.getDouble("cost"));
                ordersMap.put(coffeeOrderId, order);
            }
            CoffeeOrderItem orderItem = new CoffeeOrderItem();
            orderItem.setId(resultSet.getInt("order_item_id"));
            orderItem.setCoffeeType(new CoffeeType(resultSet.getInt("type_id"),
                    resultSet.getString("type_name"),
                    resultSet.getDouble("price"),
                    "Y".equals(resultSet.getString("disabled"))));
            orderItem.setQuantity(resultSet.getInt("quantity"));
            order.getOrderItems().add(orderItem);
        }
        return new ArrayList<>(ordersMap.values());
    }

    // TODO: add transactions
    @Override
    public CoffeeOrder save(CoffeeOrder order) {
        try (Connection connection = dataSource.getConnection()) {
            if (order.isNew()) {
                try (PreparedStatement orderStatement = connection.prepareStatement(INSERT_ORDER_QUERY, Statement.RETURN_GENERATED_KEYS);
                     PreparedStatement orderItemsStatement = connection.prepareStatement(INSERT_ORDER_ITEMS_QUERY, Statement.RETURN_GENERATED_KEYS)) {
                    connection.setAutoCommit(false);
                    fillStatementParameters(order, orderStatement);
                    int affectedRows = orderStatement.executeUpdate();
                    if (affectedRows == 0) {
                        throw new SQLException("Create of CoffeeOrder failed, no rows affected.");
                    }
                    try (ResultSet generatedKeys = orderStatement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            order.setId((int) generatedKeys.getLong(1));
                        } else {
                            throw new SQLException("Create of CoffeeOrder failed, no ID obtained.");
                        }
                    }
                    saveCoffeeOrderItems(order.getId(), order.getOrderItems(), orderItemsStatement);
                    connection.commit();
                    connection.setAutoCommit(true);
                } catch (SQLException e) {
                    log.warn(e.getMessage());
                    connection.rollback();
                    connection.setAutoCommit(true);
                    throw new DataAccessException(e);
                }
            } else {
                try (PreparedStatement orderStatement = connection.prepareStatement(UPDATE_ORDER_QUERY);
                     PreparedStatement orderItemsStatement = connection.prepareStatement(INSERT_ORDER_ITEMS_QUERY, Statement.RETURN_GENERATED_KEYS);
                     PreparedStatement deleteOrderItemsStatement = connection.prepareStatement(DELETE_ORDER_ITEMS_QUERY)) {
                    connection.setAutoCommit(false);
                    CoffeeOrder oldOrder = get(order.getId());
                    if (oldOrder == null) {
                        throw new SQLException("Update of CoffeeOrder failed, order not found.");
                    }
                    List<CoffeeOrderItem> oldOrderItems = oldOrder.getOrderItems();
                    fillStatementParameters(order, orderStatement);
                    orderStatement.setInt(5, order.getId());
                    int affectedRows = orderStatement.executeUpdate();
                    if (affectedRows == 0) {
                        throw new SQLException("Update of CoffeeOrder failed, no rows affected.");
                    }
                    if (!order.getOrderItems().equals(oldOrderItems)) {
                        int orderId = order.getId();
                        deleteOrderItemsStatement.setInt(1, orderId);
                        deleteOrderItemsStatement.execute();
                        saveCoffeeOrderItems(orderId, order.getOrderItems(), orderItemsStatement);
                    }
                    connection.commit();
                    connection.setAutoCommit(true);
                } catch (SQLException e) {
                    log.warn(e.getMessage());
                    connection.rollback();
                    connection.setAutoCommit(true);
                    throw new DataAccessException(e);
                }
            }
        } catch (SQLException e) {
            log.warn(e.getMessage());
            throw new DataAccessException(e);
        }
        return order;
    }

    // TODO: add transactions
    @SuppressWarnings("Duplicates")
    @Override
    public boolean delete(int id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_ORDER_QUERY)) {
            statement.setInt(1, id);
            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            log.warn(e.getMessage());
            throw new DataAccessException(e);
        }
    }

    @Override
    public CoffeeOrder get(int id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_QUERY)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<CoffeeOrder> orders = getCoffeeOrders(resultSet);
                return orders.isEmpty() ? null : orders.get(0);
            }
        } catch (SQLException e) {
            log.warn(e.getMessage());
            throw new DataAccessException(e);
        }
    }

    @Override
    public List<CoffeeOrder> getAll() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_QUERY)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                return getCoffeeOrders(resultSet);
            }
        } catch (SQLException e) {
            log.warn(e.getMessage());
            throw new DataAccessException(e);
        }
    }

}
