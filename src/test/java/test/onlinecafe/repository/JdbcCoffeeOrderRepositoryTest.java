package test.onlinecafe.repository;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import test.onlinecafe.CoffeeTypeTestData;
import test.onlinecafe.model.CoffeeOrder;
import test.onlinecafe.model.CoffeeOrderItem;
import test.onlinecafe.util.exception.DataAccessException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static test.onlinecafe.CoffeeOrderTestData.*;

public class JdbcCoffeeOrderRepositoryTest extends AbstractJdbcRepositoryTest {
    @Autowired
    private CoffeeOrderRepository repository;

    @Test
    public void testUpdate() throws Exception {
        int updatedId = COFFEE_ORDER1.getId();
        CoffeeOrder updated = COFFEE_ORDER1;
        updated.setName("New name");
        updated.setDeliveryAddress("New address");
        updated.setOrderDate(LocalDateTime.of(2017, 7, 4, 15, 0));
        updated.setOrderItems(getNewCoffeeOrder().getOrderItems());
        CoffeeOrderItem item = updated.getOrderItems().get(0);
        item.setCoffeeType(CoffeeTypeTestData.COFFEE_TYPE5);
        item.setQuantity(50);
        repository.save(updated);
        assertEquals(updated, repository.get(updatedId));
    }

    @Test(expected = DataAccessException.class)
    public void testUpdateInvalid() throws Exception {
        int updatedId = COFFEE_ORDER1.getId();
        CoffeeOrder updated = repository.get(updatedId);
        updated.setDeliveryAddress(null);
        repository.save(updated);
    }

    @Test(expected = DataAccessException.class)
    public void testUpdateAbsent() throws Exception {
        CoffeeOrder updated = new CoffeeOrder(
                null,
                COFFEE_ORDER1.getName(),
                COFFEE_ORDER1.getDeliveryAddress(),
                new ArrayList<>(),
                0.0
        );
        updated.setId(Integer.MAX_VALUE);
        repository.save(updated);
    }

    @Test
    public void testCreate() throws Exception {
        CoffeeOrder created = getNewCoffeeOrder();
        CoffeeOrder saved = repository.save(created);
        created.setId(saved.getId());
        assertEquals(created, saved);
        assertEquals(created, repository.get(created.getId()));
    }

    @Test(expected = DataAccessException.class)
    public void testCreateInvalid() throws Exception {
        CoffeeOrder created = getNewCoffeeOrder();
        created.setDeliveryAddress(null);
        repository.save(created);
    }

    @Test
    public void testDelete() throws Exception {
        repository.delete(COFFEE_ORDER2.getId());
        assertEquals(Arrays.asList(COFFEE_ORDER1, COFFEE_ORDER3), repository.getAll());
    }

    @Test
    public void testDeleteAbsent() throws Exception {
        assertEquals(false, repository.delete(Integer.MAX_VALUE));
    }

    @Test
    public void testGet() throws Exception {
        assertEquals(COFFEE_ORDER1, repository.get(COFFEE_ORDER1.getId()));
    }

    @Test
    public void testGetAbsent() throws Exception {
        assertEquals(null, repository.get(Integer.MAX_VALUE));
    }

    @Test
    public void testGetAll() throws Exception {
        assertEquals(COFFEE_ORDERS, repository.getAll());
    }
}