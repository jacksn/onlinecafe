package test.onlinecafe.repository;

import test.onlinecafe.model.CoffeeType;
import test.onlinecafe.util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcCoffeeTypeRepository implements CoffeeTypeRepository {
    private static final String SELECT_ALL_QUERY = "SELECT * FROM CoffeeType";
    private static final String SELECT_QUERY = "SELECT * FROM CoffeeType WHERE id = ?";
    private static final String INSERT_QUERY = "INSERT INTO CoffeeType (id, type_name, price, disabled) VALUES (NULL, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE CoffeeType SET type_name = ?, price = ?, disabled = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM CoffeeType WHERE id=?";

    private Connection connection;

    public JdbcCoffeeTypeRepository() {
        this.connection = DbUtil.getConnection();
    }

    @Override
    public CoffeeType save(CoffeeType coffeeType) {
        if (coffeeType.isNew()) {
            try (PreparedStatement statement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
                fillStatementParameters(coffeeType, statement);
                int affectedRows = statement.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Create of CoffeeType failed, no rows affected.");
                }
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        coffeeType.setId((int) generatedKeys.getLong(1));
                    } else {
                        throw new SQLException("Create of CoffeeType failed, no ID obtained.");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
                fillStatementParameters(coffeeType, statement);
                statement.setInt(4, coffeeType.getId());
                int affectedRows = statement.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Update of CoffeeType failed, no rows affected.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }
        return coffeeType;
    }

    private static void fillStatementParameters(CoffeeType coffeeType, PreparedStatement statement) throws SQLException {
        statement.setString(1, coffeeType.getTypeName());
        statement.setDouble(2, coffeeType.getPrice());
        statement.setString(3, coffeeType.getDisabled() ? "Y" : "N");
    }

    @Override
    public void delete(int id) {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, id);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deletion of CoffeeType failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public CoffeeType get(int id) {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_QUERY)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return getCoffeeType(resultSet);
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
    public List<CoffeeType> getAll() {
        try (Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(SELECT_ALL_QUERY)) {
                ArrayList<CoffeeType> coffeeTypes = new ArrayList<>();
                while (resultSet.next()) {
                    CoffeeType coffeeType = getCoffeeType(resultSet);
                    coffeeTypes.add(coffeeType);
                }
                return coffeeTypes;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static CoffeeType getCoffeeType(ResultSet resultSet) throws SQLException {
        CoffeeType coffeeType = new CoffeeType();
        coffeeType.setId(resultSet.getInt("id"));
        coffeeType.setTypeName(resultSet.getString("type_name"));
        coffeeType.setPrice(resultSet.getDouble("price"));
        coffeeType.setDisabled("Y".equals(resultSet.getString("disabled")));
        return coffeeType;
    }
}
