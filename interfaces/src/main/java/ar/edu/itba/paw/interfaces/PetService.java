package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Pet;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface PetService {
    Optional<Pet> findById(String language, long id);
    List<Pet> find(String language,String findValue, String page);
    List<Pet> list(String language, String page);
    List<Pet> filteredList(String language, String specie, String  breed, String gender, String searchCriteria, String searchOrder, String page);
    Pet create(String language, String petName, String speciesName, String breedName, String location, boolean vaccinated, String gender, String description, Date birthDate, Date uploadDate, int price, long ownerId);
    String getMaxPages();
    String getMaxSearchPages(String language, String findValue);
    String getMaxFilterPages(String language, String specieFilter, String breedFilter, String genderFilter);
}
