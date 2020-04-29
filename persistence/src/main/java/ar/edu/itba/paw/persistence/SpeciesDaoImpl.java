package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.SpeciesDao;
import ar.edu.itba.paw.models.Breed;
import ar.edu.itba.paw.models.Species;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public class SpeciesDaoImpl implements SpeciesDao {

    private static final String PET_TABLE = "pets";

    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public SpeciesDaoImpl(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(PET_TABLE)
                .usingGeneratedKeyColumns("id");
    }
    private static final RowMapper<Species> SPECIES_MAPPER = (rs, rowNum) -> new Species(
            rs.getInt("id"),
            rs.getString("speciesName")
    );

    private static final RowMapper<Breed> BREEDS_MAPPER = (rs, rowNum) -> new Breed(
            rs.getInt("id"),
            rs.getInt("speciesId"),
            rs.getString("breedName")
    );

    @Override
    public Stream<Species> speciesList(String language) {
        return jdbcTemplate.query("SELECT id, species." + language +" AS speciesName " +
                "FROM species  " +
                "ORDER BY species." + language +" ",  SPECIES_MAPPER)
                .stream();
    }
    public Stream<Breed> breedsList(String language) {
        return jdbcTemplate.query("SELECT id, speciesId ,breeds." + language +" AS breedName " +
                "FROM breeds  " +
                "ORDER BY breeds." + language +" ",  BREEDS_MAPPER)
                .stream();
    }

    @Override
    public Optional<Species> findSpeciesByName(String language, String name) {
        String sql = "SELECT id, species." + language +" AS speciesName " +
                     "FROM species " +
                     "WHERE species." + language + " = ?";
        return jdbcTemplate.query(sql, new Object[] {name}, SPECIES_MAPPER).stream().findFirst();
    }

    @Override
    public Optional<Breed> findBreedByName(String language, String name) {
        String sql = "SELECT id, speciesId, breeds." + language + " AS breedName " +
                "FROM breeds " +
                "WHERE breeds." + language + " = ?";
        return jdbcTemplate.query(sql, new Object[] {name}, BREEDS_MAPPER).stream().findFirst();
    }


}
