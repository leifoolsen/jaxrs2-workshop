package no.javabin.jaxrs.hateoas.rest.filter;

import no.javabin.jaxrs.hateoas.util.DatabaseConnection;
import no.javabin.jaxrs.hateoas.util.DatabasePopulator;
import no.javabin.jaxrs.hateoas.util.JpaDatabaseConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter(
        urlPatterns = "/api/*",
        filterName = "JPA-Filter",
        description = "JPA Session in View Filter"
)
public class JpaFilter implements Filter {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private JpaDatabaseConnectionManager.JpaDatabaseConnection connection;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        connection = DatabaseConnection.createConnection();
        connection.start();

        // Prepopulate db - for demonstration only. TODO: Use e.g. staging
        DatabasePopulator.pupulateDb(connection);

        logger.debug("JPAFilter initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        logger.debug("JpaFilter, doFilter");
        connection.unitOfWork().begin();
        try {
            chain.doFilter(request, response);
        }
        finally {
            connection.unitOfWork().end();
        }
    }

    @Override
    public void destroy() {
        connection.stop();
        logger.debug("JPAFilter destroyed");
    }
}
