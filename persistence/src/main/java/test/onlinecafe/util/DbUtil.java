package test.onlinecafe.util;

import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import static org.slf4j.LoggerFactory.getLogger;

public final class DbUtil {
    private static final Logger log = getLogger(DbUtil.class);

    private static final String DB_CONFIG_SYSTEM_PROPERTY_NAME = "dbconfig.path";
    private static final String DB_DEFAULT_CONFIG_LOCATION = "db/db.properties";
    private static String schemaFile = "db/coffee.sql";
    private static Connection connection;

    private DbUtil() {
    }

    public static Connection getConnection() {
        if (connection != null)
            return connection;
        log.debug("DB connection initialization - start");
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

            String url = properties.getProperty("db.url");
            String user = properties.getProperty("db.user");
            String password = properties.getProperty("db.password");
            connection = DriverManager.getConnection(url, user, password);
            log.debug("DB connection successfully created");
        } catch (IOException | ClassNotFoundException | SQLException e) {
            log.debug("Error creating connection");
            e.printStackTrace();
        }
        log.debug("DB connection initialization - end");
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                log.debug("Closing DB connection");
                connection.close();
            } catch (SQLException e) {
                log.debug("Error closing DB connection");
                e.printStackTrace();
            }
        }
    }

    public static void executeSQLScriptFile(String fileName) {
        if (connection == null) {
            getConnection();
        }
        log.debug("Executing SQL script file: {}", fileName);
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName), Charset.forName("UTF-8")));
             Statement sqlStatement = connection.createStatement()) {
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
            log.debug("Error executing SQL script file: {}", fileName);
            e.printStackTrace();
        }
    }

    public static void initDatabase(String dataFile) {
        initDatabase(dataFile, schemaFile);
    }

    public static void initDatabase(String dataFile, String schemaFile) {
        log.debug("Initializing database with schema {} and data {}", schemaFile, dataFile);
        if (connection == null) {
            getConnection();
        }
        executeSQLScriptFile(schemaFile);
        executeSQLScriptFile(dataFile);
    }
}
