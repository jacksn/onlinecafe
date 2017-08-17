package test.onlinecafe.repository.jpa;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import test.onlinecafe.repository.AbstractCoffeeTypeRepositoryTest;

import javax.persistence.PersistenceException;

@ActiveProfiles("repo-jpa")
public class JpaCoffeeTypeRepositoryTest extends AbstractCoffeeTypeRepositoryTest {
    @Override
    public void testUpdateInvalid() throws Exception {
        thrown.expect(DataIntegrityViolationException.class);
        super.testUpdateInvalid();
    }

    @Override
    public void testCreateInvalid() throws Exception {
        thrown.expect(PersistenceException.class);
        super.testCreateInvalid();
    }
}
