package test.onlinecafe.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test.onlinecafe.model.CoffeeType;
import test.onlinecafe.util.exception.DataAccessException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcCoffeeTypeRepository implements CoffeeTypeRepository {
    private static final String SELECT_ALL_QUERY = "SELECT * FROM CoffeeType";
    private static final String SELECT_QUERY = "SELECT * FROM CoffeeType WHERE id = ?";
    private static final String INSERT_QUERY = "INSERT INTO CoffeeType (id, type_name, price, disabled) VALUES (NULL, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE CoffeeType SET type_name = ?, price = ?, disabled = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM CoffeeType WHERE id=?";

    private static final Logger log = LoggerFactory.getLogger(JdbcCoffeeTypeRepository.class);

    private DataSource dataSource;

    public JdbcCoffeeTypeRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private static void fillStatementParameters(CoffeeType type, PreparedStatement statement) throws SQLException {
        statement.setString(1, type.getTypeName());
        statement.setDouble(2, type.getPrice());
        statement.setString(3, type.getDisabled() ? "Y" : "N");
    }

    private static CoffeeType getCoffeeType(ResultSet resultSet) throws SQLException {
        CoffeeType type = new CoffeeType();
        type.setId(resultSet.getInt("id"));
        type.setTypeName(resultSet.getString("type_name"));
        type.setPrice(resultSet.getDouble("price"));
        type.setDisabled("Y".equals(resultSet.getString("disabled")));
        return type;
    }

    @Override
    public CoffeeType save(CoffeeType type) {
        if (type.isNew()) {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
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
                log.warn(e.getMessage());
                throw new DataAccessException(e);
            }
        } else {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
                fillStatementParameters(type, statement);
                statement.setInt(4, type.getId());
                int affectedRows = statement.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Update of CoffeeType failed, no rows affected.");
                }
            } catch (SQLException e) {
                log.warn(e.getMessage());
                throw new DataAccessException(e);
            }
        }
        return type;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public boolean delete(int id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, id);
            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            log.warn(e.getMessage());
            throw new DataAccessException(e);
        }
    }

    @Override
    public CoffeeType get(int id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_QUERY)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? getCoffeeType(resultSet) : null;
            }
        } catch (SQLException e) {
            log.warn(e.getMessage());
            throw new DataAccessException(e);
        }
    }

    @Override
    public List<CoffeeType> getAll() {
        try (Connection connection = dataSource.getConnection();
             ResultSet resultSet = connection.createStatement().executeQuery(SELECT_ALL_QUERY)) {
            ArrayList<CoffeeType> types = new ArrayList<>();
            while (resultSet.next()) {
                types.add(getCoffeeType(resultSet));
            }
            return types;
        } catch (SQLException e) {
            log.warn(e.getMessage());
            throw new DataAccessException(e);
        }
    }
}
