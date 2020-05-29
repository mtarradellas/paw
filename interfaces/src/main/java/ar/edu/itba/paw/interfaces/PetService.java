package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Contact;
import ar.edu.itba.paw.models.Pet;
import ar.edu.itba.paw.models.PetList;
import ar.edu.itba.paw.models.Request;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface PetService {

    PetList petList(String language, String findValue, String species, String  breed, String gender, String searchCriteria,
                    String searchOrder, String minPrice, String maxPrice, String province, String department, String page);
    PetList filteredList(String language, String species, String  breed, String gender, String searchCriteria, String searchOrder,
                         String minPrice, String maxPrice, String province, String department, String page);
    PetList find(String language, String findValue, String page);

    PetList adminPetList(String language, String findValue, String species, String breed, String gender, String status,
                         String searchCriteria, String searchOrder, String minPrice, String maxPrice, String page);
    PetList adminFilteredList(String language, String species, String breed, String gender, String status, String searchCriteria,
                              String searchOrder, String minPrice, String maxPrice, String page);
    PetList adminFind(String language, String findValue, String status, String page);

    List<Pet> list(String language, String page);
    List<Pet> adminList(String language, String page);
    List<Pet> listAll(String language);

    Optional<Pet> create(String language, String petName, long speciesId, long breedId, boolean vaccinated, String gender,
                         String description, Date birthDate, Date uploadDate, int price, long ownerId, long department, List<byte[]> photos);

    Optional<Pet> findById(String language, long id);
    Optional<Pet> adminFindById(String language, long id);
    PetList getByUserId(String language, long userId, String page);
    boolean sellPet(long petId, long userId);
    void sellPetAdmin(long petId);
    boolean removePet(long petId, long userId);
    boolean recoverPet(long petId, long userId);
    void removePetAdmin(long petId);
    void recoverPetAdmin(long petId);
    int getPetsAmount();
    int getAdminPetsAmount();
    int getSearchPetsAmount(String language, String findValue);
    int getAdminSearchPetsAmount(String language, String findValue);
    int getFilteredPetsAmount(String language, String specieFilter, String breedFilter, String genderFilter, String minPrice,
                              String maxPrice, String province, String department);
    int getAdminFilteredPetsAmount(String language, String specieFilter, String breedFilter, String genderFilter, String statusFilter);
    int userPetsAmount(long userId);
    String getMaxPages();
    String getMaxSearchPages(int pets);
    String getMaxAdminFilterPages(int pets);
    String getMaxFilterPages(int pets);
    String getMaxUserPetsPages(int pets);
    String getAdminMaxPages();
    String getAdminMaxSearchPages(int pets);
    Optional<Contact> getPetContact(long petId);
    long getOwnerId(long petId);
    void removeAllByOwner(long ownerId);

    boolean isPetOwner(long petId, long userId);
    Optional<Pet> update(String language, long userId, long id, List<byte[]> photos, List<Long> imagesToDelete, String petName,
                         long speciesId, long breedId, boolean vaccinated, String gender, String description, Date birthDate, int price, long department);

    Optional<Pet> adminUpdate(String language, long userId, long id, List<byte[]> photos, List<Long> imagesToDelete, String petName,
                              long speciesId, long breedId, boolean vaccinated, String gender, String description, Date birthDate, int price, long department);

    List<String> autocompleteFind(String language, String findValue);
}
