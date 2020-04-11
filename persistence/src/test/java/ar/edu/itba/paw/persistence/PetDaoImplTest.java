package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Pet;
import ar.edu.itba.paw.models.User;
import org.hsqldb.jdbc.JDBCDriver;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.Optional;

import static org.junit.Assert.*;

public class PetDaoImplTest {
    private PetDaoImpl petDao;
    private JdbcTemplate jdbcTemplate;

    private final String PETS_TABLE = "pets";
    @Before
    public void setUp() {
        final SimpleDriverDataSource ds = new SimpleDriverDataSource();
        ds.setDriverClass(JDBCDriver.class);
        ds.setUrl("jdbc:hsqldb:mem:paw");
        ds.setUsername("ha");
        ds.setPassword("");

        petDao = new PetDaoImpl(ds);
        jdbcTemplate = new JdbcTemplate(ds);

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS " + PETS_TABLE + " ( "
                                    + "id INTEGER IDENTITY PRIMARY KEY, "
                                    + "petName VARCHAR(100) "
                                    + ");"
        );
    }

    @Test
    public void testPetDoesntExist() {

        JdbcTestUtils.deleteFromTables(jdbcTemplate, PETS_TABLE);

        Optional<Pet> testPet = petDao.findById(1L);

        assertFalse(testPet.isPresent());
    }

}
