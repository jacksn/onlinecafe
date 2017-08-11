package test.onlinecafe.util;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    public static void setDataSource(DataSource dataSource) {
        DbUtil.dataSource = dataSource;
    }

    private static String schemaFile = "db/coffee.sql";

    private static DataSource dataSource;

    private DbUtil() {
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
