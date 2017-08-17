package test.onlinecafe.repository.jpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import test.onlinecafe.repository.AbstractCoffeeOrderRepositoryTest;

import javax.persistence.PersistenceException;

@ActiveProfiles("repo-jpa")
public class JpaCoffeeOrderRepositoryTestImpl extends AbstractCoffeeOrderRepositoryTest {
    @Test
    @Override
    public void testCreateInvalid() throws Exception {
        thrown.expect(PersistenceException.class);
        super.testCreateInvalid();
    }
}
