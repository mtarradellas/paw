package ar.edu.itba.paw.webapp.validators;


import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

public class ImageDimensionsValidator implements ConstraintValidator<ImageDimensions, List<MultipartFile>> {

    private int MIN_WIDTH, MAX_WIDTH, MAX_HEIGHT, MIN_HEIGHT;
    private double MAX_RATIO;

    public void initialize(ImageDimensions constraintAnnotation) {
        this.MIN_WIDTH = constraintAnnotation.minWidth();
        this.MAX_WIDTH = constraintAnnotation.maxWidth();
        this.MIN_HEIGHT = constraintAnnotation.minHeight();
        this.MAX_HEIGHT = constraintAnnotation.maxHeight();
        this.MAX_RATIO = constraintAnnotation.widthToHeightMaxRatio();
    }

    public boolean isValid(List<MultipartFile> value,
                           ConstraintValidatorContext context) {

        if(value == null)
            return true;

        for(MultipartFile file : value){
            if(file.isEmpty())
                continue;

            BufferedImage image;
            try{
                image = ImageIO.read(file.getInputStream());
                if(image == null)
                    return false;
            }catch (IOException e){
                return false;
            }

            int height = image.getHeight(), width = image.getWidth();

            if(!( width >= MIN_WIDTH && width <= MAX_WIDTH
                    && height >= MIN_HEIGHT && height <= MAX_HEIGHT
                    && Math.max(width, height) <= Math.min(width, height)*MAX_RATIO))
                return false;
        }

        return true;

    }
}