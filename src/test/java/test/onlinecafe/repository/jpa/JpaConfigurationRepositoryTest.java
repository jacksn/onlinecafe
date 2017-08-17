package test.onlinecafe.repository.jpa;

import org.springframework.test.context.ActiveProfiles;
import test.onlinecafe.repository.AbstractConfigurationRepositoryTest;

import javax.persistence.PersistenceException;

@ActiveProfiles("repo-jpa")
public class JpaConfigurationRepositoryTest extends AbstractConfigurationRepositoryTest {
    @Override
    public void testSaveInvalid() throws Exception {
        thrown.expect(PersistenceException.class);
        super.testSaveInvalid();
    }
}
