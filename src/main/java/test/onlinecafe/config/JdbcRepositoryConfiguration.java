package test.onlinecafe.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("repo-jdbc")
@ComponentScan("test.onlinecafe.repository.jdbc")
@Import(DataAccessConfiguration.class)
public class JdbcRepositoryConfiguration {
}
