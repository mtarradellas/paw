package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Pet;
import ar.edu.itba.paw.models.User;
import org.hsqldb.jdbc.JDBCDriver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class PetDaoImplTest {
    private final String PETS_TABLE = "pets";
    private final String PET_NAME = "pet_test_name";
    private final String SPECIES = "pet_test_species";
    private final String BREED = "pet_test_breed";
    private final String LOCATION = "pet_test_location";
    private final Boolean VACCINATED = false;
    private final String GENDER = "pet_test_gender";
    private final String DESCRIPTION = "pet_test_description";
    private final Date BIRTH_DATE = null;
    private final Date UPLOAD_DATE = new Date(2001, 3, 2);
    private final int PRICE = 0;
    private final int OWNER_ID = 1;

    @Autowired
    private DataSource ds;

    private PetDaoImpl petDaoImpl;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @Before
    public void setUp() {
        petDaoImpl = new PetDaoImpl(ds);
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(PETS_TABLE)
                .usingGeneratedKeyColumns("id");
    }

    @Test
    public void testCreatePet() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, PETS_TABLE);

        Pet pet = petDaoImpl.create(PET_NAME, SPECIES, BREED, LOCATION, VACCINATED, GENDER,
                DESCRIPTION, BIRTH_DATE, UPLOAD_DATE, PRICE, OWNER_ID);

        assertNotNull(pet);
        assertEquals(PET_NAME, pet.getPetName());
        assertEquals(SPECIES, pet.getSpecies());
        assertEquals(BREED, pet.getBreed());
        assertEquals(LOCATION, pet.getLocation());
        assertEquals(VACCINATED, pet.isVaccinated());
        assertEquals(GENDER, pet.getGender());
        assertEquals(DESCRIPTION, pet.getDescription());
        assertEquals(BIRTH_DATE, pet.getBirthDate());
        assertEquals(UPLOAD_DATE, pet.getUploadDate());
        assertEquals(PRICE, pet.getPrice());
        assertEquals(OWNER_ID, pet.getOwnerId());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, PETS_TABLE));
    }

    @Test
    public void testFindByIdDoesNotExist() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, PETS_TABLE);

        Optional<Pet> testPet = petDaoImpl.findById(1L);

        assertFalse(testPet.isPresent());
    }

    @Test
    public void testFindByIdExists() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, PETS_TABLE);
        final Map<String, Object> values = new HashMap<String, Object>() {{
            put("petName", PET_NAME);
            put("species", SPECIES);
            put("breed", BREED);
            put("location", LOCATION);
            put("vaccinated", VACCINATED);
            put("gender", GENDER);
            put("description", DESCRIPTION);
            put("birthDate", BIRTH_DATE);
            put("uploadDate", UPLOAD_DATE);
            put("price", PRICE);
            put("ownerId", OWNER_ID);
        }};
        Number key = jdbcInsert.executeAndReturnKey(values);

        Optional<Pet> testPet = petDaoImpl.findById(key.longValue());

        assertTrue(testPet.isPresent());
        Pet pet = testPet.get();
        assertEquals(PET_NAME, pet.getPetName());
        assertEquals(SPECIES, pet.getSpecies());
        assertEquals(BREED, pet.getBreed());
        assertEquals(LOCATION, pet.getLocation());
        assertEquals(VACCINATED, pet.isVaccinated());
        assertEquals(GENDER, pet.getGender());
        assertEquals(DESCRIPTION, pet.getDescription());
        assertEquals(BIRTH_DATE, pet.getBirthDate());
        assertEquals(UPLOAD_DATE, pet.getUploadDate());
        assertEquals(PRICE, pet.getPrice());
        assertEquals(OWNER_ID, pet.getOwnerId());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, PETS_TABLE));
    }

    @Test
    public void testFilteredListSpecies() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, PETS_TABLE);
        final Map<String, Object> values = new HashMap<String, Object>() {{
            put("petName", PET_NAME);
            put("species", SPECIES);
            put("breed", BREED);
            put("location", LOCATION);
            put("vaccinated", VACCINATED);
            put("gender", GENDER);
            put("description", DESCRIPTION);
            put("birthDate", BIRTH_DATE);
            put("uploadDate", UPLOAD_DATE);
            put("price", PRICE);
            put("ownerId", OWNER_ID);
        }};
        jdbcInsert.executeAndReturnKey(values);
        final Map<String, Object> other_values = new HashMap<String, Object>() {{
            put("petName", "other_petname");
            put("species", "other_species");
            put("breed", "other_breed");
            put("location", LOCATION);
            put("vaccinated", VACCINATED);
            put("gender", "other_gender");
            put("description", DESCRIPTION);
            put("birthDate", BIRTH_DATE);
            put("uploadDate", UPLOAD_DATE);
            put("price", PRICE);
            put("ownerId", OWNER_ID);
        }};
        jdbcInsert.executeAndReturnKey(other_values);

        Stream<Pet> petStream = petDaoImpl.filteredList(SPECIES, null, null, "species", "asc");
        List<Pet> petList = petStream.collect(Collectors.toList());

        assertEquals(1, petList.size());
        Pet pet = petList.get(0);
        assertEquals(PET_NAME, pet.getPetName());
        assertEquals(SPECIES, pet.getSpecies());
        assertEquals(BREED, pet.getBreed());
        assertEquals(LOCATION, pet.getLocation());
        assertEquals(VACCINATED, pet.isVaccinated());
        assertEquals(GENDER, pet.getGender());
        assertEquals(DESCRIPTION, pet.getDescription());
        assertEquals(BIRTH_DATE, pet.getBirthDate());
        assertEquals(UPLOAD_DATE, pet.getUploadDate());
        assertEquals(PRICE, pet.getPrice());
        assertEquals(OWNER_ID, pet.getOwnerId());
    }

    @Test
    public void testFilteredListBreed() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, PETS_TABLE);
        final Map<String, Object> values = new HashMap<String, Object>() {{
            put("petName", PET_NAME);
            put("species", SPECIES);
            put("breed", BREED);
            put("location", LOCATION);
            put("vaccinated", VACCINATED);
            put("gender", GENDER);
            put("description", DESCRIPTION);
            put("birthDate", BIRTH_DATE);
            put("uploadDate", UPLOAD_DATE);
            put("price", PRICE);
            put("ownerId", OWNER_ID);
        }};
        jdbcInsert.executeAndReturnKey(values);
        final Map<String, Object> other_values = new HashMap<String, Object>() {{
            put("petName", "other_petname");
            put("species", SPECIES);
            put("breed", "other_breed");
            put("location", LOCATION);
            put("vaccinated", VACCINATED);
            put("gender", "other_gender");
            put("description", DESCRIPTION);
            put("birthDate", BIRTH_DATE);
            put("uploadDate", UPLOAD_DATE);
            put("price", PRICE);
            put("ownerId", OWNER_ID);
        }};
        jdbcInsert.executeAndReturnKey(other_values);

        Stream<Pet> petStream = petDaoImpl.filteredList(SPECIES, BREED, null, "species", "asc");
        List<Pet> petList = petStream.collect(Collectors.toList());

        assertEquals(1, petList.size());
        Pet pet = petList.get(0);
        assertEquals(PET_NAME, pet.getPetName());
        assertEquals(SPECIES, pet.getSpecies());
        assertEquals(BREED, pet.getBreed());
        assertEquals(LOCATION, pet.getLocation());
        assertEquals(VACCINATED, pet.isVaccinated());
        assertEquals(GENDER, pet.getGender());
        assertEquals(DESCRIPTION, pet.getDescription());
        assertEquals(BIRTH_DATE, pet.getBirthDate());
        assertEquals(UPLOAD_DATE, pet.getUploadDate());
        assertEquals(PRICE, pet.getPrice());
        assertEquals(OWNER_ID, pet.getOwnerId());
    }

    @Test
    public void testFilteredListGender() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, PETS_TABLE);
        final Map<String, Object> values = new HashMap<String, Object>() {{
            put("petName", PET_NAME);
            put("species", SPECIES);
            put("breed", BREED);
            put("location", LOCATION);
            put("vaccinated", VACCINATED);
            put("gender", GENDER);
            put("description", DESCRIPTION);
            put("birthDate", BIRTH_DATE);
            put("uploadDate", UPLOAD_DATE);
            put("price", PRICE);
            put("ownerId", OWNER_ID);
        }};
        Number key = jdbcInsert.executeAndReturnKey(values);
        final Map<String, Object> other_values = new HashMap<String, Object>() {{
            put("petName", "other_petname");
            put("species", "other_species");
            put("breed", "other_breed");
            put("location", LOCATION);
            put("vaccinated", VACCINATED);
            put("gender", "other_gender");
            put("description", DESCRIPTION);
            put("birthDate", BIRTH_DATE);
            put("uploadDate", UPLOAD_DATE);
            put("price", PRICE);
            put("ownerId", OWNER_ID);
        }};
        Number other_key = jdbcInsert.executeAndReturnKey(other_values);

        Stream<Pet> petStream = petDaoImpl.filteredList(null, null, GENDER, "species", "asc");
        List<Pet> petList = petStream.collect(Collectors.toList());

        assertEquals(1, petList.size());
        Pet pet = petList.get(0);
        assertEquals(PET_NAME, pet.getPetName());
        assertEquals(SPECIES, pet.getSpecies());
        assertEquals(BREED, pet.getBreed());
        assertEquals(LOCATION, pet.getLocation());
        assertEquals(VACCINATED, pet.isVaccinated());
        assertEquals(GENDER, pet.getGender());
        assertEquals(DESCRIPTION, pet.getDescription());
        assertEquals(BIRTH_DATE, pet.getBirthDate());
        assertEquals(UPLOAD_DATE, pet.getUploadDate());
        assertEquals(PRICE, pet.getPrice());
        assertEquals(OWNER_ID, pet.getOwnerId());
    }

}
