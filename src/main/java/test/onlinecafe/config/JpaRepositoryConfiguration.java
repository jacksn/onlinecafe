package test.onlinecafe.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("repo-jpa")
@ComponentScan("test.onlinecafe.repository.jpa")
public class JpaRepositoryConfiguration {
}
