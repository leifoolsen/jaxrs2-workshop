package no.javabin.jaxrs.hateoas.rest.api;

import no.javabin.jaxrs.hateoas.constraint.SearchType;
import no.javabin.jaxrs.hateoas.domain.Book;
import no.javabin.jaxrs.hateoas.repository.BookRepositoryJpa;
import no.javabin.jaxrs.hateoas.rest.interceptor.Compress;
import no.javabin.jaxrs.hateoas.util.CollectionJson;
import no.javabin.jaxrs.hateoas.util.DatabaseConnection;
import no.javabin.jaxrs.hateoas.util.JpaDatabaseConnectionManager;
import com.google.common.base.MoreObjects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Singleton
@Produces(MediaType.APPLICATION_JSON)
public class SearchResource {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final int DEFAULT_LIMIT = 20;

    private JpaDatabaseConnectionManager.JpaDatabaseConnection connection = DatabaseConnection.getConnection();
    private BookRepositoryJpa repository = new BookRepositoryJpa(connection);

    private UriInfo uriInfo;

    public SearchResource(@Context @NotNull UriInfo uriInfo) {
        this.uriInfo = uriInfo;
        logger.debug(this.getClass().getSimpleName() + " created");
    }

    @GET
    @Compress
    public Response allBooks(
            @SearchType @PathParam("searchType") final String searchType,
            @QueryParam("q") final String searchValue,
            @QueryParam("offset") final Integer offset,
            @QueryParam("limit") final Integer limit) {

        Integer off = Math.max(MoreObjects.firstNonNull(offset, 0), 0);            // At least zero
        Integer lim = Math.max(MoreObjects.firstNonNull(limit, DEFAULT_LIMIT), 1); // Greater than zero

        List<Book> books = repository.findBooksBySearchType(SearchType.Type.get(searchType), searchValue, off, lim);

        CollectionJson collectionJson = CollectionJsonResourceHelper.buildCollectionJson(uriInfo, books);

        UriBuilder rootUriBuilder = CollectionJsonResourceHelper
                .resourceRootUriBuilder(uriInfo)
                .path("search")
                .path(searchType);

        if(searchValue != null) {
            rootUriBuilder.queryParam("q", searchValue);
        }
        if(off > 0) {
            int pOffset = Math.max(off - lim, 0);
            collectionJson.collection().addLink(
                    "prev", rootUriBuilder.clone()
                            .queryParam("offset", pOffset)
                            .queryParam("limit", lim).toString());
        }
        if(books.size() >= lim) {
            int nOffset = off + lim;
            collectionJson.collection().addLink(
                    "next", rootUriBuilder.clone()
                            .queryParam("offset", nOffset)
                            .queryParam("limit", lim).toString());
        }
        return Response
                .ok(collectionJson)
                .location(uriInfo.getRequestUri())
                .build();
    }

}
