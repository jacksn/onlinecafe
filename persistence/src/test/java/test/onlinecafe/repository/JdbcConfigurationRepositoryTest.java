package test.onlinecafe.repository;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import test.onlinecafe.model.ConfigurationItem;
import test.onlinecafe.util.exception.DataAccessException;

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

    @Test(expected = DataAccessException.class)
    public void testSaveInvalid() throws Exception {
        repository.save(new ConfigurationItem(null, "New value"));
    }

    @Test
    public void testDelete() throws Exception {
            repository.delete("x");
                Assert.assertEquals(null, repository.get("x"));
    }

    @Test
    public void testGet() throws Exception {
        ConfigurationItem expected = new ConfigurationItem("x", "10");
        ConfigurationItem actual = repository.get("x");
        assertEquals(expected, actual);
    }

}