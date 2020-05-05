package ar.edu.itba.paw.webapp.validators;


import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageDimensionsValidator implements ConstraintValidator<ImageDimensions, Object> {

    private int MIN_WIDTH, MAX_WIDTH, MAX_HEIGHT, MIN_HEIGHT;

    public void initialize(ImageDimensions constraintAnnotation) {
        this.MIN_WIDTH = constraintAnnotation.minWidth();
        this.MAX_WIDTH = constraintAnnotation.maxWidth();
        this.MIN_HEIGHT = constraintAnnotation.minHeight();
        this.MAX_HEIGHT = constraintAnnotation.maxHeight();
    }

    public boolean isValid(Object value,
                           ConstraintValidatorContext context) {

        if(value == null)
            return true;

        MultipartFile val = (MultipartFile) value;

        BufferedImage image;
        try{
            image = ImageIO.read(val.getInputStream());
        }catch (IOException e){
            return false;
        }

        return image.getWidth() >= MIN_WIDTH && image.getWidth() <= MAX_WIDTH
                && image.getHeight() >= MIN_HEIGHT && image.getHeight() <= MAX_HEIGHT;
    }
}