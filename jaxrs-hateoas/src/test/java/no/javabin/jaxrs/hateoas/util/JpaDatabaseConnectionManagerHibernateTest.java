package no.javabin.jaxrs.hateoas.util;

import no.javabin.jaxrs.hateoas.domain.Book;
import no.javabin.jaxrs.hateoas.domain.Publisher;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.Properties;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class JpaDatabaseConnectionManagerHibernateTest {

    private static final String PU_NAME = "jpa-example-hibernate";
    private static JpaDatabaseConnectionManager.JpaDatabaseConnection connection;

    @BeforeClass
    public static void beforeClass() {

        Properties overridingProperties = new Properties();
        overridingProperties.put("javax.persistence.jdbc.driver", "org.hsqldb.jdbcDriver");
        overridingProperties.put("javax.persistence.jdbc.url", "jdbc:hsqldb:mem:mymemdb");
        overridingProperties.put("javax.persistence.jdbc.user", "sa");
        overridingProperties.put("javax.persistence.jdbc.password", "");
        overridingProperties.put("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");

        // Configure PU //
        Properties properties = PersistenceProperties.createPropertiesForProvider(
                PersistenceProperties.HIBERNATE, overridingProperties, Arrays.asList(Publisher.class, Book.class));


        connection = JpaDatabaseConnectionManager.createConnection(PU_NAME, properties);
        connection.start();
    }

    @AfterClass
    public static void afterClass() {
        JpaDatabaseConnectionManager.removeConnection(PU_NAME);
    }

    @Before
    public void before() {
        connection.unitOfWork().begin();
    }

    @After
    public void after() {
        connection.unitOfWork().end();
    }

    @Test
    public void testPersist() throws Exception {
        EntityManager em = connection.provider().get();
        assertTrue(em.isOpen());

        Publisher publisher = new Publisher(DomainPopulator.ALMA_BOOKS, "Alma books");

        em.getTransaction().begin();
        em.persist(publisher);
        em.flush();
        em.getTransaction().commit();

        Publisher persistedPublisher = em.find(Publisher.class, publisher.getId());
        assertNotNull(persistedPublisher);
    }
}
