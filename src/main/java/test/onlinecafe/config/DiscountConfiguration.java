package test.onlinecafe.config;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import test.onlinecafe.util.CoffeeOrderUtil;
import test.onlinecafe.util.discount.Discount;

@Configuration
public class DiscountConfiguration {
    @Bean
    public Discount discount(ConfigurableApplicationContext context, Environment env) {
        Discount discount = context.getBean(env.getProperty("app.discount_class_name"), Discount.class);
        CoffeeOrderUtil.setDiscount(discount);
        return discount;
    }
}
