package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.*;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface PetDao {
    Optional<Pet> findById(String language, long id, int level);
    Stream<Pet> list(String language, String page, int level);
    Stream<Pet> listAll(String language);
    Stream<Pet> find(String language, String findValue,String page, int level);
    Stream<Pet> adminFilteredList(String language, String specie, String breed, String gender, String status, String searchCriteria, String searchOrder, String page);
    Stream<Pet> filteredList(String language, String specieFilter, String breedFilter, String genderFilter, String searchCriteria, String searchOrder, String minPrice, String maxPrice, String page);
    Stream<Pet> getByUserId(String language, long ownerId, String page);
    Pet create(String petName, Species species, Breed breed, String location, boolean vaccinated, String gender,
               String description, Date birthDate, Date uploadDate, int price, long ownerId, Status status);
    void updateStatus(long id, long newStatus);
    boolean isPetOwner(long petId, long userId);
    String maxPages(int level);
    String maxSearchPages(String language, String findValue, int level);
    String maxAdminFilterPages(String language,String specieFilter,String breedFilter,String genderFilter,String statusFilter);
    String maxFilterPages(String language, String specieFilter, String breedFilter, String genderFilter, String minPrice, String maxPrice);
    String getMaxUserPetsPages(long userId);
    Optional<Contact> getPetContact(long petId);
    long getOwnerId(long petId);
    Optional<Status> findStatusById(String language, long id);
}
