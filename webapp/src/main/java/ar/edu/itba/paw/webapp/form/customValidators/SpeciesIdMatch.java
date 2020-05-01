package ar.edu.itba.paw.webapp.form.customValidators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Constraint(validatedBy = SpeciesIdMatchValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface SpeciesIdMatch {

    String message() default "{SpeciesIdMatch}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}