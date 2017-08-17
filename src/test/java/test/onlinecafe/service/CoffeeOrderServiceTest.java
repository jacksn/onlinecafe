package test.onlinecafe.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import test.onlinecafe.dto.CoffeeOrderDto;
import test.onlinecafe.model.CoffeeOrder;
import test.onlinecafe.repository.CoffeeOrderRepository;
import test.onlinecafe.util.exception.DataAccessException;
import test.onlinecafe.util.exception.NotFoundException;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static test.onlinecafe.CoffeeOrderTestData.*;

public class CoffeeOrderServiceTest {
    private CoffeeOrderService service;
    private CoffeeOrderRepository repository;

    @Before
    public void initClass() {
        repository = Mockito.mock(CoffeeOrderRepository.class);
        service = new CoffeeOrderServiceImpl(repository);
    }

    @Test
    public void testSave() throws Exception {
        CoffeeOrder order = getNewCoffeeOrder();
        when(repository.save(order)).thenReturn(order);
        assertEquals(order, service.save(order));
        verify(repository).save(order);
    }

    @Test(expected = NullPointerException.class)
    public void testSaveNullOrder() throws Exception {
        CoffeeOrder order = null;
        service.save(order);
    }

    @Test(expected = NullPointerException.class)
    public void testSaveNullOrderDto() throws Exception {
        CoffeeOrderDto orderDto = null;
        service.save(orderDto);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveNotNew() throws Exception {
        service.save(getCoffeeOrder1());
    }

    @Test(expected = DataAccessException.class)
    public void testSaveInvalid() throws Exception {
        CoffeeOrder order = getNewCoffeeOrder();
        when(repository.save(order)).thenThrow(new DataAccessException(new SQLException()));
        service.save(order);
    }

    @Test
    public void testUpdate() throws Exception {
        CoffeeOrder order = getCoffeeOrder1();
        when(repository.save(order)).thenReturn(order);
        assertEquals(order, service.update(order));
        verify(repository).save(order);
    }

    @Test(expected = DataAccessException.class)
    public void testUpdateInvalid() throws Exception {
        CoffeeOrder order = getCoffeeOrder1();
        when(repository.save(order)).thenThrow(new DataAccessException(new SQLException()));
        service.update(order);
    }

    @Test(expected = DataAccessException.class)
    public void testUpdateNew() throws Exception {
        CoffeeOrder order = getCoffeeOrder2();
        when(repository.save(order)).thenThrow(new DataAccessException(new SQLException()));
        service.update(order);
    }

    @Test(expected = NullPointerException.class)
    public void testUpdateNull() throws Exception {
        service.update(null);
    }

    @Test
    public void testDelete() throws Exception {
        int id = getCoffeeOrder1().getId();
        when(repository.delete(id)).thenReturn(true);
        service.delete(id);
        verify(repository).delete(id);
    }

    @Test(expected = NotFoundException.class)
    public void testDeleteAbsent() throws Exception {
        when(repository.delete(anyInt())).thenReturn(false);
        service.delete(0);
    }

    @Test
    public void testGet() throws Exception {
        CoffeeOrder order = getCoffeeOrder1();
        int id = order.getId();
        when(repository.get(id)).thenReturn(order);
        assertEquals(order, service.get(order.getId()));
        verify(repository).get(id);
    }

    @Test(expected = NotFoundException.class)
    public void testGetAbsent() throws Exception {
        when(repository.get(anyInt())).thenReturn(null);
        service.get(0);
    }

    @Test
    public void testGetAll() throws Exception {
        when(repository.getAll()).thenReturn(COFFEE_ORDERS);
        assertEquals(COFFEE_ORDERS, service.getAll());
        verify(repository).getAll();
    }
}
