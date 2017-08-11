package test.onlinecafe.repository;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import test.onlinecafe.config.AppConfiguration;
import test.onlinecafe.util.DbUtil;

import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfiguration.class, loader = AnnotationConfigContextLoader.class)
@TestPropertySource("classpath:db/db_hsqldb.properties")
public abstract class AbstractJdbcRepositoryTest {
    private static final Logger log = getLogger(AbstractJdbcRepositoryTest.class);
    private static StringBuilder results = new StringBuilder();

    @Autowired
    DataSource dataSource;

    @Before
    public void initDatabase() {
        DbUtil.setDataSource(dataSource);
        DbUtil.initDatabase("db/testdata.sql", "db/coffee_hsqldb.sql");
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
