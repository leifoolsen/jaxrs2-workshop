# jaxrs2-workshop
Workshop: REST-baserte tjenester med JAX-RS 2, javaBin Sørlandet, 2015-04-15 

## Laptop som skal benyttes må ha følgende:
* JDK-1.7 (eller høyere)
* Maven3 
* IDE, f.eks. NetBeans, IntelliJ, Eclipse
* Nettleser med JSON plugin (jeg benytter Chrome med JSONView: https://chrome.google.com/webstore/detail/jsonview/chklaanhfefbnpoihckbnefhakgolnmc)

## Forberedelser
* Clone eller last ned ZIP-fil.
* For å komme kjapt i gang med selve workshopen, så er det en fordel å bygge begge mavenprosjektene på forhånd. 
  Det er to prosjektmapper; jaxrs-start og jaxrs-hateoas. Kjør mavenkommandoen "mvn clean install -U" i begge prosjektmappene.

## Agenda
* Komme i gang
  * Lage en enkel tjeneste
  * JAX-RS Annotasjoner
  * Responskoder
  * @POST, @PUT, @GET, @DELETE
* Feilhåndtering
  * Responskoder
  * WebApplicationException
  * Bruk av Exceptionmapper for å fange "alle typer" feil
* Filters & interceptors
  * Noen ekaempler og bruksområder
* HATEOAS
  * Hvilke hjelpemidler finnes i API'et
  * Eksempel på 3'dje-parts rammeverk
  * Design/modellering av meldingsstruktur med tanke på navigerbarhet, JSON
  * Implementere HATEOAS med "collection+json" mediatype

### Nyttige lenker
* Jersey home : https://jersey.java.net/
* Jersey User Guide : https://jersey.java.net/documentation/latest/user-guide.html
* Jersey Examples bundle : https://jersey.java.net/download.html
* Collection+JSON - Hypermedia Type : http://amundsen.com/media-types/collection/
* Collection+JSON Git : https://github.com/collection-json
* Java implementation of Collection+JSON, Git : https://github.com/hamnis/json-collection
* Collection JSON JAXRS 2.0 Module : https://github.com/hamnis/collection-json-jaxrs
* HAL - Hypertext Application Language : http://stateless.co/hal_specification.html
* {json:api} : http://jsonapi.org/
* JSON for Linking Data : http://json-ld.org/
* On choosing a hypermedia type for your API - HAL, JSON-LD, Collection+JSON, SIREN, Oh My! : http://sookocheff.com/posts/2014-03-11-on-choosing-a-hypermedia-format/







  * @WebListener
  * Interceptors. GZIP-interceptor  
  * @Context. Resource injection av UriInfo og ResourceContext  
  * @WebFilter
  * @POST og @PUT med @BeanParam injection
  * Guice og Guice Persist (utgår)

