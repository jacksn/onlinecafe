package test.onlinecafe.config;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import test.onlinecafe.util.DbUtil;

@Configuration
@PropertySources({
        @PropertySource("classpath:db/db.properties"),
        @PropertySource(value = "classpath:db/${db_config_path}", ignoreResourceNotFound = true)})
public class DataAccessConfiguration {

    @Value("${db.driver}")
    private String databaseDriverClassName;

    @Value("${db.url}")
    private String databaseUrl;

    @Value("${db.user}")
    private String databaseUsername;

    @Value("${db.password}")
    private String databasePassword;

    @Value("${db.schema_file}")
    private String databaseSchemaFile;

    @Value("${db.data_file}")
    private String databaseDataFile;

    @Bean(name = "dataSource")
    public DataSource dataSource() throws ClassNotFoundException {

        DbUtil.setSchemaFile(databaseSchemaFile);
        DbUtil.setDataFile(databaseDataFile);

        PoolProperties poolProperties = new PoolProperties();
        poolProperties.setDriverClassName(databaseDriverClassName);
        poolProperties.setUrl(databaseUrl);
        poolProperties.setUsername(databaseUsername);
        poolProperties.setPassword(databasePassword);
        poolProperties.setJmxEnabled(true);
        poolProperties.setTestWhileIdle(false);
        poolProperties.setTestOnBorrow(true);
        poolProperties.setValidationQuery("SELECT 1");
        poolProperties.setTestOnReturn(false);
        poolProperties.setRemoveAbandonedTimeout(60);
        poolProperties.setMinIdle(10);
        poolProperties.setRemoveAbandoned(true);
        poolProperties.setRollbackOnReturn(true);
        poolProperties.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;" +
                "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
        DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
        dataSource.setPoolProperties(poolProperties);
        return dataSource;
    }
}
