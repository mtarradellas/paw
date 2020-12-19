package ar.edu.itba.paw.interfaces;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import ar.edu.itba.paw.models.Answer;
import ar.edu.itba.paw.models.Breed;
import ar.edu.itba.paw.models.Department;
import ar.edu.itba.paw.models.Pet;
import ar.edu.itba.paw.models.Province;
import ar.edu.itba.paw.models.Question;
import ar.edu.itba.paw.models.Species;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.constants.PetStatus;
import ar.edu.itba.paw.models.constants.PriceRange;
import ar.edu.itba.paw.models.constants.QuestionStatus;

public interface PetDao {

    List<Pet> list(int page, int pageSize);
    List<Pet> searchList(String locale, List<String> find, User user, User newOwner, Species species, Breed breed, String gender, PetStatus status, String searchCriteria,
                         String searchOrder, int minPrice, int maxPrice, Province province, Department department, int page, int pageSize);
    List<Breed> searchBreedList(String locale, List<String> find, User user,  Species species, Breed breed, String gender, PetStatus status,
                                int minPrice, int maxPrice, Province province, Department department);
    List<Department> searchDepartmentList(String locale, List<String> find, User user, Species species, Breed breed, String gender, PetStatus status,
                                            int minPrice, int maxPrice, Province province, Department department);
    Set<PriceRange>searchRangesList(String locale, List<String> find, User user, Species species, Breed breed, String gender,
                                  PetStatus status, int minPrice, int maxPrice, Province province, Department department);
    Set<String> searchGenderList(String locale, List<String> find, User user, Species species, Breed breed, String gender,
                                  PetStatus status, int minPrice, int maxPrice, Province province, Department department);
    List<Pet> filteredList(String locale, User user, Species species, Breed breed, String gender, PetStatus status, String searchCriteria,
                           String searchOrder, int minPrice, int maxPrice, Province province, Department department, int page, int pageSize);
    List<Pet> listByUser(long userId, int page, int pageSize);

    int getListAmount();
    int getSearchListAmount(String locale, List<String> find, User user, User newOwner, Species species, Breed breed, String gender, PetStatus status,
                            int minPrice, int maxPrice, Province province, Department department);
    int getFilteredListAmount(String locale, User user, Species species, Breed breed, String gender, PetStatus status,
                              int minPrice, int maxPrice, Province province, Department department);
    int getListByUserAmount(long userId);

    Optional<Pet> findById(long id);

    Pet create(String petName, LocalDateTime birthDate, String gender, boolean vaccinated, int price, LocalDateTime uploadDate,
               String description, PetStatus status, User user, Species species, Breed breed, Province province, Department department);
    Optional<Pet> update(Pet pet);
    void updateByStatusAndOwner(User user, PetStatus oldStatus, PetStatus newStatus);

    List<String> autocompleteFind(String locale, String find);

    List<Question> listQuestions(long petId, int page, int pageSize);
    int getListQuestionsAmount(long petId);
    Optional<Question> findQuestionById(long questionId);
    Optional<Answer> findAnswerById(long answerId);
    Question createQuestion(String content, User user, User target, Pet pet, QuestionStatus status);
    Answer createAnswer(Question question, String content, User user, User target, Pet pet, QuestionStatus status);
}
