package no.javabin.jaxrs.start.rest.api;

import no.javabin.jaxrs.start.domain.Book;
import no.javabin.jaxrs.start.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Singleton
@Path("books")
@Produces(MediaType.APPLICATION_JSON)
public class BookResource {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private UriInfo uriInfo; // actual uri info provided by parent resource (threadsafe)

    public BookResource(@Context UriInfo uriInfo) {
        this.uriInfo = uriInfo;
        logger.debug("Resource created");
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("ping")
    public String ping() {
        // TODO: returner korrekt tekst
        return null;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(final Book book) {

        Book.validate(book); // --> Response.Status.BAD_REQUEST if validation fails

        if(BookRepository.findBook(book.getIsbn()) != null) {
            logger.debug("Can not create book. ISBN: '{}' already in repository", book.getIsbn());
            throw new WebApplicationException(
                Response.status(Response.Status.CONFLICT)
                        .location(uriInfo.getRequestUri())
                        .build()
            );
        }
        BookRepository.addBook(book);
        logger.debug("Book with isbn: '{}' created", book.getIsbn());
        return Response.created(uriInfo.getRequestUri())
                .entity(book)
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response create(@BeanParam final BookParams params) {
        logger.debug("@POST with @BeanParam");
        Book book = Book.with(params.isbn)
                .title(params.title)
                .author(params.author)
                .published(params.published != null ? params.published.getDate() : null)
                .translator(params.translator)
                .summary(params.summary)
                .build();

        return create(book);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Book update(final Book book) {

        Book.validate(book);  // ==> Response.Status.BAD_REQUEST if validation fails

        if(BookRepository.findBook(book.getIsbn()) == null) {
            logger.debug("Could not update book with isbn: '{}'. Not such book in repository", book.getIsbn());
            throw new WebApplicationException(
                    Response.status(Response.Status.NOT_FOUND)
                            .location(uriInfo.getRequestUri())
                            .build()
            );
        }
        BookRepository.updateBook(book);
        logger.debug("Book with isbn: '{}' updated", book.getIsbn());
        return book;  // ==> Response.Status.OK
    }

    @DELETE
    @Path("{isbn}")
    public void delete(@PathParam("isbn") final String isbn) {

        if(BookRepository.findBook(isbn) != null) {
            boolean deleted = BookRepository.removeBook(isbn);
            logger.debug((deleted ? "Book with isbn: '{}' deleted" : "Nothing to delete @ isbn: '{}'"), isbn);
        }
        // return; // ==> void return Response.Status.NO_CONTENT to client
    }

    @GET
    @Path("{isbn}")
    public Book byIsbn(
            @NotNull
            @Size(min = 13, max = 13)
            @Pattern(regexp = "\\d+", message = "ISBN must be a valid number")
            @PathParam("isbn") final String isbn) {

        Book result = BookRepository.findBook(isbn);
        if (result == null) {
            logger.debug(("Book with isbn: '{}' not found"), isbn);
            throw new WebApplicationException(
                    Response.status(Response.Status.NOT_FOUND)
                            .location(uriInfo.getRequestUri())
                            .build()
            );
        }
        return result; // ==>  Response.Status.OK
        // return Response.Status.BAD_REQUEST if Bean validation fails
    }

    @GET
    public Response allBooks(@QueryParam("offset") Integer offset, @QueryParam("limit") Integer limit) {

        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().clone();
        if(offset != null) {
            uriBuilder.queryParam("offset", offset);
        }
        if(limit != null) {
            uriBuilder.queryParam("limit", limit);
        }

        List<Book> books = BookRepository.getAllBooks(offset, limit);

        if(books.size()< 1) {
            return Response
                    .noContent()
                    .location(uriBuilder.build())
                    .build();
        }

        GenericEntity<List<Book>> entities = new GenericEntity<List<Book>>(books){};
        return Response
            .ok(entities)
            .location(uriBuilder.build())
            .build();
    }

    public static class DateAdapter {
        private Date date;

        public DateAdapter(String date){
            this.date = getDateFromString(date);
        }

        public Date getDate(){
            return this.date;
        }

        public static Date getDateFromString(String dateString) {
            try {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                return df.parse(dateString);
            } catch (ParseException e) {
                try {
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    return df.parse(dateString);
                } catch (ParseException e2) {
                    return null;
                }
            }
        }
    }

    public static class BookParams {
        @FormParam("isbn")
        String isbn;

        @FormParam("title")
        String title;

        @FormParam("author")
        String author;

        @FormParam("published")
        DateAdapter published;

        @FormParam("translator")
        String translator;

        @FormParam("summary")
        String summary;
    }
}
