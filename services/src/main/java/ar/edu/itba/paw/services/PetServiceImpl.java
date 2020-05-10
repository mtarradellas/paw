package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PetServiceImpl implements PetService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PetServiceImpl.class);

    static final long AVAILABLE_STATUS = 1;
    static final long REMOVED_STATUS = 2;
    static final long SOLD_STATUS = 3;

    static final int USER_LEVEL = 0;
    static final int ADMIN_LEVEL = 1;


    @Autowired
    private PetDao petDao;
    @Autowired
    private SpeciesDao speciesDao;
    @Autowired
    private ImageService imageService;


    @Override
    public Optional<Pet> findById(String language, long id) {
        return petDao.findById(language, id, USER_LEVEL);
    }

    @Override
    public Optional<Pet> adminFindById(String language, long id) {
        return petDao.findById(language, id, ADMIN_LEVEL);
    }

    @Override
    public PetList petList(String language, String findValue, String species, String  breed, String gender, String searchCriteria, String searchOrder, String minPrice, String maxPrice, String page) {
        if (findValue == null) return filteredList(language, species, breed, gender, searchCriteria, searchOrder, minPrice, maxPrice, page);
        return find(language, findValue, page);
    }

    @Override
    public List<Pet> list(String language,String page){

        return petDao.list(language, page, USER_LEVEL).collect(Collectors.toList());
    }

    @Override
    public List<Pet> listAll(String language) {
        return petDao.listAll(language).collect(Collectors.toList());
    }

    @Override
    public List<Pet> adminFilteredList(String language, String specie, String breed, String gender, String status, String searchCriteria, String searchOrder, String page) {
        return petDao.adminFilteredList(language, specie, breed, gender, status, searchCriteria, searchOrder, page).collect(Collectors.toList());
    }

    @Override
    public PetList filteredList(String language, String species, String  breed, String gender, String searchCriteria, String searchOrder, String minPrice, String maxPrice, String page) {
        List<Pet> list = petDao.filteredList(language,species, breed, gender, searchCriteria, searchOrder, minPrice, maxPrice, page).collect(Collectors.toList());
        String maxPage = getMaxFilterPages(language, species, breed, gender, minPrice, maxPrice);
        return new PetList(list, maxPage);
    }

    @Override
    public PetList find(String language,String findValue, String page){
        List<Pet> list = petDao.find(language, findValue, page, USER_LEVEL).collect(Collectors.toList());
        String maxPage = getMaxSearchPages(language, findValue);
        return new PetList(list, maxPage);

    }

    @Override
    public List<Pet> getByUserId(String language, long ownerId, String page) {
        return petDao.getByUserId(language, ownerId, page).collect(Collectors.toList());
    }

    @Override
    public List<Pet> adminList(String language, String page) {
        return petDao.list(language, page, ADMIN_LEVEL).collect(Collectors.toList());
    }

    @Override
    public List<Pet> adminSearchList(String language, String find, String page) {
        return petDao.find(language, find, page, ADMIN_LEVEL).collect(Collectors.toList());
    }

    @Override
    public Optional<Pet> create(String language, String petName, long speciesId, long breedId, String location, boolean vaccinated,
                                String gender, String description, Date birthDate, Date uploadDate, int price, long ownerId, List<byte[]> photos) {
        LOGGER.debug("Attempting to create pet with name: {}, species: {}, breed: {}, location: {}, vaccinated: {}, gender: {}, description: {}, birthdate: {}, upDate: {}, price: {}, owner: {}",
                petName, speciesId, breedId, location, vaccinated, gender, description, birthDate, uploadDate, price, ownerId);


        Optional<Species> opSpecies = speciesDao.findSpeciesById(language, speciesId);
        if (!opSpecies.isPresent()) {
            LOGGER.warn("Species {} not found, pet creation failed", speciesId);
            return Optional.empty();
        }
        Species species = opSpecies.get();

        Optional<Breed> opBreed = speciesDao.findBreedById(language, breedId);
        if (!opBreed.isPresent()) {
            LOGGER.warn("Breed {} not found, pet creation failed", breedId);
            return Optional.empty();
        }
        Breed breed = opBreed.get();

        Optional<Status> opStatus = petDao.findStatusById(language, AVAILABLE_STATUS);
        if (!opStatus.isPresent()) {
            LOGGER.warn("Status {} not found, pet creation failed", AVAILABLE_STATUS);
            return Optional.empty();
        }
        Status status = opStatus.get();

        Pet pet = petDao.create(petName, species, breed, location, vaccinated, gender, description, birthDate, uploadDate, price, ownerId, status);
        LOGGER.debug("Pet {} successfully created", pet);

        for (byte[] photo : photos) {
            imageService.create(pet.getId(), photo, ownerId);
        }

        return Optional.of(pet);
    }

    @Override
    public long getOwnerId(long petId) {
        return petDao.getOwnerId(petId);
    }

    @Override
    public void removeAllByOwner(long ownerId) {
        petDao.updateAllByOwner(ownerId, (int)REMOVED_STATUS);
    }

    @Override
    public Optional<Pet> update(String language, long userId, long id, List<byte[]> photos, List<Integer> imagesToDelete, String petName, long speciesId, long breedId, String location,
                       boolean vaccinated, String gender, String description, Date birthDate, int price) {
        LOGGER.debug("Attempting user update of pet {} with: petName: {}, speciesId: {}, breedId: {}, location: {}, " +
                        "vaccinated: {}, gender: {}, description: {}, birthDate: {}, price: {}",
                id, petName, speciesId, breedId, location, vaccinated, gender, description, birthDate, price);

        if(! petDao.isPetOwner(id, userId)) {
            LOGGER.warn("Logged user is not the owner of pet {}, update aborted", id);
            return Optional.empty();
        }
        if (!speciesDao.findSpeciesById(language, speciesId).isPresent()) {
            LOGGER.warn("Species {} not found, pet update failed", speciesId);
            return Optional.empty();
        }
        if (!speciesDao.findBreedById(language, breedId).isPresent()) {
            LOGGER.warn("Breed {} not found, pet update failed", breedId);
            return Optional.empty();
        }

        LOGGER.debug("Deleting from pet {} images {}", id, imagesToDelete);
        imageService.delete(imagesToDelete);

        petDao.update(id, petName, speciesId, breedId, location, vaccinated, gender, description, birthDate, price);
        Optional<Pet> opPet = petDao.findById(language, id, USER_LEVEL);
        if (!opPet.isPresent()){
            LOGGER.debug("Pet {} update failed", id);
        }
        LOGGER.debug("Pet {} successfully updated", opPet.get());

        for (byte[] photo : photos) {
            LOGGER.debug("Adding image to pet {}", id);
            imageService.create(opPet.get().getId(), photo, opPet.get().getOwnerId());
        }
        return opPet;
    }

    @Override
    public String getMaxPages() {
        return petDao.maxPages(USER_LEVEL);
    }

    @Override
    public String getMaxSearchPages(String language, String findValue) {
        return petDao.maxSearchPages(language, findValue, USER_LEVEL);
    }

    @Override
    public String getMaxFilterPages(String language, String specieFilter, String breedFilter, String genderFilter, String minPrice, String maxPrice) {
        return petDao.maxFilterPages(language, specieFilter, breedFilter, genderFilter, minPrice, maxPrice);
    }

    @Override
    public String getMaxAdminFilterPages(String language, String specieFilter, String breedFilter, String genderFilter, String statusFilter) {
        return petDao.maxAdminFilterPages(language, specieFilter, breedFilter, genderFilter, statusFilter);
    }

    @Override
    public String getMaxUserPetsPages(long userId) {
        return petDao.getMaxUserPetsPages(userId);
    }

    @Override
    public String getAdminMaxSearchPages(String language, String find) {
        return petDao.maxSearchPages(language, find, ADMIN_LEVEL);
    }

    @Override
    public String getAdminMaxPages() {
        return petDao.maxPages(ADMIN_LEVEL);

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

    @Override
    public void removePetAdmin(long petId) {
        petDao.updateStatus(petId, REMOVED_STATUS);
    }

    @Override
    public void sellPetAdmin(long petId) {
        petDao.updateStatus(petId, SOLD_STATUS);
    }

    @Override
    public void recoverPetAdmin(long petId) {
        petDao.updateStatus(petId, AVAILABLE_STATUS);
    }
}
