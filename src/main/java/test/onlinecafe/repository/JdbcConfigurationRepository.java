package test.onlinecafe.repository;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import test.onlinecafe.model.ConfigurationItem;
import test.onlinecafe.util.exception.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class JdbcConfigurationRepository implements ConfigurationRepository {
    private static final String SELECT_QUERY = "SELECT * FROM Configuration WHERE id = ?";
    private static final String INSERT_QUERY = "INSERT INTO Configuration (id, value) VALUES (?, ?)";
    private static final String DELETE_QUERY = "DELETE FROM Configuration WHERE id=?";

    private static final Logger log = LoggerFactory.getLogger(JdbcConfigurationRepository.class);

    private DataSource dataSource;

    public JdbcConfigurationRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ConfigurationItem save(ConfigurationItem configurationItem) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(DELETE_QUERY);
             PreparedStatement insertStatement = connection.prepareStatement(INSERT_QUERY)) {
            String id = configurationItem.getId();
            deleteStatement.setString(1, id);
            deleteStatement.executeUpdate();
            insertStatement.setString(1, id);
            insertStatement.setString(2, configurationItem.getValue());
            int affectedRows = insertStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creation/update of configuration item failed, no rows affected.");
            }
        } catch (SQLException e) {
            log.warn(e.getMessage());
            throw new DataAccessException(e);
        }
        return null;
    }

    @Override
    public boolean delete(String id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setString(1, id);
            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            log.warn(e.getMessage());
            throw new DataAccessException(e);
        }
    }

    @Override
    public ConfigurationItem get(String id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_QUERY)) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? new ConfigurationItem(id, resultSet.getString("value")) : null;
            }
        } catch (SQLException e) {
            log.warn(e.getMessage());
            throw new DataAccessException(e);
        }
    }
}
