package ar.edu.itba.paw.webapp.validators;

import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FieldsValueDifferentValidator implements ConstraintValidator<FieldsValueDifferent, Object> {

    private String field;
    private String otherField;

    public void initialize(FieldsValueDifferent constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.otherField = constraintAnnotation.otherField();
    }

    public boolean isValid(Object value,
                           ConstraintValidatorContext context) {

        Object fieldValue = new BeanWrapperImpl(value)
                .getPropertyValue(field);
        Object fieldMatchValue = new BeanWrapperImpl(value)
                .getPropertyValue(otherField);

        if (fieldValue != null && fieldMatchValue != null) {
            return !fieldValue.equals(fieldMatchValue);
        }
        return true;
    }
}