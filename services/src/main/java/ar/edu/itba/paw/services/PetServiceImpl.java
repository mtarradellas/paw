package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.PetDao;
import ar.edu.itba.paw.models.Pet;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PetServiceImpl {
    @Autowired
    private PetDao petDao;
    public Pet findById(String id){
        return petDao.findById(id);
    }
    public List<Pet> list(){
        return petDao.list();
    }
}
