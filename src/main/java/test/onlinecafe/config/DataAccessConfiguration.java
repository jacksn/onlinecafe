package test.onlinecafe.config;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

@Configuration
@PropertySources({
        @PropertySource("classpath:db/db.properties"),
        @PropertySource(value = "classpath:db/${db_config_path}", ignoreResourceNotFound = true)})
@Import({JdbcRepositoryConfiguration.class,
        JpaConfiguration.class,
        JpaRepositoryConfiguration.class})
public class DataAccessConfiguration {

    @Value("${db.driver}")
    private String databaseDriverClassName;

    @Value("${db.url}")
    private String databaseUrl;

    @Value("${db.user}")
    private String databaseUsername;

    @Value("${db.password}")
    private String databasePassword;

    @Value("${app.initialize_database}")
    private boolean initializeDatabase;

    @Value("classpath:${db.schema_script}")
    private Resource schemaScript;

    @Value("classpath:${db.data_script}")
    private Resource dataScript;

    @Bean(name = "dataSource")
    public DataSource dataSource() throws ClassNotFoundException {
        PoolProperties poolProperties = new PoolProperties();
        poolProperties.setDriverClassName(databaseDriverClassName);
        poolProperties.setUrl(databaseUrl);
        poolProperties.setUsername(databaseUsername);
        poolProperties.setPassword(databasePassword);
        poolProperties.setJmxEnabled(true);
        poolProperties.setTestWhileIdle(true);
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

    @Bean
    public DataSourceInitializer dataSourceInitializer(DataSource dataSource, DatabasePopulator populator) {
        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setEnabled(initializeDatabase);
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(populator);
        return initializer;
    }

    @Bean
    public ResourceDatabasePopulator databasePopulator() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.setSqlScriptEncoding("UTF-8");
        populator.addScript(schemaScript);
        populator.addScript(dataScript);
        return populator;
    }
}
