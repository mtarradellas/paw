package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.constants.PetStatus;
import ar.edu.itba.paw.models.constants.RequestStatus;
import ar.edu.itba.paw.models.constants.UserStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RequestServiceImplTest {

    private static final String LOCALE = "en_US";

    /* SPECIES */
    private static final Long SPEC_ID = 1L;
    private static final Species SPECIES = new Species("species", "species");

    private static final Long O_SPEC_ID = 2L;
    private static final Species O_SPECIES = new Species("other_species", "other_species");

    /* BREED */
    private static final Long BREED_ID = 1L;
    private static final Breed BREED = new Breed(SPECIES, "breed", "breed");

    private static final Long O_BREED_ID = 2L;
    private static final Breed O_BREED = new Breed(O_SPECIES, "other_breed", "other_breed");

    /* PROVINCE */
    private static final Long PROV_ID = 1L;
    private static final Province PROVINCE = new Province("province", 10, 10);

    private static final Long O_PROV_ID = 2L;
    private static final Province O_PROVINCE = new Province("another", 50, 50);

    /* DEPARTMENT */
    private static final Long DEPT_ID = 1L;
    private static final Department DEPARTMENT = new Department("department", 10, 10, PROVINCE);

    private static final Long O_DEPT_ID = 2L;
    private static final Department O_DEPARTMENT = new Department("other_dep", 50, 50, O_PROVINCE);

    /* USER */
    private static final Long USER_ID = 1L;
    private static final User USER = new User("username", "password", "mail@mail.com", UserStatus.ACTIVE, LOCALE);

    private static final Long O_USER_ID = 2L;
    private static final User O_USER = new User("other_username", "other_password", "other_mail@mail.com", UserStatus.ACTIVE, LOCALE);

    /* PET */
    private static final Long PET_ID = 1L;
    private static final Pet PET = new Pet("petname", LocalDateTime.now(), "gender", true, 0, LocalDateTime.now(),
            "description", PetStatus.AVAILABLE, USER, SPECIES, BREED, PROVINCE, DEPARTMENT);

    private static final Long O_PET_ID = 2L;
    private static final Pet O_PET = new Pet("other_pet", LocalDateTime.now().minusMonths(3L), "another", false, 500, LocalDateTime.now().minusMonths(3L),
            "other pet", PetStatus.UNAVAILABLE, O_USER, O_SPECIES, O_BREED, O_PROVINCE, O_DEPARTMENT);

    /* REQUEST */
    private static final Long REQ_ID = 1L;
    private static final Request REQUEST = new Request(LocalDateTime.now(), RequestStatus.PENDING, O_USER, USER, PET);

    private static final int PAGE = 1;
    private static final int PAGE_SIZE = 50;

    @InjectMocks
    private final RequestServiceImpl requestService = new RequestServiceImpl();

    @Mock
    private RequestDao requestDao;
    @Mock
    private PetService petService;
    @Mock
    private UserService userService;
    @Mock
    private MailService mailService;

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
        REQUEST.setId(REQ_ID);
        O_USER.setRequestList(new ArrayList<>());
    }

    private void assertRequest(Request request, long id, LocalDateTime creationDate, RequestStatus status) {
        assertEquals(id, request.getId().longValue());
        assertEquals(creationDate, request.getCreationDate());
        assertEquals(status, request.getStatus());
    }

//    @Test
//    public void testFilteredListRequest() {
//        List<Request> requestList = new ArrayList<>();
//        requestList.add(REQUEST);
//        when(requestDao.searchList(eq(REQUEST.getUser()), any(), any(), any(), any(), any(), anyInt(), anyInt()))
//                .thenReturn(requestList);
//
//
//        List<Request> returnList = requestService.filteredList(REQUEST.getUser(), null, null, null,
//                null,  null, PAGE, PAGE_SIZE);
//
//        assertEquals(1, returnList.size());
//        assertRequest(returnList.get(0), REQUEST.getId(), REQUEST.getCreationDate(), REQUEST.getStatus());
//    }

    @Test
    public void testFindById() {
        when(requestDao.findById(eq(USER.getId()))).thenReturn(Optional.of(REQUEST));

        Optional<Request> opRequest = requestService.findById(REQUEST.getId());

        assertTrue(opRequest.isPresent());
        assertRequest(opRequest.get(), REQUEST.getId(), REQUEST.getCreationDate(), REQUEST.getStatus());
    }

    @Test
    public void testFilteredListRequestByPetOwner() {
        List<Request> requestList = new ArrayList<>();
        requestList.add(REQUEST);
        when(requestDao.searchListByPetOwner(eq(REQUEST.getTarget()), any(), any(), any(), any(), any(), anyInt(), anyInt()))
                .thenReturn(requestList);

        List<Request> returnList = requestService.filteredListByPetOwner(REQUEST.getTarget(), null, null, null,
                null,  null, PAGE, PAGE_SIZE);

        assertEquals(1, returnList.size());
        assertRequest(returnList.get(0), REQUEST.getId(), REQUEST.getCreationDate(), REQUEST.getStatus());
    }

    @Test
    public void testCreate() {
        when(requestDao.create(eq(REQUEST.getUser()), eq(REQUEST.getPet()),eq(REQUEST.getStatus()), any())).thenReturn(REQUEST);
        when(userService.findById(anyLong())).thenReturn(Optional.of(REQUEST.getUser()));
        when(petService.findById(any(), eq(REQUEST.getPet().getId()))).thenReturn(Optional.of(REQUEST.getPet()));

        Optional<Request> opRequest = requestService.create(LOCALE, REQUEST.getUser().getId(), REQUEST.getPet().getId(), null);

        assertTrue(opRequest.isPresent());
        assertRequest(opRequest.get(), REQUEST.getId(), REQUEST.getCreationDate(), REQUEST.getStatus());
    }
}
