package test.onlinecafe.repository;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import test.onlinecafe.model.BaseEntity;
import test.onlinecafe.model.CoffeeType;
import test.onlinecafe.util.exception.DataAccessException;

import java.util.Comparator;
import java.util.List;

import static test.onlinecafe.CoffeeTypeTestData.*;

public class JdbcCoffeeTypeRepositoryTest extends AbstractJdbcRepositoryTest {
    @Autowired
    private CoffeeTypeRepository repository;

    @Test
    public void testUpdate() throws Exception {
        int updatedId = COFFEE_TYPE1.getId();
        CoffeeType updated = repository.get(updatedId);
        updated.setTypeName("Updated name");
        updated.setPrice(updated.getPrice() + 1.0);
        updated.setDisabled(!updated.getDisabled());
        repository.save(updated);
    }

    @Test(expected = DataAccessException.class)
    public void testUpdateInvalid() throws Exception {
        int updatedId = COFFEE_TYPE1.getId();
        CoffeeType updated = repository.get(updatedId);
        updated.setTypeName(null);
        repository.save(updated);
    }

    @Test(expected = DataAccessException.class)
    public void testUpdateAbsent() throws Exception {
        int updatedId = COFFEE_TYPE1.getId();
        CoffeeType updated = repository.get(updatedId);
        updated.setId(Integer.MAX_VALUE);
        repository.save(updated);
    }

    @Test
    public void testCreate() throws Exception {
        CoffeeType created = new CoffeeType(null, "New type", 7.00, false);
        created = repository.save(created);
        Assert.assertEquals(created, repository.get(created.getId()));
    }

    @Test(expected = DataAccessException.class)
    public void testCreateInvalid() throws Exception {
        CoffeeType created = new CoffeeType(null, null, 0.00, false);
        repository.save(created);
    }

    @Test
    public void testDelete() throws Exception {
        repository.delete(5);
        List<CoffeeType> types = repository.getAll();
        types.sort(Comparator.comparingInt(BaseEntity::getId));
        Assert.assertEquals(COFFEE_TYPES_ENABLED, types);
    }

    @Test
    public void testDeleteAbsent() throws Exception {
        Assert.assertEquals(false, repository.delete(Integer.MAX_VALUE));
    }

    @Test
    public void testGet() throws Exception {
        CoffeeType type = repository.get(COFFEE_TYPE1.getId());
        Assert.assertEquals(COFFEE_TYPE1, type);
    }

    @Test
    public void testGetAbsent() throws Exception {
        CoffeeType type = repository.get(Integer.MAX_VALUE);
        Assert.assertEquals(null, type);
    }

    @Test
    public void testGetAll() throws Exception {
        List<CoffeeType> types = repository.getAll();
        types.sort(Comparator.comparingInt(BaseEntity::getId));
        Assert.assertEquals(COFFEE_TYPES_ALL, types);
    }
}
