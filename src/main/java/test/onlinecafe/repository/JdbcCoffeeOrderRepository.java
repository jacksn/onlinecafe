package test.onlinecafe.repository;

import test.onlinecafe.model.CoffeeOrder;
import test.onlinecafe.model.CoffeeOrderItem;
import test.onlinecafe.model.CoffeeType;
import test.onlinecafe.util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static test.onlinecafe.repository.CoffeeOrderQueryStrings.*;

public class JdbcCoffeeOrderRepository implements CoffeeOrderRepository {

    private Connection connection;

    public JdbcCoffeeOrderRepository() {
        this.connection = DbUtil.getConnection();
    }

    private static List<CoffeeOrder> getCoffeeOrdersFromResultSetRow(ResultSet resultSet) throws SQLException {
        resultSet.beforeFirst();
        Map<Integer, CoffeeOrder> coffeeOrderMap = new HashMap<>();
        while (resultSet.next()) {
            int coffeeOrderId = resultSet.getInt("order_id");
            CoffeeOrder coffeeOrder = coffeeOrderMap.get(coffeeOrderId);
            if (coffeeOrder == null) {
                coffeeOrder = new CoffeeOrder(
                        coffeeOrderId,
                        resultSet.getTimestamp("order_date").toLocalDateTime(),
                        resultSet.getString("name"),
                        resultSet.getString("delivery_address"),
                        new ArrayList<>(),
                        resultSet.getDouble("cost"));
                coffeeOrderMap.put(coffeeOrderId, coffeeOrder);
            }
            CoffeeOrderItem orderItem = new CoffeeOrderItem();
            orderItem.setId(resultSet.getInt("order_item_id"));
            orderItem.setCoffeeType(
                    new CoffeeType(
                            resultSet.getInt("type_id"),
                            resultSet.getString("type_name"),
                            resultSet.getDouble("price"),
                            "Y".equals(resultSet.getString("disabled"))
                    )
            );
            orderItem.setQuantity(resultSet.getInt("quantity"));
            coffeeOrder.getOrderItems().add(orderItem);
        }
        return new ArrayList<>(coffeeOrderMap.values());
    }

    private static void fillStatementParameters(CoffeeOrder coffeeOrder, PreparedStatement statement) throws SQLException {
        statement.setTimestamp(1, Timestamp.valueOf(coffeeOrder.getOrderDate()));
        statement.setString(2, coffeeOrder.getName());
        statement.setString(3, coffeeOrder.getDeliveryAddress());
        statement.setDouble(4, coffeeOrder.getCost());
    }

    @Override
    public CoffeeOrder save(CoffeeOrder coffeeOrder) {
        boolean inTransaction = false;
        if (coffeeOrder.isNew()) {
            try (PreparedStatement orderStatement = connection.prepareStatement(INSERT_ORDER_QUERY, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement orderItemsStatement = connection.prepareStatement(INSERT_ORDER_ITEMS_QUERY, Statement.RETURN_GENERATED_KEYS)) {

                // Begin transaction if there is no one yet
                inTransaction = !connection.getAutoCommit();
                if (!inTransaction) {
                    connection.setAutoCommit(false);
                }

                fillStatementParameters(coffeeOrder, orderStatement);

                int affectedRows = orderStatement.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Create of CoffeeOrder failed, no rows affected.");
                }

                try (ResultSet generatedKeys = orderStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        coffeeOrder.setId((int) generatedKeys.getLong(1));
                    } else {
                        throw new SQLException("Create of CoffeeOrder failed, no ID obtained.");
                    }
                }

                List<CoffeeOrderItem> orderItems = coffeeOrder.getOrderItems();
                int orderId = coffeeOrder.getId();

                saveCoffeeOrderItems(orderId, orderItems, orderItemsStatement);
                if (!inTransaction) {
                    connection.commit();
                    connection.setAutoCommit(true);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                if (!inTransaction) {
                    closeTransaction();
                }
                return null;
            }
        } else {
            try (PreparedStatement orderStatement = connection.prepareStatement(UPDATE_ORDER_QUERY);
                 PreparedStatement orderItemsStatement = connection.prepareStatement(INSERT_ORDER_ITEMS_QUERY, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement deleteOrderItemsStatement = connection.prepareStatement(DELETE_ORDER_ITEMS_QUERY)) {

                // Begin transaction if there is no one yet
                inTransaction = !connection.getAutoCommit();
                if (!inTransaction) {
                    connection.setAutoCommit(false);
                }

                CoffeeOrder oldOrder = get(coffeeOrder.getId());
                if (oldOrder == null) {
                    throw new SQLException("Update of CoffeeOrder failed, order not found.");
                }
                List<CoffeeOrderItem> oldOrderItems = oldOrder.getOrderItems();

                fillStatementParameters(coffeeOrder, orderStatement);
                orderStatement.setInt(5, coffeeOrder.getId());
                int affectedRows = orderStatement.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Update of CoffeeOrder failed, no rows affected.");
                }
                if (!coffeeOrder.getOrderItems().equals(oldOrderItems)) {
                    int orderId = coffeeOrder.getId();
                    deleteOrderItemsStatement.setInt(1, orderId);
                    deleteOrderItemsStatement.execute();
                    saveCoffeeOrderItems(orderId, coffeeOrder.getOrderItems(), orderItemsStatement);
                }

                if (!inTransaction) {
                    // Commit transaction
                    connection.commit();
                    connection.setAutoCommit(true);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                if (!inTransaction) {
                    closeTransaction();
                }
                return null;
            }
        }
        return coffeeOrder;
    }

    private static void saveCoffeeOrderItems(int orderId, List<CoffeeOrderItem> orderItems, PreparedStatement orderItemsStatement) throws SQLException {
        for (CoffeeOrderItem coffeeOrderItem : orderItems) {
            orderItemsStatement.setInt(1, coffeeOrderItem.getCoffeeType().getId());
            orderItemsStatement.setInt(2, orderId);
            orderItemsStatement.setInt(3, coffeeOrderItem.getQuantity());
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

    private void closeTransaction() {
        try {
            connection.rollback();
            connection.setAutoCommit(true);
        } catch (SQLException conEx) {
            conEx.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_ORDER_QUERY)) {
            statement.setInt(1, id);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deletion of CoffeeOrder failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public CoffeeOrder get(int id) {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_QUERY)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<CoffeeOrder> coffeeOrders = getCoffeeOrdersFromResultSetRow(resultSet);
                if (!coffeeOrders.isEmpty()) {
                    return coffeeOrders.get(0);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<CoffeeOrder> getAll() {
        try (ResultSet resultSet = connection.createStatement().executeQuery(SELECT_ALL_QUERY)) {
            List<CoffeeOrder> coffeeOrders = getCoffeeOrdersFromResultSetRow(resultSet);
            if (!coffeeOrders.isEmpty()) {
                return coffeeOrders;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
