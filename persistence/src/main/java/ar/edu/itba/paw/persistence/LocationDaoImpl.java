package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.LocationDao;
import ar.edu.itba.paw.models.Department;
import ar.edu.itba.paw.models.Province;
import ar.edu.itba.paw.models.Species;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public class LocationDaoImpl implements LocationDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public LocationDaoImpl(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    private static final RowMapper<Province> PROVINCE_MAPPER = (rs, rowNum) -> new Province(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getFloat("latitude"),
            rs.getFloat("longitude")
    );

    private static final RowMapper<Department> DEPARTMENT_MAPPER = (rs, rowNum) -> new Department(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getFloat("latitude"),
            rs.getFloat("longitude"),
            new Province(rs.getLong("provId"), rs.getString("provName"),
                    rs.getFloat("provLat"), rs.getFloat("provLon"))
    );


    @Override
    public List<Department> departmentList() {
        String sql = "SELECT departments.id AS id, departments.name AS name, departments.latitude AS latitude, departments.longitude AS longitude, " +
                             "provinces.id AS provId, provinces.name AS provName, provinces.latitude AS provLat, provinces.longitude AS provLon " +
                     "FROM departments INNER JOIN provinces ON departments.province = provinces.name ";

        return jdbcTemplate.query(sql, DEPARTMENT_MAPPER);
    }

    @Override
    public List<Province> provinceList() {
        String sql = "SELECT * " +
                     "FROM provinces ";

        return jdbcTemplate.query(sql, PROVINCE_MAPPER);
    }

    @Override
    public Optional<Department> findDepartmentById(long id) {
        String sql = "SELECT departments.id AS id, departments.name AS name, departments.latitude AS latitude, departments.longitude AS longitude, " +
                "provinces.id AS provId, provinces.name AS provName, provinces.latitude AS provLat, provinces.longitude AS provLon " +
                "FROM departments INNER JOIN provinces ON departments.province = provinces.name " +
                "WHERE departments.id = ?";

        return jdbcTemplate.query(sql, new Object[] {id}, DEPARTMENT_MAPPER).stream().findFirst();
    }
}
