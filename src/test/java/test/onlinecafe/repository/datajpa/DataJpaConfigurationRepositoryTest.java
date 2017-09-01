package test.onlinecafe.repository.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import test.onlinecafe.model.ConfigurationItem;
import test.onlinecafe.repository.AbstractConfigurationRepositoryTest;

import javax.validation.ConstraintViolationException;

@ActiveProfiles("repo-datajpa")
public class DataJpaConfigurationRepositoryTest extends AbstractConfigurationRepositoryTest {
    @Test
    public void testValidation() throws Exception {
        validateRootCause(() -> repository.save(new ConfigurationItem("", "Value")), ConstraintViolationException.class);
        validateRootCause(() -> repository.save(new ConfigurationItem("Id", null)), ConstraintViolationException.class);
    }
}
