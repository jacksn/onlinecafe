package test.onlinecafe.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@Profile("repo-jpa")
@EnableTransactionManagement
public class JpaConfiguration {
}
