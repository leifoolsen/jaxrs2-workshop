package no.javabin.jaxrs.start.rest.api;

import no.javabin.jaxrs.start.embeddedjetty.JettyFactory;
import no.javabin.jaxrs.start.rest.application.ApplicationConfig;
import org.eclipse.jetty.server.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JaxbTest {

    private static Server server;
    private static WebTarget target;

    @BeforeClass
    public static void startServer() throws Exception {

        // Start the server
        server = new JettyFactory()
                .build();

        JettyFactory.start(server);

        // Create the client
        Client c = ClientBuilder.newClient();

        target = c.target(server.getURI()).path(ApplicationConfig.APPLICATION_PATH);
    }

    @AfterClass
    public static void stopServer() throws Exception {
        JettyFactory.stop(server);
    }

    /**
     * Verifies that the application.wadl is reachable.
     */
    @Test
    public void shouldGetApplicationWadl() throws Exception {
        final Response response = target
                .path("application.wadl")
                .request(MediaType.APPLICATION_XML)
                .get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        String wadl = response.readEntity(String.class);
        assertTrue(wadl.length() > 0);
    }

}
