package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Department;
import ar.edu.itba.paw.models.Province;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface LocationDao {
    List<Department> departmentList();
    List<Province> provinceList();
    Optional<Department> findDepartmentById(long id);
}
