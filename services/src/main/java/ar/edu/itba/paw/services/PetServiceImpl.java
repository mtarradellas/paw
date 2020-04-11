package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.PetDao;
import ar.edu.itba.paw.interfaces.PetService;
import ar.edu.itba.paw.models.Pet;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class PetServiceImpl implements PetService {
    @Autowired
    private PetDao petDao;

    @Override
    public Optional<Pet> findById(long id){
        return petDao.findById(id);
    }

    @Override
    public Stream<Pet> list(){
        return petDao.list();
    }

    @Override
    public Pet create(String petName, String species, String breed, String location, boolean vaccinated, String gender, String description, Date birthDate, Date uploadDate, int price, long ownerId) {
        return this.petDao.create(petName, species, breed,location,vaccinated,gender,description,birthDate,uploadDate,price,ownerId);
    }
}
