package test.onlinecafe.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public final class DbUtil {
    private static final String DEFAULT_DB_CONFIG_LOCATION = "db/db.properties";
    private static String schemaFile = "db/coffee.sql";
    private static Connection connection;

    private DbUtil() {
    }

    public static Connection getConnection() {
        if (connection != null)
            return connection;
        String dbConfigLocation = System.getProperty("dbconfig.path");
        if (dbConfigLocation == null) {
            dbConfigLocation = DEFAULT_DB_CONFIG_LOCATION;
        }
        try {
            Properties properties = ConfigurationUtil.getPropertiesFromFile(dbConfigLocation);
            String driver = properties.getProperty("db.driver");
            String url = properties.getProperty("db.url");
            String user = properties.getProperty("db.user");
            String password = properties.getProperty("db.password");
            String schema = properties.getProperty("db.schema");
            if (schema != null) {
                DbUtil.schemaFile = schema;
            }
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void executeSQLScriptFile(String fileName) {
        if (connection == null) {
            getConnection();
        }
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
            e.printStackTrace();
        }
    }

    public static void initDatabase(String dataFile){
        initDatabase(dataFile, schemaFile);
    }

    public static void initDatabase(String dataFile, String schemaFile) {
        if (connection == null) {
            getConnection();
        }
        executeSQLScriptFile(schemaFile);
        executeSQLScriptFile(dataFile);
    }
}
