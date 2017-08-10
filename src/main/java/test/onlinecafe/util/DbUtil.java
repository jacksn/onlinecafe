package test.onlinecafe.util;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import static org.slf4j.LoggerFactory.getLogger;

public final class DbUtil {
    private static final Logger log = getLogger(DbUtil.class);

    public static final String DB_CONFIG_SYSTEM_PROPERTY_NAME = "dbconfig.path";
    private static final String DB_DEFAULT_CONFIG_LOCATION = "db/db.properties";
    private static String schemaFile = "db/coffee.sql";
    private static Connection connection;
    private static DataSource dataSource;

    private DbUtil() {
    }

    public static DataSource getDataSource() {
        if (dataSource != null) {
            return dataSource;
        }
        log.debug("Data source initialization - start");
        String dbConfigLocation = System.getProperty(DB_CONFIG_SYSTEM_PROPERTY_NAME);
        if (dbConfigLocation == null) {
            log.debug("System property {} not found", DB_CONFIG_SYSTEM_PROPERTY_NAME);
            log.debug("Loading configuration from default path: {}", DB_DEFAULT_CONFIG_LOCATION);
            dbConfigLocation = DB_DEFAULT_CONFIG_LOCATION;
        }
        try (InputStream inputStream = DbUtil.class.getClassLoader().getResourceAsStream(dbConfigLocation)) {
            Properties properties = new Properties();
            log.debug("Loading DB connection properties");
            properties.load(inputStream);

            String driver = properties.getProperty("db.driver");
            log.debug("Loading DB driver class {}", driver);
            Class.forName(driver);

            String schema = properties.getProperty("db.schema");
            if (schema != null) {
                DbUtil.schemaFile = schema;
            }
            PoolProperties poolProperties = new PoolProperties();
            poolProperties.setUrl(properties.getProperty("db.url"));
            poolProperties.setDriverClassName(properties.getProperty("db.driver"));
            poolProperties.setUsername(properties.getProperty("db.user"));
            poolProperties.setPassword(properties.getProperty("db.password"));
            poolProperties.setJmxEnabled(true);
            poolProperties.setTestWhileIdle(false);
            poolProperties.setTestOnBorrow(true);
            poolProperties.setValidationQuery("SELECT 1");
            poolProperties.setTestOnReturn(false);
            poolProperties.setValidationInterval(30000);
            poolProperties.setTimeBetweenEvictionRunsMillis(30000);
            poolProperties.setMaxActive(100);
            poolProperties.setInitialSize(10);
            poolProperties.setMaxWait(10000);
            poolProperties.setRemoveAbandonedTimeout(60);
            poolProperties.setMinEvictableIdleTimeMillis(30000);
            poolProperties.setMinIdle(10);
            poolProperties.setLogAbandoned(true);
            poolProperties.setRemoveAbandoned(true);
            poolProperties.setRollbackOnReturn(true);
            poolProperties.setDefaultTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            poolProperties.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;" +
                    "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
            dataSource = new DataSource();
            dataSource.setPoolProperties(poolProperties);

            log.debug("Data source successfully created");
        } catch (IOException | ClassNotFoundException | RuntimeException e) {
            log.error("Error initializing data source");
            e.printStackTrace();
        }
        log.debug("Data source initialization - end");
        return dataSource;
    }


    public static void closeConnection() {
        if (connection != null) {
            try {
                log.debug("Closing DB connection");
                connection.close();
            } catch (SQLException e) {
                log.error("Error closing DB connection");
                e.printStackTrace();
            }
        }
    }

    public static void executeSQLScriptFile(String fileName) {
        log.debug("Executing SQL script file: {}", fileName);
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(DbUtil.class.getClassLoader().getResourceAsStream(fileName), Charset.forName("UTF-8")));
             Statement sqlStatement = dataSource.getConnection().createStatement()) {
            String s;
            while ((s = br.readLine()) != null) {
                sb.append(s);
            }
            String[] statements = sb.toString().split(";");
            for (String statement : statements) {
                if (!statement.trim().equals("")) {
                    sqlStatement.executeUpdate(statement);
                }
            }
        } catch (IOException | SQLException e) {
            log.error("Error executing SQL script file: {}", fileName);
            e.printStackTrace();
        }
    }

    public static void initDatabase(String dataFile) {
        initDatabase(dataFile, schemaFile);
    }

    public static void initDatabase(String dataFile, String schemaFile) {
        log.debug("Initializing database with schema {} and data {}", schemaFile, dataFile);
        executeSQLScriptFile(schemaFile);
        executeSQLScriptFile(dataFile);
    }
}
