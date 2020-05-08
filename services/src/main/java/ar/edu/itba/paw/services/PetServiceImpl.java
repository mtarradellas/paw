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
    static final long REMOVED_STATUS = 2;
    static final long SOLD_STATUS = 3;

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
    public List<Pet> filteredList(String language, String specie, String  breed, String gender, String searchCriteria, String searchOrder, String minPrice, String maxPrice, String page) {
        return petDao.filteredList(language,specie, breed, gender, searchCriteria, searchOrder, minPrice, maxPrice, page).collect(Collectors.toList());
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
    public Optional<Pet> create(String language, String petName, long speciesId, long breedId, String location,
                      boolean vaccinated, String gender, String description, Date birthDate, Date uploadDate, int price, long ownerId) {
        Optional<Species> opSpecies = speciesDao.findSpeciesById(language, speciesId);
        Optional<Breed> opBreed = speciesDao.findBreedById(language, breedId);
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
    public String getMaxFilterPages(String language, String specieFilter, String breedFilter, String genderFilter, String minPrice, String maxPrice) {
        return petDao.maxFilterPages(language,specieFilter,breedFilter,genderFilter, minPrice, maxPrice);
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
    public boolean sellPet(long petId, long userId) {
        if (petDao.isPetOwner(petId, userId)) {
            petDao.updateStatus(petId, SOLD_STATUS);
            return true;
        }
        return false;
    }

    @Override
    public boolean removePet(long petId, long userId) {
        if (petDao.isPetOwner(petId, userId)) {
            petDao.updateStatus(petId, REMOVED_STATUS);
            return true;
        }
        return false;
    }
}
