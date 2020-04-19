package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Pet;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface PetService {
    Optional<Pet> findById(long id);
    List<Pet> find(String findValue);
    List<Pet> list();
    List<Pet> filteredList(String specie, String  breed, String gender, String searchCriteria, String searchOrder);
    //Pet create(String petName, String species, String breed, String location, boolean vaccinated, String gender, String description, Date birthDate, Date uploadDate, int price, long ownerId);

}
