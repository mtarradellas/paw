package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.constants.PetStatus;
import ar.edu.itba.paw.models.constants.UserStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PetServiceImplTest {

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
    private static final Long SPEC_ID = 1L;
    private static Species SPECIES = new Species("species", "species");

    private static final Long O_SPEC_ID = 2L;
    private static Species O_SPECIES = new Species("other_species", "other_species");

    /* BREED */
    private static final Long BREED_ID = 1L;
    private static Breed BREED = new Breed(SPECIES, "breed", "breed");

    private static final Long O_BREED_ID = 2L;
    private static Breed O_BREED = new Breed(O_SPECIES, "other_breed", "other_breed");

    /* PROVINCE */
    private static final Long PROV_ID = 1L;
    private static Province PROVINCE = new Province("province", 10, 10);

    private static final Long O_PROV_ID = 2L;
    private static Province O_PROVINCE = new Province("another", 50, 50);

    /* DEPARTMENT */
    private static final Long DEPT_ID = 1L;
    private static Department DEPARTMENT = new Department("department", 10, 10, PROVINCE);

    private static final Long O_DEPT_ID = 2L;
    private static Department O_DEPARTMENT = new Department("other_dep", 50, 50, O_PROVINCE);

    /* USER */
    private static final Long USER_ID = 1L;
    private static User USER = new User("username", "password", "mail@mail.com", UserStatus.ACTIVE, LOCALE);

    private static final Long O_USER_ID = 2L;
    private static User O_USER = new User("other_username", "other_password", "other_mail@mail.com", UserStatus.ACTIVE, LOCALE);

    /* PET */
    private static final Long PET_ID = 1L;
    private static final List<byte[]> PHOTOS = new ArrayList<>();
    private Pet PET = new Pet("petname", LocalDateTime.now(), "gender", true, 0, LocalDateTime.now(),
            "description", PetStatus.AVAILABLE, USER, SPECIES, BREED, PROVINCE, DEPARTMENT);

    private static final Long O_PET_ID = 2L;
    private Pet O_PET = new Pet("other_pet", LocalDateTime.now().minusMonths(3L), "another", false, 500, LocalDateTime.now().minusMonths(3L),
            "other pet", PetStatus.UNAVAILABLE, O_USER, O_SPECIES, O_BREED, O_PROVINCE, O_DEPARTMENT);


    private static final int PAGE = 1;
    private static final int PAGE_SIZE = 50;


    @InjectMocks
    private final PetServiceImpl petServiceImpl = new PetServiceImpl();

    @Mock
    private PetDao petDao;
    @Mock
    private LocationService locationService;
    @Mock
    private UserService userService;
    @Mock
    private SpeciesService speciesService;
    @Mock
    private ImageService imageService;

    @Before
    public void setUp() {
        SPECIES.setId(SPEC_ID);
        O_SPECIES.setId(O_SPEC_ID);
        BREED.setId(BREED_ID);
        O_BREED.setId(O_BREED_ID);
        PROVINCE.setId(PROV_ID);
        O_PROVINCE.setId(O_PROV_ID);
        DEPARTMENT.setId(DEPT_ID);
        O_DEPARTMENT.setId(O_DEPT_ID);
        USER.setId(USER_ID);
        O_USER.setId(O_USER_ID);
        PET.setId(PET_ID);
        O_PET.setId(O_PET_ID);
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
    public void testCreatePet() {
        when(petDao.create(anyString(), any(), anyString(), anyBoolean(), anyInt(), any(), anyString(), any(), any(),
                any(), any(), any(), any())).thenReturn(PET);
        when(locationService.findProvinceById(anyLong())).thenReturn(Optional.of(PROVINCE));
        when(locationService.findDepartmentById(anyLong())).thenReturn(Optional.of(DEPARTMENT));
        when(speciesService.findSpeciesById(anyString(), anyLong())).thenReturn(Optional.of(SPECIES));
        when(speciesService.findBreedById(anyString(), anyLong())).thenReturn(Optional.of(BREED));
        when(userService.findById(anyLong())).thenReturn(Optional.of(USER));

        Optional<Pet> opPet = Optional.empty();
        try {
            opPet = petServiceImpl.create(LOCALE, PET.getPetName(), PET.getBirthDate(), PET.getGender(), PET.isVaccinated(),
                    PET.getPrice(), PET.getDescription(), PET.getStatus(), PET.getUser().getId(), PET.getSpecies().getId(),
                    PET.getBreed().getId(), PET.getProvince().getId(), PET.getDepartment().getId(), PHOTOS);

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("unexpected error during operation create pet");
        }

        assertTrue(opPet.isPresent());
        assertPet(opPet.get(), PET.getId(), PET.getPetName(), PET.getBirthDate(), PET.getGender(), PET.isVaccinated(),
                PET.getPrice(), PET.getUploadDate(), PET.getDescription(), PET.getStatus(), PET.getUser(),
                PET.getSpecies(), PET.getBreed(), PET.getProvince(), PET.getDepartment());

    }
}
