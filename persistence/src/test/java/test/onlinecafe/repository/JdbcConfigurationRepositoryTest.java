package test.onlinecafe.repository;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import test.onlinecafe.model.ConfigurationItem;

import static org.junit.Assert.assertEquals;

public class JdbcConfigurationRepositoryTest extends AbstractJdbcRepositoryTest {
    private static ConfigurationRepository repository;

    @BeforeClass
    public static void init() {
        initDatabase();
        repository = new JdbcConfigurationRepository(connection);
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
        connection.setAutoCommit(false);
        repository.delete("x");
        try {
            Assert.assertEquals(null, repository.get("x"));
        } finally {
            connection.rollback();
        }
        connection.setAutoCommit(true);
    }

    @Test
    public void testGet() throws Exception {
        ConfigurationItem expected = new ConfigurationItem("x", "10");
        ConfigurationItem actual = repository.get("x");
        assertEquals(expected, actual);
    }

}