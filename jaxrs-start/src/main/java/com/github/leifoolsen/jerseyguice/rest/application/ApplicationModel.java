package com.github.leifoolsen.jerseyguice.rest.application;

import com.github.leifoolsen.jerseyguice.service.HelloGuiceModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ResourceConfig;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import javax.inject.Inject;
import javax.ws.rs.ApplicationPath;

@ApplicationPath("/api/*")
public class ApplicationModel extends ResourceConfig {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final String APPLICATION_PATH = "/api";

    @Inject
    public ApplicationModel(ServiceLocator serviceLocator) {

        // Jersey uses java.util.logging. Bridge to slf4j
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        // Scans during deployment for JAX-RS components in packages
        packages("com.github.leifoolsen.jerseyguice.rest");


        // Enable LoggingFilter & output entity.
        //registerInstances(new LoggingFilter(java.util.logging.Logger.getLogger(this.getClass().getName()), true));

        // Guice
        Injector injector = Guice.createInjector(new HelloGuiceModule());

        // Guice HK2 bridge
        // See e.g. https://github.com/t-tang/jetty-jersey-HK2-Guice-boilerplate
        GuiceBridge.getGuiceBridge().initializeGuiceBridge(serviceLocator);
        GuiceIntoHK2Bridge bridge = serviceLocator.getService(GuiceIntoHK2Bridge.class);
        bridge.bridgeGuiceInjector(injector);

        logger.debug("'{}' initialized", getClass().getName());
    }
}
