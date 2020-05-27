package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.interfaces.exception.InvalidImageQuantityException;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.constants.PetStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PetServiceImpl implements PetService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PetServiceImpl.class);

    static final long AVAILABLE_STATUS = 1;
    static final long REMOVED_STATUS = 2;
    static final long SOLD_STATUS = 3;

    private static final int PETS_PER_PAGE = 12;
    private static final int PETS_IN_USER_PAGE = 4;
    private static final int ADMIN_SHOWCASE_ITEMS = 25;

    static final int USER_LEVEL = 0;
    static final int ADMIN_LEVEL = 1;

    static final int MIN_IMAGES = 1;
    static final int MAX_IMAGES = 5;

    @Autowired
    private PetDao petDao;
    @Autowired
    private SpeciesDao speciesDao;
    @Autowired
    private ImageService imageService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private RequestService requestService;



    @Override
    public Optional<Pet> findById(String language, long id) {
        return petDao.findById(language, id);
    }

    @Override
    public Optional<Pet> adminFindById(String language, long id) {
        return petDao.findById(language, id);
    }

    @Override
    public PetList petList(String language, String findValue, String species, String  breed, String gender, String searchCriteria,
                           String searchOrder, String minPrice, String maxPrice, String province, String department, String page) {

        if (findValue == null) return filteredList(language, species, breed, gender, searchCriteria, searchOrder,
                minPrice, maxPrice, province, department, page);
        return find(language, findValue, page);
    }

    @Override
    public PetList filteredList(String language, String species, String  breed, String gender, String searchCriteria, String searchOrder,
                                String minPrice, String maxPrice, String province, String department, String page) {
        LOGGER.debug("Filter parameters: species: {}, breed: {}, gender: {}, searchCriteria: {}, searchOrder: {}, minPrice: {}, " +
                "maxPrice: {}, province: {}, department: {}",species, breed, gender, searchCriteria, searchOrder,
                minPrice, maxPrice,  province, department);
        List<Pet> list = petDao.filteredList(language,species, breed, gender, searchCriteria, searchOrder,
                minPrice, maxPrice,  province, department, page).collect(Collectors.toList());
        int pets = getFilteredPetsAmount(language, species, breed, gender, minPrice, maxPrice, province, department);
        String maxPage = getMaxFilterPages(pets);
        return new PetList(list, maxPage, pets);
    }

    @Override
    public PetList find(String language,String findValue, String page){
        List<Pet> list = petDao.find(language, findValue, page, USER_LEVEL).collect(Collectors.toList());
        int pets = getSearchPetsAmount(language, findValue);
        String maxPage = getMaxSearchPages(pets);
        return new PetList(list, maxPage,pets);
    }

    @Override
    public List<String> autocompleteFind(String language, String findValue){
        return petDao.autocompleteFind(language, findValue).collect(Collectors.toList());
    }

    @Override
    public PetList adminPetList(String language, String findValue, String species, String  breed, String gender, String status, String searchCriteria, String searchOrder, String minPrice, String maxPrice, String page) {
        if (findValue == null) return adminFilteredList(language, species, breed, gender, status, searchCriteria, searchOrder, minPrice, maxPrice, page);
        return adminFind(language, findValue, status, page);
    }

    @Override
    public PetList adminFilteredList(String language, String species, String breed, String gender, String status, String searchCriteria, String searchOrder, String minPrice, String maxPrice, String page) {
        List<Pet> list = petDao.adminFilteredList(language, species, breed, gender, status, searchCriteria, searchOrder, page).collect(Collectors.toList());
        int pets = getAdminFilteredPetsAmount(language, species, breed, gender, status);
        String maxPage = getMaxAdminFilterPages(pets);
        return new PetList(list, maxPage, pets);
    }

    @Override
    public PetList adminFind(String language, String findValue, String status, String page) {
        List<Pet> list = petDao.find(language, findValue, page, 1).collect(Collectors.toList()); //1 is admin
        int pets = getAdminSearchPetsAmount(language, findValue);
        String maxPage = getAdminMaxSearchPages(pets);
        return new PetList(list, maxPage, pets);
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
    public PetList getByUserId(String language, long ownerId, String page) {
        int pets = userPetsAmount(ownerId);
        String maxPages = getMaxUserPetsPages(pets);
        return new PetList(petDao.getByUserId(language, ownerId, page).collect(Collectors.toList()), maxPages, pets);
    }

    @Override
    public List<Pet> adminList(String language, String page) {
        return petDao.list(language, page, ADMIN_LEVEL).collect(Collectors.toList());
    }

    @Override
    public Optional<Pet> create(String language, String petName, long speciesId, long breedId,boolean vaccinated, String gender,
                                String description, Date birthDate, Date uploadDate, int price, long ownerId, long department, List<byte[]> photos) {
        LOGGER.debug("Attempting to create pet with name: {}, species: {}, breed: {}, department: {}, vaccinated: {}, gender: {}, description: {}, birthdate: {}, upDate: {}, price: {}, owner: {}",
                petName, speciesId, breedId, department, vaccinated, gender, description, birthDate, uploadDate, price, ownerId);


        if(!locationService.findDepartmentById(department).isPresent()){
            LOGGER.warn("Department {} not found, pet update failed", department);
            return Optional.empty();
        }
        Optional<Species> opSpecies = speciesDao.findSpeciesById(speciesId);
        if (!opSpecies.isPresent()) {
            LOGGER.warn("Species {} not found, pet creation failed", speciesId);
            return Optional.empty();
        }
        Species species = opSpecies.get();

        Optional<Breed> opBreed = speciesDao.findBreedById(breedId);
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

        long id = petDao.create(petName, species, breed, vaccinated, gender, description, birthDate, uploadDate, price, ownerId, status, department);
        LOGGER.debug("Pet id: {} successfully created", id);

        for (byte[] photo : photos) {
            imageService.create(id, photo, ownerId);
        }
        Optional<Pet> opPet = findById(language, id);
        if(!opPet.isPresent()){
            LOGGER.warn("Pet creation failed");
            return Optional.empty();
        }
        return opPet;
    }

    @Override
    public long getOwnerId(long petId) {
        return petDao.getOwnerId(petId);
    }

    @Override
    public void removeAllByOwner(long ownerId) {
        requestService.rejectAllByPetOwner(ownerId);
        petDao.updateAllByOwner(ownerId, PetStatus.REMOVED.getValue());
    }

    @Override
    public Optional<Pet> update(String language, long userId, long id, List<byte[]> photos, List<Integer> imagesToDelete, String petName, long speciesId, long breedId,
                       boolean vaccinated, String gender, String description, Date birthDate, int price, long department) throws InvalidImageQuantityException {
        LOGGER.debug("Attempting user update of pet {} with: petName: {}, speciesId: {}, breedId: {}, " +
                        "vaccinated: {}, gender: {}, description: {}, birthDate: {}, price: {}, department: {},",
                id, petName, speciesId, breedId, vaccinated, gender, description, birthDate, price, department);

        if(! petDao.isPetOwner(id, userId)) {
            LOGGER.warn("Logged user is not the owner of pet {}, update aborted", id);
            return Optional.empty();
        }
        if(!locationService.findDepartmentById(department).isPresent()){
            LOGGER.warn("Department {} not found, pet update failed", department);
            return Optional.empty();
        }
        if (!speciesDao.findSpeciesById(speciesId).isPresent()) {
            LOGGER.warn("Species {} not found, pet update failed", speciesId);
            return Optional.empty();
        }
        if (!speciesDao.findBreedById(breedId).isPresent()) {
            LOGGER.warn("Breed {} not found, pet update failed", breedId);
            return Optional.empty();
        }
        int toDelete;
        if(imagesToDelete == null){
            toDelete = 0;
        }
        else {
            toDelete = imagesToDelete.size();
        }
        int previousImageQuantity = imageService.quantityByPetId(id);
        int finalImageQuantity = previousImageQuantity + photos.size() - toDelete;
        if(finalImageQuantity < MIN_IMAGES || finalImageQuantity > MAX_IMAGES) {
            throw new InvalidImageQuantityException("Pet must have between 1 and 5 images");
        }
        if(imagesToDelete != null ) {
            LOGGER.debug("Deleting from pet {} images {}", id, imagesToDelete);
            imageService.delete(imagesToDelete);
        }
        if(photos != null) {
            for (byte[] photo : photos) {
                LOGGER.debug("Adding image to pet {}", id);
                imageService.create(id, photo, userId);
            }
        }
        petDao.update(id, petName, speciesId, breedId, vaccinated, gender, description, birthDate, price, department);
        Optional<Pet> opPet = petDao.findById(language, id);
        if (!opPet.isPresent()){
            LOGGER.debug("Pet {} update failed", id);
            return Optional.empty();
        }
        LOGGER.debug("Pet {} successfully updated", opPet.get());


        return opPet;
    }

    @Override
    public String getMaxPages() {
        int pets = petDao.maxPetsAmount(USER_LEVEL);
        pets = (int) Math.ceil((double) pets / PETS_PER_PAGE);
        return Integer.toString(pets);
    }

    @Override
    public int getPetsAmount(){
        return petDao.maxPetsAmount(USER_LEVEL);
    }

    @Override
    public int getAdminPetsAmount(){
        return petDao.maxPetsAmount(ADMIN_LEVEL);
    }

    @Override
    public int getSearchPetsAmount(String language, String findValue){
        return petDao.maxSearchPetsAmount(language, findValue, USER_LEVEL);
    }

    @Override
    public int getAdminSearchPetsAmount(String language, String findValue){
        return petDao.maxSearchPetsAmount(language, findValue, ADMIN_LEVEL);
    }

    @Override
    public int getFilteredPetsAmount(String language, String specieFilter, String breedFilter, String genderFilter, String minPrice, String maxPrice, String province, String department) {
        return petDao.maxFilteredPetsAmount(language, specieFilter, breedFilter, genderFilter, minPrice, maxPrice, province, department);
    }

    @Override
    public int getAdminFilteredPetsAmount(String language, String specieFilter, String breedFilter, String genderFilter, String statusFilter) {
        return petDao.maxAdminFilteredPetsAmount(language, specieFilter, breedFilter, genderFilter, statusFilter);
    }

    @Override
    public int userPetsAmount(long userId) {
        return petDao.maxUserPetsAmount(userId);
    }

    @Override
    public String getMaxSearchPages(int pets) {
        int aux = (int) Math.ceil((double) pets / PETS_PER_PAGE);
        return Integer.toString(aux);
    }

    @Override
    public String getMaxFilterPages(int pets) {
        int aux = (int) Math.ceil((double) pets / PETS_PER_PAGE);
        return Integer.toString(aux);
    }

    @Override
    public String getMaxAdminFilterPages(int pets) {
        int aux = (int) Math.ceil((double) pets / ADMIN_SHOWCASE_ITEMS);
        return Integer.toString(aux);
    }

    @Override
    public String getMaxUserPetsPages(int pets) {
        int aux = (int) Math.ceil((double) pets / PETS_IN_USER_PAGE);
        return Integer.toString(aux);

    }

    @Override
    public String getAdminMaxSearchPages(int pets) {
        int aux = (int) Math.ceil((double) pets / ADMIN_SHOWCASE_ITEMS);
        return Integer.toString(aux);
    }

    @Override
    public String getAdminMaxPages() {
        int pets = petDao.maxPetsAmount(ADMIN_LEVEL);
        pets = (int) Math.ceil((double) pets / ADMIN_SHOWCASE_ITEMS);
        return Integer.toString(pets);
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

            requestService.rejectAllByPet(petId);
            petDao.updateStatus(petId, PetStatus.REMOVED.getValue());
            return true;
        }
        return false;
    }

    @Override
    public boolean recoverPet(long petId, long userId) {
        if (petDao.isPetOwner(petId, userId)) {
            petDao.updateStatus(petId, PetStatus.AVAILABLE.getValue());
            return true;
        }
        return false;
    }

    @Override
    public void removePetAdmin(long petId) {
        requestService.rejectAllByPet(petId);
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

    @Override
    public Optional<Pet> adminUpdate(String language, long userId, long id, List<byte[]> photos, List<Integer> imagesToDelete, String petName,
                                     long speciesId, long breedId, boolean vaccinated, String gender, String description, Date birthDate, int price, long department) throws InvalidImageQuantityException {
        LOGGER.debug("Attempting user update of pet {} with: petName: {}, speciesId: {}, breedId: {}, department: {}, " +
                        "vaccinated: {}, gender: {}, description: {}, birthDate: {}, price: {}",
                id, petName, speciesId, breedId, department, vaccinated, gender, description, birthDate, price);


        if (!speciesDao.findSpeciesById(speciesId).isPresent()) {
            LOGGER.warn("Species {} not found, pet update failed", speciesId);
            return Optional.empty();
        }
        if (!speciesDao.findBreedById(breedId).isPresent()) {
            LOGGER.warn("Breed {} not found, pet update failed", breedId);
            return Optional.empty();
        }
        int toDelete;
        if(imagesToDelete == null){
            toDelete = 0;
        }
        else {
            toDelete = imagesToDelete.size();
        }
        int previousImageQuantity = imageService.quantityByPetId(id);
        int finalImageQuantity = previousImageQuantity + photos.size() - toDelete;
        if(finalImageQuantity < MIN_IMAGES || finalImageQuantity > MAX_IMAGES) {
            throw new InvalidImageQuantityException("Pet must have between 1 and 5 images");
        }
        if(imagesToDelete != null ) {
            LOGGER.debug("Deleting from pet {} images {}", id, imagesToDelete);
            imageService.delete(imagesToDelete);
        }
        if(photos != null) {
            for (byte[] photo : photos) {
                LOGGER.debug("Adding image to pet {}", id);
                imageService.createAdmin(id, photo);
            }
        }
        petDao.update(id, petName, speciesId, breedId, vaccinated, gender, description, birthDate, price, department);
        Optional<Pet> opPet = petDao.findById(language, id);
        if (!opPet.isPresent()){
            LOGGER.debug("Pet {} update failed", id);
            return Optional.empty();
        }
        LOGGER.debug("Pet {} successfully updated", opPet.get());


        return opPet;
    }
}
