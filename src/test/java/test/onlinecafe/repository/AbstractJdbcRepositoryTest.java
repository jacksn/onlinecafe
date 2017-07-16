package test.onlinecafe.repository;

import org.junit.BeforeClass;
import test.onlinecafe.util.DbUtil;

import java.sql.Connection;

public class AbstractJdbcRepositoryTest {
    static CoffeeOrderRepository repository;
    static Connection connection;

    @BeforeClass
    public static void init() {
        repository = new JdbcCoffeeOrderRepository();
        connection = DbUtil.getConnection();
        DbUtil.executeFile("sql/coffee.sql");
        DbUtil.executeFile("sql/testdata.sql");
    }

}
