package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.constants.PetStatus;
import ar.edu.itba.paw.models.constants.UserStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import javax.sql.DataSource;
import java.util.Date;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class PetDaoImplTest {

    /* TABLES */
    private final String PETS_TABLE = "pets";
    private final String USERS_TABLE = "users";
    private final String PROVINCES_TABLE = "provinces";
    private final String DEPARTMENTS_TABLE = "departments";
    private final String SPECIES_TABLE = "species";
    private final String BREEDS_TABLE = "breeds";
    private final String IMAGES_TABLE = "images";

    private static final String LOCALE = "en_US";

    /* SPECIES */
    private static Species SPECIES;
    private static Long SPEC_ID;
    private static final String SPEC_ESAR = "species";
    private static final String SPEC_ENUS = "species";

    /* OTHER SPECIES */
    private static Species O_SPECIES;
    private static Long O_SPEC_ID;
    private static final String O_SPEC_ESAR = "other_species";
    private static final String O_SPEC_ENUS = "other_species";

    /* BREED */
    private static Breed BREED;
    private static Long BREED_ID;
    private static final String BREED_ESAR = "breed";
    private static final String BREED_ENUS = "breed";

    /* OTHER BREED */
    private static Breed O_BREED;
    private static Long O_BREED_ID;
    private static final String O_BREED_ESAR = "other_breed";
    private static final String O_BREED_ENUS = "other_breed";

    /* PROVINCE */
    private static Province PROVINCE;
    private static Long PROV_ID;
    private static final String PROV_NAME = "province";
    private static final double PROV_LAT = 10;
    private static final double PROV_LON = 10;

    /* OTHER PROVINCE */
    private static Province O_PROVINCE;
    private static Long O_PROV_ID;
    private static final String O_PROV_NAME = "other_province";
    private static final double O_PROV_LAT = 50;
    private static final double O_PROV_LON = 50;

    /* DEPARTMENT */
    private static Department DEPARTMENT;
    private static Long DEPT_ID;
    private static final String DEPT_NAME = "department";
    private static final double DEPT_LAT = 10;
    private static final double DEPT_LON = 10;

    /* OTHER DEPARTMENT */
    private static Department O_DEPARTMENT;
    private static Long O_DEPT_ID;
    private static final String O_DEPT_NAME = "other_department";
    private static final double O_DEPT_LAT = 50;
    private static final double O_DEPT_LON = 50;

    /* USER */
    private static User USER;
    private static Long USER_ID;
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String MAIL = "user@mail.com";
    private static final UserStatus USER_STATUS = UserStatus.ACTIVE;

    /* USER */
    private static User O_USER;
    private static Long O_USER_ID;
    private static final String O_USERNAME = "other_username";
    private static final String O_PASSWORD = "other_password";
    private static final String O_MAIL = "other_user@mail.com";
    private static final UserStatus O_USER_STATUS = UserStatus.ACTIVE;

    /* PET */
    private static Long PET_ID;
    private static final String PET_NAME = "petname";
    private static final Boolean VACCINATED = true;
    private static final String GENDER = "gender";
    private static final String DESCRIPTION = "description";
    private static final Date BIRTH_DATE = null;
    private Date UPLOAD_DATE;
    private static final int PRICE = 0;
    private static final PetStatus STATUS = PetStatus.AVAILABLE;

    /* OTHER PET */
    private static Long O_PET_ID;
    private static final String O_PET_NAME = "petname";
    private static final Boolean O_VACCINATED = false;
    private static final String O_GENDER = "other_gender";
    private static final String O_DESCRIPTION = "other_description";
    private static final Date O_BIRTH_DATE = null;
    private Date O_UPLOAD_DATE;
    private static final int O_PRICE = 100;
    private static final PetStatus O_STATUS = PetStatus.AVAILABLE;

    private static final int PAGE = 1;
    private static final int PAGE_SIZE = 50;

    @Autowired
    private DataSource ds;

    private PetJpaDaoImpl petDaoImpl;
    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcInsert jdbcInsertPet;
    private SimpleJdbcInsert jdbcInsertUser;
    private SimpleJdbcInsert jdbcInsertProvince;
    private SimpleJdbcInsert jdbcInsertDepartment;
    private SimpleJdbcInsert jdbcInsertSpecies;
    private SimpleJdbcInsert jdbcInsertBreed;
    private SimpleJdbcInsert jdbcInsertImage;

    @Before
    public void setUp() {
        petDaoImpl = new PetJpaDaoImpl();

        jdbcTemplate = new JdbcTemplate(ds);

        /* PET */
        jdbcInsertPet = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(PETS_TABLE)
                .usingGeneratedKeyColumns("id");
        /* USER */
        jdbcInsertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(USERS_TABLE)
                .usingGeneratedKeyColumns("id");
        /* SPECIES */
        jdbcInsertSpecies = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(SPECIES_TABLE)
                .usingGeneratedKeyColumns("id");
        /* BREED */
        jdbcInsertBreed = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(BREEDS_TABLE)
                .usingGeneratedKeyColumns("id");
        /* IMAGE */
        jdbcInsertImage = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(IMAGES_TABLE)
                .usingGeneratedKeyColumns("id");
        /* PROVINCE */
        jdbcInsertProvince = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(PROVINCES_TABLE)
                .usingGeneratedKeyColumns("id");
        /* DEPARTMENT */
        jdbcInsertDepartment = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(DEPARTMENTS_TABLE)
                .usingGeneratedKeyColumns("id");

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2001);
        cal.set(Calendar.MONTH, 2);
        cal.set(Calendar.DATE, 2);
        UPLOAD_DATE = new Date(cal.getTimeInMillis());

        setUpTablePetContext();
    }

    private void setUpTablePetContext() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, IMAGES_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, PETS_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, DEPARTMENTS_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, PROVINCES_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, BREEDS_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, SPECIES_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, USERS_TABLE);

        /* PROVINCE */
        final Map<String, Object> provincesValues = new HashMap<String, Object>() {{
            put("name", PROV_NAME);
            put("latitude", PROV_LAT);
            put("longitude", PROV_LON);
        }};
        Number p_id = jdbcInsertProvince.executeAndReturnKey(provincesValues);
        PROV_ID = p_id.longValue();
        PROVINCE = new Province(PROV_NAME, PROV_LAT, PROV_LON);
        PROVINCE.setId(PROV_ID);

        /* DEPARTMENT */
        final Map<String, Object> departmentValues = new HashMap<String, Object>() {{
            put("name", DEPT_NAME);
            put("latitude", DEPT_LAT);
            put("longitude", DEPT_LON);
            put("province", PROVINCE.getName());
        }};
        Number d_id = jdbcInsertDepartment.executeAndReturnKey(departmentValues);
        DEPT_ID = d_id.longValue();
        DEPARTMENT = new Department(DEPT_NAME, DEPT_LAT, DEPT_LON, PROVINCE);
        DEPARTMENT.setId(DEPT_ID);
        DEPARTMENT.setProvince(PROVINCE);

        /* OTHER PROVINCE */
        final Map<String, Object> o_provincesValues = new HashMap<String, Object>() {{
            put("name", O_PROV_NAME);
            put("latitude", O_PROV_LAT);
            put("longitude", O_PROV_LON);
        }};

        Number op_id = jdbcInsertProvince.executeAndReturnKey(o_provincesValues);
        O_PROV_ID = op_id.longValue();
        O_PROVINCE = new Province(O_PROV_NAME, O_PROV_LAT, O_PROV_LON);
        O_PROVINCE.setId(O_PROV_ID);

        /* OTHER DEPARTMENT */
        final Map<String, Object> o_departmentValues = new HashMap<String, Object>() {{
            put("name", O_DEPT_NAME);
            put("latitude", O_DEPT_LAT);
            put("longitude", O_DEPT_LON);
            put("province", O_PROVINCE.getName());
        }};
        Number od_id = jdbcInsertDepartment.executeAndReturnKey(o_departmentValues).longValue();
        O_DEPT_ID = od_id.longValue();
        O_DEPARTMENT = new Department(O_DEPT_NAME, O_DEPT_LAT, O_DEPT_LON, O_PROVINCE);
        O_DEPARTMENT.setId(O_DEPT_ID);
        O_DEPARTMENT.setProvince(O_PROVINCE);

        /* USER */
        final Map<String, Object> userValues = new HashMap<String, Object>() {{
            put("username", USERNAME);
            put("password", PASSWORD);
            put("mail", MAIL);
            put("status", USER_STATUS.ordinal());
            put("locale", LOCALE);
        }};
        Number u_id = jdbcInsertUser.executeAndReturnKey(userValues);
        USER_ID = u_id.longValue();
        USER = new User(USERNAME, PASSWORD, MAIL, USER_STATUS, LOCALE);
        USER.setId(USER_ID);

        /* OTHER USER */
        final Map<String, Object> o_userValues = new HashMap<String, Object>() {{
            put("username", O_USERNAME);
            put("password", O_PASSWORD);
            put("mail", O_MAIL);
            put("status", O_USER_STATUS.ordinal());
            put("locale", LOCALE);
        }};
        Number ou_id = jdbcInsertUser.executeAndReturnKey(o_userValues).longValue();
        O_USER_ID = ou_id.longValue();
        O_USER = new User(O_USERNAME, O_PASSWORD, O_MAIL, O_USER_STATUS, LOCALE);
        O_USER.setId(O_USER_ID);

        /* SPECIES */
        final Map<String, Object> speciesValues = new HashMap<String, Object>() {{
            put("es_ar", SPEC_ESAR);
            put("en_us", SPEC_ENUS);
        }};
        Number s_id = jdbcInsertSpecies.executeAndReturnKey(speciesValues).longValue();
        SPEC_ID = s_id.longValue();
        SPECIES = new Species(SPEC_ENUS, SPEC_ESAR);
        SPECIES.setId(SPEC_ID);

        /* BREED */
        final Map<String, Object> breedValues = new HashMap<String, Object>() {{
            put("speciesId", SPECIES.getId());
            put("es_ar", BREED_ESAR);
            put("en_us", BREED_ENUS);
        }};
        Number b_id = jdbcInsertBreed.executeAndReturnKey(breedValues).longValue();
        BREED_ID = b_id.longValue();
        BREED = new Breed(SPECIES, BREED_ENUS, BREED_ESAR);
        BREED.setId(BREED_ID);
        BREED.setSpecies(SPECIES);

        /* OTHER SPECIES */
        final Map<String, Object> o_speciesValues = new HashMap<String, Object>() {{
            put("es_ar", O_SPEC_ESAR);
            put("en_us", O_SPEC_ENUS);
        }};
        Number os_id = jdbcInsertSpecies.executeAndReturnKey(o_speciesValues).longValue();
        O_SPEC_ID = os_id.longValue();
        O_SPECIES = new Species(O_SPEC_ENUS, O_SPEC_ESAR);
        O_SPECIES.setId(O_SPEC_ID);

        /* OTHER BREED */
        final Map<String, Object> o_breedValues = new HashMap<String, Object>() {{
            put("speciesId", O_SPECIES.getId());
            put("es_ar", O_BREED_ESAR);
            put("en_us", O_BREED_ENUS);
        }};
        Number ob_id = jdbcInsertBreed.executeAndReturnKey(o_breedValues).longValue();
        O_BREED_ID = ob_id.longValue();
        O_BREED = new Breed(O_SPECIES, O_BREED_ENUS, O_BREED_ESAR);
        O_BREED.setId(O_BREED_ID);
        O_BREED.setSpecies(O_SPECIES);
    }

    private Pet insertPet(String petName, Date birthDate, String gender, boolean vaccinated, int price, Date uploadDate,
                           String description, PetStatus status, User user, Species species, Breed breed, Province province, Department department) {
        final Map<String, Object> petValues = new HashMap<String, Object>() {{
            put("petName", petName);
            put("species", species.getId());
            put("breed", breed.getId());
            put("vaccinated", vaccinated);
            put("gender", gender);
            put("description", description);
            put("birthDate", birthDate);
            put("uploadDate", uploadDate);
            put("price", price);
            put("ownerId", user.getId());
            put("status", status.ordinal());
            put("department", department.getId());
            put("province", province.getId());
        }};
        long id = jdbcInsertPet.executeAndReturnKey(petValues).longValue();
        Pet pet = new Pet(petName, birthDate, gender, vaccinated, price, uploadDate, description, status, user,
                species, breed, province, department);
        pet.setId(id);
        pet.setUser(user);
        pet.setSpecies(species);
        pet.setBreed(breed);
        pet.setProvince(province);
        pet.setDepartment(department);

        byte[] bytes = new byte[] {(byte) 0x3f};
        final Map<String, Object> imageValues = new HashMap<String, Object>() {{
            put("img", bytes);
            put("petId", id);
        }};
        jdbcInsertImage.execute(imageValues);

        return pet;
    }

    private void assertPet(Pet pet, long id, String petName, Date birthDate, String gender, boolean vaccinated, int price, Date uploadDate,
                           String description, PetStatus status, User user, Species species, Breed breed, Province province, Department department) {

        assertEquals(id, pet.getId().longValue());
        assertEquals(petName, pet.getPetName());
        assertEquals(species, pet.getSpecies());
        assertEquals(breed, pet.getBreed());
        assertEquals(vaccinated, pet.isVaccinated());
        assertEquals(gender, pet.getGender());
        assertEquals(description, pet.getDescription());
        assertDate(birthDate, pet.getBirthDate());
        assertDate(uploadDate, pet.getUploadDate());
        assertEquals(price, pet.getPrice());
        assertEquals(user, pet.getUser());
        assertEquals(status, pet.getStatus());
        assertEquals(department, pet.getDepartment());
        assertEquals(province, pet.getProvince());
    }

    private void assertDate(Date expected, Date actual) {
        assertTrue((expected == null && actual == null) ||
                expected != null && actual != null && expected.toString().equals(actual.toString()));
    }

    @Test
    public void testCreatePet() {
        Pet pet = petDaoImpl.create(PET_NAME, BIRTH_DATE, GENDER, VACCINATED, PRICE, UPLOAD_DATE, DESCRIPTION, STATUS,
                USER, SPECIES, BREED, PROVINCE, DEPARTMENT);

        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, PETS_TABLE));
        assertPet(pet, pet.getId(), PET_NAME, BIRTH_DATE, GENDER, VACCINATED, PRICE, UPLOAD_DATE, DESCRIPTION, STATUS,
                USER, SPECIES, BREED, PROVINCE, DEPARTMENT);
    }

    @Test
    public void testFindByIdNotExists() {
        Optional<Pet> opPet = petDaoImpl.findById(100);

        assertFalse(opPet.isPresent());
    }

    @Test
    public void testFindByIdExists() {
        Pet pet = insertPet(PET_NAME, BIRTH_DATE, GENDER, VACCINATED, PRICE, UPLOAD_DATE, DESCRIPTION, STATUS,
                USER, SPECIES, BREED, PROVINCE, DEPARTMENT);

        Optional<Pet> opPet = petDaoImpl.findById(pet.getId());

        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, PETS_TABLE));
        assertTrue(opPet.isPresent());
        assertPet(opPet.get(), pet.getId(), PET_NAME, BIRTH_DATE, GENDER, VACCINATED, PRICE, UPLOAD_DATE, DESCRIPTION, STATUS,
                USER, SPECIES, BREED, PROVINCE, DEPARTMENT);
    }

    @Test
    public void testFilteredListSpecies() {
        Pet pet = insertPet(PET_NAME, BIRTH_DATE, GENDER, VACCINATED, PRICE, UPLOAD_DATE, DESCRIPTION, STATUS,
                USER, SPECIES, BREED, PROVINCE, DEPARTMENT);
        insertPet(PET_NAME, BIRTH_DATE, GENDER, VACCINATED, PRICE, UPLOAD_DATE, DESCRIPTION, STATUS,
                USER, O_SPECIES, BREED, PROVINCE, DEPARTMENT);


        List<Pet> petList = petDaoImpl.filteredList(LOCALE, null, SPECIES, null, null,
                null, null, null, 0, -1, null, null,
                PAGE, PAGE_SIZE);

        assertEquals(1, petList.size());
        Pet petL = petList.get(0);
        assertPet(petL, pet.getId(), PET_NAME, BIRTH_DATE, GENDER, VACCINATED, PRICE, UPLOAD_DATE, DESCRIPTION, STATUS,
                USER, SPECIES, BREED, PROVINCE, DEPARTMENT);
    }

    @Test
    public void testFilteredListBreed() {
        Pet pet = insertPet(PET_NAME, BIRTH_DATE, GENDER, VACCINATED, PRICE, UPLOAD_DATE, DESCRIPTION, STATUS,
                USER, SPECIES, BREED, PROVINCE, DEPARTMENT);
        insertPet(PET_NAME, BIRTH_DATE, GENDER, VACCINATED, PRICE, UPLOAD_DATE, DESCRIPTION, STATUS,
                USER, O_SPECIES, O_BREED, PROVINCE, DEPARTMENT);


        List<Pet> petList = petDaoImpl.filteredList(LOCALE, null, SPECIES, BREED, null,
                null, null, null, 0, -1, null, null,
                PAGE, PAGE_SIZE);

        assertEquals(1, petList.size());
        Pet petL = petList.get(0);
        assertPet(petL, pet.getId(), PET_NAME, BIRTH_DATE, GENDER, VACCINATED, PRICE, UPLOAD_DATE, DESCRIPTION, STATUS,
                USER, SPECIES, BREED, PROVINCE, DEPARTMENT);
    }

    @Test
    public void testFilteredListGender() {
        Pet pet = insertPet(PET_NAME, BIRTH_DATE, GENDER, VACCINATED, PRICE, UPLOAD_DATE, DESCRIPTION, STATUS,
                USER, SPECIES, BREED, PROVINCE, DEPARTMENT);
        insertPet(PET_NAME, BIRTH_DATE, O_GENDER, VACCINATED, PRICE, UPLOAD_DATE, DESCRIPTION, STATUS,
                USER, SPECIES, BREED, PROVINCE, DEPARTMENT);


        List<Pet> petList = petDaoImpl.filteredList(LOCALE, null, null, null, GENDER,
                null, null, null, 0, -1, null, null,
                PAGE, PAGE_SIZE);

        assertEquals(1, petList.size());
        Pet petL = petList.get(0);
        assertPet(petL, pet.getId(), PET_NAME, BIRTH_DATE, GENDER, VACCINATED, PRICE, UPLOAD_DATE, DESCRIPTION, STATUS,
                USER, SPECIES, BREED, PROVINCE, DEPARTMENT);
    }
}
