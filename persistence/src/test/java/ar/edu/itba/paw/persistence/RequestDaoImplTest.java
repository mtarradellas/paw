//package ar.edu.itba.paw.persistence;
//
//import ar.edu.itba.paw.models.*;
//import ar.edu.itba.paw.models.constants.PetStatus;
//import ar.edu.itba.paw.models.constants.RequestStatus;
//import ar.edu.itba.paw.models.constants.UserStatus;
//import org.junit.Before;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.jdbc.JdbcTestUtils;
//
//import javax.sql.DataSource;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = TestConfig.class)
//public class RequestDaoImplTest {
//
//    @Autowired
//    private DataSource ds;
//
//    /* TABLES */
//    private static final String REQUESTS_TABLE = "requests";
//    private static final String PETS_TABLE = "pets";
//    private static final String USERS_TABLE = "users";
//    private static final String PROVINCES_TABLE = "provinces";
//    private static final String DEPARTMENTS_TABLE = "departments";
//    private static final String SPECIES_TABLE = "species";
//    private static final String BREEDS_TABLE = "breeds";
//    private static final String IMAGES_TABLE = "images";
//
//    private static final String LOCALE = "en_US";
//
//    /* SPECIES */
//    private static Species SPECIES;
//    private static Long SPEC_ID;
//    private static final String SPEC_ESAR = "species";
//    private static final String SPEC_ENUS = "species";
//
//    /* BREED */
//    private static Breed BREED;
//    private static Long BREED_ID;
//    private static final String BREED_ESAR = "breed";
//    private static final String BREED_ENUS = "breed";
//
//    /* PROVINCE */
//    private static Province PROVINCE;
//    private static Long PROV_ID;
//    private static final String PROV_NAME = "province";
//    private static final double PROV_LAT = 10;
//    private static final double PROV_LON = 10;
//
//    /* DEPARTMENT */
//    private static Department DEPARTMENT;
//    private static Long DEPT_ID;
//    private static final String DEPT_NAME = "department";
//    private static final double DEPT_LAT = 10;
//    private static final double DEPT_LON = 10;
//
//    /* USER */
//    private static User USER;
//    private static Long USER_ID;
//    private static final String USERNAME = "username";
//    private static final String PASSWORD = "password";
//    private static final String MAIL = "user@mail.com";
//    private static final UserStatus USER_STATUS = UserStatus.ACTIVE;
//
//    /* OTHER USER */
//    private static User O_USER;
//    private static Long O_USER_ID;
//    private static final String O_USERNAME = "other_username";
//    private static final String O_PASSWORD = "other_password";
//    private static final String O_MAIL = "other_user@mail.com";
//    private static final UserStatus O_USER_STATUS = UserStatus.ACTIVE;
//
//    /* PET */
//    private static Long PET_ID;
//    private static final String PET_NAME = "petname";
//    private static final Boolean VACCINATED = true;
//    private static final String GENDER = "gender";
//    private static final String DESCRIPTION = "description";
//    private static final Date BIRTH_DATE = null;
//    private Date UPLOAD_DATE;
//    private static final int PRICE = 0;
//    private static final PetStatus PET_STATUS = PetStatus.AVAILABLE;
//
//    /* REQUEST */
//    private static Long REQ_ID;
//    private static final RequestStatus STATUS = RequestStatus.PENDING;
//
//    private static final int PAGE = 1;
//    private static final int PAGE_SIZE = 50;
//
//
//    private RequestJpaDaoImpl requestDao;
//    private JdbcTemplate jdbcTemplate;
//
//    private SimpleJdbcInsert jdbcInsertRequest;
//    private SimpleJdbcInsert jdbcInsertPet;
//    private SimpleJdbcInsert jdbcInsertUser;
//    private SimpleJdbcInsert jdbcInsertProvince;
//    private SimpleJdbcInsert jdbcInsertDepartment;
//    private SimpleJdbcInsert jdbcInsertSpecies;
//    private SimpleJdbcInsert jdbcInsertBreed;
//    private SimpleJdbcInsert jdbcInsertImage;
//
//    @Before
//    public void setUp() {
//        requestDao = new RequestJpaDaoImpl();
//        jdbcTemplate = new JdbcTemplate(ds);
//
//        /* REQUEST */
//        jdbcInsertRequest = new SimpleJdbcInsert(jdbcTemplate)
//                .withTableName(REQUESTS_TABLE)
//                .usingColumns("id");
//
//        /* PET */
//        jdbcInsertPet = new SimpleJdbcInsert(jdbcTemplate)
//                .withTableName(PETS_TABLE)
//                .usingGeneratedKeyColumns("id");
//        /* USER */
//        jdbcInsertUser = new SimpleJdbcInsert(jdbcTemplate)
//                .withTableName(USERS_TABLE)
//                .usingGeneratedKeyColumns("id");
//        /* SPECIES */
//        jdbcInsertSpecies = new SimpleJdbcInsert(jdbcTemplate)
//                .withTableName(SPECIES_TABLE)
//                .usingGeneratedKeyColumns("id");
//        /* BREED */
//        jdbcInsertBreed = new SimpleJdbcInsert(jdbcTemplate)
//                .withTableName(BREEDS_TABLE)
//                .usingGeneratedKeyColumns("id");
//        /* IMAGE */
//        jdbcInsertImage = new SimpleJdbcInsert(jdbcTemplate)
//                .withTableName(IMAGES_TABLE)
//                .usingGeneratedKeyColumns("id");
//        /* PROVINCE */
//        jdbcInsertProvince = new SimpleJdbcInsert(jdbcTemplate)
//                .withTableName(PROVINCES_TABLE)
//                .usingGeneratedKeyColumns("id");
//        /* DEPARTMENT */
//        jdbcInsertDepartment = new SimpleJdbcInsert(jdbcTemplate)
//                .withTableName(DEPARTMENTS_TABLE)
//                .usingGeneratedKeyColumns("id");
//
//        Calendar cal = Calendar.getInstance();
//        cal.set(Calendar.YEAR, 2001);
//        cal.set(Calendar.MONTH, 2);
//        cal.set(Calendar.DATE, 2);
//        UPLOAD_DATE = new Date(cal.getTimeInMillis());
//
//        setUpTableContext();
//    }
//
//    private void setUpTableContext() {
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, IMAGES_TABLE);
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, REQUESTS_TABLE);
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, PETS_TABLE);
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, DEPARTMENTS_TABLE);
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, PROVINCES_TABLE);
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, BREEDS_TABLE);
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, SPECIES_TABLE);
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, USERS_TABLE);
//
//        /* PROVINCE */
//        final Map<String, Object> provincesValues = new HashMap<String, Object>() {{
//            put("name", PROV_NAME);
//            put("latitude", PROV_LAT);
//            put("longitude", PROV_LON);
//        }};
//        Number p_id = jdbcInsertProvince.executeAndReturnKey(provincesValues);
//        PROV_ID = p_id.longValue();
//        PROVINCE = new Province(PROV_NAME, PROV_LAT, PROV_LON);
//        PROVINCE.setId(PROV_ID);
//
//        /* DEPARTMENT */
//        final Map<String, Object> departmentValues = new HashMap<String, Object>() {{
//            put("name", DEPT_NAME);
//            put("latitude", DEPT_LAT);
//            put("longitude", DEPT_LON);
//            put("province", PROVINCE.getName());
//        }};
//        Number d_id = jdbcInsertDepartment.executeAndReturnKey(departmentValues);
//        DEPT_ID = d_id.longValue();
//        DEPARTMENT = new Department(DEPT_NAME, DEPT_LAT, DEPT_LON, PROVINCE);
//        DEPARTMENT.setId(DEPT_ID);
//        DEPARTMENT.setProvince(PROVINCE);
//
//        /* USER */
//        final Map<String, Object> userValues = new HashMap<String, Object>() {{
//            put("username", USERNAME);
//            put("password", PASSWORD);
//            put("mail", MAIL);
//            put("status", USER_STATUS.ordinal());
//            put("locale", LOCALE);
//        }};
//        Number u_id = jdbcInsertUser.executeAndReturnKey(userValues);
//        USER_ID = u_id.longValue();
//        USER = new User(USERNAME, PASSWORD, MAIL, USER_STATUS, LOCALE);
//        USER.setId(USER_ID);
//
//        /* OTHER USER */
//        final Map<String, Object> o_userValues = new HashMap<String, Object>() {{
//            put("username", O_USERNAME);
//            put("password", O_PASSWORD);
//            put("mail", O_MAIL);
//            put("status", O_USER_STATUS.ordinal());
//            put("locale", LOCALE);
//        }};
//        Number ou_id = jdbcInsertUser.executeAndReturnKey(o_userValues).longValue();
//        O_USER_ID = ou_id.longValue();
//        O_USER = new User(O_USERNAME, O_PASSWORD, O_MAIL, O_USER_STATUS, LOCALE);
//        O_USER.setId(O_USER_ID);
//
//        /* SPECIES */
//        final Map<String, Object> speciesValues = new HashMap<String, Object>() {{
//            put("es_ar", SPEC_ESAR);
//            put("en_us", SPEC_ENUS);
//        }};
//        Number s_id = jdbcInsertSpecies.executeAndReturnKey(speciesValues).longValue();
//        SPEC_ID = s_id.longValue();
//        SPECIES = new Species(SPEC_ENUS, SPEC_ESAR);
//        SPECIES.setId(SPEC_ID);
//
//        /* BREED */
//        final Map<String, Object> breedValues = new HashMap<String, Object>() {{
//            put("speciesId", SPECIES.getId());
//            put("es_ar", BREED_ESAR);
//            put("en_us", BREED_ENUS);
//        }};
//        Number b_id = jdbcInsertBreed.executeAndReturnKey(breedValues).longValue();
//        BREED_ID = b_id.longValue();
//        BREED = new Breed(SPECIES, BREED_ENUS, BREED_ESAR);
//        BREED.setId(BREED_ID);
//        BREED.setSpecies(SPECIES);
//
//        /* PET */
//        final Map<String, Object> pet
//    }
//}
