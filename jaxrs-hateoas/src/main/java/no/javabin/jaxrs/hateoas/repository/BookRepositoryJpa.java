package no.javabin.jaxrs.hateoas.repository;

import no.javabin.jaxrs.hateoas.constraint.SearchType;
import no.javabin.jaxrs.hateoas.domain.Book;
import no.javabin.jaxrs.hateoas.domain.Publisher;
import no.javabin.jaxrs.hateoas.util.QueryParameter;
import no.javabin.jaxrs.hateoas.util.Repository;
import no.javabin.jaxrs.hateoas.util.RepositoryJPA;
import no.javabin.jaxrs.hateoas.util.StringUtil;
import com.google.common.base.Joiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Provider;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BookRepositoryJpa {
    private static final Logger logger = LoggerFactory.getLogger(BookRepositoryJpa.class);

    private final Repository repository;

    public BookRepositoryJpa(final Provider<EntityManager> provider) {
        this.repository = new RepositoryJPA(provider);
    }

    public Book newBook(final Book book) {
        try {
            return repository.persist(book);
        }
        catch (PersistenceException e) {
            if(findBookByISBN(book.getISBN()) != null) {
                throw new EntityExistsException("Could not create book. Duplicate ISBN: " + book.getISBN(), e);
            }
            throw e;
        }
    }

    public Book updateBook(final Book book) {
        if(book.getId() == null) {
            throw new ConstraintViolationException("Can not update book with id = null", null);
        }
        try {
            return repository.merge(book);
        }
        catch (PersistenceException e) {
            Book b = findBookByISBN(book.getISBN());
            if(b != null && ! b.getId().equals(book.getId())) {
                throw new EntityExistsException("Could not update book. Duplicate ISBN: " + book.getISBN(), e);
            }
            throw e;
        }
    }

    public Book createOrUpdateBook(final Book book) {
        return repository.createOrUpdate(book);
    }

    public Book findBook(final String id) {
        return repository.find(Book.class, id);
    }

    public void deleteBook(final Book book) {
        repository.remove(book);
    }

    public void deleteBook(final String id) {
        repository.remove(Book.class, id);
    }

    public Long countBooks() {
        return repository.count(Book.class);
    }

    public List<Book> findBooks(final Integer offset, final Integer limit) {
        return repository.find(Book.class, offset, limit);
    }

    public Book findBookByISBN(final String isbn) {
        String s = StringUtil.blankToNull(isbn);
        if(s != null) {
            final String jpql = "select b from %s b where b.isbn = :isbn";
            final QueryParameter qp = QueryParameter.with("isbn", s);
            return RepositoryJPA.findFirstWithQuery(createQuery(jpql, Book.class, qp.parameters()));
        }
        return null;
    }

    public List<Book> findBooksBySearchType(
            final SearchType.Type searchType, final String searchValue, final Integer offset, final Integer limit) {

        String jpql = "select b from %s b ";
        QueryParameter qp = null;

        final String sv = StringUtil.blankToNull(searchValue);

        if(SearchType.Type.ANY == searchType && sv != null) {
            jpql += "where ";
            List<String> any = new ArrayList<>();
            for (SearchType.Type t : SearchType.Type.values()) {
                if(SearchType.Type.ANY != t ) {
                    any.add("LOWER(b." + t.type() + ") like :searchType ");
                }
            }
            jpql += Joiner.on("or ").join(any);
            qp = QueryParameter.with("searchType", "%" + sv.toLowerCase() + "%");
        }
        else if(sv != null) {
            jpql += "where LOWER(b." + searchType.type() + ") like :searchType ";
            qp = QueryParameter.with("searchType", "%" + sv.toLowerCase() + "%");
        }

        if(SearchType.Type.ANY != searchType) {
            jpql += "order by b." + searchType.type();
        }

        return RepositoryJPA.findWithQuery(
                createQuery(jpql, Book.class, (qp != null ? qp.parameters() : null)), offset, limit);
    }

    public Publisher findPublisherByCode(final String publisherCode) {
        final String jpql = "select p from %s p where p.code = :code";
        final QueryParameter qp = QueryParameter.with("code", publisherCode);
        return RepositoryJPA.findFirstWithQuery(createQuery(jpql, Publisher.class, qp.parameters()));
    }

    public List<Publisher> findPublishersByName(final String publisherName) {
        final String jpql = "select p from %s p where p.name like :name";
        final QueryParameter qp = QueryParameter.with("name", publisherName+"%");
        return RepositoryJPA.findWithQuery(createQuery(jpql, Publisher.class, qp.parameters()), null, null);
    }

    public List<Publisher> findPublishers (final Integer offset, final Integer limit) {
        return repository.find(Publisher.class, offset, limit);
    }

    private <T> TypedQuery<T> createQuery(final String queryString, final Class<T> entityClass, final Map<String, Object> parameters) {
        final String entityName = RepositoryJPA.entityName(entityClass);
        final String jpql = String.format(queryString, entityName);
        return repository.createQuery(jpql, entityClass, parameters);
    }
}
