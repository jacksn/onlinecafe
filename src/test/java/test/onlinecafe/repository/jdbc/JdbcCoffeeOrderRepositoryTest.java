package test.onlinecafe.repository.jdbc;

import org.springframework.test.context.ActiveProfiles;
import test.onlinecafe.repository.AbstractCoffeeOrderRepositoryTest;
import test.onlinecafe.util.exception.DataAccessException;

import static org.junit.Assert.fail;

@ActiveProfiles("repo-jdbc")
public class JdbcCoffeeOrderRepositoryTest extends AbstractCoffeeOrderRepositoryTest {
    @Override
    public void testCreateInvalid() throws Exception {
        thrown.expect(DataAccessException.class);
        super.testCreateInvalid();
        fail();
    }
}
