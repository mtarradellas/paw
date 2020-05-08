package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.PetDao;
import ar.edu.itba.paw.interfaces.PetService;
import ar.edu.itba.paw.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PetServiceImpl implements PetService {
    static final long AVAILABLE_STATUS = 1;
    static final long REMOVED_STATUS = 2;
    static final long SOLD_STATUS = 3;

    @Autowired
    private PetDao petDao;

    @Override
    public Optional<Pet> findById(String language, long id){
        return petDao.findById(language, id, 0);
    }

    @Override
    public Optional<Pet> adminFindById(String language, long id){
        return petDao.findById(language, id, 1);
    }

    @Override
    public List<Pet> list(String language,String page){
        return petDao.list(language, page, 0).collect(Collectors.toList());// 0 is user
    }

    @Override
    public List<Pet> adminFilteredList(String language, String specie, String breed, String gender, String status, String searchCriteria, String searchOrder, String page) {
        return petDao.adminFilteredList(language, specie, breed, gender, status, searchCriteria, searchOrder, page).collect(Collectors.toList());
    }

    @Override
    public List<Pet> filteredList(String language, String specie, String  breed, String gender, String searchCriteria, String searchOrder, String minPrice, String maxPrice, String page) {
        return petDao.filteredList(language,specie, breed, gender, searchCriteria, searchOrder, minPrice, maxPrice, page).collect(Collectors.toList());
    }

    @Override
    public List<Pet> find(String language,String findValue, String page){
        return petDao.find(language, findValue, page, 0).collect(Collectors.toList());// 0 is user
    }

    @Override
    public List<Pet> getByUserId(String language, long ownerId, String page){
        return petDao.getByUserId( language, ownerId, page).collect(Collectors.toList());
    }

    @Override
    public List<Pet> adminList(String language, String page){
        return petDao.list(language, page, 1).collect(Collectors.toList());// 1 is admin
    }

    @Override
    public List<Pet> adminSearchList(String language, String find, String page){
        return petDao.find(language, find, page, 1).collect(Collectors.toList()); //1 is admin
    }

    @Override
    public Pet create(String language, String petName, String speciesName, String breedName, String location, boolean vaccinated, String gender, String description, Date birthDate, Date uploadDate, int price, long ownerId) {
//        Species species = speciesDao.findSpeciesByName(language, speciesName);
//        Breed breed = speciesDao.findBreedByName(language, breedName);
//        return petDao.create(petName, species, breed, location, vaccinated, gender, description, birthDate, uploadDate, price, ownerId, AVAILABLE_STATUS);
        return new Pet();
    }

    @Override
    public long getOwnerId(long petId){
        return petDao.getOwnerId(petId);
    }

    @Override
    public String getMaxPages(){
        return petDao.maxPages(0);
    } // 0 is user

    @Override
    public String getMaxSearchPages(String language, String findValue) {
        return petDao.maxSearchPages(language,findValue, 0); //0 is user
    }

    @Override
    public String getMaxFilterPages(String language, String specieFilter, String breedFilter, String genderFilter) {
        return petDao.maxFilterPages(language,specieFilter,breedFilter,genderFilter);
    }

    @Override
    public String getMaxAdminFilterPages(String language, String specieFilter, String breedFilter, String genderFilter, String statusFilter) {
        return petDao.maxAdminFilterPages(language, specieFilter, breedFilter, genderFilter,statusFilter);
    }

    @Override
    public String getMaxUserPetsPages(long userId){
        return petDao.getMaxUserPetsPages(userId);
    }

    @Override
    public String getAdminMaxSearchPages(String language, String find){
        return petDao.maxSearchPages(language, find, 1); //1 is admin
    }

    @Override
    public String getAdminMaxPages(){
        return petDao.maxPages(1); //level 1 is admin

    }

    @Override
    public Optional<Contact> getPetContact(long petId) {
        return petDao.getPetContact(petId);
    }

    @Override
    public boolean sellPet(long petId, long userId) {
        if (petDao.isPetOwner(petId, userId)) {
            petDao.updateStatus(petId, SOLD_STATUS);
            return true;
        }
        return false;
    }

    @Override
    public boolean removePet(long petId, long userId) {
        if (petDao.isPetOwner(petId, userId)) {
            petDao.updateStatus(petId, REMOVED_STATUS);
            return true;
        }
        return false;
    }
}
