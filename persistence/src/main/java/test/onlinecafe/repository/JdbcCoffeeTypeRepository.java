package test.onlinecafe.repository;

import test.onlinecafe.model.CoffeeType;

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

    public JdbcCoffeeTypeRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public CoffeeType save(CoffeeType type) {
        if (type.isNew()) {
            try (PreparedStatement statement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
                fillStatementParameters(type, statement);
                int affectedRows = statement.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Create of CoffeeType failed, no rows affected.");
                }
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        type.setId((int) generatedKeys.getLong(1));
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
                fillStatementParameters(type, statement);
                statement.setInt(4, type.getId());
                int affectedRows = statement.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Update of CoffeeType failed, no rows affected.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }
        return type;
    }

    private static void fillStatementParameters(CoffeeType type, PreparedStatement statement) throws SQLException {
        statement.setString(1, type.getTypeName());
        statement.setDouble(2, type.getPrice());
        statement.setString(3, type.getDisabled() ? "Y" : "N");
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
                ArrayList<CoffeeType> types = new ArrayList<>();
                while (resultSet.next()) {
                    types.add(getCoffeeType(resultSet));
                }
                return types;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static CoffeeType getCoffeeType(ResultSet resultSet) throws SQLException {
        CoffeeType type = new CoffeeType();
        type.setId(resultSet.getInt("id"));
        type.setTypeName(resultSet.getString("type_name"));
        type.setPrice(resultSet.getDouble("price"));
        type.setDisabled("Y".equals(resultSet.getString("disabled")));
        return type;
    }
}
