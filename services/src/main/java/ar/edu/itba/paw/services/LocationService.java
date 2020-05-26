package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.LocationDao;
import ar.edu.itba.paw.models.Department;
import ar.edu.itba.paw.models.DepartmentList;
import ar.edu.itba.paw.models.ProvinceList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LocationService implements ar.edu.itba.paw.interfaces.LocationService {

    @Autowired
    LocationDao locationDao;

    @Override
    public ProvinceList provinceList() {
        return new ProvinceList(locationDao.provinceList());
    }

    @Override
    public DepartmentList departmentList() {
        return new DepartmentList(locationDao.departmentList());
    }

    @Override
    public Optional<Department> findDepartmentById(long id) {
        return locationDao.findDepartmentById(id);
    }
}
