package test.onlinecafe.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import test.onlinecafe.model.CoffeeType;
import test.onlinecafe.repository.CoffeeTypeRepository;
import test.onlinecafe.util.exception.DataAccessException;
import test.onlinecafe.util.exception.NotFoundException;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static test.onlinecafe.CoffeeTypeTestData.COFFEE_TYPE1;
import static test.onlinecafe.CoffeeTypeTestData.COFFEE_TYPES_ALL;
import static test.onlinecafe.CoffeeTypeTestData.COFFEE_TYPES_ENABLED;

public class CoffeeTypeServiceTest {
    private CoffeeTypeService service;
    private CoffeeTypeRepository repository;

    @Before
    public void initClass() {
        repository = Mockito.mock(CoffeeTypeRepository.class);
        service = new CoffeeTypeServiceImpl(repository);
    }

    @Test
    public void testSave() throws Exception {
        CoffeeType newType = new CoffeeType(null, "New type", 1.0, false);
        CoffeeType savedType = new CoffeeType(1, "New type", 1.0, false);
        when(repository.save(newType)).thenReturn(savedType);
        assertEquals(savedType, service.save(newType));
        verify(repository).save(newType);
    }

    @Test(expected = DataAccessException.class)
    public void testSaveInvalid() throws Exception {
        CoffeeType invalid = new CoffeeType(null, 0.0, null);
        when(repository.save(invalid)).thenThrow(new DataAccessException(new SQLException()));
        service.save(invalid);
    }

    @Test
    public void testUpdate() throws Exception {
        CoffeeType updatedType = new CoffeeType(1, "Updated type", 2.0, true);
        when(repository.save(updatedType)).thenReturn(updatedType);
        assertEquals(updatedType, service.save(updatedType));
        verify(repository).save(updatedType);
    }

    @Test(expected = DataAccessException.class)
    public void testUpdateInvalid() throws Exception {
        CoffeeType invalid = new CoffeeType(null, 0.0, null);
        when(repository.save(invalid)).thenThrow(new DataAccessException(new SQLException()));
        service.save(invalid);
    }

    @Test
    public void testDelete() throws Exception {
        int id = COFFEE_TYPE1.getId();
        when(repository.delete(id)).thenReturn(true);
        service.delete(id);
        verify(repository).delete(id);
    }

    @Test(expected = NotFoundException.class)
    public void testDeleteAbsent() throws Exception {
        int id = Integer.MAX_VALUE;
        when(repository.delete(id)).thenReturn(false);
        service.delete(id);
    }

    @Test
    public void testGet() throws Exception {
        int id = COFFEE_TYPE1.getId();
        when(repository.get(id)).thenReturn(COFFEE_TYPE1);
        assertEquals(COFFEE_TYPE1, service.get(COFFEE_TYPE1.getId()));
        verify(repository).get(id);
    }

    @Test(expected = NotFoundException.class)
    public void testGetAbsent() throws Exception {
        int id = Integer.MAX_VALUE;
        when(repository.get(id)).thenReturn(null);
        service.get(id);
    }

    @Test
    public void testGetAll() throws Exception {
        when(repository.getAll()).thenReturn(COFFEE_TYPES_ALL);
        assertEquals(COFFEE_TYPES_ALL, service.getAll());
        verify(repository).getAll();
    }

    @Test
    public void testGetEnabled() throws Exception {
        when(repository.getAll()).thenReturn(COFFEE_TYPES_ALL);
        assertEquals(COFFEE_TYPES_ENABLED, service.getEnabled());
        verify(repository).getAll();
    }
}
