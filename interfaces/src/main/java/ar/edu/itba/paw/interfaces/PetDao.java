package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Pet;

import java.util.List;

public interface PetDao {
    Pet findById(String id);
    List<Pet> list();
    Pet save(Pet pet);
}
