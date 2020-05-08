package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Contact;
import ar.edu.itba.paw.models.Pet;
import ar.edu.itba.paw.models.Request;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface PetService {
    Optional<Pet> findById(String language, long id);
    Optional<Pet> adminFindById(String language, long id);
    List<Pet> find(String language,String findValue, String page);
    List<Pet> list(String language, String page);
    List<Pet> adminFilteredList(String language, String specie, String  breed, String gender, String status, String searchCriteria, String searchOrder, String page);
    List<Pet> filteredList(String language, String specie, String  breed, String gender, String searchCriteria, String searchOrder, String minPrice, String maxPrice, String page);
    List<Pet> getByUserId(String language, long userId, String page);
    List<Pet> adminList(String language, String page);
    List<Pet> adminSearchList(String language, String find, String page);
    Pet create(String language, String petName, String speciesName, String breedName, String location, boolean vaccinated, String gender, String description, Date birthDate, Date uploadDate, int price, long ownerId);
    boolean sellPet(long petId, long userId);
    boolean removePet(long petId, long userId);
    String getMaxPages();
    String getMaxSearchPages(String language, String findValue);
    String getMaxFilterPages(String language, String specieFilter, String breedFilter, String genderFilter);
    String getMaxAdminFilterPages(String language, String specieFilter, String breedFilter, String genderFilter, String statusFilter);
    String getMaxUserPetsPages(long userId);
    String getAdminMaxPages();
    String getAdminMaxSearchPages(String language,String find);
    Optional<Contact> getPetContact(long petId);
    long getOwnerId(long petId);

}
