package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Pet;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface PetService {
    Optional<Pet> findById(String language, long id);
    List<Pet> find(String language,String findValue);
    List<Pet> list(String language);
    List<Pet> filteredList(String language, String specie, String  breed, String gender, String searchCriteria, String searchOrder);
    Pet create(String language, String petName, String speciesName, String breedName, String location, boolean vaccinated, String gender, String description, Date birthDate, Date uploadDate, int price, long ownerId);

}
