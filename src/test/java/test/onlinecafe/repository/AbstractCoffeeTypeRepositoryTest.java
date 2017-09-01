package test.onlinecafe.repository;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import test.onlinecafe.model.BaseEntity;
import test.onlinecafe.model.CoffeeType;

import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeFalse;
import static test.onlinecafe.CoffeeTypeTestData.*;

public abstract class AbstractCoffeeTypeRepositoryTest extends AbstractRepositoryTest {
    @Autowired
    protected CoffeeTypeRepository repository;

    @Test
    public void testUpdate() throws Exception {
        int updatedId = COFFEE_TYPE1.getId();
        CoffeeType updated = repository.get(updatedId);
        updated.setTypeName("Updated name");
        updated.setPrice(updated.getPrice() + 1.0);
        updated.setDisabled(!updated.getDisabled());
        repository.save(updated);
        assertEquals(updated, repository.get(updatedId));
    }

    @Test
    public void testUpdateInvalid() throws Exception {
        assumeFalse(isJpaBased());
        int updatedId = COFFEE_TYPE1.getId();
        CoffeeType updated = repository.get(updatedId);
        updated.setTypeName(null);
        repository.save(updated);
        fail();
    }

    @Test
    public void testUpdateAbsent() throws Exception {
        int updatedId = COFFEE_TYPE1.getId();
        CoffeeType updated = repository.get(updatedId);
        updated.setId(Integer.MAX_VALUE);
        assertNull(repository.save(updated));
    }

    @Test
    public void testCreate() throws Exception {
        CoffeeType created = new CoffeeType(null, "New type", 7.00, false);
        created = repository.save(created);
        assertEquals(created, repository.get(created.getId()));
    }

    @Test
    public void testCreateInvalid() throws Exception {
        CoffeeType created = new CoffeeType(null, null, 0.00, false);
        repository.save(created);
        fail();
    }

    @Test
    public void testDelete() throws Exception {
        repository.delete(5);
        List<CoffeeType> types = repository.getAll();
        types.sort(Comparator.comparingInt(BaseEntity::getId));
        assertEquals(COFFEE_TYPES_ENABLED, types);
    }

    @Test
    public void testDeleteAbsent() throws Exception {
        assertFalse(repository.delete(Integer.MAX_VALUE));
    }

    @Test
    public void testGet() throws Exception {
        CoffeeType type = repository.get(COFFEE_TYPE1.getId());
        assertEquals(COFFEE_TYPE1, type);
    }

    @Test
    public void testGetAbsent() throws Exception {
        CoffeeType type = repository.get(Integer.MAX_VALUE);
        assertNull(type);
    }

    @Test
    public void testGetAll() throws Exception {
        List<CoffeeType> types = repository.getAll();
        types.sort(Comparator.comparingInt(BaseEntity::getId));
        assertEquals(COFFEE_TYPES_ALL, types);
    }

    @Test
    public void testGetEnabled() throws Exception {
        List<CoffeeType> types = repository.getEnabled();
        types.sort(Comparator.comparingInt(BaseEntity::getId));
        assertEquals(COFFEE_TYPES_ENABLED, types);
    }
}
