package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.LocationDao;
import ar.edu.itba.paw.models.Department;
import ar.edu.itba.paw.models.Province;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;


@Repository
public class LocationJpaDaoImpl implements LocationDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Department> departmentList() {
        final TypedQuery<Department> query = em.createQuery("from Departments ", Department.class);
        return query.getResultList();
    }

    @Override
    public List<Province> provinceList() {
        final TypedQuery<Province> query = em.createQuery("from Provinces ", Province.class);
        return query.getResultList();
    }

    @Override
    public Optional<Department> findDepartmentById(long id) {
        return Optional.ofNullable(em.find(Department.class, id));
    }
}
