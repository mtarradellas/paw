package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.interfaces.LocationService;
import ar.edu.itba.paw.interfaces.SpeciesService;
import ar.edu.itba.paw.models.Breed;
import ar.edu.itba.paw.models.Province;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.stream.Stream;

public class ProvinceIdMatchValidator implements ConstraintValidator<ProvinceIdMatch, Object> {

    @Autowired
    private LocationService locationService;

    public void initialize(ProvinceIdMatch constraintAnnotation) {

    }

    public boolean isValid(Object value,
                           ConstraintValidatorContext context) {

        Long val = (Long) value;

        if(val == null)
            return false;

        Stream<Province> provinceStream = locationService.provinceList().stream();

        return provinceStream.anyMatch(p->val == p.getId());
    }
}