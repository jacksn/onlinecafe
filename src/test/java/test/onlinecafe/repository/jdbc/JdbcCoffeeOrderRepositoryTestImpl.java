package test.onlinecafe.repository.jdbc;

import org.springframework.test.context.ActiveProfiles;
import test.onlinecafe.repository.AbstractCoffeeOrderRepositoryTest;
import test.onlinecafe.util.exception.DataAccessException;

@ActiveProfiles("repo-jdbc")
public class JdbcCoffeeOrderRepositoryTestImpl extends AbstractCoffeeOrderRepositoryTest {
    @Override
    public void testCreateInvalid() throws Exception {
        thrown.expect(DataAccessException.class);
        super.testCreateInvalid();
    }
}
