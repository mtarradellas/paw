package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Breed;
import ar.edu.itba.paw.models.Pet;
import ar.edu.itba.paw.models.Species;

import java.sql.Date;
import java.util.Optional;
import java.util.stream.Stream;

public interface PetDao {
    Optional<Pet> findById(String language, long id);
    Stream<Pet> list(String language);
    Stream<Pet> filteredList(String language, String specieFilter, String breedFilter, String genderFilter, String searchCriteria, String searchOrder);
    Stream<Pet> find(String language, String findValue);
    Pet create(String petName, Species species, Breed breed, String location, boolean vaccinated, String gender, String description, Date birthDate, Date uploadDate, int price, long ownerId);
}
