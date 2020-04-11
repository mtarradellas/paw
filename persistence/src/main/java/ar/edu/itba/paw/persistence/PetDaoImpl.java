package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.PetDao;
import ar.edu.itba.paw.models.Pet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@Repository
public class PetDaoImpl implements PetDao {

    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private static final RowMapper<Pet> PET_MAPPER = (rs, rowNum) -> new Pet(
            rs.getLong("id"),
            rs.getString("petName"),
            rs.getString("species"),
            rs.getString("breed"),
            rs.getString("location"),
            rs.getBoolean("vaccinated"),
            rs.getString("gender"),
            rs.getString("description"),
            rs.getDate("birthDate"),
            rs.getDate("uploadDate"),
            rs.getInt("price"),
            rs.getLong("ownerId")
    );

    @Autowired
    public PetDaoImpl(final DataSource dataSource) {

        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource);
    }

    @Override
    public Optional<Pet> findById(long id) {
        return jdbcTemplate.query("SELECT * FROM pets WHERE id = ?", new Object[] {id}, PET_MAPPER)
                .stream().findFirst();
    }

    @Override
    public Stream<Pet> list() {
        return jdbcTemplate.query("SELECT * FROM pets", PET_MAPPER)
                .stream();
    }

    @Override
    public Pet create(String petName, String species, String breed, String location, boolean vaccinated, String gender, String description, Date birthDate, Date uploadDate, int price, long ownerId) {
        final Map<String, Object> values = new HashMap<String, Object>() {{
            put("petName", petName);
            put("species", species);
            put("breed", breed);
            put("location", location);
            put("vaccinated", vaccinated);
            put("gender", gender);
            put("description", description);
            put("birthDate", birthDate);
            put("uploadDate", uploadDate);
            put("price", price);
            put("ownerId", ownerId);
        }};
        final Number key = jdbcInsert.executeAndReturnKey(values);

        return new Pet(key.longValue(), petName, species, breed,location,vaccinated,gender,description,birthDate,uploadDate,price,ownerId);
    }
}
