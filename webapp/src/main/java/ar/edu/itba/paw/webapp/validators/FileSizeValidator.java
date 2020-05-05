package ar.edu.itba.paw.webapp.validators;


import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FileSizeValidator implements ConstraintValidator<FileSize, Object> {

    private int MAX_SIZE;

    public void initialize(FileSize constraintAnnotation) {
        this.MAX_SIZE = constraintAnnotation.max();
    }

    public boolean isValid(Object value,
                           ConstraintValidatorContext context) {
        if(value == null)
            return true;

        MultipartFile val = (MultipartFile) value;


        return val.getSize() <= MAX_SIZE;
    }
}