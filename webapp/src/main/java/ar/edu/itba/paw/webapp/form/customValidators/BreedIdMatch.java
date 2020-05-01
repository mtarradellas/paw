package ar.edu.itba.paw.webapp.form.customValidators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Constraint(validatedBy = BreedIdMatchValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface BreedIdMatch {

    String message() default "{BreedIdMatch}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}