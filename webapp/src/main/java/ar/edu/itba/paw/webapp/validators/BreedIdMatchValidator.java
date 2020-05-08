package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.interfaces.SpeciesService;
import ar.edu.itba.paw.models.Breed;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.stream.Stream;

public class BreedIdMatchValidator implements ConstraintValidator<BreedIdMatch, Object> {

    @Autowired
    private SpeciesService speciesService;

    public void initialize(BreedIdMatch constraintAnnotation) {

    }

    public boolean isValid(Object value,
                           ConstraintValidatorContext context) {

        Integer val = (Integer) value;

        if(val == null)
            return false;

        Stream<Breed> breedStream = speciesService.breedsList("en_US");

        return breedStream.anyMatch(p->val == p.getId());
    }
}