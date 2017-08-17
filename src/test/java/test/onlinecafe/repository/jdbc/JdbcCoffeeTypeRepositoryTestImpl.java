package test.onlinecafe.repository.jdbc;

import org.springframework.test.context.ActiveProfiles;
import test.onlinecafe.repository.AbstractCoffeeTypeRepositoryTest;
import test.onlinecafe.util.exception.DataAccessException;

@ActiveProfiles("repo-jdbc")
public class JdbcCoffeeTypeRepositoryTestImpl extends AbstractCoffeeTypeRepositoryTest {
    @Override
    public void testUpdateInvalid() throws Exception {
        thrown.expect(DataAccessException.class);
        super.testUpdateInvalid();
    }

    @Override
    public void testUpdateAbsent() throws Exception {
        thrown.expect(DataAccessException.class);
        super.testUpdateAbsent();
    }

    @Override
    public void testCreateInvalid() throws Exception {
        thrown.expect(DataAccessException.class);
        super.testCreateInvalid();
    }
}
