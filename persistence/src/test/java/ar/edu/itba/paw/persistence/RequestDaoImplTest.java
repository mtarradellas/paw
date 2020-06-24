package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.constants.PetStatus;
import ar.edu.itba.paw.models.constants.RequestStatus;
import ar.edu.itba.paw.models.constants.UserStatus;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.PersistenceExceptionTranslationInterceptor;
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
public class RequestDaoImplTest {

    @Autowired
    private DataSource ds;

    /* TABLES */
    private static final String REQUESTS_TABLE = "requests";
    private static final String PETS_TABLE = "pets";
    private static final String USERS_TABLE = "users";
    private static final String PROVINCES_TABLE = "provinces";
    private static final String DEPARTMENTS_TABLE = "departments";
    private static final String SPECIES_TABLE = "species";
    private static final String BREEDS_TABLE = "breeds";
    private static final String IMAGES_TABLE = "images";

    private static final String LOCALE = "en_US";

    /* SPECIES */
    private static Species SPECIES;
    private static final Long SPEC_ID = 1L;
    private static final String SPEC_ESAR = "species";
    private static final String SPEC_ENUS = "species";

    /* BREED */
    private static Breed BREED;
    private static final Long BREED_ID = 1L;
    private static final String BREED_ESAR = "breed";
    private static final String BREED_ENUS = "breed";

    /* PROVINCE */
    private static Province PROVINCE;
    private static final Long PROV_ID = 1L;
    private static final String PROV_NAME = "province";
    private static final double PROV_LAT = 10;
    private static final double PROV_LON = 10;

    /* DEPARTMENT */
    private static Department DEPARTMENT;
    private static final Long DEPT_ID = 1L;
    private static final String DEPT_NAME = "department";
    private static final double DEPT_LAT = 10;
    private static final double DEPT_LON = 10;

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
    private static Pet PET;
    private static final Long PET_ID = 1L;
    private static final String PET_NAME = "petname";
    private static final Boolean VACCINATED = true;
    private static final String GENDER = "gender";
    private static final String DESCRIPTION = "description";
    private static final LocalDateTime BIRTH_DATE = LocalDateTime.now().minusMonths(1);
    private LocalDateTime UPLOAD_DATE = LocalDateTime.now();
    private static final int PRICE = 0;
    private static final PetStatus PET_STATUS = PetStatus.AVAILABLE;

    /* O_PET */
    private static Pet O_PET;
    private static final Long O_PET_ID = 2L;
    private static final String O_PET_NAME = "petname";

    /* REQUEST */
    private static final Long REQ_ID = 1L;
    private static final RequestStatus STATUS = RequestStatus.PENDING;
    private static final LocalDateTime UPDATE_DATE = LocalDateTime.now();

    private static final int PAGE = 1;
    private static final int PAGE_SIZE = 50;


    @Autowired
    private RequestJpaDaoImpl requestDao;

    @PersistenceContext
    private EntityManager em;

    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcInsert jdbcInsertRequest;
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

        /* REQUEST */
        jdbcInsertRequest = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(REQUESTS_TABLE);

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

        setUpTableContext();
    }

    private void setUpTableContext() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, IMAGES_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, REQUESTS_TABLE);
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

        /* PET */
        final Map<String, Object> petValues = new HashMap<>();
        petValues.put("id", PET_ID);
        petValues.put("petName", PET_NAME);
        petValues.put("species", SPECIES.getId());
        petValues.put("breed", BREED.getId());
        petValues.put("vaccinated", VACCINATED);
        petValues.put("gender", GENDER);
        petValues.put("description", DESCRIPTION);
        petValues.put("birthDate", Timestamp.valueOf(BIRTH_DATE));
        petValues.put("uploadDate", Timestamp.valueOf(UPLOAD_DATE));
        petValues.put("price", PRICE);
        petValues.put("ownerId", O_USER.getId());
        petValues.put("status", STATUS.ordinal());
        petValues.put("department", DEPARTMENT.getId());
        petValues.put("province", PROVINCE.getId());
        jdbcInsertPet.execute(petValues);
        PET = new Pet(PET_NAME, BIRTH_DATE, GENDER, VACCINATED, PRICE, UPLOAD_DATE, DESCRIPTION, PET_STATUS, O_USER,
                SPECIES, BREED, PROVINCE, DEPARTMENT);
        PET.setId(PET_ID);

        /* O_PET */
        final Map<String, Object> o_petValues = new HashMap<>();
        o_petValues.put("id", O_PET_ID);
        o_petValues.put("petName", O_PET_NAME);
        o_petValues.put("species", SPECIES.getId());
        o_petValues.put("breed", BREED.getId());
        o_petValues.put("vaccinated", VACCINATED);
        o_petValues.put("gender", GENDER);
        o_petValues.put("description", DESCRIPTION);
        o_petValues.put("birthDate", Timestamp.valueOf(BIRTH_DATE));
        o_petValues.put("uploadDate", Timestamp.valueOf(UPLOAD_DATE));
        o_petValues.put("price", PRICE);
        o_petValues.put("ownerId", USER.getId());
        o_petValues.put("status", STATUS.ordinal());
        o_petValues.put("department", DEPARTMENT.getId());
        o_petValues.put("province", PROVINCE.getId());
        jdbcInsertPet.execute(o_petValues);
        O_PET = new Pet(O_PET_NAME, BIRTH_DATE, GENDER, VACCINATED, PRICE, UPLOAD_DATE, DESCRIPTION, PET_STATUS, USER,
                SPECIES, BREED, PROVINCE, DEPARTMENT);
        O_PET.setId(O_PET_ID);
    }

    private void indexTables() {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);
        try {
            fullTextEntityManager.createIndexer().startAndWait();
        } catch(InterruptedException ignored) {}
    }

    private Request insertRequest(long id, LocalDateTime creationDate, RequestStatus status, Pet pet, User user, User target, LocalDateTime updateDate) {
        final Map<String, Object> reqValues = new HashMap<>();
        reqValues.put("id", id);
        reqValues.put("creationDate", Timestamp.valueOf(creationDate));
        reqValues.put("updateDate", Timestamp.valueOf(updateDate));
        reqValues.put("status", status.ordinal());
        reqValues.put("ownerId", user.getId());
        reqValues.put("targetId", target.getId());
        reqValues.put("petId", pet.getId());
        jdbcInsertRequest.execute(reqValues);
        Request request = new Request(creationDate, status, user, target, pet);
        request.setId(id);
        return request;
    }

    private void assertRequest(Request request, long id, LocalDateTime creationDate, RequestStatus status) {
        assertEquals(id, request.getId().longValue());
        assertEquals(creationDate, request.getCreationDate());
        assertEquals(status, request.getStatus());
    }

    @Test
    public void testFindByIdNotExists() {
        Optional<Request> opReq = requestDao.findById(100);

        assertFalse(opReq.isPresent());
    }

    @Test
    @Transactional
    public void testFindByIdExists() {
        Request request = insertRequest(REQ_ID, UPLOAD_DATE, STATUS, PET, USER, O_USER, UPDATE_DATE);

        Optional<Request> opReq = requestDao.findById(request.getId());

        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, REQUESTS_TABLE));
        assertTrue(opReq.isPresent());
        assertRequest(opReq.get(), request.getId(), UPLOAD_DATE, STATUS);
    }

    @Test
    @Transactional
    public void testCreate() {
        Request request = requestDao.create(USER, PET, STATUS, UPLOAD_DATE);
        try {
            em.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("Create request failed");
        }

        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, REQUESTS_TABLE));
        assertRequest(request, request.getId(), UPLOAD_DATE, STATUS);
    }

    @Test
    public void testSearchListUser() {
        Request request = insertRequest(REQ_ID, UPLOAD_DATE, STATUS, PET, USER, O_USER, UPDATE_DATE);
        insertRequest(REQ_ID+1, UPLOAD_DATE, STATUS, O_PET, O_USER, USER, UPDATE_DATE);
        indexTables();

        List<Request> requestList = requestDao.searchList(USER, null, null, null, null, null, PAGE, PAGE_SIZE);

        assertEquals(1, requestList.size());
        Request reqL = requestList.get(0);
        assertRequest(reqL, request.getId(), UPLOAD_DATE, STATUS);
    }

    @Test
    public void testSearchListPet() {
        Request request = insertRequest(REQ_ID, UPLOAD_DATE, STATUS, PET, USER, O_USER, UPDATE_DATE);
        insertRequest(REQ_ID+1, UPLOAD_DATE, STATUS, O_PET, O_USER, USER, UPDATE_DATE);
        indexTables();

        List<Request> requestList = requestDao.searchList(null, PET, null, null, null, null, PAGE, PAGE_SIZE);

        assertEquals(1, requestList.size());
        Request reqL = requestList.get(0);
        assertRequest(reqL, request.getId(), UPLOAD_DATE, STATUS);
    }

    @Test
    public void testSearchListStatus() {
        Request request = insertRequest(REQ_ID, UPLOAD_DATE, STATUS, PET, USER, O_USER, UPDATE_DATE);
        insertRequest(REQ_ID+1, UPLOAD_DATE, RequestStatus.CANCELED, O_PET, O_USER, USER, UPDATE_DATE);
        indexTables();

        List<Request> requestList = requestDao.searchList(null, null, null, STATUS, null, null, PAGE, PAGE_SIZE);

        assertEquals(1, requestList.size());
        Request reqL = requestList.get(0);
        assertRequest(reqL, request.getId(), UPLOAD_DATE, STATUS);
    }

    @Test
    public void testSearchListByPetOwner() {
        Request request = insertRequest(REQ_ID, UPLOAD_DATE, STATUS, PET, USER, O_USER, UPDATE_DATE);
        insertRequest(REQ_ID+1, UPLOAD_DATE, RequestStatus.CANCELED, O_PET, O_USER, USER, UPDATE_DATE);
        indexTables();

        List<Request> requestList = requestDao.searchListByPetOwner(O_USER, null, null, null, null, null, PAGE, PAGE_SIZE);

        assertEquals(1, requestList.size());
        Request reqL = requestList.get(0);
        assertRequest(reqL, request.getId(), UPLOAD_DATE, STATUS);
    }

    @Test
    @Transactional
    public void testUpdateByStatusAndUser() {
        insertRequest(REQ_ID, UPLOAD_DATE, RequestStatus.PENDING, PET, USER, O_USER, UPDATE_DATE);

        requestDao.updateByStatusAndUser(USER, RequestStatus.PENDING, RequestStatus.ACCEPTED);

        Optional<Request> updated = jdbcTemplate.query("SELECT * FROM REQUESTS WHERE id = " + REQ_ID, (r, rowNum) ->
                new Request(UPLOAD_DATE, RequestStatus.values()[r.getInt("status")],
                            USER, O_USER, PET)).stream().findFirst();

        assertTrue(updated.isPresent());
        assertEquals(updated.get().getStatus(), RequestStatus.ACCEPTED);
    }

    @Test
    @Transactional
    public void testUpdateByStatusAndPetOwner() {
        insertRequest(REQ_ID, UPLOAD_DATE, RequestStatus.PENDING, PET, USER, O_USER, UPDATE_DATE);

        requestDao.updateByStatusAndPetOwner(O_USER, RequestStatus.PENDING, RequestStatus.ACCEPTED);

        Optional<Request> updated = jdbcTemplate.query("SELECT * FROM REQUESTS WHERE id = " + REQ_ID, (r, rowNum) ->
                new Request(UPLOAD_DATE, RequestStatus.values()[r.getInt("status")],
                        USER, O_USER, PET)).stream().findFirst();

        assertTrue(updated.isPresent());
        assertEquals(updated.get().getStatus(), RequestStatus.ACCEPTED);
    }

    @Test
    @Transactional
    public void testUpdateByStatusAndPet() {
        insertRequest(REQ_ID, UPLOAD_DATE, RequestStatus.PENDING, PET, USER, O_USER, UPDATE_DATE);

        requestDao.updateByStatusAndPet(PET, RequestStatus.PENDING, RequestStatus.ACCEPTED);

        Optional<Request> updated = jdbcTemplate.query("SELECT * FROM REQUESTS WHERE id = " + REQ_ID, (r, rowNum) ->
                new Request(UPLOAD_DATE, RequestStatus.values()[r.getInt("status")],
                        USER, O_USER, PET)).stream().findFirst();

        assertTrue(updated.isPresent());
        assertEquals(updated.get().getStatus(), RequestStatus.ACCEPTED);
    }
}
