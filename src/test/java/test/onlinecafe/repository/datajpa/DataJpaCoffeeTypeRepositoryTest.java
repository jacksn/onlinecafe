package test.onlinecafe.repository.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import test.onlinecafe.model.CoffeeType;
import test.onlinecafe.repository.AbstractCoffeeTypeRepositoryTest;

import javax.validation.ConstraintViolationException;

@ActiveProfiles("repo-datajpa")
public class DataJpaCoffeeTypeRepositoryTest extends AbstractCoffeeTypeRepositoryTest {

    @Test
    public void testValidation() throws Exception {
        validateRootCause(() -> repository.save(new CoffeeType(null, 1.0, false)), ConstraintViolationException.class);
        validateRootCause(() -> repository.save(new CoffeeType("New type", null, false)), ConstraintViolationException.class);
        validateRootCause(() -> repository.save(new CoffeeType("New type", 1.0, null)), ConstraintViolationException.class);
    }

    @Override
    public void testCreateInvalid() throws Exception {
        thrown.expect(ConstraintViolationException.class);
        super.testCreateInvalid();
    }
}
