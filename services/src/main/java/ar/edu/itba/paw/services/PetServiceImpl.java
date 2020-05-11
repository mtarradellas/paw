package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.constants.PetStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PetServiceImpl implements PetService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PetServiceImpl.class);

    @Autowired
    private PetDao petDao;
    @Autowired
    private SpeciesDao speciesDao;
    @Autowired
    private ImageService imageService;


    @Override
    public PetList petList(String language, String findValue, String species, String  breed, String gender, String searchCriteria, String searchOrder, String minPrice, String maxPrice, String page) {
        if (findValue == null) return filteredList(language, species, breed, gender, searchCriteria, searchOrder, minPrice, maxPrice, page);
        return find(language, findValue, page);
    }

    @Override
    public PetList filteredList(String language, String species, String  breed, String gender, String searchCriteria, String searchOrder, String minPrice, String maxPrice, String page) {
        List<Pet> list = petDao.filteredList(language,species, breed, gender, searchCriteria, searchOrder, minPrice, maxPrice, page).collect(Collectors.toList());
        String maxPage = getMaxFilterPages(language, species, breed, gender, minPrice, maxPrice);
        return new PetList(list, maxPage);
    }

    @Override
    public PetList find(String language,String findValue, String page){
        List<Pet> list = petDao.find(language, findValue, page, 0).collect(Collectors.toList());// 0 is user
        String maxPage = getMaxSearchPages(language, findValue);
        return new PetList(list, maxPage);
    }

    @Override
    public PetList adminPetList(String language, String findValue, String species, String  breed, String gender, String status, String searchCriteria, String searchOrder, String minPrice, String maxPrice, String page) {
        if (findValue == null) return adminFilteredList(language, species, breed, gender, status, searchCriteria, searchOrder, minPrice, maxPrice, page);
        return adminFind(language, findValue, status, page);
    }

    @Override
    public PetList adminFilteredList(String language, String species, String breed, String gender, String status, String searchCriteria, String searchOrder, String minPrice, String maxPrice, String page) {
        List<Pet> list = petDao.adminFilteredList(language, species, breed, gender, status, searchCriteria, searchOrder, page).collect(Collectors.toList());
        String maxPage = getMaxAdminFilterPages(language, species, breed, gender, status);
        return new PetList(list, maxPage);
    }

    @Override
    public PetList adminFind(String language, String findValue, String status, String page) {
        List<Pet> list = petDao.find(language, findValue, page, 1).collect(Collectors.toList()); //1 is admin
        String maxPage = getAdminMaxSearchPages(language, findValue);
        return new PetList(list, maxPage);
    }

    @Override
    public Optional<Pet> findById(String language, long id) {
        return petDao.findById(language, id, 0);
    }

    @Override
    public Optional<Pet> adminFindById(String language, long id) {
        return petDao.findById(language, id, 1);
    }


    @Override
    public List<Pet> list(String language,String page){

        return petDao.list(language, page, 0).collect(Collectors.toList());// 0 is user
    }

    @Override
    public List<Pet> listAll(String language) {
        return petDao.listAll(language).collect(Collectors.toList());
    }

    @Override
    public List<Pet> getByUserId(String language, long ownerId, String page) {
        return petDao.getByUserId(language, ownerId, page).collect(Collectors.toList());
    }

    @Override
    public List<Pet> adminList(String language, String page) {
        return petDao.list(language, page, 1).collect(Collectors.toList());// 1 is admin
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

        Optional<Status> opStatus = petDao.findStatusById(language, PetStatus.AVAILABLE.getValue());
        if (!opStatus.isPresent()) {
            LOGGER.warn("Status {} not found, pet creation failed", PetStatus.AVAILABLE.getValue());
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
        petDao.updateAllByOwner(ownerId, PetStatus.REMOVED.getValue());
    }

    @Override
    public String getMaxPages() {
        return petDao.maxPages(0);
    } // 0 is user

    @Override
    public String getMaxSearchPages(String language, String findValue) {
        return petDao.maxSearchPages(language, findValue, 0); //0 is user
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
        return petDao.maxSearchPages(language, find, 1); //1 is admin
    }

    @Override
    public String getAdminMaxPages() {
        return petDao.maxPages(1); //level 1 is admin

    }

    @Override
    public Optional<Contact> getPetContact(long petId) {
        return petDao.getPetContact(petId);
    }

    @Override
    public boolean sellPet(long petId, long userId) {
        if (petDao.isPetOwner(petId, userId)) {
            petDao.updateStatus(petId, PetStatus.SOLD.getValue());
            return true;
        }
        return false;
    }

    @Override
    public boolean removePet(long petId, long userId) {
        if (petDao.isPetOwner(petId, userId)) {
            petDao.updateStatus(petId, PetStatus.REMOVED.getValue());
            return true;
        }
        return false;
    }

    @Override
    public void removePetAdmin(long petId) {
        petDao.updateStatus(petId, PetStatus.REMOVED.getValue());
    }

    @Override
    public void sellPetAdmin(long petId) {
        petDao.updateStatus(petId, PetStatus.SOLD.getValue());
    }

    @Override
    public void recoverPetAdmin(long petId) {
        petDao.updateStatus(petId, PetStatus.AVAILABLE.getValue());
    }

    @Override
    public boolean isPetOwner(long petId, long userId) {
        return petDao.isPetOwner(petId, userId);
    }
}
