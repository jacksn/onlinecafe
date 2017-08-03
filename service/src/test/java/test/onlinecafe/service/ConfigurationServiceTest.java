package test.onlinecafe.service;

import org.junit.Before;
import org.junit.Test;
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
        when(repository.save(CONFIGURATION_ITEM1)).thenReturn(CONFIGURATION_ITEM1);
        assertEquals(CONFIGURATION_ITEM1, service.save(CONFIGURATION_ITEM1));
        verify(repository).save(CONFIGURATION_ITEM1);
    }

    @Test(expected = DataAccessException.class)
    public void testSaveInvalid() throws Exception {
        when(repository.save(CONFIGURATION_ITEM1)).thenThrow(new DataAccessException(new SQLException()));
        service.save(CONFIGURATION_ITEM1);
    }

    @Test(expected = NullPointerException.class)
    public void testSaveNull() throws Exception {
        service.save(null);
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

    @Test(expected = NullPointerException.class)
    public void testDeleteNull() throws Exception {
        service.delete(null);
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

    @Test(expected = NullPointerException.class)
    public void testGetNull() throws Exception {
        service.get(null);
    }

}
