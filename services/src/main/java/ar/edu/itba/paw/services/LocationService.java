package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.LocationDao;
import ar.edu.itba.paw.models.Department;
import ar.edu.itba.paw.models.Province;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class LocationService implements ar.edu.itba.paw.interfaces.LocationService {

    @Autowired
    LocationDao locationDao;

    @Override
    public List<Province> provinceList() {
        return locationDao.provinceList();
    }

    @Override
    public List<Department> departmentList() {
        return locationDao.departmentList();
    }

    @Override
    public Optional<Province> findProvinceById(long id) {
        return locationDao.findProvinceById(id);
    }

    @Override
    public Optional<Department> findDepartmentById(long id) {
        return locationDao.findDepartmentById(id);
    }
}
