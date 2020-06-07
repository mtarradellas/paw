package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Department;
import ar.edu.itba.paw.models.Province;
import java.util.List;
import java.util.Optional;

public interface LocationDao {
    List<Province> provinceList();
    List<Department> departmentList();
    Optional<Province> findProvinceById(long id);
    Optional<Department> findDepartmentById(long id);
}
