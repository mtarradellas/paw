package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.constants.PetStatus;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PetDao {

    List<Pet> list(int page, int pageSize);
    List<Pet> searchList(String locale, List<String> find, User user, Species species, Breed breed, String gender, PetStatus status, String searchCriteria,
                         String searchOrder, int minPrice, int maxPrice, Province province, Department department, int page, int pageSize);
    List<Pet> filteredList(String locale, User user, Species species, Breed breed, String gender, PetStatus status, String searchCriteria,
                           String searchOrder, int minPrice, int maxPrice, Province province, Department department, int page, int pageSize);

    int getListAmount();
    int getSearchListAmount(String locale, List<String> find, User user, Species species, Breed breed, String gender, PetStatus status,
                            int minPrice, int maxPrice, Province province, Department department);
    int getFilteredListAmount(String locale, User user, Species species, Breed breed, String gender, PetStatus status,
                              int minPrice, int maxPrice, Province province, Department department);

    Optional<Pet> findById(long id);

    Pet create(String petName, Date birthDate, String gender, boolean vaccinated, int price, Date uploadDate,
               String description, PetStatus status, User user, Species species, Breed breed, Province province, Department department);
    Optional<Pet> update(Pet pet);
    void updateByStatusAndOwner(User user, PetStatus oldStatus, PetStatus newStatus);

    List<String> autocompleteFind(String locale, String find);
}
