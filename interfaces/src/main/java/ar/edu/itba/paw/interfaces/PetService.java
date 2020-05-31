package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.constants.PetStatus;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface PetService {

    List<Pet> list(String locale, int page, int pageSize);
    List<Pet> filteredList(String locale, String find, User user, String species, String breed, String gender, PetStatus status, String searchCriteria,
                           String searchOrder, int minPrice, int maxPrice, String province, String department, int page, int pageSize);

    int getListAmount();
    int getFilteredListAmount(String find, User user, String species, String breed, String gender, PetStatus status,
                              int minPrice, int maxPrice, String province, String department);

    Optional<Pet> findById(String locale, long id);

    Optional<Pet> create(String locale, String petName, Date birthDate, String gender, boolean vaccinated, int price, String description,
                        PetStatus status, long userId, long speciesId, long breedId, long provinceId, long departmentId, List<byte[]> photos);
    Optional<Pet> update(Pet pet);
    Optional<Pet> update(String locale, long id, long userId, String petName, Date birthDate, String gender, boolean vaccinated, int price,
                         String description, PetStatus status, long speciesId, long breedId, long provinceId, long department, List<byte[]> photos, List<Long> imagesToDelete);


    boolean    sellPet(long petId, User user);
    boolean  removePet(long petId, User user);
    boolean recoverPet(long petId, User user);

    boolean    adminSellPet(long petId);
    boolean  adminRemovePet(long petId);
    boolean adminRecoverPet(long petId);

    void removeAllByUser(User user);

    List<String> autocompleteFind(String locale, String find);

}
