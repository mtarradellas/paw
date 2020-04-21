package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.ImageService;
import ar.edu.itba.paw.interfaces.PetDao;
import ar.edu.itba.paw.interfaces.PetService;
import ar.edu.itba.paw.models.Pet;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PetServiceImpl implements PetService {
    @Autowired
    private PetDao petDao;

    @Override
    public Optional<Pet> findById(String language, long id){
        return petDao.findById(language, id);
    }

    @Override
    public List<Pet> list(String language){
        return petDao.list(language).collect(Collectors.toList());
    }

    @Override
    public List<Pet> filteredList(String language, String specie, String  breed, String gender, String searchCriteria, String searchOrder) {
        return petDao.filteredList(language,specie, breed, gender, searchCriteria, searchOrder).collect(Collectors.toList());
    }

    @Override
    public List<Pet> find(String language,String findValue){
        return petDao.find(language, findValue).collect(Collectors.toList());
    }

//    @Override
//    public Pet create(String petName, String species, String breed, String location, boolean vaccinated, String gender, String description, Date birthDate, Date uploadDate, int price, long ownerId) {
//        return this.petDao.create(petName, species, breed,location,vaccinated,gender,description,birthDate,uploadDate,price,ownerId);
//    }
}
