package test.onlinecafe.repository;

import test.onlinecafe.model.ConfigurationItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcConfigurationRepository implements ConfigurationRepository {
    private static final String SELECT_QUERY = "SELECT * FROM Configuration WHERE id = ?";
    private static final String INSERT_QUERY = "INSERT INTO Configuration (id, value) VALUES (?, ?)";
    private static final String DELETE_QUERY = "DELETE FROM Configuration WHERE id=?";

    private Connection connection;

    public JdbcConfigurationRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public ConfigurationItem save(ConfigurationItem configurationItem) {
        try (PreparedStatement deleteStatement = connection.prepareStatement(DELETE_QUERY);
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
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void delete(String id) {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setString(1, id);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deletion of configuration item failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ConfigurationItem get(String id) {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_QUERY)) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new ConfigurationItem(id, resultSet.getString("value"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
