package ar.edu.itba.paw.interfaces;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import ar.edu.itba.paw.interfaces.exceptions.InvalidImageQuantityException;
import ar.edu.itba.paw.models.Answer;
import ar.edu.itba.paw.models.Breed;
import ar.edu.itba.paw.models.Department;
import ar.edu.itba.paw.models.Pet;
import ar.edu.itba.paw.models.Question;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.constants.PetStatus;
import ar.edu.itba.paw.models.constants.PriceRange;

public interface PetService {

    List<Pet> list(String locale, int page, int pageSize);
    List<Pet> filteredList(String locale, List<String> find, Long userId, Long newOwnerId, Long species, Long breed, String gender, PetStatus status, String searchCriteria,
                           String searchOrder, int minPrice, int maxPrice, Long province, Long department, int page, int pageSize);
    List<Breed> filteredBreedList(String locale, List<String> find, Long userId, Long speciesId, Long breedId, String gender,
                                  PetStatus status, int minPrice, int maxPrice, Long provinceId, Long departmentId);
    List<Department> filteredDepartmentList(String locale, List<String> find, Long userId, Long speciesId, Long breedId, String gender,
                                            PetStatus status, int minPrice, int maxPrice, Long provinceId, Long departmentId);
    Set<PriceRange> filteredRangesList(String locale, List<String> find, Long userId, Long speciesId, Long breedId, String gender,
                                  PetStatus status, int minPrice, int maxPrice, Long provinceId, Long departmentId);
    Set<String> filteredGenderList(String locale, List<String> find, Long userId, Long speciesId, Long breedId, String gender,
                                    PetStatus status, int minPrice, int maxPrice, Long provinceId, Long departmentId);
    List<Pet> listByUser(String locale, Long userId, int page, int pageSize);

    int getListAmount();
    int getFilteredListAmount(String locale, List<String> find, Long userId, Long newOwnerId, Long species, Long breed, String gender, PetStatus status,
                              int minPrice, int maxPrice, Long province, Long department);
    int getListByUserAmount(String locale, Long userId);

    Optional<Pet> findById(String locale, long id);
    Optional<Pet> findById(long id);

    Optional<Pet> create(String locale, String petName, LocalDateTime birthDate, String gender, boolean vaccinated, int price, String description,
                         PetStatus status, long userId, long speciesId, long breedId, long provinceId, long departmentId, List<byte[]> photos);
    Optional<Pet> update(Pet pet);
    Optional<Pet> update(String locale, long id, Long userId, String petName, LocalDateTime birthDate, String gender, boolean vaccinated, int price,
                         String description, PetStatus status, long speciesId, long breedId, long provinceId, long department, List<byte[]> photos, List<Long> imagesToDelete)
                         throws InvalidImageQuantityException;


    void sellPet(Pet pet, User owner, User newOwner, String contextURL);
    void removePet(long petId, long userId);
    void recoverPet(long petId, long userId);

    void adminSellPet(long petId,  long newOwnerId);
    void adminRemovePet(long petId);
    void adminRecoverPet(long petId);

    void removeAllByUser(User user);

    List<String> autocompleteFind(String locale, String find);

    void setLocale(String locale, Pet pet);
    void setLocale(String locale, List<Pet> pet);

    List<Question> listQuestions(long petId, int page, int pageSize);
    int getListQuestionsAmount(Long petId);
    Optional<Question> findQuestionById(long id);
    Optional<Answer> findAnswerById(long id);
    Optional<Question> createQuestion(String content, Long userId, long petId, String contextUrl);
    Optional<Answer> createAnswer(Long questionId, String content, Long userId, String contextUrl);
}
