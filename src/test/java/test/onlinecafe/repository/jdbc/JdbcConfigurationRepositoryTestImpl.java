package test.onlinecafe.repository.jdbc;

import org.springframework.test.context.ActiveProfiles;
import test.onlinecafe.repository.AbstractConfigurationRepositoryTest;
import test.onlinecafe.util.exception.DataAccessException;

@ActiveProfiles("repo-jdbc")
public class JdbcConfigurationRepositoryTestImpl extends AbstractConfigurationRepositoryTest {
    @Override
    public void testSaveInvalid() throws Exception {
        thrown.expect(DataAccessException.class);
        super.testSaveInvalid();
    }
}
