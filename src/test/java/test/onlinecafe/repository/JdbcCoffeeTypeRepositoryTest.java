package test.onlinecafe.repository;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import test.onlinecafe.model.BaseEntity;
import test.onlinecafe.model.CoffeeType;
import test.onlinecafe.util.DbUtil;

import java.sql.Connection;
import java.util.Comparator;
import java.util.List;

import static test.onlinecafe.CoffeeTypeTestData.*;

public class JdbcCoffeeTypeRepositoryTest extends AbstractJdbcRepositoryTest{
    private static CoffeeTypeRepository repository;

    @BeforeClass
    public static void init(){
        initDatabase();
        repository = new JdbcCoffeeTypeRepository(connection);
    }

    @Test
    public void testUpdate() throws Exception {
        Connection connection = DbUtil.getConnection();
        connection.setAutoCommit(false);
        int updatedId = COFFEE_TYPE1.getId();
        CoffeeType updated = repository.get(updatedId);
        updated.setTypeName("Updated name");
        updated.setPrice(updated.getPrice() + 1.0);
        updated.setDisabled(!updated.getDisabled());
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
        Connection connection = DbUtil.getConnection();
        connection.setAutoCommit(false);
        int updatedId = COFFEE_TYPE1.getId();
        CoffeeType updated = repository.get(updatedId);
        updated.setTypeName(null);
        try {
            Assert.assertEquals(null, repository.save(updated));
        } finally {
            connection.rollback();
        }
        connection.setAutoCommit(true);
    }

    @Test
    public void testUpdateAbsent() throws Exception {
        Connection connection = DbUtil.getConnection();
        connection.setAutoCommit(false);
        int updatedId = COFFEE_TYPE1.getId();
        CoffeeType updated = repository.get(updatedId);
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
        Connection connection = DbUtil.getConnection();
        connection.setAutoCommit(false);
        CoffeeType created = new CoffeeType(null, "New type", 7.00, false);
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
        Connection connection = DbUtil.getConnection();
        connection.setAutoCommit(false);
        CoffeeType created = new CoffeeType(null, null, 0.00, false);
        try {
            Assert.assertEquals(null, repository.save(created));
        } finally {
            connection.rollback();
        }
        connection.setAutoCommit(true);
    }

    @Test
    public void testDelete() throws Exception {
        Connection connection = DbUtil.getConnection();
        connection.setAutoCommit(false);
        repository.delete(5);
        List<CoffeeType> types = repository.getAll();
        types.sort(Comparator.comparingInt(BaseEntity::getId));
        try {
            Assert.assertEquals(COFFEE_TYPES_ENABLED, types);
        } finally {
            connection.rollback();
        }
        connection.setAutoCommit(true);
    }

    @Test
    public void testDeleteAbsent() throws Exception {
        Connection connection = DbUtil.getConnection();
        connection.setAutoCommit(false);
        repository.delete(Integer.MAX_VALUE);
        List<CoffeeType> types = repository.getAll();
        try {
            Assert.assertEquals(COFFEE_TYPES_ALL, types);
        } finally {
            connection.rollback();
        }
        connection.setAutoCommit(true);
    }

    @Test
    public void testGet() throws Exception {
        CoffeeType coffeeType = repository.get(COFFEE_TYPE1.getId());
        Assert.assertEquals(COFFEE_TYPE1, coffeeType);
    }

    @Test
    public void testGetAbsent() throws Exception {
        CoffeeType coffeeType = repository.get(Integer.MAX_VALUE);
        Assert.assertEquals(null, coffeeType);
    }

    @Test
    public void testGetAll() throws Exception {
        List<CoffeeType> types = repository.getAll();
        types.sort(Comparator.comparingInt(BaseEntity::getId));
        Assert.assertEquals(COFFEE_TYPES_ALL, types);
    }
}
