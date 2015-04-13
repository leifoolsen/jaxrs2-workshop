package no.javabin.jaxrs.hateoas.rest.application;

import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.ws.rs.ApplicationPath;

// The REST-application
@WebServlet(loadOnStartup = 1)
@ApplicationPath("/api/*")
public class HateoasApp extends ResourceConfig {
    public static final String APPLICATION_PATH = "api";

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    public HateoasApp() {
        packages("no.javabin.jaxrs.hateoas.rest");

        // Enable LoggingFilter & output entity.
        //registerInstances(new LoggingFilter(java.util.logging.Logger.getLogger(JerseyJpaApp.class.getName()), true));

        // Enable Tracing support.
        //property(ServerProperties.TRACING, "ALL");

        logger.debug("Application '{}' initialized", getClass().getSimpleName());
    }
}
