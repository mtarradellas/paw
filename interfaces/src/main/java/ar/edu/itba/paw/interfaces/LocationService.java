package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.DepartmentList;
import ar.edu.itba.paw.models.ProvinceList;

public interface LocationService {
    ProvinceList provinceList();
    DepartmentList departmentList();
}
