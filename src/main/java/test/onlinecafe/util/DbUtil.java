package test.onlinecafe.util;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public final class DbUtil {
    private static final String DEFAULT_DB_CONFIG_LOCATION = "sql/db.properties";
    private static Connection connection;
    public static String schemaFile = "sql/coffee.sql";

    private DbUtil() {
    }

    public static Connection getConnection() {
        if (connection != null)
            return connection;
        String dbConfigLocation = System.getProperty("dbconfig.path");
        if (dbConfigLocation == null) {
            dbConfigLocation = DEFAULT_DB_CONFIG_LOCATION;
        }
        try (InputStream inputStream = DbUtil.class.getClassLoader().getResourceAsStream(dbConfigLocation)) {
            Properties properties = new Properties();
            properties.load(inputStream);
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
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }

    public static void closeConnection() {
        if (connection == null)
            return;
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void executeFile(String fileName) {
        StringBuilder sb = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName)
                    )
            );
            String s;
            while ((s = br.readLine()) != null) {
                sb.append(s);
            }

            String[] inst = sb.toString().split(";");

            getConnection();
            Statement sqlStatement = connection.createStatement();

            for (String statement : inst) {
                if (!statement.trim().equals("")) {
                    sqlStatement.executeUpdate(statement);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
