package test.onlinecafe.repository.jpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import test.onlinecafe.repository.AbstractCoffeeOrderRepositoryTest;

import javax.validation.ConstraintViolationException;

import static org.junit.Assert.fail;

@ActiveProfiles("repo-jpa")
public class JpaCoffeeOrderRepositoryTest extends AbstractCoffeeOrderRepositoryTest {
    @Test
    @Override
    public void testCreateInvalid() throws Exception {
        thrown.expect(ConstraintViolationException.class);
        super.testCreateInvalid();
        fail();
    }
}
