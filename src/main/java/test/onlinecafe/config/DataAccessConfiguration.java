package test.onlinecafe.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({
        @PropertySource("classpath:db/db.properties"),
        @PropertySource(value = "classpath:db/${db_config_path}", ignoreResourceNotFound = true)})
public class DataAccessConfiguration {
}
