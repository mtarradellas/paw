package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.*;

import java.sql.Date;
import java.util.Optional;
import java.util.stream.Stream;

public interface PetDao {
    Optional<Pet> findById(String language, long id);
    Stream<Pet> list(String language, String page);
    Stream<Pet> filteredList(String language, String specieFilter, String breedFilter, String genderFilter,
                             String searchCriteria, String searchOrder, String minPrice, String maxPrice, String page);
    Stream<Pet> find(String language, String findValue,String page);
    Stream<Pet> getByUserId(String language, long ownerId, String page);
    Pet create(String petName, Species species, Breed breed, String location, boolean vaccinated, String gender,
               String description, Date birthDate, Date uploadDate, int price, long ownerId, Status status);
    void updateStatus(long id, long newStatus);
    boolean isPetOwner(long petId, long userId);
    String maxPages();
    String maxSearchPages(String language, String findValue);
    String maxFilterPages(String language, String specieFilter, String breedFilter, String genderFilter);
    String getMaxUserPetsPages(long userId);
    Optional<Contact> getPetContact(long petId);
    long getOwnerId(long petId);
    Optional<Status> findStatusById(String language, long id);
}
