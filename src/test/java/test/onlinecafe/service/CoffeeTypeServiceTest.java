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
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static test.onlinecafe.CoffeeTypeTestData.*;

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
        CoffeeType newType = getNewCoffeeType();
        when(repository.save(newType)).thenReturn(newType);
        assertEquals(newType, service.save(newType));
        verify(repository).save(newType);
    }

    @Test(expected = NullPointerException.class)
    public void testSaveNull() throws Exception {
        service.save(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveNotNew() throws Exception {
        service.save(COFFEE_TYPE1);
    }

    @Test(expected = DataAccessException.class)
    public void testSaveInvalid() throws Exception {
        CoffeeType newType = getNewCoffeeType();
        when(repository.save(newType)).thenThrow(new DataAccessException(new SQLException()));
        service.save(newType);
    }

    @Test
    public void testUpdate() throws Exception {
        when(repository.save(COFFEE_TYPE1)).thenReturn(COFFEE_TYPE1);
        assertEquals(COFFEE_TYPE1, service.update(COFFEE_TYPE1));
        verify(repository).save(COFFEE_TYPE1);
    }

    @Test(expected = DataAccessException.class)
    public void testUpdateInvalid() throws Exception {
        when(repository.save(COFFEE_TYPE1)).thenThrow(new DataAccessException(new SQLException()));
        service.update(COFFEE_TYPE1);
    }

    @Test(expected = NullPointerException.class)
    public void testUpdateNull() throws Exception {
        service.update(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateNew() throws Exception {
        CoffeeType newType = getNewCoffeeType();
        when(repository.save(newType)).thenThrow(new DataAccessException(new SQLException()));
        service.update(newType);
    }

    @Test
    public void testDelete() throws Exception {
        when(repository.delete(anyInt())).thenReturn(true);
        service.delete(0);
        verify(repository).delete(0);
    }

    @Test(expected = NotFoundException.class)
    public void testDeleteAbsent() throws Exception {
        when(repository.delete(anyInt())).thenReturn(false);
        service.delete(0);
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
        when(repository.getEnabled()).thenReturn(COFFEE_TYPES_ENABLED);
        assertEquals(COFFEE_TYPES_ENABLED, service.getEnabled());
        verify(repository).getEnabled();
    }
}
