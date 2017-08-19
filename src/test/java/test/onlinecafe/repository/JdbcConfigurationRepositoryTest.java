package test.onlinecafe.repository;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import test.onlinecafe.model.ConfigurationItem;
import test.onlinecafe.util.exception.DataAccessException;

import static org.junit.Assert.assertEquals;
import static test.onlinecafe.ConfigurationTestData.CONFIGURATION_ITEM1;

public class JdbcConfigurationRepositoryTest extends AbstractJdbcRepositoryTest {
    @Autowired
    private ConfigurationRepository repository;

    @Test
    public void testSave() throws Exception {
        String id = "New id";
        ConfigurationItem configurationItem = new ConfigurationItem(id, "New value");
        repository.save(configurationItem);
        assertEquals(configurationItem, repository.get(id));
    }

    @Test
    public void testSaveInvalid() throws Exception {
        thrown.expect(DataAccessException.class);
        repository.save(new ConfigurationItem(null, "New value"));
    }

    @Test
    public void testDelete() throws Exception {
        repository.delete(CONFIGURATION_ITEM1.getId());
        Assert.assertNull(repository.get(CONFIGURATION_ITEM1.getId()));
    }

    @Test
    public void testDeleteAbsent() throws Exception {
        Assert.assertFalse(repository.delete("Absent key"));
    }

    @Test
    public void testGet() throws Exception {
        ConfigurationItem actual = repository.get(CONFIGURATION_ITEM1.getId());
        assertEquals(CONFIGURATION_ITEM1, actual);
    }
}
