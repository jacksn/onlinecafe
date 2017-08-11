package test.onlinecafe.config;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({
        @PropertySource("classpath:db/db.properties"),
        @PropertySource(value = "classpath:db/#{systemProperty['dbconfig.path']}", ignoreResourceNotFound = true)})
public class DataAccessConfiguration {

    @Value("${db.driver}")
    private String databaseDriverClassName;

    @Value("${db.url}")
    private String databaseUrl;

    @Value("${db.user}")
    private String databaseUsername;

    @Value("${db.password}")
    private String databasePassword;

    @Bean
    public DataSource dataSource() throws ClassNotFoundException {

//        String schema = properties.getProperty("db.schema");
//        if (schema != null) {
//            DbUtil.schemaFile = schema;
//        }

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
//        poolProperties.setLogAbandoned(true);
        poolProperties.setRemoveAbandoned(true);
        poolProperties.setRollbackOnReturn(true);
//        poolProperties.setDefaultTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
        poolProperties.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;" +
                "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
        DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
        dataSource.setPoolProperties(poolProperties);
        return dataSource;
    }
}
