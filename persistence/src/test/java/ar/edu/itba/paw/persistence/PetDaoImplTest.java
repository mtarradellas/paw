package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.constants.PetStatus;
import ar.edu.itba.paw.models.constants.UserStatus;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
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
    private static final Long SPEC_ID = 1L;
    private static final String SPEC_ESAR = "species";
    private static final String SPEC_ENUS = "species";

    /* OTHER SPECIES */
    private static Species O_SPECIES;
    private static final Long O_SPEC_ID = 2L;
    private static final String O_SPEC_ESAR = "other_species";
    private static final String O_SPEC_ENUS = "other_species";

    /* BREED */
    private static Breed BREED;
    private static final Long BREED_ID = 1L;
    private static final String BREED_ESAR = "breed";
    private static final String BREED_ENUS = "breed";

    /* OTHER BREED */
    private static Breed O_BREED;
    private static final Long O_BREED_ID = 2L;
    private static final String O_BREED_ESAR = "other_breed";
    private static final String O_BREED_ENUS = "other_breed";

    /* PROVINCE */
    private static Province PROVINCE;
    private static final Long PROV_ID = 1L;
    private static final String PROV_NAME = "province";
    private static final double PROV_LAT = 10;
    private static final double PROV_LON = 10;

    /* OTHER PROVINCE */
    private static Province O_PROVINCE;
    private static final Long O_PROV_ID = 2L;
    private static final String O_PROV_NAME = "another";
    private static final double O_PROV_LAT = 50;
    private static final double O_PROV_LON = 50;

    /* DEPARTMENT */
    private static Department DEPARTMENT;
    private static final Long DEPT_ID = 1L;
    private static final String DEPT_NAME = "department";
    private static final double DEPT_LAT = 10;
    private static final double DEPT_LON = 10;

    /* OTHER DEPARTMENT */
    private static Department O_DEPARTMENT;
    private static final Long O_DEPT_ID = 2L;
    private static final String O_DEPT_NAME = "other_department";
    private static final double O_DEPT_LAT = 50;
    private static final double O_DEPT_LON = 50;

    /* USER */
    private static User USER;
    private static final Long USER_ID = 1L;
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String MAIL = "user@mail.com";
    private static final UserStatus USER_STATUS = UserStatus.ACTIVE;

    /* OTHER USER */
    private static User O_USER;
    private static final Long O_USER_ID = 2L;
    private static final String O_USERNAME = "other_username";
    private static final String O_PASSWORD = "other_password";
    private static final String O_MAIL = "other_user@mail.com";
    private static final UserStatus O_USER_STATUS = UserStatus.ACTIVE;

    /* PET */
    private static final Long PET_ID = 1L;
    private static final String PET_NAME = "petname";
    private static final Boolean VACCINATED = true;
    private static final String GENDER = "gender";
    private static final String DESCRIPTION = "description";
    private static final LocalDateTime BIRTH_DATE = LocalDateTime.now().minusMonths(1);
    private LocalDateTime UPLOAD_DATE = LocalDateTime.now();
    private static final int PRICE = 0;
    private static final PetStatus STATUS = PetStatus.AVAILABLE;

    /* OTHER PET */
    private static final Long O_PET_ID = 2L;
    private static final String O_GENDER = "another";
    private static final int O_PRICE = 100;
    private static final PetStatus O_STATUS = PetStatus.UNAVAILABLE;

    private int IMG_ID = 1;
    private static final int PAGE = 1;
    private static final int PAGE_SIZE = 50;

    @Autowired
    private DataSource ds;

    @PersistenceContext
    private EntityManager em;

    @Autowired
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
        jdbcTemplate = new JdbcTemplate(ds);

        /* PET */
        jdbcInsertPet = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(PETS_TABLE);
        /* USER */
        jdbcInsertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(USERS_TABLE);
        /* SPECIES */
        jdbcInsertSpecies = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(SPECIES_TABLE);
        /* BREED */
        jdbcInsertBreed = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(BREEDS_TABLE);
        /* IMAGE */
        jdbcInsertImage = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(IMAGES_TABLE);
        /* PROVINCE */
        jdbcInsertProvince = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(PROVINCES_TABLE);
        /* DEPARTMENT */
        jdbcInsertDepartment = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(DEPARTMENTS_TABLE);

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
        final Map<String, Object> provincesValues = new HashMap<>();
        provincesValues.put("id", PROV_ID);
        provincesValues.put("name", PROV_NAME);
        provincesValues.put("latitude", PROV_LAT);
        provincesValues.put("longitude", PROV_LON);
        jdbcInsertProvince.execute(provincesValues);
        PROVINCE = new Province(PROV_NAME, PROV_LAT, PROV_LON);
        PROVINCE.setId(PROV_ID);

        /* DEPARTMENT */
        final Map<String, Object> departmentValues = new HashMap<>();
        departmentValues.put("id", DEPT_ID);
        departmentValues.put("name", DEPT_NAME);
        departmentValues.put("latitude", DEPT_LAT);
        departmentValues.put("longitude", DEPT_LON);
        departmentValues.put("province", PROVINCE.getName());
        jdbcInsertDepartment.execute(departmentValues);
        DEPARTMENT = new Department(DEPT_NAME, DEPT_LAT, DEPT_LON, PROVINCE);
        DEPARTMENT.setId(DEPT_ID);
        DEPARTMENT.setProvince(PROVINCE);

        /* OTHER PROVINCE */
        final Map<String, Object> o_provincesValues = new HashMap<>();
        o_provincesValues.put("id", O_PROV_ID);
        o_provincesValues.put("name", O_PROV_NAME);
        o_provincesValues.put("latitude", O_PROV_LAT);
        o_provincesValues.put("longitude", O_PROV_LON);
        jdbcInsertProvince.execute(o_provincesValues);
        O_PROVINCE = new Province(O_PROV_NAME, O_PROV_LAT, O_PROV_LON);
        O_PROVINCE.setId(O_PROV_ID);

        /* OTHER DEPARTMENT */
        final Map<String, Object> o_departmentValues = new HashMap<>();
        o_departmentValues.put("id", O_DEPT_ID);
        o_departmentValues.put("name", O_DEPT_NAME);
        o_departmentValues.put("latitude", O_DEPT_LAT);
        o_departmentValues.put("longitude", O_DEPT_LON);
        o_departmentValues.put("province", O_PROVINCE.getName());
        jdbcInsertDepartment.execute(o_departmentValues);
        O_DEPARTMENT = new Department(O_DEPT_NAME, O_DEPT_LAT, O_DEPT_LON, O_PROVINCE);
        O_DEPARTMENT.setId(O_DEPT_ID);
        O_DEPARTMENT.setProvince(O_PROVINCE);

        /* USER */
        final Map<String, Object> userValues = new HashMap<>();
        userValues.put("id", USER_ID);
        userValues.put("username", USERNAME);
        userValues.put("password", PASSWORD);
        userValues.put("mail", MAIL);
        userValues.put("status", USER_STATUS.ordinal());
        userValues.put("locale", LOCALE);
        jdbcInsertUser.execute(userValues);
        USER = new User(USERNAME, PASSWORD, MAIL, USER_STATUS, LOCALE);
        USER.setId(USER_ID);

        /* OTHER USER */
        final Map<String, Object> o_userValues = new HashMap<>();
        o_userValues.put("id", O_USER_ID);
        o_userValues.put("username", O_USERNAME);
        o_userValues.put("password", O_PASSWORD);
        o_userValues.put("mail", O_MAIL);
        o_userValues.put("status", O_USER_STATUS.ordinal());
        o_userValues.put("locale", LOCALE);
        jdbcInsertUser.execute(o_userValues);
        O_USER = new User(O_USERNAME, O_PASSWORD, O_MAIL, O_USER_STATUS, LOCALE);
        O_USER.setId(O_USER_ID);

        /* SPECIES */
        final Map<String, Object> speciesValues = new HashMap<>();
        speciesValues.put("id", SPEC_ID);
        speciesValues.put("es_ar", SPEC_ESAR);
        speciesValues.put("en_us", SPEC_ENUS);
        jdbcInsertSpecies.execute(speciesValues);
        SPECIES = new Species(SPEC_ENUS, SPEC_ESAR);
        SPECIES.setId(SPEC_ID);

        /* BREED */
        final Map<String, Object> breedValues = new HashMap<>();
        breedValues.put("id", BREED_ID);
        breedValues.put("speciesId", SPECIES.getId());
        breedValues.put("es_ar", BREED_ESAR);
        breedValues.put("en_us", BREED_ENUS);
        jdbcInsertBreed.execute(breedValues);
        BREED = new Breed(SPECIES, BREED_ENUS, BREED_ESAR);
        BREED.setId(BREED_ID);
        BREED.setSpecies(SPECIES);

        /* OTHER SPECIES */
        final Map<String, Object> o_speciesValues = new HashMap<>();
        o_speciesValues.put("id", O_SPEC_ID);
        o_speciesValues.put("es_ar", O_SPEC_ESAR);
        o_speciesValues.put("en_us", O_SPEC_ENUS);
        jdbcInsertSpecies.execute(o_speciesValues);
        O_SPECIES = new Species(O_SPEC_ENUS, O_SPEC_ESAR);
        O_SPECIES.setId(O_SPEC_ID);

        /* OTHER BREED */
        final Map<String, Object> o_breedValues = new HashMap<>();
            o_breedValues.put("id", O_BREED_ID);
            o_breedValues.put("speciesId", O_SPECIES.getId());
            o_breedValues.put("es_ar", O_BREED_ESAR);
            o_breedValues.put("en_us", O_BREED_ENUS);
        jdbcInsertBreed.execute(o_breedValues);
        O_BREED = new Breed(O_SPECIES, O_BREED_ENUS, O_BREED_ESAR);
        O_BREED.setId(O_BREED_ID);
        O_BREED.setSpecies(O_SPECIES);
    }

    private void indexTables() {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);
        try {
            fullTextEntityManager.createIndexer().startAndWait();
        } catch(InterruptedException ignored) {}
    }

    private Pet insertPet(long id, String petName, LocalDateTime birthDate, String gender, boolean vaccinated, int price, LocalDateTime uploadDate,
                           String description, PetStatus status, User user, Species species, Breed breed, Province province, Department department) {
        final Map<String, Object> petValues = new HashMap<>();
        petValues.put("id", id);
        petValues.put("petName", petName);
        petValues.put("species", species.getId());
        petValues.put("breed", breed.getId());
        petValues.put("vaccinated", vaccinated);
        petValues.put("gender", gender);
        petValues.put("description", description);
        petValues.put("birthDate", Timestamp.valueOf(birthDate));
        petValues.put("uploadDate", Timestamp.valueOf(uploadDate));
        petValues.put("price", price);
        petValues.put("ownerId", user.getId());
        petValues.put("status", status.ordinal());
        petValues.put("department", department.getId());
        petValues.put("province", province.getId());
        jdbcInsertPet.execute(petValues);
        Pet pet = new Pet(petName, birthDate, gender, vaccinated, price, uploadDate, description, status, user,
                species, breed, province, department);
        pet.setId(id);

        byte[] bytes = new byte[] {(byte) 0x3f};
        final Map<String, Object> imageValues = new HashMap<>();
        imageValues.put("id", IMG_ID++);
        imageValues.put("img", bytes);
        imageValues.put("petId", id);
        jdbcInsertImage.execute(imageValues);

        return pet;
    }

    private void assertPet(Pet pet, long id, String petName, LocalDateTime birthDate, String gender, boolean vaccinated, int price, LocalDateTime uploadDate,
                           String description, PetStatus status, User user, Species species, Breed breed, Province province, Department department) {

        assertEquals(id, pet.getId().longValue());
        assertEquals(petName, pet.getPetName());
        assertEquals(species.getId(), pet.getSpecies().getId());
        assertEquals(breed.getId(), pet.getBreed().getId());
        assertEquals(vaccinated, pet.isVaccinated());
        assertEquals(gender, pet.getGender());
        assertEquals(description, pet.getDescription());
        assertDate(birthDate, pet.getBirthDate());
        assertDate(uploadDate, pet.getUploadDate());
        assertEquals(price, pet.getPrice());
        assertEquals(status, pet.getStatus());
    }

    private void assertDate(LocalDateTime expected, LocalDateTime actual) {
        assertTrue((expected == null && actual == null) || (expected != null && actual != null));
        if (expected != null) assertEquals(expected, actual);
    }

    @Test
    @Transactional
    public void testCreatePet() {
        Pet pet = petDaoImpl.create(PET_NAME, BIRTH_DATE, GENDER, VACCINATED, PRICE, UPLOAD_DATE, DESCRIPTION, STATUS,
                USER, SPECIES, BREED, PROVINCE, DEPARTMENT);

        try {
            em.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("Create Pet failed");
        }
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
    @Transactional
    public void testFindByIdExists() {
        Pet pet = insertPet(PET_ID, PET_NAME, BIRTH_DATE, GENDER, VACCINATED, PRICE, UPLOAD_DATE, DESCRIPTION, STATUS,
                USER, SPECIES, BREED, PROVINCE, DEPARTMENT);

        Optional<Pet> opPet = petDaoImpl.findById(pet.getId());

        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, PETS_TABLE));
        assertTrue(opPet.isPresent());
        assertPet(opPet.get(), pet.getId(), PET_NAME, BIRTH_DATE, GENDER, VACCINATED, PRICE, UPLOAD_DATE, DESCRIPTION, STATUS,
                USER, SPECIES, BREED, PROVINCE, DEPARTMENT);
    }

    @Test
    public void testFilteredListSpecies() {
        Pet pet = insertPet(PET_ID, PET_NAME, BIRTH_DATE, GENDER, VACCINATED, PRICE, UPLOAD_DATE, DESCRIPTION, STATUS,
                USER, SPECIES, BREED, PROVINCE, DEPARTMENT);
        insertPet(O_PET_ID, PET_NAME, BIRTH_DATE, GENDER, VACCINATED, PRICE, UPLOAD_DATE, DESCRIPTION, STATUS,
                USER, O_SPECIES, BREED, PROVINCE, DEPARTMENT);
        indexTables();

        List<Pet> petList = petDaoImpl.searchList(LOCALE, null, null, null,  SPECIES, null, null,
                null, null, null, 0, -1, null, null,
                PAGE, PAGE_SIZE);

        assertEquals(1, petList.size());
        Pet petL = petList.get(0);
        assertPet(petL, pet.getId(), PET_NAME, BIRTH_DATE, GENDER, VACCINATED, PRICE, UPLOAD_DATE, DESCRIPTION, STATUS,
                USER, SPECIES, BREED, PROVINCE, DEPARTMENT);
    }

    @Test
    public void testFilteredListBreed() {
        Pet pet = insertPet(PET_ID, PET_NAME, BIRTH_DATE, GENDER, VACCINATED, PRICE, UPLOAD_DATE, DESCRIPTION, STATUS,
                USER, SPECIES, BREED, PROVINCE, DEPARTMENT);
        insertPet(O_PET_ID, PET_NAME, BIRTH_DATE, GENDER, VACCINATED, PRICE, UPLOAD_DATE, DESCRIPTION, STATUS,
                USER, O_SPECIES, O_BREED, PROVINCE, DEPARTMENT);
        indexTables();

        List<Pet> petList = petDaoImpl.searchList(LOCALE, null, null, null, SPECIES, BREED, null,
                null, null, null, 0, -1, null, null,
                PAGE, PAGE_SIZE);

        assertEquals(1, petList.size());
        Pet petL = petList.get(0);
        assertPet(petL, pet.getId(), PET_NAME, BIRTH_DATE, GENDER, VACCINATED, PRICE, UPLOAD_DATE, DESCRIPTION, STATUS,
                USER, SPECIES, BREED, PROVINCE, DEPARTMENT);
    }

    @Test
    public void testFilteredListGender() {
        Pet pet = insertPet(PET_ID, PET_NAME, BIRTH_DATE, GENDER, VACCINATED, PRICE, UPLOAD_DATE, DESCRIPTION, STATUS,
                USER, SPECIES, BREED, PROVINCE, DEPARTMENT);
        insertPet(O_PET_ID, PET_NAME, BIRTH_DATE, O_GENDER, VACCINATED, PRICE, UPLOAD_DATE, DESCRIPTION, STATUS,
                USER, SPECIES, BREED, PROVINCE, DEPARTMENT);
        indexTables();

        List<Pet> petList = petDaoImpl.searchList(LOCALE, null, null, null, null, null, GENDER,
                null, null, null, 0, -1, null, null,
                PAGE, PAGE_SIZE);

        assertEquals(1, petList.size());
        Pet petL = petList.get(0);
        assertPet(petL, pet.getId(), PET_NAME, BIRTH_DATE, GENDER, VACCINATED, PRICE, UPLOAD_DATE, DESCRIPTION, STATUS,
                USER, SPECIES, BREED, PROVINCE, DEPARTMENT);
    }

    @Test
    public void testFilteredListStatus() {
        Pet pet = insertPet(PET_ID, PET_NAME, BIRTH_DATE, GENDER, VACCINATED, PRICE, UPLOAD_DATE, DESCRIPTION, STATUS,
                USER, SPECIES, BREED, PROVINCE, DEPARTMENT);
        insertPet(O_PET_ID, PET_NAME, BIRTH_DATE, GENDER, VACCINATED, PRICE, UPLOAD_DATE, DESCRIPTION, O_STATUS,
                USER, SPECIES, BREED, PROVINCE, DEPARTMENT);
        indexTables();

        List<Pet> petList = petDaoImpl.searchList(LOCALE, null, null, null, null, null, null,
                STATUS, null, null, 0, -1, null, null,
                PAGE, PAGE_SIZE);

        assertEquals(1, petList.size());
        Pet petL = petList.get(0);
        assertPet(petL, pet.getId(), PET_NAME, BIRTH_DATE, GENDER, VACCINATED, PRICE, UPLOAD_DATE, DESCRIPTION, STATUS,
                USER, SPECIES, BREED, PROVINCE, DEPARTMENT);
    }

    @Test
    public void testFilteredListMinPrice() {
        Pet pet = insertPet(PET_ID, PET_NAME, BIRTH_DATE, GENDER, VACCINATED, O_PRICE, UPLOAD_DATE, DESCRIPTION, STATUS,
                USER, SPECIES, BREED, PROVINCE, DEPARTMENT);
        insertPet(O_PET_ID, PET_NAME, BIRTH_DATE, GENDER, VACCINATED, PRICE, UPLOAD_DATE, DESCRIPTION, STATUS,
                USER, SPECIES, BREED, PROVINCE, DEPARTMENT);
        indexTables();

        List<Pet> petList = petDaoImpl.searchList(LOCALE, null, null,  null,null, null, null,
                null, null, null, O_PRICE, -1, null, null,
                PAGE, PAGE_SIZE);

        assertEquals(1, petList.size());
        Pet petL = petList.get(0);
        assertPet(petL, pet.getId(), PET_NAME, BIRTH_DATE, GENDER, VACCINATED, O_PRICE, UPLOAD_DATE, DESCRIPTION, STATUS,
                USER, SPECIES, BREED, PROVINCE, DEPARTMENT);
    }

    @Test
    public void testFilteredListMaxPrice() {
        Pet pet = insertPet(PET_ID, PET_NAME, BIRTH_DATE, GENDER, VACCINATED, PRICE, UPLOAD_DATE, DESCRIPTION, STATUS,
                USER, SPECIES, BREED, PROVINCE, DEPARTMENT);
        insertPet(O_PET_ID, PET_NAME, BIRTH_DATE, GENDER, VACCINATED, O_PRICE, UPLOAD_DATE, DESCRIPTION, STATUS,
                USER, SPECIES, BREED, PROVINCE, DEPARTMENT);
        indexTables();

        List<Pet> petList = petDaoImpl.searchList(LOCALE, null, null, null,null, null, null,
                null, null, null, 0, PRICE, null, null,
                PAGE, PAGE_SIZE);

        assertEquals(1, petList.size());
        Pet petL = petList.get(0);
        assertPet(petL, pet.getId(), PET_NAME, BIRTH_DATE, GENDER, VACCINATED, PRICE, UPLOAD_DATE, DESCRIPTION, STATUS,
                USER, SPECIES, BREED, PROVINCE, DEPARTMENT);
    }

    @Test
    public void testFilteredListProvince() {
        Pet pet = insertPet(PET_ID, PET_NAME, BIRTH_DATE, GENDER, VACCINATED, PRICE, UPLOAD_DATE, DESCRIPTION, STATUS,
                USER, SPECIES, BREED, PROVINCE, DEPARTMENT);
        insertPet(O_PET_ID, PET_NAME, BIRTH_DATE, GENDER, VACCINATED, PRICE, UPLOAD_DATE, DESCRIPTION, STATUS,
                USER, SPECIES, BREED, O_PROVINCE, O_DEPARTMENT);
        indexTables();

        List<Pet> petList = petDaoImpl.searchList(LOCALE, null, null, null, null, null, null,
                null, null, null, 0, -1, PROVINCE, null,
                PAGE, PAGE_SIZE);

        assertEquals(1, petList.size());
        Pet petL = petList.get(0);
        assertPet(petL, pet.getId(), PET_NAME, BIRTH_DATE, GENDER, VACCINATED, PRICE, UPLOAD_DATE, DESCRIPTION, STATUS,
                USER, SPECIES, BREED, PROVINCE, DEPARTMENT);
    }

    @Test
    public void testFilteredListDepartment() {
        Pet pet = insertPet(PET_ID, PET_NAME, BIRTH_DATE, GENDER, VACCINATED, PRICE, UPLOAD_DATE, DESCRIPTION, STATUS,
                USER, SPECIES, BREED, PROVINCE, DEPARTMENT);
        insertPet(O_PET_ID, PET_NAME, BIRTH_DATE, GENDER, VACCINATED, PRICE, UPLOAD_DATE, DESCRIPTION, STATUS,
                USER, SPECIES, BREED, O_PROVINCE, O_DEPARTMENT);
        indexTables();

        List<Pet> petList = petDaoImpl.searchList(LOCALE, null, null, null,null, null, null,
                null, null, null, 0, -1, PROVINCE, DEPARTMENT,
                PAGE, PAGE_SIZE);

        assertEquals(1, petList.size());
        Pet petL = petList.get(0);
        assertPet(petL, pet.getId(), PET_NAME, BIRTH_DATE, GENDER, VACCINATED, PRICE, UPLOAD_DATE, DESCRIPTION, STATUS,
                USER, SPECIES, BREED, PROVINCE, DEPARTMENT);
    }

    /* TODO questions */
}
