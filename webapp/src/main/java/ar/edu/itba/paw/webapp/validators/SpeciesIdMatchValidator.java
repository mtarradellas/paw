package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.interfaces.SpeciesService;
import ar.edu.itba.paw.models.Species;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.stream.Stream;

public class SpeciesIdMatchValidator implements ConstraintValidator<SpeciesIdMatch, Object> {

    @Autowired
    private SpeciesService speciesService;

    public void initialize(SpeciesIdMatch constraintAnnotation) {

    }

    public boolean isValid(Object value,
                           ConstraintValidatorContext context) {


        Long val = (Long) value;

        if(val == null)
            return false;

        Stream<Species> speciesStream = speciesService.speciesList().stream();

        return speciesStream.anyMatch(p-> val.equals(p.getId()));
    }
}