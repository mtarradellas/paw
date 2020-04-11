package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Pet;

import java.util.Date;
import java.util.Optional;
import java.util.stream.Stream;

public interface PetService {
    Optional<Pet> findById(long id);
    Stream<Pet> list();
    Pet create(String petName, String species, String breed, String location, boolean vaccinated, String gender, String description, Date birthDate, Date uploadDate, int price, long ownerId);

}
