package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.PetDao;
import ar.edu.itba.paw.interfaces.PetService;
import ar.edu.itba.paw.interfaces.SpeciesDao;
import ar.edu.itba.paw.interfaces.SpeciesService;
import ar.edu.itba.paw.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PetServiceImpl implements PetService {
    static final long AVAILABLE_STATUS = 1;

    @Autowired
    private PetDao petDao;
    @Autowired
    private SpeciesDao speciesDao;

    @Override
    public Optional<Pet> findById(String language, long id){
        return petDao.findById(language, id);
    }

    @Override
    public List<Pet> list(String language,String page){
        return petDao.list(language, page).collect(Collectors.toList());
    }

    @Override
    public List<Pet> filteredList(String language, String specie, String  breed, String gender, String searchCriteria, String searchOrder, String page) {
        return petDao.filteredList(language,specie, breed, gender, searchCriteria, searchOrder,page).collect(Collectors.toList());
    }

    @Override
    public List<Pet> find(String language,String findValue, String page){
        return petDao.find(language, findValue, page).collect(Collectors.toList());
    }

    @Override
    public List<Pet> getByUserId(String language, long ownerId, String page){
        return petDao.getByUserId( language, ownerId, page).collect(Collectors.toList());
    }

    @Override
    public Optional<Pet> create(String language, String petName, String speciesName, String breedName, String location,
                      boolean vaccinated, String gender, String description, Date birthDate, Date uploadDate, int price, long ownerId) {
        System.out.printf("DATA:\n%s\n%s\n%s\n%s\n%s\n%s\n%s\n%s\n%s\n%s\n%d\n%d\n",
                language, petName, speciesName, breedName, location, vaccinated, gender, description, birthDate, uploadDate,
                price, ownerId);
        Optional<Species> opSpecies = speciesDao.findSpeciesByName(language, speciesName);
        Optional<Breed> opBreed = speciesDao.findBreedByName(language, breedName);
        Optional<Status> opStatus = petDao.findStatusById(language, AVAILABLE_STATUS);
        if (!opSpecies.isPresent() || !opBreed.isPresent() || !opStatus.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(petDao.create(petName, opSpecies.get(), opBreed.get(), location, vaccinated, gender, description, birthDate, uploadDate, price, ownerId, opStatus.get()));
    }

    @Override
    public long getOwnerId(long petId){
        return petDao.getOwnerId(petId);
    }

    @Override
    public String getMaxPages(){
        return petDao.maxPages();
    }

    @Override
    public String getMaxSearchPages(String language, String findValue) {
        return petDao.maxSearchPages(language,findValue);
    }

    @Override
    public String getMaxFilterPages(String language, String specieFilter, String breedFilter, String genderFilter) {
        return petDao.maxFilterPages(language,specieFilter,breedFilter,genderFilter);
    }

    @Override
    public String getMaxUserPetsPages(long userId){
        return petDao.getMaxUserPetsPages(userId);
    }

    @Override
    public Optional<Contact> getPetContact(long petId) {
        return petDao.getPetContact(petId);
    }

    @Override
    public boolean updateStatus(long petId, long userId, long newStatus) {
        if (petDao.isPetOwner(petId, userId)) {
            petDao.updateStatus(petId, newStatus);
            return true;
        }
        return false;
    }
}
