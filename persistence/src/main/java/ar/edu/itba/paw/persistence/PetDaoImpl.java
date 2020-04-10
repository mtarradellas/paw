package ar.edu.itba.paw.persistance;

import ar.edu.itba.paw.interfaces.PetDao;
import ar.edu.itba.paw.models.Pet;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@Repository
public class PetDaoImpl implements PetDao {
    private JdbcTemplate jdbcTemplate;

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
    public Optional<Pet> save(Pet pet){
        return jdbcTemplate.query("INSERT INTO pets(id, petName, species, breed, location, vaccinated, gender, description, birthDate, uploadDate, price, ownerId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                new Object[] {pet.getId(), pet.getPetName(), pet.getSpecies(), pet.getBreed(), pet.getLocation(), pet.isVaccinated(), pet.getGender(), pet.getDescription(), pet.getBirthDate(), pet.getUploadDate(), pet.getPrice(), pet.getOwnerId()}, PET_MAPPER)
                .stream().findFirst();
    };
}
