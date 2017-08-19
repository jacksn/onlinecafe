package test.onlinecafe.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import test.onlinecafe.util.discount.Discount;

@Configuration
public class DiscountConfiguration {
    @Value("${app.discount_class_qualifier}")
    private String discountClassQualifier;

    @Bean
    public Discount discount(ConfigurableApplicationContext context) {
        return context.getBean(discountClassQualifier, Discount.class);
    }
}
