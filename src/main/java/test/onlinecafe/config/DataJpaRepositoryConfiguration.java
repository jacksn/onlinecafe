package test.onlinecafe.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@Profile("repo-datajpa")
@ComponentScan("test.onlinecafe.repository.datajpa")
@EnableJpaRepositories(basePackages = "test.onlinecafe.repository.datajpa")
public class DataJpaRepositoryConfiguration {
}
