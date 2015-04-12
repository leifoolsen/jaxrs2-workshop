package no.javabin.jaxrs.hateoas.util;

import javax.persistence.EntityManager;

public interface UnitOfWork {
    EntityManager begin();
    void end();
}
