package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.ImageDao;
import ar.edu.itba.paw.interfaces.ImageService;
import ar.edu.itba.paw.interfaces.PetService;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.Pet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageServiceImpl.class);

    @Autowired
    private ImageDao imageDao;
    @Autowired
    private PetService petService;

    @Override
    public List<Image> findByPetId(long id) {
        return this.imageDao.findByPetId(id);
    }

    @Override
    public Integer quantityByPetId(long id) {
        return this.imageDao.quantityByPetId(id);
    }

    @Override
    public Optional<byte[]> getDataById(long id) {
        return this.imageDao.getDataById(id);
    }


    @Transactional
    @Override
    public Optional<Image> create(long petId, byte[] bytes, long userId) {
        Optional<Pet> opPet = petService.findById(petId);
        if (!opPet.isPresent()) {
            LOGGER.warn("Pet {} not found, image not created", petId);
            return Optional.empty();
        }
        Pet pet = opPet.get();
        if (userId != 0 && pet.getUser().getId() == userId) {
            LOGGER.warn("User {} is not pet {} owner, image not created", userId, petId);
            return Optional.empty();
        }
        return imageDao.create(petId, bytes);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        this.imageDao.delete(id);
    }

    @Transactional
    @Override
    public void delete(List<Long> ids) {
        for (Long id: ids) {
            this.imageDao.delete(id);
        }
    }
}