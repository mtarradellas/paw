package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Pet;

import java.util.Date;
import java.util.Optional;
import java.util.stream.Stream;

public interface PetDao {
    Optional<Pet> findById(long id);
    Stream<Pet> list();
    Stream<Pet> filteredList(String specieFilter, String breedFilter, String genderFilter, String searchCriteria, String searchOrder);
    Stream<Pet> find(String findValue);
    Pet create(String petName, String species, String breed, String location, boolean vaccinated, String gender, String description, Date birthDate, Date uploadDate, int price, long ownerId);
}
