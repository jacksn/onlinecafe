package test.onlinecafe.repository.jdbc;

import org.springframework.test.context.ActiveProfiles;
import test.onlinecafe.repository.AbstractConfigurationRepositoryTest;
import test.onlinecafe.util.exception.DataAccessException;

import static org.junit.Assert.fail;

@ActiveProfiles("repo-jdbc")
public class JdbcConfigurationRepositoryTest extends AbstractConfigurationRepositoryTest {
    @Override
    public void testSaveInvalid() throws Exception {
        thrown.expect(DataAccessException.class);
        super.testSaveInvalid();
        fail();
    }
}
