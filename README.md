# jaxrs2-workshop
Workshop: REST-baserte tjenester med JAX-RS 2, javaBin Sørlandet, 2015-04-15 

## Agenda
* Komme i gang
  * Lage en "Hello World" tjeneste
  * @Application
  * @WebFilter, EntityManager og Unit of Work
  * Interceptors. GZIP-interceptor.  
  * Koble opp database med JPA
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

## Laptop som skal benyttes må ha følgende:
* JDK-1.7 (eller høyere)
* Maven3 
* IDE, f.eks. NetBeans, IntelliJ, Eclipse
* Nettleser med JSON plugin (jeg benytter Chrome med JSONView: https://chrome.google.com/webstore/detail/jsonview/chklaanhfefbnpoihckbnefhakgolnmc)

### Bygg prosjektet på forhånd slik at prosjektets avhengigheter ligger i lokalt .m2-repo
* Fork, Clone eller last ned ZIP
* Bygg prosjektet: mvn clean install -U

