package test.onlinecafe.repository;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import test.onlinecafe.model.BaseEntity;
import test.onlinecafe.model.CoffeeOrder;
import test.onlinecafe.util.DbUtil;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static test.onlinecafe.CoffeeOrderTestData.*;

public class JdbcCoffeeOrderRepositoryTest {
    private static CoffeeOrderRepository repository;
    private static Connection connection;

    @BeforeClass
    public static void init() {
        repository = new JdbcCoffeeOrderRepository();
        connection = DbUtil.getConnection();
        DbUtil.executeFile("sql/coffee.sql");
        DbUtil.executeFile("sql/testdata.sql");
    }

    @Test
    public void testUpdate() throws Exception {
        connection.setAutoCommit(false);
        int updatedId = COFFEE_ORDER1.getId();
        CoffeeOrder updated = COFFEE_ORDER1;
        updated.setName("New name");
        updated.setDeliveryAddress("New address");
        updated.setOrderDate(LocalDateTime.of(2017, 7, 4, 15,0));
        updated.setOrderItems(NEW_COFFEE_ORDER.getOrderItems());
        repository.save(updated);
        try {
            Assert.assertEquals(updated, repository.get(updatedId));
        } finally {
            connection.rollback();
        }
        connection.setAutoCommit(true);
    }

    @Test
    public void testUpdateInvalid() throws Exception {
        connection.setAutoCommit(false);
        int updatedId = COFFEE_ORDER1.getId();
        CoffeeOrder updated = repository.get(updatedId);
        updated.setDeliveryAddress(null);
        repository.save(updated);
        try {
            Assert.assertEquals(COFFEE_ORDER1, repository.get(updatedId));
        } finally {
            connection.rollback();
        }
        connection.setAutoCommit(true);
    }

    @Test
    public void testUpdateAbsent() throws Exception {
        connection.setAutoCommit(false);
        int updatedId = COFFEE_ORDER1.getId();
        CoffeeOrder updated = repository.get(updatedId);
        updated.setId(Integer.MAX_VALUE);
        try {
            Assert.assertEquals(null, repository.save(updated));
        } finally {
            connection.rollback();
        }
        connection.setAutoCommit(true);
    }

    @Test
    public void testCreate() throws Exception {
        connection.setAutoCommit(false);
        CoffeeOrder created = NEW_COFFEE_ORDER;
        created = repository.save(created);
        try {
            Assert.assertEquals(created, repository.get(created.getId()));
        } finally {
            connection.rollback();
        }
        connection.setAutoCommit(true);
    }

    @Test
    public void testCreateInvalid() throws Exception {
        connection.setAutoCommit(false);
        CoffeeOrder created = NEW_COFFEE_ORDER;
        created.setDeliveryAddress(null);
        try {
            Assert.assertEquals(null, repository.save(created));
        } finally {
            connection.rollback();
        }
        connection.setAutoCommit(true);
    }

    @Test
    public void testDelete() throws Exception {
        connection.setAutoCommit(false);
        repository.delete(COFFEE_ORDER2.getId());
        List<CoffeeOrder> orders = repository.getAll();
        orders.sort(Comparator.comparingInt(BaseEntity::getId));
        try {
            Assert.assertEquals(Arrays.asList(COFFEE_ORDER1, COFFEE_ORDER3), orders);
        } finally {
            connection.rollback();
        }
        connection.setAutoCommit(true);
    }

    @Test
    public void testDeleteAbsent() throws Exception {
        connection.setAutoCommit(false);
        repository.delete(Integer.MAX_VALUE);
        List<CoffeeOrder> orders = repository.getAll();
        try {
            Assert.assertEquals(COFFEE_ORDERS, orders);
        } finally {
            connection.rollback();
        }
        connection.setAutoCommit(true);
    }

    @Test
    public void testGet() throws Exception {
        CoffeeOrder coffeeType = repository.get(COFFEE_ORDER1.getId());
        Assert.assertEquals(COFFEE_ORDER1, coffeeType);
    }

    @Test
    public void testGetAbsent() throws Exception {
        CoffeeOrder coffeeOrder = repository.get(Integer.MAX_VALUE);
        Assert.assertEquals(null, coffeeOrder);
    }

    @Test
    public void testGetAll() throws Exception {
        List<CoffeeOrder> order = repository.getAll();
        Assert.assertEquals(COFFEE_ORDERS, order);
    }
}