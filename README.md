# jaxrs2-workshop
Workshop: REST-baserte tjenester med JAX-RS 2, javaBin Sørlandet, 2015-04-15 

## Agenda
* Komme i gang
  * Lage en "Hello World" tjeneste
  * @Application
  * @WebListener
  * Interceptors. GZIP-interceptor  
  * @Context. Resource injection av UriInfo og ResourceContext  
  * @WebFilter, EntityManager og Unit of Work. Koble opp database med JPA
  * @POST og @PUT med @BeanParam injection
  * Guice og Guice Persist
* Feilhåndtering
  * WebApplicationException
  * BeanValidation-API
  * Bruk av Exceptionmapper for å fange "alle typer" feil
  * Design av meldingsformat, JSON
* HATEOAS
  * Hvilke hjelpemidler finnes i API'et
  * Eksempel på 3'dje-parts rammeverk
  * Design/modellering av meldingsstruktur med tanke på navigerbarhet, JSON
  * Implementere HATEOAS med "collection+json" mediatype

## Laptop som skal benyttes må ha følgende:
* JDK-1.7 (eller høyere)
* Maven3 
* IDE, f.eks. NetBeans, IntelliJ, Eclipse
* Nettleser med JSON plugin (jeg benytter Chrome med JSONView: https://chrome.google.com/webstore/detail/jsonview/chklaanhfefbnpoihckbnefhakgolnmc)

### Bygg prosjektene på forhånd slik at prosjektets avhengigheter ligger i lokalt .m2-repo
* Fork, Clone eller last ned ZIP
* Bygg prosjektet: mvn clean install -U

### Nyttige lenker
* Jersey home : https://jersey.java.net/
* Jersey User Guide : https://jersey.java.net/documentation/latest/user-guide.html
* Jersey Examples bundle : https://jersey.java.net/download.html
* Collection+JSON - Hypermedia Type : http://amundsen.com/media-types/collection/
* Collection+JSON Git : https://github.com/collection-json
* On choosing a hypermedia type for your API - HAL, JSON-LD, Collection+JSON, SIREN, Oh My! : http://sookocheff.com/posts/2014-03-11-on-choosing-a-hypermedia-format/
* HAL - Hypertext Application Language : http://stateless.co/hal_specification.html
* {json:api} : http://jsonapi.org/
* JSON for Linking Data : http://json-ld.org/

