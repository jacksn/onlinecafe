package test.onlinecafe.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;

@Configuration
@ComponentScan({"test.onlinecafe.repository", "test.onlinecafe.service", "test.onlinecafe.util.discount"})
@PropertySource("classpath:application.properties")
@Import(DataAccessConfiguration.class)
public class AppConfiguration {

    @Value("${app.i18n.supported_languages}")
    private String supportedLanguages;

    @Bean(name = "messageSource")
    public ResourceBundleMessageSource resourceBundleMessageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages/app");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setFallbackToSystemLocale(false);
        messageSource.setUseCodeAsDefaultMessage(true);
        Locale.setDefault(Locale.forLanguageTag(supportedLanguages.split(",")[0]));
        return messageSource;
    }
}
