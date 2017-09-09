package test.onlinecafe.repository.jpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import test.onlinecafe.model.CoffeeType;
import test.onlinecafe.repository.AbstractCoffeeTypeRepositoryTest;

import javax.validation.ConstraintViolationException;

import java.math.BigDecimal;

import static org.junit.Assert.fail;

@ActiveProfiles("repo-jpa")
public class JpaCoffeeTypeRepositoryTest extends AbstractCoffeeTypeRepositoryTest {

    @Test
    public void testValidation() throws Exception {
        validateRootCause(() -> repository.save(new CoffeeType(null, new BigDecimal("1.0"), false)), ConstraintViolationException.class);
        validateRootCause(() -> repository.save(new CoffeeType("New type", null, false)), ConstraintViolationException.class);
        validateRootCause(() -> repository.save(new CoffeeType("New type", new BigDecimal("1.0"), null)), ConstraintViolationException.class);
    }

    @Override
    public void testCreateInvalid() throws Exception {
        thrown.expect(ConstraintViolationException.class);
        super.testCreateInvalid();
        fail();
    }
}
