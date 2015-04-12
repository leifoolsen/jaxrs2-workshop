package no.javabin.jaxrs.hateoas.constraint;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Pattern;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Checks if ISBN is valid.
 */

@Retention(RetentionPolicy.RUNTIME)
@NotBlank
@Pattern(regexp = "\\d{13}")
@ReportAsSingleViolation
@Constraint(validatedBy = {})
public @interface Isbn {

    String message() default "{Isbn.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
