package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Department;
import ar.edu.itba.paw.models.DepartmentList;
import ar.edu.itba.paw.models.ProvinceList;

import java.util.Optional;

public interface LocationService {
    ProvinceList provinceList();
    DepartmentList departmentList();
    Optional<Department> findDepartmentById(long id);
}
