package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Pet;

import java.util.List;

public interface PetService {

    Pet findById(String id);
    List<Pet> list();
    //List<Pet> search(String keyWords);
    //List<Pet> filter(); como se pasarian los filtros?
}
