package test.onlinecafe.repository.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import test.onlinecafe.repository.AbstractCoffeeOrderRepositoryTest;

import javax.validation.ConstraintViolationException;

@ActiveProfiles("repo-datajpa")
public class DataJpaCoffeeOrderRepositoryTest extends AbstractCoffeeOrderRepositoryTest {
    @Test
    @Override
    public void testCreateInvalid() throws Exception {
        thrown.expect(ConstraintViolationException.class);
        super.testCreateInvalid();
    }
}
