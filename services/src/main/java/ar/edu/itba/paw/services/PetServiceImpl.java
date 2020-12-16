package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.interfaces.exceptions.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.constants.MailType;
import ar.edu.itba.paw.models.constants.PetStatus;
import ar.edu.itba.paw.models.constants.QuestionStatus;
import ar.edu.itba.paw.models.constants.UserStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PetServiceImpl implements PetService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PetServiceImpl.class);

    private static final int MIN_IMAGES = 1;
    private static final int MAX_IMAGES = 5;

    @Autowired
    private PetDao petDao;

    @Autowired
    private UserService userService;
    @Autowired
    private SpeciesService speciesService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private RequestService requestService;
    @Autowired
    private MailService mailService;

    @Override
    public List<Pet> list(String locale, int page, int pageSize) {
        List<Pet> petList = petDao.list(page, pageSize);
        setLocale(locale, petList);
        return petList;
    }

    @Override
    public List<Pet> filteredList(String locale, List<String> find, Long userId, Long newOwnerId, Long speciesId, Long breedId, String gender,
                                  PetStatus status, String searchCriteria, String searchOrder, int minPrice, int maxPrice,
                                  Long provinceId, Long departmentId, int page, int pageSize) {
        List<Pet> petList;
        User user = null;
        Breed breed = null;
        Species species = null;
        Department department = null;
        Province province = null;
        User newOwner = null;
        if (userId != null) {
            user = userService.findById(userId).orElse(null);
            if (user == null) throw new NotFoundException("User " + userId + " not found.");
        }
        if (newOwnerId != null) {
            newOwner = userService.findById(newOwnerId).orElse(null);
            if (newOwner == null) throw new NotFoundException("User " + newOwnerId + " not found.");
        }

        breed = validateBreed(breedId, speciesId);
        species = (breed != null)? breed.getSpecies() : validateSpecies(speciesId);

        department = validateDepartment(departmentId, provinceId);
        province = (department != null)? department.getProvince() : validateProvince(provinceId);

        LOGGER.debug("Parameters for filteredList <Pet>: user {}, status {}, species {}, breed {}, gender {},  " +
                    "min price {}, max price {}, province {}, department {}, searchCriteria {}, searchOrder {}, page {}, pageSize {}",
                    user, status, species, breed, gender, minPrice, maxPrice, province, department, searchCriteria, searchOrder, page, pageSize);


        petList = petDao.searchList(locale, find, user, newOwner, species, breed, gender, status, searchCriteria, searchOrder,
                    minPrice, maxPrice, province, department, page, pageSize);

        setLocale(locale, petList);
        return petList;
    }

    @Override
    public List<Breed> filteredBreedList(String locale, List<String> find, Long userId, Long speciesId, Long breedId, String gender,
                                         PetStatus status, int minPrice, int maxPrice, Long provinceId, Long departmentId) {
        User user = null;
        Breed breed = null;
        Species species = null;
        Department department = null;
        Province province = null;

        if (userId != null) {
            user = userService.findById(userId).orElse(null);
            if (user == null) throw new NotFoundException("User " + userId + " not found.");
        }

        breed = validateBreed(breedId, speciesId);
        species = (breed != null)? breed.getSpecies() : validateSpecies(speciesId);
        System.out.println("WWWWWWWWWWW"+breed+breedId);

        department = validateDepartment(departmentId, provinceId);
        province = (department != null)? department.getProvince() : validateProvince(provinceId);

        List<Breed> breedList = petDao.searchBreedList(locale, find, user, species, breed, gender, status, minPrice, maxPrice, province, department);
        speciesService.setBreedLocale(locale, breedList);
        return breedList;
    }

    @Override
    public List<Department> filteredDepartmentList(String locale, List<String> find, Long userId, Long speciesId, Long breedId, String gender,
                                                   PetStatus status, int minPrice, int maxPrice, Long provinceId, Long departmentId) {
        User user = null;
        Breed breed = null;
        Species species = null;
        Department department = null;
        Province province = null;

        if (userId != null) {
            user = userService.findById(userId).orElse(null);
            if (user == null) throw new NotFoundException("User " + userId + " not found.");
        }

        breed = validateBreed(breedId, speciesId);
        species = (breed != null)? breed.getSpecies() : validateSpecies(speciesId);

        department = validateDepartment(departmentId, provinceId);
        province = (department != null)? department.getProvince() : validateProvince(provinceId);

        return petDao.searchDepartmentList(locale, find, user, species, breed, gender, status, minPrice, maxPrice, province, department);
    }

    @Override
    public Set<Integer> filteredRangesList(String locale, List<String> find, Long userId, Long speciesId, Long breedId, String gender,
                                         PetStatus status, int minPrice, int maxPrice, Long provinceId, Long departmentId) {
        User user = null;
        Breed breed = null;
        Species species = null;
        Department department = null;
        Province province = null;

        if (userId != null) {
            user = userService.findById(userId).orElse(null);
            if (user == null) throw new NotFoundException("User " + userId + " not found.");
        }

        breed = validateBreed(breedId, speciesId);
        species = (breed != null)? breed.getSpecies() : validateSpecies(speciesId);

        department = validateDepartment(departmentId, provinceId);
        province = (department != null)? department.getProvince() : validateProvince(provinceId);

        return petDao.searchRangesList(locale, find, user, species, breed, gender, status, minPrice, maxPrice, province, department);
    }

    @Override
    public Set<String> filteredGenderList(String locale, List<String> find, Long userId, Long speciesId, Long breedId, String gender, PetStatus status, int minPrice, int maxPrice, Long provinceId, Long departmentId) {
        User user = null;
        Breed breed = null;
        Species species = null;
        Department department = null;
        Province province = null;

        if (userId != null) {
            user = userService.findById(userId).orElse(null);
            if (user == null) throw new NotFoundException("User " + userId + " not found.");
        }

        breed = validateBreed(breedId, speciesId);
        species = (breed != null)? breed.getSpecies() : validateSpecies(speciesId);

        department = validateDepartment(departmentId, provinceId);
        province = (department != null)? department.getProvince() : validateProvince(provinceId);

        return petDao.searchGenderList(locale, find, user, species, breed, gender, status, minPrice, maxPrice, province, department);
    }

    @Override
    public List<Pet> listByUser(String locale, Long userId, int page, int pageSize) {
        if (userId == null) return filteredList(locale,null,  userId, null,null, null, null, null, null, null,0, -1, null,null, page, pageSize);
        List<Pet> petList = petDao.listByUser(userId, page, pageSize);
        setLocale(locale, petList);
        return petList;
    }

    @Override
    public int getListAmount() {
        return petDao.getListAmount();
    }

    @Override
    public int getFilteredListAmount(String locale, List<String> find, Long userId, Long newOwnerId, Long speciesId, Long breedId, String gender, PetStatus status,
                                     int minPrice, int maxPrice, Long provinceId, Long departmentId) {
        User user = null;
        Breed breed = null;
        Species species = null;
        Department department = null;
        Province province = null;
        User newOwner = null;

        if (userId != null) user = userService.findById(userId).orElse(null);
        if (newOwnerId != null) newOwner = userService.findById(newOwnerId).orElse(null);
        breed = validateBreed(breedId, speciesId);
        if (breed != null) species = breed.getSpecies();
        else species = validateSpecies(speciesId);

        department = validateDepartment(departmentId, provinceId);
        if (department != null) province = department.getProvince();
        else province = validateProvince(provinceId);

        return petDao.getSearchListAmount(locale, find, user, newOwner, species, breed, gender, status,minPrice, maxPrice, province, department);
    }

    @Override
    public int getListByUserAmount(String locale, Long userId) {
        if (userId == null) return getFilteredListAmount(locale, null, userId, null,null, null, null, null,
                0, -1, null, null);
        return petDao.getListByUserAmount(userId);
    }

    private Breed validateBreed(Long breedId, Long speciesId) {
        if (breedId == null) return null;
        Optional<Breed> opBreed = speciesService.findBreedById(breedId);
        if (!opBreed.isPresent()) return null;
        Breed breed = opBreed.get();
        if(speciesId != null) {
            Optional<Species> opSpecies = speciesService.findSpeciesById(speciesId);
            if (!opSpecies.isPresent()) return null;
            Species species = opSpecies.get();
            if (!breed.getSpecies().getId().equals(species.getId())) throw new PetException("Species and breed don't match");
        }
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
    public Optional<Pet> create(String locale, String petName, LocalDateTime birthDate, String gender, boolean vaccinated, int price,
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

        if (!department.getProvince().getId().equals(province.getId())) {
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

        if (!breed.getSpecies().getId().equals(species.getId())) {
            LOGGER.warn("Breed {} does not belong to Species {}, pet creation failed", breedId, speciesId);
            return Optional.empty();
        }

        Optional<User> opUser = userService.findById(userId);
        if (!opUser.isPresent()) {
            LOGGER.warn("User {} not found, pet creation failed", userId);
            throw new UserException("Invalid user");
        }
        User user = opUser.get();

        if (!user.getStatus().equals(UserStatus.ACTIVE)) {
            LOGGER.warn("User {} is not active, pet creation failed", userId);
        }

        System.out.println("EEEEEEEEEEEEEEE"+status);

        Pet pet = petDao.create(petName, birthDate, gender, vaccinated, price, LocalDateTime.now(), description, status, user,
                species, breed, province, department);

        LOGGER.debug("Pet id: {} successfully created", pet);
if(photos != null) { //TODO sacar esto, las imagene no pueden ser nulll
    for (byte[] photo : photos) {
        imageService.create(pet.getId(), photo, user.getId());
    }
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
    public Optional<Pet> update(String locale, long id, Long userId, String petName, LocalDateTime birthDate, String gender,
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

            if(!pet.getUser().getId().equals(user.getId())) {
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

        if (!department.getProvince().getId().equals(province.getId())) {
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
    public void sellPet(long petId, long ownerId, long newOwnerId, String contextURL) {
        Optional<Pet> opPet = petDao.findById(petId);
        if (!opPet.isPresent()) throw new NotFoundException("Pet " + petId + " not found.");
        Pet pet = opPet.get();

        if (pet.getNewOwner() != null) {
            LOGGER.warn("Pet {} is already sold to user {}", petId, pet.getNewOwner().getId());
            throw new PetException("Pet already sold");
        }

        Optional<User> opOwner = userService.findById(ownerId);
        if (!opOwner.isPresent()) throw new NotFoundException("User " + opOwner + " not found.");
        User owner = opOwner.get();

        if (pet.getUser().getId().equals(owner.getId())) {
            Optional<User> opUser = userService.findById(newOwnerId);
            if (!opUser.isPresent()) throw new NotFoundException("Target new owner"+ newOwnerId +" was not found");

            pet.setNewOwner(opUser.get());
            pet.setStatus(PetStatus.SOLD);
            requestService.sell(pet, opUser.get());

            Map<String, Object> arguments = new HashMap<>();

            arguments.put("petURL", contextURL + "/pet/" + pet.getId());
            arguments.put("petName", pet.getPetName());
            arguments.put("ownerUsername", pet.getUser().getUsername());
            arguments.put("ownerURL", contextURL + "/user/" + pet.getUser().getId());

            String userLocale = pet.getNewOwner().getLocale();

            mailService.sendMail(pet.getNewOwner().getMail(), userLocale, arguments, MailType.PET_SOLD);

            boolean updated = petDao.update(pet).isPresent();
            if (updated) {
                requestService.rejectAllByPet(pet.getId());
            }
            return;
        }
        LOGGER.warn("Owner and logged user are not the same");
        throw new PetException("Owner and logged user are not the same");
    }

    @Transactional
    @Override
    public void removePet(long petId, long userId) {
        Optional<Pet> opPet = petDao.findById(petId);
        if (!opPet.isPresent()) throw new NotFoundException("Pet " + petId + " not found.");
        Pet pet = opPet.get();

        if (pet.getUser().getId().equals(userId)) {
            requestService.rejectAllByPet(petId);
            pet.setStatus(PetStatus.REMOVED);
            petDao.update(pet).orElseThrow(PetException::new);
            return;
        }
        LOGGER.warn("Owner and logged user are not the same");
        throw new PetException("Owner and logged user are not the same");
    }

    @Transactional
    @Override
    public void recoverPet(long petId, long userId) {
        Optional<Pet> opPet = petDao.findById(petId);
        if (!opPet.isPresent()) throw new NotFoundException("Pet " + petId + " not found.");
        Pet pet = opPet.get();

        if (pet.getUser().getId().equals(userId) && pet.getNewOwner() == null) {
            pet.setStatus(PetStatus.AVAILABLE);
            petDao.update(pet).orElseThrow(PetException::new);
            return;
        }
        LOGGER.warn("Owner and logged user are not the same");
        throw new PetException("Owner and logged user are not the same");
    }

    @Transactional
    @Override
    public void adminSellPet(long petId, long newOwnerId) {
        Optional<Pet> opPet = petDao.findById(petId);
        if (!opPet.isPresent()) {
            LOGGER.warn("Pet {} not found", petId);
            throw new NotFoundException("Pet " + petId + " not found.");
        }
        Pet pet = opPet.get();
        Optional<User> opNewOwner = userService.findById(newOwnerId);
        if (!opNewOwner.isPresent()) {
            LOGGER.warn("New owner {} not found", newOwnerId);
            throw new NotFoundException("New Owner " + newOwnerId + " not found.");
        }
        pet.setNewOwner(opNewOwner.get());
        pet.setStatus(PetStatus.SOLD);
        petDao.update(pet).orElseThrow(PetException::new);
    }

    @Transactional
    @Override
    public void adminRemovePet(long petId) {
        Optional<Pet> opPet = petDao.findById(petId);
        if (!opPet.isPresent()) {
            LOGGER.warn("Pet {} not found", petId);
            throw new NotFoundException("Pet " + petId + " not found.");
        }
        Pet pet = opPet.get();
        requestService.rejectAllByPet(petId);
        pet.setStatus(PetStatus.REMOVED);
        petDao.update(pet).orElseThrow(PetException::new);
    }

    @Transactional
    @Override
    public void adminRecoverPet(long petId) {
        Optional<Pet> opPet = petDao.findById(petId);
        if (!opPet.isPresent()) {
            LOGGER.warn("Pet {} not found", petId);
            throw new NotFoundException("Pet " + petId + " not found.");
        }
        Pet pet = opPet.get();
        pet.setStatus(PetStatus.AVAILABLE);
        petDao.update(pet).orElseThrow(PetException::new);
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

    @Override
    public List<Question> listQuestions(long petId, int page, int pageSize) {
        Optional<Pet> pet = petDao.findById(petId);
        if (pet.isPresent()) {
            return petDao.listQuestions(petId, page, pageSize);
        }
        else throw new NotFoundException("Pet " + petId + " not found.");
    }

    @Override
    public int getListQuestionsAmount(Long petId) {
        Optional<Pet> pet = petDao.findById(petId);
        if (pet.isPresent()) {
            return petDao.getListQuestionsAmount(petId);
        }
        else throw new NotFoundException("Pet " + petId + " not found.");
    }

    @Override
    public Optional<Question> findQuestionById(long id) {
        return petDao.findQuestionById(id);
    }

    @Override
    public Optional<Answer> findAnswerById(long id) {
        return petDao.findAnswerById(id);
    }

    @Transactional
    @Override
    public Optional<Question> createQuestion(String content, Long userId, long petId, String contextURL) {
        Optional<User> opUser = userService.findById(userId);
        if (!opUser.isPresent()) throw new NotFoundException("User " + userId + " not found.");
        User user = opUser.get();

        Optional<Pet> opPet = petDao.findById(petId);
        if (!opPet.isPresent()) throw new NotFoundException("Pet " + petId + " not found.");
        Pet pet = opPet.get();

        if (user.getId().equals(pet.getUser().getId())) {
            LOGGER.warn("User {} cannot ask question to himself", pet.getUser().getId());
            throw new QuestionException("User cannot ask question to himself");
        }

        Question question = petDao.createQuestion(content, user, pet.getUser(), pet, QuestionStatus.VALID);

        Map<String, Object> arguments = new HashMap<>();

        arguments.put("petURL", contextURL + "/pet/" + pet.getId());
        arguments.put("petName", pet.getPetName());
        arguments.put("userUsername", user.getUsername()); // User who asked the question
        arguments.put("userURL", contextURL + "/user/" + user.getId()); // User who asked the question
        arguments.put("question", content);

        String userLocale = pet.getUser().getLocale();

        mailService.sendMail(pet.getUser().getMail(), userLocale, arguments, MailType.QUESTION_ASK);

        return Optional.of(question);
    }

    @Transactional
    @Override
    public Optional<Answer> createAnswer(Long questionId, String content, Long userId, String contextURL) {
        Optional<User> opUser = userService.findById(userId);
        if (!opUser.isPresent()) throw new NotFoundException("User " + userId + " not found.");
        User user = opUser.get();

        Optional<Question> opQuestion = petDao.findQuestionById(questionId);
        if (!opQuestion.isPresent()) throw new NotFoundException("Question " + questionId + " not found.");
        Question question = opQuestion.get();

        if (user.getId().equals(question.getUser().getId())) {
            LOGGER.warn("User {} cannot answer his own question", userId);
            throw new QuestionException("User cannot answer his own question");
        }

        Pet pet = question.getPet();

        if (!pet.getUser().getId().equals(user.getId())) {
            LOGGER.warn("User {} is not pet {} owner", user.getId(), pet.getId());
            return Optional.empty();
        }
        Answer answer = petDao.createAnswer(question, content, user, question.getUser(), pet, QuestionStatus.VALID);

        Map<String, Object> arguments = new HashMap<>();

        arguments.put("petURL", contextURL + "/pet/" + pet.getId());
        arguments.put("petName", pet.getPetName());
        arguments.put("userUsername", user.getUsername()); // User who answered the question (pet owner)
        arguments.put("userURL", contextURL + "/user/" + user.getId()); // User who answered the question (pet owner)
        arguments.put("question", question.getContent());
        arguments.put("answer", content);

        String userLocale = question.getUser().getLocale();

        mailService.sendMail(question.getUser().getMail(), userLocale, arguments, MailType.QUESTION_ANSWER);

        return Optional.of(answer);
    }
}
