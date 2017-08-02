package test.onlinecafe.service;

import org.junit.Before;
import org.junit.Test;
import test.onlinecafe.model.ConfigurationItem;
import test.onlinecafe.repository.ConfigurationRepository;
import test.onlinecafe.util.exception.DataAccessException;
import test.onlinecafe.util.exception.NotFoundException;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static test.onlinecafe.ConfigurationTestData.CONFIGURATION_ITEM1;

public class ConfigurationServiceTest {
    private ConfigurationService service;
    private ConfigurationRepository repository;

    @Before
    public void initClass() {
        repository = mock(ConfigurationRepository.class);
        service = new ConfigurationServiceImpl(repository);
    }

    @Test
    public void testSave() throws Exception {
        String id = "New id";
        ConfigurationItem configurationItem = new ConfigurationItem(id, "New value");
        when(repository.save(configurationItem)).thenReturn(configurationItem);
        assertEquals(configurationItem, service.save(configurationItem));
        verify(repository).save(configurationItem);
    }

    @Test(expected = DataAccessException.class)
    public void testSaveInvalid() throws Exception {
        ConfigurationItem invalidItem = new ConfigurationItem(null, "Invalid");
        when(repository.save(invalidItem)).thenThrow(new DataAccessException(new SQLException()));
        when(repository.save(CONFIGURATION_ITEM1)).thenReturn(CONFIGURATION_ITEM1);
        service.save(invalidItem);
    }

    @Test
    public void testDelete() throws Exception {
        String id = CONFIGURATION_ITEM1.getId();
        when(repository.delete(id)).thenReturn(true);
        service.delete(id);
        verify(repository).delete(id);
    }

    @Test(expected = NotFoundException.class)
    public void testDeleteAbsent() throws Exception {
        String id = "ABSENT";
        when(repository.delete(id)).thenReturn(false);
        service.delete(id);
    }

    @Test
    public void testGet() throws Exception {
        String id = CONFIGURATION_ITEM1.getId();
        when(repository.get(id)).thenReturn(CONFIGURATION_ITEM1);
        assertEquals(CONFIGURATION_ITEM1, service.get(CONFIGURATION_ITEM1.getId()));
        verify(repository).get(id);
    }

    @Test(expected = NotFoundException.class)
    public void testGetAbsent() throws Exception {
        String id = "ABSENT";
        when(repository.get(id)).thenReturn(null);
        service.get(id);
    }
}
