package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.ImageService;
import ar.edu.itba.paw.interfaces.PetDao;
import ar.edu.itba.paw.interfaces.PetService;
import ar.edu.itba.paw.models.Pet;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PetServiceImpl implements PetService {
    @Autowired
    private PetDao petDao;
    @Autowired
    private ImageService imageService;

    @Override
    public Optional<Pet> findById(long id){
        Optional<Pet> pet = petDao.findById(id);
        pet.ifPresent(value -> value.setImages(imageService.findByPetId(value.getId()).collect(Collectors.toList())));
        return pet;
    }

    @Override
    public List<Pet> list(){
        List<Pet> pets = petDao.list().collect(Collectors.toList());
        pets.forEach(pet ->{
            pet.setImages(imageService.findByPetId(pet.getId()).collect(Collectors.toList()));
        });
        return pets;
    }

    @Override
    public List<Pet> filteredList(String specie, String  breed, String gender, String searchCriteria, String searchOrder) {
        List<Pet> pets = petDao.filteredList(specie, breed, gender, searchCriteria, searchOrder).collect(Collectors.toList());
        pets.forEach(pet ->{
           pet.setImages(imageService.findByPetId(pet.getId()).collect(Collectors.toList()));
        });
        return pets;
    }

    @Override
    public List<Pet> find(String findValue){
        List<Pet> pets = petDao.find(findValue).collect(Collectors.toList());
        pets.forEach(pet ->{
            pet.setImages(imageService.findByPetId(pet.getId()).collect(Collectors.toList()));
        });
        return pets;
    }

    @Override
    public Pet create(String petName, String species, String breed, String location, boolean vaccinated, String gender, String description, Date birthDate, Date uploadDate, int price, long ownerId) {
        return this.petDao.create(petName, species, breed,location,vaccinated,gender,description,birthDate,uploadDate,price,ownerId);
    }
}
