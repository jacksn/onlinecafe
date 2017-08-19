package test.onlinecafe.repository;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import test.onlinecafe.model.CoffeeOrder;
import test.onlinecafe.util.exception.DataAccessException;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;
import static test.onlinecafe.CoffeeOrderTestData.*;

public class JdbcCoffeeOrderRepositoryTest extends AbstractJdbcRepositoryTest {
    @Autowired
    private CoffeeOrderRepository repository;

    private void assertOrdersEqual(CoffeeOrder order1, CoffeeOrder order2) {
        assertEquals(order1.getName(), order2.getName());
        assertEquals(order1.getDeliveryAddress(), order2.getDeliveryAddress());
        assertEquals(order1.getOrderDate(), order2.getOrderDate());
        assertTrue(Double.compare(order1.getCost(), order2.getCost()) == 0);
        assertEquals(order1.getId(), order2.getId());
        assertEquals(new ArrayList<>(order1.getOrderItems()), new ArrayList<>(order2.getOrderItems()));
    }

    @Test
    public void testForbidUpdate() throws Exception {
        thrown.expect(UnsupportedOperationException.class);
        repository.save(getCoffeeOrder1());
    }

    @Test
    public void testCreate() throws Exception {
        CoffeeOrder saved = repository.save(getNewCoffeeOrder());
        CoffeeOrder fromRepository = repository.get(saved.getId());
        assertOrdersEqual(saved, fromRepository);
    }

    @Test
    public void testCreateInvalid() throws Exception {
        thrown.expect(DataAccessException.class);
        CoffeeOrder created = getNewCoffeeOrder();
        created.setDeliveryAddress(null);
        repository.save(created);
    }

    @Test
    public void testDelete() throws Exception {
        repository.delete(getCoffeeOrder2().getId());
        assertEquals(Arrays.asList(getCoffeeOrder1(), getCoffeeOrder3()), repository.getAll());
    }

    @Test
    public void testDeleteAbsent() throws Exception {
        assertFalse(repository.delete(Integer.MAX_VALUE));
    }

    @Test
    public void testGet() throws Exception {
        CoffeeOrder order = getCoffeeOrder1();
        assertEquals(order, repository.get(order.getId()));
    }

    @Test
    public void testGetAbsent() throws Exception {
        assertNull(repository.get(Integer.MAX_VALUE));
    }

    @Test
    public void testGetAll() throws Exception {
        assertEquals(COFFEE_ORDERS, repository.getAll());
    }
}