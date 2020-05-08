package ar.edu.itba.paw.webapp.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ImageDimensionsValidator.class)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ImageDimensions {

    String message() default "{dimensions}";

    Class<?>[] groups() default {};

    int maxHeight() default 10000;

    int minHeight() default 100;

    int maxWidth() default 10000;

    int minWidth() default 100;

    double widthToHeightMaxRatio() default 3;

    Class<? extends Payload>[] payload() default {};
}