package ar.edu.itba.paw.webapp.validators;

import org.springframework.web.multipart.MultipartFile;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class NotEmptyMultipartValidator implements ConstraintValidator<NotEmptyMultipart, List<MultipartFile>> {
    @Override
    public void initialize(NotEmptyMultipart notEmptyMultipart) {

    }

    @Override
    public boolean isValid(List<MultipartFile> multipartFiles, ConstraintValidatorContext constraintValidatorContext) {
        if(multipartFiles == null)
            return true;

        for(MultipartFile file : multipartFiles){
            if(file.isEmpty())
                return false;
        }

        return true;
    }
}
