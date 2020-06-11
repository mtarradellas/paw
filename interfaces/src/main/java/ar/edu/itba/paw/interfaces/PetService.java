package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.interfaces.exceptions.InvalidImageQuantityException;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.constants.PetStatus;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PetService {

    List<Pet> list(String locale, int page, int pageSize);
    List<Pet> filteredList(String locale, List<String> find, Long userId, Long species, Long breed, String gender, PetStatus status, String searchCriteria,
                           String searchOrder, int minPrice, int maxPrice, Long province, Long department, int page, int pageSize);
    List<Pet> listByUser(String locale, Long userId, int page, int pageSize);

    int getListAmount();
    int getFilteredListAmount(String locale, List<String> find, Long userId, Long species, Long breed, String gender, PetStatus status,
                              int minPrice, int maxPrice, Long province, Long department);
    int getListByUserAmount(String locale, Long userId);

    Optional<Pet> findById(String locale, long id);
    Optional<Pet> findById(long id);

    Optional<Pet> create(String locale, String petName, Date birthDate, String gender, boolean vaccinated, int price, String description,
                        PetStatus status, long userId, long speciesId, long breedId, long provinceId, long departmentId, List<byte[]> photos);
    Optional<Pet> update(Pet pet);
    Optional<Pet> update(String locale, long id, Long userId, String petName, Date birthDate, String gender, boolean vaccinated, int price,
                         String description, PetStatus status, long speciesId, long breedId, long provinceId, long department, List<byte[]> photos, List<Long> imagesToDelete)
                         throws InvalidImageQuantityException;


    boolean    sellPet(long petId, User owner, long newOwnerId, String contextURL);
    boolean  removePet(long petId, User user);
    boolean recoverPet(long petId, User user);

    boolean    adminSellPet(long petId,  long newOwnerId);
    boolean  adminRemovePet(long petId);
    boolean adminRecoverPet(long petId);

    void removeAllByUser(User user);

    List<String> autocompleteFind(String locale, String find);

    void setLocale(String locale, Pet pet);
    void setLocale(String locale, List<Pet> pet);

}
