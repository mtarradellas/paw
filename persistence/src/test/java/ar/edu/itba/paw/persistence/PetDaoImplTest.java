//package ar.edu.itba.paw.persistence;
//
//import ar.edu.itba.paw.models.*;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.RowMapper;
//import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.jdbc.JdbcTestUtils;
//
//import javax.sql.DataSource;
//import java.sql.Date;
//import java.util.*;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//import static org.junit.Assert.*;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = TestConfig.class)
//public class PetDaoImplTest {
//    private final String PETS_TABLE = "pets";
//    private final String USERS_TABLE = "users";
//    private final String USER_STATUS_TABLE = "user_status";
//    private final String PROVINCES_TABLE = "provinces";
//    private final String DEPARTMENTS_TABLE = "departments";
//    private final String SPECIES_TABLE = "species";
//    private final String BREEDS_TABLE = "breeds";
//    private final String IMAGES_TABLE = "images";
//    private final String PET_STATUS_TABLE = "pet_status";
//
//    private static final long ID = 1;
//    private static final String PET_NAME = "pet_test_name";
//    private static final Species SPECIES = new Species(Long.valueOf(1), "pet_test_species");
//    private static final Breed BREED = new Breed(Long.valueOf(1),  "pet_test_breed", SPECIES);
//    private static final Boolean VACCINATED = false;
//    private static final String GENDER = "pet_test_gender";
//    private static final String DESCRIPTION = "pet_test_description";
//    private static final Date BIRTH_DATE = null;
//    private  java.sql.Date UPLOAD_DATE ;
//    private static final int PRICE = 0;
//    private static final Province PROVINCE = new Province(1, "Buenos Aires", 10, 10);
//    private static final Department DEPARTMENT = new Department(1, "Tigre", 10, 10, PROVINCE);
//    private int OWNER_ID = 1;
//    private static final Status STATUS = new Status(1, "Available");
//
//    private final String LANG = "en_us";
//
//    private static long OTHER_SPECIES_ID = SPECIES.getId() + 1;
//    private static long OTHER_BREED_ID = BREED.getId() + 1;
//
//    @Autowired
//    private DataSource ds;
//
//    private static final RowMapper<Pet> PET_MAPPER = (rs, rowNum) -> {
//        Province prov =new Province(PROVINCE.getId(), PROVINCE.getName(),
//                PROVINCE.getLatitude(), PROVINCE.getLongitude());
//        Department dept = new Department(rs.getLong("department"), DEPARTMENT.getName(),
//                DEPARTMENT.getLatitude(), DEPARTMENT.getLongitude(), prov);
//        return new Pet(
//                rs.getLong("id"),
//                rs.getString("petname"),
//                new Species(rs.getLong("species"), SPECIES.getName()),
//                new Breed(rs.getLong("breed"), BREED.getName(), SPECIES),
//                rs.getBoolean("vaccinated"),
//                rs.getString("gender"),
//                rs.getString("description"),
//                rs.getDate("birthDate"),
//                rs.getDate("uploadDate"),
//                rs.getInt("price"),
//                rs.getLong("ownerId"),
//                new Status(rs.getInt("status"), STATUS.getName()),
//                prov,
//                dept
//        );
//    };
//
//    private PetDaoImpl petDaoImpl;
//    private JdbcTemplate jdbcTemplate;
//
//    private SimpleJdbcInsert jdbcInsertPet;
//    private SimpleJdbcInsert jdbcInsertUser;
//    private SimpleJdbcInsert jdbcInsertUserStatus;
//    private SimpleJdbcInsert jdbcInsertProvince;
//    private SimpleJdbcInsert jdbcInsertDepartment;
//    private SimpleJdbcInsert jdbcInsertSpecies;
//    private SimpleJdbcInsert jdbcInsertBreed;
//    private SimpleJdbcInsert jdbcInsertImage;
//    private SimpleJdbcInsert jdbcInsertPetStatus;
//
//    @Before
//    public void setUp() {
//        petDaoImpl = new PetDaoImpl(ds);
//        jdbcTemplate = new JdbcTemplate(ds);
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
//                .withTableName(SPECIES_TABLE);
//        /* BREED */
//        jdbcInsertBreed = new SimpleJdbcInsert(jdbcTemplate)
//                .withTableName(BREEDS_TABLE);
//        /* IMAGE */
//        jdbcInsertImage = new SimpleJdbcInsert(jdbcTemplate)
//                .withTableName(IMAGES_TABLE)
//                .usingGeneratedKeyColumns("id");
//        /* USER STATUS*/
//        jdbcInsertUserStatus = new SimpleJdbcInsert(jdbcTemplate)
//                .withTableName(USER_STATUS_TABLE);
//        /* PROVINCE */
//        jdbcInsertProvince = new SimpleJdbcInsert(jdbcTemplate)
//                .withTableName(PROVINCES_TABLE);
//        /* DEPARTMENT */
//        jdbcInsertDepartment = new SimpleJdbcInsert(jdbcTemplate)
//                .withTableName(DEPARTMENTS_TABLE);
//        /* PET STATUS */
//        jdbcInsertPetStatus = new SimpleJdbcInsert(jdbcTemplate)
//                .withTableName(PET_STATUS_TABLE);
//
//        Calendar cal = Calendar.getInstance();
//        cal.set(Calendar.YEAR, 2001);
//        cal.set(Calendar.MONTH, 2);
//        cal.set(Calendar.DATE, 2);
//        UPLOAD_DATE = new java.sql.Date(cal.getTimeInMillis());
//
//        setUpTablePetContext();
//    }
//
//    private void setUpTablePetContext() {
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, IMAGES_TABLE);
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, PETS_TABLE);
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, PET_STATUS_TABLE);
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, DEPARTMENTS_TABLE);
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, PROVINCES_TABLE);
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, BREEDS_TABLE);
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, SPECIES_TABLE);
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, USERS_TABLE);
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, USER_STATUS_TABLE);
//        /* USER STATUS */
//        final Map<String, Object> userStatusValues = new HashMap<String, Object>() {{
//            put("id", 1);
//            put("en_US", "Active");
//            put("es_AR", "Activo");
//        }};
//        jdbcInsertUserStatus.execute(userStatusValues);
//        /* PROVINCES */
//        final Map<String, Object> provincesValues = new HashMap<String, Object>() {{
//            put("id", 1);
//            put("name", PROVINCE.getName());
//            put("latitude", PROVINCE.getLatitude());
//            put("longitude", PROVINCE.getLongitude());
//        }};
//        jdbcInsertProvince.execute(provincesValues);
//        /* DEPARTMENTS */
//        final Map<String, Object> departmentValues = new HashMap<String, Object>() {{
//            put("id", 1);
//            put("name", DEPARTMENT.getName());
//            put("latitude", DEPARTMENT.getLatitude());
//            put("longitude", DEPARTMENT.getLongitude());
//            put("province", PROVINCE.getName());
//        }};
//        jdbcInsertDepartment.execute(departmentValues);
//        /* USER */
//        final Map<String, Object> userValues = new HashMap<String, Object>() {{
//            put("username", "pet_test_username");
//            put("password", "pet_test_password");
//            put("mail", "pet_test_mail");
//            put("status", 1);
//        }};
//        OWNER_ID = jdbcInsertUser.executeAndReturnKey(userValues).intValue();
//        /* SPECIES */
//        final Map<String, Object> speciesValues = new HashMap<String, Object>() {{
//            put("id", SPECIES.getId());
//            put("es_ar", SPECIES.getName());
//            put("en_us", SPECIES.getName());
//        }};
//        jdbcInsertSpecies.execute(speciesValues);
//        /* BREED */
//        final Map<String, Object> breedValues = new HashMap<String, Object>() {{
//            put("id", BREED.getId());
//            put("speciesId", BREED.getSpecies().getId());
//            put("es_ar", BREED.getName());
//            put("en_us", BREED.getName());
//        }};
//        jdbcInsertBreed.execute(breedValues);
//        /* OTHER SPECIES*/
//        final Map<String, Object> otherSpeciesValues = new HashMap<String, Object>() {{
//            put("id", OTHER_SPECIES_ID);
//            put("es_ar", "other");
//            put("en_us", "other");
//        }};
//        jdbcInsertSpecies.execute(otherSpeciesValues);
//        /* OTHER BREED */
//        final Map<String, Object> otherBreedValues = new HashMap<String, Object>() {{
//            put("id", OTHER_BREED_ID);
//            put("es_ar", "other");
//            put("en_us", "other");
//        }};
//        jdbcInsertBreed.execute(otherBreedValues);
//        /* PET STATUS */
//        final Map<String, Object> available = new HashMap<String, Object>() {{
//            put("id", 1);
//            put("en_US", "Available");
//            put("es_AR", "Disponible");
//        }};
//        jdbcInsertPetStatus.execute(available);
//        final Map<String, Object> removed = new HashMap<String, Object>() {{
//            put("id", 2);
//            put("en_US", "Removed");
//            put("es_AR", "Removido");
//        }};
//        jdbcInsertPetStatus.execute(removed);
//        final Map<String, Object> sold = new HashMap<String, Object>() {{
//            put("id", 3);
//            put("en_US", "Sold");
//            put("es_AR", "Vendido");
//        }};
//        jdbcInsertPetStatus.execute(sold);
//    }
//
//    private long insertPet(long id, String name,  long species, long breed, boolean vaccinated, String gender,
//                           String description, Date birthDate, Date uploadDate, int price, long ownerId, long status, long department) {
//        final Map<String, Object> petValues = new HashMap<String, Object>() {{
//            put("id", id);
//            put("petName", name);
//            put("species", species);
//            put("breed", breed);
//            put("vaccinated", vaccinated);
//            put("gender", gender);
//            put("description", description);
//            put("birthDate", birthDate);
//            put("uploadDate", uploadDate);
//            put("price", price);
//            put("ownerId", ownerId);
//            put("status", status);
//            put("department", department);
//        }};
//        long key = jdbcInsertPet.executeAndReturnKey(petValues).longValue();
//        byte[] bytes = new byte[] {(byte) 0x3f};
//        final Map<String, Object> imageValues = new HashMap<String, Object>() {{
//            put("img", bytes);
//            put("petId", key);
//        }};
//        jdbcInsertImage.execute(imageValues);
//        return key;
//    }
//
//    private void assertPet(Pet pet, long id, String name, Species species, Breed breed, boolean vaccinated, String gender,
//                           String description, Date birthDate, Date uploadDate, int price, long ownerId, Status status, Department department) {
//
//        assertEquals(id, pet.getId());
//        assertEquals(name, pet.getPetName());
//        assertEquals(species, pet.getSpecies());
//        assertEquals(breed, pet.getBreed());
//        assertEquals(vaccinated, pet.isVaccinated());
//        assertEquals(gender, pet.getGender());
//        assertEquals(description, pet.getDescription());
//        assertDate(birthDate, pet.getBirthDate());
//        assertDate(uploadDate, pet.getUploadDate());
//        assertEquals(price, pet.getPrice());
//        assertEquals(ownerId, pet.getOwnerId());
//        assertEquals(status, pet.getStatus());
//        assertEquals(department, pet.getDepartment());
//    }
//
//    @Test
//    public void testCreatePet() {
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, IMAGES_TABLE);
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, PETS_TABLE);
//
//        /**/
//        long id = petDaoImpl.create(PET_NAME, SPECIES, BREED, VACCINATED, GENDER,
//                DESCRIPTION, BIRTH_DATE, UPLOAD_DATE, PRICE, OWNER_ID, STATUS, DEPARTMENT.getId());
//
//        Optional<Pet> opPet = jdbcTemplate.query("SELECT * FROM pets", PET_MAPPER).stream().findFirst();
//        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, PETS_TABLE));
//        assertTrue(opPet.isPresent());
//        Pet pet = opPet.get();
//        assertPet(pet, id, PET_NAME, SPECIES, BREED, VACCINATED, GENDER,
//                DESCRIPTION, BIRTH_DATE, UPLOAD_DATE, PRICE, OWNER_ID, STATUS, DEPARTMENT);
//    }
//
//    @Test
//    public void testFindByIdNotExists() {
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, IMAGES_TABLE);
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, PETS_TABLE);
//
//        /**/
//        Optional<Pet> opPet = petDaoImpl.findById(LANG, ID);
//
//        assertFalse(opPet.isPresent());
//    }
//
//    @Test
//    public void testFindByIdExists() {
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, IMAGES_TABLE);
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, PETS_TABLE);
//        long id = insertPet(ID, PET_NAME, SPECIES.getId(), BREED.getId(), VACCINATED,GENDER,DESCRIPTION, BIRTH_DATE,
//                UPLOAD_DATE, PRICE, OWNER_ID, STATUS.getId(), DEPARTMENT.getId());
//
//        /**/
//        Optional<Pet> opPet = petDaoImpl.findById(LANG, id);
//
//        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, PETS_TABLE));
//        assertTrue(opPet.isPresent());
//        Pet pet = opPet.get();
//        assertPet(pet, id, PET_NAME, SPECIES, BREED, VACCINATED, GENDER, DESCRIPTION, BIRTH_DATE,
//                UPLOAD_DATE, PRICE, OWNER_ID, STATUS, DEPARTMENT);
//    }
//
//    @Test
//    public void testFilteredListSpecies() {
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, IMAGES_TABLE);
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, PETS_TABLE);
//        long id = insertPet(ID, PET_NAME, SPECIES.getId(), BREED.getId(), VACCINATED,GENDER,DESCRIPTION, BIRTH_DATE,
//                UPLOAD_DATE, PRICE, OWNER_ID, STATUS.getId(), DEPARTMENT.getId());
//
//        /**/
//        Stream<Pet> petStream = petDaoImpl.filteredList(LANG, String.valueOf(SPECIES.getId()), null,
//                                                null, null, null,null,
//                                                null, null, null, null);
//
//        List<Pet> petList = petStream.collect(Collectors.toList());
//
//        assertEquals(1, petList.size());
//        Pet pet = petList.get(0);
//        assertPet(pet, id, PET_NAME, SPECIES, BREED, VACCINATED, GENDER, DESCRIPTION, BIRTH_DATE,
//                UPLOAD_DATE, PRICE, OWNER_ID, STATUS, DEPARTMENT);
//    }
//
//    @Test
//    public void testFilteredListBreed() {
//
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, IMAGES_TABLE);
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, PETS_TABLE);
//        long id = insertPet(ID, PET_NAME, SPECIES.getId(), BREED.getId(), VACCINATED, GENDER, DESCRIPTION, BIRTH_DATE,
//                UPLOAD_DATE, PRICE, OWNER_ID, STATUS.getId(), DEPARTMENT.getId());
//
//        /**/
//        Stream<Pet> petStream = petDaoImpl.filteredList(LANG, String.valueOf(SPECIES.getId()), String.valueOf(BREED.getId()),
//                null, null, null,null,
//                null, null, null, null);
//        List<Pet> petList = petStream.collect(Collectors.toList());
//
//        assertEquals(1, petList.size());
//        Pet pet = petList.get(0);
//        assertPet(pet, id, PET_NAME, SPECIES, BREED, VACCINATED, GENDER, DESCRIPTION, BIRTH_DATE,
//                UPLOAD_DATE, PRICE, OWNER_ID, STATUS, DEPARTMENT);
//    }
//
//    @Test
//    public void testFilteredListGender() {
//
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, IMAGES_TABLE);
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, PETS_TABLE);
//        long id = insertPet(ID, PET_NAME, SPECIES.getId(), BREED.getId(), VACCINATED, GENDER, DESCRIPTION, BIRTH_DATE,
//                UPLOAD_DATE, PRICE, OWNER_ID, STATUS.getId(), DEPARTMENT.getId());
//
//        /**/
//        Stream<Pet> petStream = petDaoImpl.filteredList(LANG, null, null,
//                GENDER, null, null,null,
//                null, null, null, null);
//        List<Pet> petList = petStream.collect(Collectors.toList());
//
//        assertEquals(1, petList.size());
//        Pet pet = petList.get(0);
//        assertPet(pet, id, PET_NAME, SPECIES, BREED, VACCINATED, GENDER, DESCRIPTION, BIRTH_DATE,
//                UPLOAD_DATE, PRICE, OWNER_ID, STATUS, DEPARTMENT);
//    }
//
//
//    private void assertDate(Date expected, Date actual) {
//        assertTrue((expected == null && actual == null) ||
//                expected != null && actual != null && expected.toString().equals(actual.toString()));
//    }
//}
