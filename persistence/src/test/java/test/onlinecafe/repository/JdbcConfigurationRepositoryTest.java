package test.onlinecafe.repository;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import test.onlinecafe.model.ConfigurationItem;

import java.sql.Connection;

import static org.junit.Assert.assertEquals;

public class JdbcConfigurationRepositoryTest extends AbstractJdbcRepositoryTest {
    private static ConfigurationRepository repository;

    @BeforeClass
    public static void init() {
        repository = new JdbcConfigurationRepository(dataSource);
    }

    @Test
    public void testSave() throws Exception {
        String id = "New id";
        ConfigurationItem configurationItem = new ConfigurationItem(id, "New value");
        repository.save(configurationItem);
        assertEquals(configurationItem, repository.get(id));
    }

    @Test
    public void testSaveInvalid() throws Exception {
        ConfigurationItem configurationItem = new ConfigurationItem(null, "New value");
        configurationItem = repository.save(configurationItem);
        assertEquals(null, configurationItem);
    }

    @Test
    public void testDelete() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            repository.delete("x");
            try {
                Assert.assertEquals(null, repository.get("x"));
            } finally {
                connection.rollback();
            }
            connection.setAutoCommit(true);
            connection.close();
        }
    }

    @Test
    public void testGet() throws Exception {
        ConfigurationItem expected = new ConfigurationItem("x", "10");
        ConfigurationItem actual = repository.get("x");
        assertEquals(expected, actual);
    }

}