package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.PetDao;
import ar.edu.itba.paw.models.Pet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Date;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.any;

@RunWith(MockitoJUnitRunner.class)
public class PetServiceImplTest {
    private final String PETS_TABLE = "pets";
    private final long PET_ID = 1;
    private final String PET_NAME = "pet_test_name";
    private final String SPECIES = "pet_test_species";
    private final String BREED = "pet_test_breed";
    private final String LOCATION = "pet_test_location";
    private final Boolean VACCINATED = false;
    private final String GENDER = "pet_test_gender";
    private final String DESCRIPTION = "pet_test_description";
    private final Date BIRTH_DATE = null;
    private final Date UPLOAD_DATE = null;
    private final int PRICE = 0;
    private final int OWNER_ID = 1;

    @InjectMocks
    private PetServiceImpl petServiceImpl = new PetServiceImpl();

    @Mock
    private PetDao petDao;

    @Test
    public void testCreatePet() {
        Pet pet = new Pet(PET_ID, PET_NAME, SPECIES, BREED, LOCATION, VACCINATED,
                GENDER, DESCRIPTION, BIRTH_DATE, UPLOAD_DATE, PRICE, OWNER_ID);
        when(petDao.create(anyString(), anyString(), anyString(),
                anyString(), anyBoolean(), anyString(), anyString(), any(), any(), anyInt(), anyInt()))
                .thenReturn(pet);
        try {
            Pet maybePet = petServiceImpl.create(PET_NAME, SPECIES, BREED, LOCATION, VACCINATED,
                    GENDER, DESCRIPTION, BIRTH_DATE, UPLOAD_DATE, PRICE, OWNER_ID);

        } catch (Exception e) {
            Assert.fail("unexpected error during operation create user");
        }
    }
}
