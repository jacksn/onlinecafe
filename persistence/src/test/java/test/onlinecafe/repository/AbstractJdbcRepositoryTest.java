package test.onlinecafe.repository;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;
import test.onlinecafe.util.DbUtil;

import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

public abstract class AbstractJdbcRepositoryTest {
    private static final String TEST_DB_CONFIG = "db/db_hsqldb.properties";

    private static final Logger log = getLogger(AbstractJdbcRepositoryTest.class);
    private static StringBuilder results = new StringBuilder();

    static DataSource dataSource;

    @Before
    public void initDatabase() {
        DbUtil.initDatabase("db/testdata.sql");
    }

    @BeforeClass
    public static void initDataSource() {
        System.setProperty(DbUtil.DB_CONFIG_SYSTEM_PROPERTY_NAME, TEST_DB_CONFIG);
        dataSource = DbUtil.getDataSource();
    }

    @Rule
    // http://stackoverflow.com/questions/14892125/what-is-the-best-practice-to-determine-the-execution-time-of-the-bussiness-relev
    public Stopwatch stopwatch = new Stopwatch() {
        @Override
        protected void finished(long nanos, Description description) {
            String result = String.format("%-85s %7d", description.getDisplayName(), TimeUnit.NANOSECONDS.toMillis(nanos));
            results.append(result).append('\n');
            log.info(result + " ms\n");
        }
    };

    @AfterClass
    public static void printResult() {
        log.info("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" +
                 "\nTest                                                                                 Duration, ms" +
                 "\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                 results +
                   "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        results.setLength(0);
    }

}
