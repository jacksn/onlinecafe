package test.onlinecafe.repository;

import test.onlinecafe.util.DbUtil;

import java.sql.Connection;

public class AbstractJdbcRepositoryTest {
    static Connection connection;

    public static void initDatabase() {
        connection = DbUtil.getConnection();
        DbUtil.initDatabase("db/testdata.sql");
    }

}
