package test.onlinecafe.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Configuration
@ComponentScan({"test.onlinecafe.service"})
@PropertySource("classpath:application.properties")
@Import({DiscountConfiguration.class,
        JdbcRepositoryConfiguration.class,
        JpaConfiguration.class,
        JpaRepositoryConfiguration.class})
public class AppConfiguration {

    @Autowired
    Environment environment;

    @Bean(name = "messageSource")
    public ResourceBundleMessageSource resourceBundleMessageSource(List<String> supportedLanguages) {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages/app");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setFallbackToSystemLocale(false);
        messageSource.setUseCodeAsDefaultMessage(true);
        Locale.setDefault(Locale.forLanguageTag(supportedLanguages.get(0)));
        return messageSource;
    }

    @Bean(name = "supportedLanguages")
    List<String> supportedLanguages() {
        List<String> supportedLanguages = new ArrayList<>();
        supportedLanguages.addAll(Arrays.asList(environment.getProperty("app.i18n.supported_languages").split(",")));
        return supportedLanguages;
    }
}
