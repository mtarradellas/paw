package ar.edu.itba.paw.persistance;

import ar.edu.itba.paw.models.Pet;
import ar.edu.itba.paw.models.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PetDaoImpl {
    //mock db till we have an actual db
    private Map<String, Pet> pets = new ConcurrentHashMap<>();
    public PetDaoImpl() {

        Pet pet1 = new Pet();
        pet1.setId("1");
        pet1.setName("Len");
        pet1.setBirthDate(new Date(2020,1,1));
        pet1.setSpecies("Dog");
        pet1.setBreed("German Shepherd");
        pet1.setGender("Male");
        pet1.setLocation("Pilar");
        pet1.setVaccinated(true);
        pet1.setUploadDate(new Date(2020, 4, 8));
        pet1.setPrice(2000);

        Pet pet2 = new Pet();
        pet2.setId("2");
        pet2.setName("Fran");
        pet2.setBirthDate(new Date(2020,2,1));
        pet2.setSpecies("Dog");
        pet2.setBreed("Collie");
        pet2.setGender("Male");
        pet2.setLocation("Pilar");
        pet2.setVaccinated(true);
        pet2.setUploadDate(new Date(2020, 4, 8));
        pet2.setPrice(1000);

    }
    public Pet findById(String id) {
        return pets.get(id);
    }
    public List<Pet> list() {
        return new ArrayList<>(this.pets.values());
    }
    public Pet save(Pet pet){
        return this.pets.put(pet.getId(), pet);
    };
}
