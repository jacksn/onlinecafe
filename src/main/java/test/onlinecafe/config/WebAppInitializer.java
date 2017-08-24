package test.onlinecafe.config;

import org.slf4j.Logger;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class WebAppInitializer implements WebApplicationInitializer {
    private static final Logger log = getLogger(WebAppInitializer.class);

    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        try {
            PropertySource ps = new ResourcePropertySource(new ClassPathResource("application.properties")); // handle exception
            ctx.getEnvironment().getPropertySources().addFirst(ps);
        } catch (IOException e) {
            log.error("Unable to load application.properties", e);
        }
        ctx.register(AppConfiguration.class,
                DiscountConfiguration.class,
                DataAccessConfiguration.class,
                JdbcRepositoryConfiguration.class,
                JpaConfiguration.class,
                JpaRepositoryConfiguration.class,
                WebMvcConfiguration.class);
        ctx.setServletContext(servletContext);
        servletContext.addListener(new ContextLoaderListener(ctx));
        DispatcherServlet servlet = new DispatcherServlet(ctx);
        servlet.setThrowExceptionIfNoHandlerFound(true);
        ServletRegistration.Dynamic registration = servletContext.addServlet("MVC dispatcher", servlet);
        registration.setLoadOnStartup(1);
        registration.addMapping("/");
    }
}
