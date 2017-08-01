package test.onlinecafe.repository;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import test.onlinecafe.model.CoffeeOrder;
import test.onlinecafe.util.exception.DataAccessException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static test.onlinecafe.CoffeeOrderTestData.*;

public class JdbcCoffeeOrderRepositoryTest extends AbstractJdbcRepositoryTest {
    private static CoffeeOrderRepository repository;

    @BeforeClass
    public static void init() {
        repository = new JdbcCoffeeOrderRepository(dataSource);
    }

    @Test
    public void testUpdate() throws Exception {
        int updatedId = COFFEE_ORDER1.getId();
        CoffeeOrder updated = COFFEE_ORDER1;
        updated.setName("New name");
        updated.setDeliveryAddress("New address");
        updated.setOrderDate(LocalDateTime.of(2017, 7, 4, 15, 0));
        updated.setOrderItems(NEW_COFFEE_ORDER.getOrderItems());
        repository.save(updated);
        Assert.assertEquals(updated, repository.get(updatedId));
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
        CoffeeOrder created = NEW_COFFEE_ORDER;
        created = repository.save(created);
        Assert.assertEquals(created, repository.get(NEW_COFFEE_ORDER.getId()));
    }

    @Test(expected = DataAccessException.class)
    public void testCreateInvalid() throws Exception {
        CoffeeOrder created = NEW_COFFEE_ORDER;
        created.setDeliveryAddress(null);
        repository.save(created);
    }

    @Test
    public void testDelete() throws Exception {
        repository.delete(COFFEE_ORDER2.getId());
        Assert.assertEquals(Arrays.asList(COFFEE_ORDER1, COFFEE_ORDER3), repository.getAll());
    }

    @Test
    public void testDeleteAbsent() throws Exception {
        Assert.assertEquals(false, repository.delete(Integer.MAX_VALUE));
    }

    @Test
    public void testGet() throws Exception {
        Assert.assertEquals(COFFEE_ORDER1, repository.get(COFFEE_ORDER1.getId()));
    }

    @Test
    public void testGetAbsent() throws Exception {
        Assert.assertEquals(null, repository.get(Integer.MAX_VALUE));
    }

    @Test
    public void testGetAll() throws Exception {
        Assert.assertEquals(COFFEE_ORDERS, repository.getAll());
    }
}