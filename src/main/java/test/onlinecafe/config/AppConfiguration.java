package test.onlinecafe.config;

import org.springframework.context.annotation.*;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import java.util.Locale;

@Configuration
@ComponentScan({"test.onlinecafe.service"})
@PropertySource("classpath:application.properties")
@Import({JdbcRepositoryConfiguration.class,
        JpaConfiguration.class,
        JpaRepositoryConfiguration.class})
public class AppConfiguration {

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages/app");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setFallbackToSystemLocale(false);
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }

    @Bean
    LocaleResolver localeResolver() {
        CookieLocaleResolver resolver = new CookieLocaleResolver();
        resolver.setCookieName("locale");
        resolver.setDefaultLocale(Locale.forLanguageTag("en"));
        return resolver;
    }
}
