package test.onlinecafe.repository.jdbc;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import test.onlinecafe.model.CoffeeType;
import test.onlinecafe.repository.CoffeeTypeRepository;
import test.onlinecafe.util.exception.DataAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Profile("repo-jdbc")
@Repository
public class JdbcCoffeeTypeRepository implements CoffeeTypeRepository {
    private static final String SELECT_ALL = "SELECT * FROM CoffeeType";
    private static final String SELECT_ENABLED = "SELECT * FROM CoffeeType WHERE disabled = 'N'";
    private static final String SELECT_ONE = "SELECT * FROM CoffeeType WHERE id = ?";
    private static final String INSERT = "INSERT INTO CoffeeType (type_name, price, disabled) VALUES (?, ?, ?)";
    private static final String UPDATE = "UPDATE CoffeeType SET type_name = ?, price = ?, disabled = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM CoffeeType WHERE id=?";

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

    // TODO: add transactions
    @Override
    public CoffeeType save(CoffeeType type) {
        if (type.isNew()) {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
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
                 PreparedStatement statement = connection.prepareStatement(UPDATE)) {
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

    // TODO: add transactions
    @SuppressWarnings("Duplicates")
    @Override
    public boolean delete(int id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE)) {
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
             PreparedStatement statement = connection.prepareStatement(SELECT_ONE)) {
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
        return getCoffeeTypes(false);
    }

    @Override
    public List<CoffeeType> getEnabled() {
        return getCoffeeTypes(true);
    }

    private List<CoffeeType> getCoffeeTypes(boolean onlyEnabled) {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(onlyEnabled ? SELECT_ENABLED : SELECT_ALL)) {
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
