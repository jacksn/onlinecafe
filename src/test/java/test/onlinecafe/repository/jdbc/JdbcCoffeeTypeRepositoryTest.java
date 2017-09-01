package test.onlinecafe.repository.jdbc;

import org.springframework.test.context.ActiveProfiles;
import test.onlinecafe.repository.AbstractCoffeeTypeRepositoryTest;
import test.onlinecafe.util.exception.DataAccessException;

import static org.junit.Assert.fail;

@ActiveProfiles("repo-jdbc")
public class JdbcCoffeeTypeRepositoryTest extends AbstractCoffeeTypeRepositoryTest {
    @Override
    public void testUpdateInvalid() throws Exception {
        thrown.expect(DataAccessException.class);
        super.testUpdateInvalid();
        fail();
    }

    @Override
    public void testUpdateAbsent() throws Exception {
        thrown.expect(DataAccessException.class);
        super.testUpdateAbsent();
        fail();
    }

    @Override
    public void testCreateInvalid() throws Exception {
        thrown.expect(DataAccessException.class);
        super.testCreateInvalid();
        fail();
    }
}
