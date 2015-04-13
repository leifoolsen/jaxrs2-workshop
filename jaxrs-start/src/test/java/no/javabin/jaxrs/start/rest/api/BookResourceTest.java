package no.javabin.jaxrs.start.rest.api;

import no.javabin.jaxrs.start.domain.Book;
import no.javabin.jaxrs.start.embeddedjetty.JettyFactory;
import no.javabin.jaxrs.start.repository.BookRepository;
import org.eclipse.jetty.server.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.GregorianCalendar;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class BookResourceTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String BOOK_RESOURCE_PATH = "books";
    private static final String TRAVELLING_TO_INFINITY_ISBN = "9781846883668";
    private static final String FISKEN_ISBN = "9788202148683";
    private static final String ISBN_NOT_IN_REPOSITORY = "9788202148680";

    private static Server server;
    private static WebTarget target;

    @BeforeClass
    public static void setUp() throws Exception {

        // start the server
        server = new JettyFactory().build();
        JettyFactory.start(server);

        assertTrue(server.isStarted());
        assertTrue(server.isRunning());

        // create the client
        Client c = ClientBuilder.newClient();
        target = c.target(server.getURI()).path("api");
    }

    @AfterClass
    public static void tearDown() throws Exception {
        JettyFactory.stop(server);
    }

    @Test
    @Ignore
    public void pingShouldReturnPong() {
        final Response response = target
                .path(BOOK_RESOURCE_PATH)
                .path("ping")
                .request(MediaType.TEXT_PLAIN)
                .get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        String ping = response.readEntity(String.class);
        assertEquals(ping, "Pong!");
    }

    @Test
    @Ignore
    public void createBookShouldReturn_CREATED() {
        Book book = Book
                .with("9788202289331")
                .title("Kurtby")
                .author("Loe, Erlend")
                .published(new GregorianCalendar(2008, 1, 1).getTime())
                .summary("Kurt og gjengen er på vei til Mummidalen da Kurt sovner ved rattet og trucken havner " +
                        "i en svensk elv. Et langt stykke nedover elva ligger Kurtby - et lite samfunn hvor en " +
                        "dame som heter Kirsti Brud styrer og steller i samråd med Den hellige ånd. Det går " +
                        "ikke bedre enn at Kurt havner på kjøret, nærmere bestemt på Jesus-kjøret. " +
                        "Så blir han pastor og går bananas.")
                .build();

        final Response response = target
                .path(BOOK_RESOURCE_PATH)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(book, MediaType.APPLICATION_JSON_TYPE));

        // TODO: Verifiser response.getStatus() == Response.Status.CREATED
    }


    @Test
    @Ignore
    public void createBookWithFormParamShouldReturn_CREATED() {

        Form form = new Form();
        form.param("isbn", "9780857520197")
                .param("title", "Second Life")
                .param("author", "Watson, S. J.")
                .param("published", "2015-02-12")
                .param("translator", null)
                .param("summary", "The sensational new psychological thriller from ... ");

        // TODO: POST form og verifiser response.getStatus() == Response.Status.CREATED
    }


    @Test
    @Ignore
    public void updateBookShouldReturn_OK() {
        Book bookToUpdate = BookRepository.findBook(TRAVELLING_TO_INFINITY_ISBN);
        assertNotNull(bookToUpdate);

        Book updatedBook = Book.with(bookToUpdate)
                .title("Travelling to Infinity: The True Story behind")
                .build();

        final Response response = target
                .path(BOOK_RESOURCE_PATH)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.entity(updatedBook, MediaType.APPLICATION_JSON_TYPE));

        // TODO: Verifiser response.getStatus() == Response.Status.OK
    }

    @Test
    @Ignore
    public void deleteBookShouldReturn_NO_CONTENT() {
        Response response = target
                .path(BOOK_RESOURCE_PATH)
                .path(FISKEN_ISBN)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .delete();

        // TODO: Verifiser response.getStatus() == Response.Status.NO_CONTENT
    }


    @Test
    @Ignore
    public void getBookByIsbnShouldReturn_OK() {
        final Response response = target
                .path(BOOK_RESOURCE_PATH)
                .path(TRAVELLING_TO_INFINITY_ISBN)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        // TODO: Verifiser response.getStatus() == Response.Status.OK
        // TODO: Verifiser at ISBN på returnert bok stemmer overens med ISBN i request
    }


    @Test
    public void shouldGetFiveBooks() {
        Integer offset = 0;
        Integer limit = 5;
        Response response = target
                    .path(BOOK_RESOURCE_PATH)
                    .queryParam("offset", offset)
                    .queryParam("limit", limit)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        final List<Book> result = response.readEntity(new GenericType<List<Book>>() {});
        assertThat(result.size(), equalTo(limit));

    }


    @Test
    @Ignore("Kan kjøres etter implementasjon av ...")
    public void headerShouldContainContentTypeUtf8() {
        final Response response = target
                .path(BOOK_RESOURCE_PATH)
                .path(TRAVELLING_TO_INFINITY_ISBN)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        List<Object> objects = response.getHeaders().get("Content-Type");
        assertNotNull(objects);
        String s = objects.toString();
        assertThat(s, containsString("utf-8"));
    }


    @Test
    @Ignore("Kan kjøres etter implementasjon av ...")
    public void headerShouldContainContentEncodingGzip() {
        Response response = target
                .path(BOOK_RESOURCE_PATH)
                .queryParam("offset", 0)
                .queryParam("limit", 5)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        List<Object> objects = response.getHeaders().get("Content-Encoding");
        assertTrue(objects != null && objects.contains("gzip"));
    }

}
