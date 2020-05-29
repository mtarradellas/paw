package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.interfaces.LocationService;
import ar.edu.itba.paw.models.Department;
import ar.edu.itba.paw.models.Province;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.stream.Stream;

public class DepartmentIdMatchValidator implements ConstraintValidator<DepartmentIdMatch, Object> {

    @Autowired
    private LocationService locationService;

    public void initialize(DepartmentIdMatch constraintAnnotation) {

    }

    public boolean isValid(Object value,
                           ConstraintValidatorContext context) {

        Long val = (Long) value;

        if(val == null)
            return false;

        Stream<Department> departmentStream = locationService.departmentList().stream();

        boolean b = departmentStream.anyMatch(p-> val.equals(p.getId()));

        return b;
    }
}