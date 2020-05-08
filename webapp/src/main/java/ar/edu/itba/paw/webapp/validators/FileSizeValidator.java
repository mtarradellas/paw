package ar.edu.itba.paw.webapp.validators;


import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class FileSizeValidator implements ConstraintValidator<FileSize, List<MultipartFile>> {

    private int MAX_SIZE;

    public void initialize(FileSize constraintAnnotation) {
        this.MAX_SIZE = constraintAnnotation.max();
    }

    public boolean isValid(List<MultipartFile> list,
                           ConstraintValidatorContext context) {
        if(list == null)
            return true;

        for(MultipartFile file : list) {
            if(file.getSize() > MAX_SIZE)
                return false;
        }

        return true;
    }
}