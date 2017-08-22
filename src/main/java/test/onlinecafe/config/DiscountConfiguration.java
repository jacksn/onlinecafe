package test.onlinecafe.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"discount-none","discount-simple"})
@ComponentScan("test.onlinecafe.util")
public class DiscountConfiguration {
}
