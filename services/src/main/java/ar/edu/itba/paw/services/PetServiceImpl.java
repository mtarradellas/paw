package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.interfaces.exception.InvalidImageQuantityException;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.constants.PetStatus;
import ar.edu.itba.paw.models.constants.UserStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
public class PetServiceImpl implements PetService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PetServiceImpl.class);

    private static final int MIN_IMAGES = 1;
    private static final int MAX_IMAGES = 5;

    @Autowired
    PetDao petDao;

    @Autowired
    UserService userService;
    @Autowired
    SpeciesService speciesService;
    @Autowired
    ImageService imageService;
    @Autowired
    LocationService locationService;
    @Autowired
    RequestService requestService;

    @Override
    public List<Pet> list(String locale, int page, int pageSize) {
        List<Pet> petList = petDao.list(page, pageSize);
        setLocale(locale, petList);
        return petList;
    }

    @Override
    public List<Pet> filteredList(String locale, List<String> find, Long userId, Long speciesId, Long breedId, String gender,
                                  PetStatus status, String searchCriteria, String searchOrder, int minPrice, int maxPrice,
                                  Long provinceId, Long departmentId, int page, int pageSize) {
        List<Pet> petList;
            User user = null;
            Breed breed = null;
            Species species = null;
            Department department = null;
            Province province = null;

            if (userId != null) user = userService.findById(userId).orElse(null);
            breed = validateBreed(breedId, speciesId);
            if (breed != null) species = breed.getSpecies();
            else species = validateSpecies(speciesId);

            department = validateDepartment(departmentId, provinceId);
            if (department != null) province = department.getProvince();
            else province = validateProvince(provinceId);

            LOGGER.debug("Parameters for filteredList <Pet>: user {}, status {}, species {}, breed {}, gender {},  " +
                            "min price {}, max price {}, province {}, department {}, searchCriteria {}, searchOrder {}, page {}, pageSize {}", user, status, species, breed,
                    gender, minPrice, maxPrice, province, department, searchCriteria, searchOrder, page, pageSize);

            petList = petDao.searchList(locale, find, user, species, breed, gender, status, searchCriteria, searchOrder,
                    minPrice, maxPrice, province, department, page, pageSize);

        setLocale(locale, petList);
        return petList;
    }

    @Override
    public List<Pet> listByUser(String locale, Long userId, int page, int pageSize) {
        if (userId != null && !userService.findById(userId).isPresent()) userId = null;
        return filteredList(locale,null,  userId, null, null, null, null,
                null, null,0, -1, null,null, page, pageSize);
    }

    @Override
    public int getListAmount() {
        return petDao.getListAmount();
    }

    @Override
    public int getFilteredListAmount(String locale, List<String> find, Long userId, Long speciesId, Long breedId, String gender, PetStatus status,
                                     int minPrice, int maxPrice, Long provinceId, Long departmentId) {
            User user = null;
            Breed breed = null;
            Species species = null;
            Department department = null;
            Province province = null;

            if (userId != null) user = userService.findById(userId).orElse(null);
            breed = validateBreed(breedId, speciesId);
            if (breed != null) species = breed.getSpecies();
            else species = validateSpecies(speciesId);

            department = validateDepartment(departmentId, provinceId);
            if (department != null) province = department.getProvince();
            else province = validateProvince(provinceId);

            return petDao.getSearchListAmount(locale, find, user, species, breed, gender, status,minPrice, maxPrice, province, department);
    }

    @Override
    public int getListByUserAmount(String locale, Long userId) {
        return getFilteredListAmount(locale, null, userId, null, null, null, null,
                0, -1, null, null);
    }

    private Breed validateBreed(Long breedId, Long speciesId) {
        if (breedId == null || speciesId == null) return null;
        Optional<Breed> opBreed = speciesService.findBreedById(breedId);
        if (!opBreed.isPresent()) return null;
        Breed breed = opBreed.get();
        Optional<Species> opSpecies = speciesService.findSpeciesById(speciesId);
        if (!opSpecies.isPresent()) return null;
        Species species = opSpecies.get();
        if (!breed.getSpecies().getId().equals(species.getId())) return null;
        return breed;
    }

    private Species validateSpecies(Long speciesId) {
        if (speciesId == null) return null;
        Optional<Species> opSpecies = speciesService.findSpeciesById(speciesId);
        return opSpecies.orElse(null);
    }

    private Department validateDepartment(Long departmentId, Long provinceId) {
        if (departmentId == null || provinceId == null) return null;

        Optional<Department> opDepartment = locationService.findDepartmentById(departmentId);
        if (!opDepartment.isPresent()) return null;
        Department department = opDepartment.get();

        Optional<Province> opProvince = locationService.findProvinceById(provinceId);
        if (!opProvince.isPresent()) return null;
        Province province = opProvince.get();

        if (!department.getProvince().getId().equals(province.getId())) return null;
        return department;
    }

    private Province validateProvince(Long provinceId) {
        if (provinceId == null) return null;
        Optional<Province> opProvince = locationService.findProvinceById(provinceId);
        return opProvince.orElse(null);
    }

    @Override
    public Optional<Pet> findById(String locale, long id) {
        Optional<Pet> opPet = petDao.findById(id);
        opPet.ifPresent(pet -> setLocale(locale, pet));
        return opPet;
    }

    @Override
    public Optional<Pet> findById(long id) {
        return petDao.findById(id);
    }

    @Transactional
    @Override
    public Optional<Pet> create(String locale, String petName, Date birthDate, String gender, boolean vaccinated, int price,
                      String description, PetStatus status, long userId, long speciesId, long breedId, long provinceId, long departmentId, List<byte[]> photos) {

        LOGGER.debug("Attempting to create pet with name: {}, species: {}, breed: {}, department: {}, province: {}, vaccinated: {}, gender: {}, description: {}, birthdate: {}, price: {}, owner: {}",
               petName, speciesId, breedId, departmentId, provinceId, vaccinated, gender, description, birthDate, price, userId);


        Optional<Department> opDepartment = locationService.findDepartmentById(departmentId);
        if(!opDepartment.isPresent()) {
            LOGGER.warn("Department {} not found, pet creation failed", departmentId);
            return Optional.empty();
        }
        Department department = opDepartment.get();

        Optional<Province> opProvince = locationService.findProvinceById(provinceId);
        if(!opProvince.isPresent()) {
            LOGGER.warn("Province {} not found, pet creation failed", departmentId);
            return Optional.empty();
        }
        Province province = opProvince.get();

        if (!department.getProvince().equals(province)) {
            LOGGER.warn("Department {} does not belong to province {}, pet creation failed", departmentId, provinceId);
            return Optional.empty();
        }

        Optional<Species> opSpecies = speciesService.findSpeciesById(locale, speciesId);
        if (!opSpecies.isPresent()) {
            LOGGER.warn("Species {} not found, pet creation failed", speciesId);
            return Optional.empty();
        }
        Species species = opSpecies.get();

        Optional<Breed> opBreed = speciesService.findBreedById(locale, breedId);
        if (!opBreed.isPresent()) {
            LOGGER.warn("Breed {} not found, pet creation failed", breedId);
            return Optional.empty();
        }
        Breed breed = opBreed.get();

        if (!breed.getSpecies().equals(species)) {
            LOGGER.warn("Breed {} does not belong to Species {}, pet creation failed", breedId, speciesId);
            return Optional.empty();
        }

        Optional<User> opUser = userService.findById(userId);
        if (!opUser.isPresent()) {
            LOGGER.warn("User {} not found, pet creation failed", userId);
            return Optional.empty();
        }
        User user = opUser.get();

        if (!user.getStatus().equals(UserStatus.ACTIVE)) {
            LOGGER.warn("User {} is not active, pet creation failed", userId);
        }

        java.util.Date today = new java.util.Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        today = cal.getTime();

        Pet pet = petDao.create(petName, birthDate, gender, vaccinated, price, (Date) today, description, status, user,
                species, breed, province, department);

        LOGGER.debug("Pet id: {} successfully created", pet);

        for (byte[] photo : photos) {
            imageService.create(pet.getId(), photo, user.getId());
        }

        return Optional.of(pet);
    }

    @Transactional
    @Override
    public Optional<Pet> update(Pet pet) {
        return petDao.update(pet);
    }

    @Transactional
    @Override
    public Optional<Pet> update(String locale, long id, Long userId, String petName, Date birthDate, String gender,
                                boolean vaccinated, int price, String description, PetStatus status, long speciesId,
                                long breedId, long provinceId, long departmentId, List<byte[]> photos, List<Long> imagesToDelete) {
        LOGGER.debug("Attempting user {} update of pet {} with: petName: {}, speciesId: {}, breedId: {}, " +
                        "vaccinated: {}, gender: {}, description: {}, birthDate: {}, price: {}, department: {},",
                userId, id, petName, speciesId, breedId, vaccinated, gender, description, birthDate, price, departmentId);

        Optional<Pet> opPet = petDao.findById(id);
        if (!opPet.isPresent()) {
            LOGGER.warn("Pet {} not found, pet update failed", id);
            return Optional.empty();
        }
        Pet pet = opPet.get();

        if (status == null) status = pet.getStatus();

        // If userId = null then dont check user credentials (user is admin)
        if (userId != null) {
            Optional<User> opUser = userService.findById(userId);
            if (!opUser.isPresent()) {
                LOGGER.warn("User {} not found, pet update failed", userId);
                return Optional.empty();
            }
            User user = opUser.get();

            if (!user.getStatus().equals(UserStatus.ACTIVE)) {
                LOGGER.warn("User {} is not active, pet update failed", userId);
            }

            if(!pet.getUser().equals(user)) {
                LOGGER.warn("Logged user is not the owner of pet {}, update aborted", id);
                return Optional.empty();
            }
        }


        Optional<Department> opDepartment = locationService.findDepartmentById(departmentId);
        if(!opDepartment.isPresent()) {
            LOGGER.warn("Department {} not found, pet update failed", departmentId);
            return Optional.empty();
        }
        Department department = opDepartment.get();

        Optional<Province> opProvince = locationService.findProvinceById(provinceId);
        if(!opProvince.isPresent()) {
            LOGGER.warn("Province {} not found, pet update failed", departmentId);
            return Optional.empty();
        }
        Province province = opProvince.get();

        if (!department.getProvince().equals(province)) {
            LOGGER.warn("Department {} does not belong to province {}, pet update failed", departmentId, provinceId);
            return Optional.empty();
        }

        Optional<Species> opSpecies = speciesService.findSpeciesById(locale, speciesId);
        if (!opSpecies.isPresent()) {
            LOGGER.warn("Species {} not found, pet update failed", speciesId);
            return Optional.empty();
        }
        Species species = opSpecies.get();

        Optional<Breed> opBreed = speciesService.findBreedById(locale, breedId);
        if (!opBreed.isPresent()) {
            LOGGER.warn("Breed {} not found, pet update failed", breedId);
            return Optional.empty();
        }
        Breed breed = opBreed.get();

        if (!breed.getSpecies().getId().equals(species.getId())) {
            LOGGER.warn("Breed {} does not belong to Species {}, pet update failed", breedId, speciesId);
            return Optional.empty();
        }

        int toDelete;
        if(imagesToDelete == null){
            toDelete = 0;
        }
        else {
            toDelete = imagesToDelete.size();
        }
        int previousImageQuantity = pet.getImages().size();
        int finalImageQuantity = previousImageQuantity + photos.size() - toDelete;
        if(finalImageQuantity < MIN_IMAGES || finalImageQuantity > MAX_IMAGES) {
            throw new InvalidImageQuantityException("Pet must have between 1 and 5 images");
        }
        if(imagesToDelete != null ) {
            LOGGER.debug("Deleting from pet {} images {}", id, imagesToDelete);
            imageService.delete( imagesToDelete);
        }
        if(!photos.isEmpty()) {
            for (byte[] photo : photos) {
                LOGGER.debug("Adding image to pet {}", id);
                imageService.create(id, photo, userId);
            }
        }

        pet.setPetName(petName);
        pet.setBirthDate(birthDate);
        pet.setGender(gender);
        pet.setVaccinated(vaccinated);
        pet.setPrice(price);
        pet.setDescription(description);
        pet.setStatus(status);
        pet.setSpecies(species);
        pet.setBreed(breed);
        pet.setProvince(province);
        pet.setDepartment(department);

        if (!petDao.update(pet).isPresent()){
            LOGGER.debug("Pet {} update failed", id);
            return Optional.empty();
        }
        LOGGER.debug("Pet {} successfully updated", pet.getId());

        return Optional.of(pet);
    }

    @Transactional
    @Override
    public boolean sellPet(long petId, User user) {
        Optional<Pet> opPet = petDao.findById(petId);
        if (!opPet.isPresent()) {
            LOGGER.warn("Pet {} not found", petId);
            return false;
        }
        Pet pet = opPet.get();

        if (pet.getUser().equals(user)) {
            pet.setStatus(PetStatus.SOLD);
            return petDao.update(pet).isPresent();
        }
        return false;
    }

    @Transactional
    @Override
    public boolean removePet(long petId, User user) {
        Optional<Pet> opPet = petDao.findById(petId);
        if (!opPet.isPresent()) {
            LOGGER.warn("Pet {} not found", petId);
            return false;
        }
        Pet pet = opPet.get();

        if (pet.getUser().equals(user)) {
            requestService.rejectAllByPet("LENIA", petId);
            pet.setStatus(PetStatus.REMOVED);
            return petDao.update(pet).isPresent();
        }
        return false;
    }

    @Transactional
    @Override
    public boolean recoverPet(long petId, User user) {
        Optional<Pet> opPet = petDao.findById(petId);
        if (!opPet.isPresent()) {
            LOGGER.warn("Pet {} not found", petId);
            return false;
        }
        Pet pet = opPet.get();

        if (pet.getUser().equals(user)) {
            pet.setStatus(PetStatus.AVAILABLE);
            return petDao.update(pet).isPresent();
        }
        return false;
    }

    @Transactional
    @Override
    public boolean adminSellPet(long petId) {
        Optional<Pet> opPet = petDao.findById(petId);
        if (!opPet.isPresent()) {
            LOGGER.warn("Pet {} not found", petId);
            return false;
        }
        Pet pet = opPet.get();
        pet.setStatus(PetStatus.SOLD);
        return petDao.update(pet).isPresent();
    }

    @Transactional
    @Override
    public boolean adminRemovePet(long petId) {
        Optional<Pet> opPet = petDao.findById(petId);
        if (!opPet.isPresent()) {
            LOGGER.warn("Pet {} not found", petId);
            return false;
        }
        Pet pet = opPet.get();
        requestService.rejectAllByPet("LENIA", petId);
        pet.setStatus(PetStatus.REMOVED);
        return petDao.update(pet).isPresent();
    }

    @Transactional
    @Override
    public boolean adminRecoverPet(long petId) {
        Optional<Pet> opPet = petDao.findById(petId);
        if (!opPet.isPresent()) {
            LOGGER.warn("Pet {} not found", petId);
            return false;
        }
        Pet pet = opPet.get();
        pet.setStatus(PetStatus.AVAILABLE);
        return petDao.update(pet).isPresent();
    }

    @Transactional
    @Override
    public void removeAllByUser(User user) {
        requestService.rejectAllByPetOwner(user.getId());
        petDao.updateByStatusAndOwner(user, PetStatus.AVAILABLE, PetStatus.REMOVED);
    }

    @Override
    public List<String> autocompleteFind(String locale, String find) {
        return petDao.autocompleteFind(locale, find);
    }

    @Override
    public void setLocale(String locale, Pet pet) {
        pet.setLocale(locale);
    }

    @Override
    public void setLocale(String locale, List<Pet> petList) {
        petList.forEach(pet -> pet.setLocale(locale));
    }

}
