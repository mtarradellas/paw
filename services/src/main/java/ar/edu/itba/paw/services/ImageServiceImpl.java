package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.ImageDao;
import ar.edu.itba.paw.interfaces.ImageService;
import ar.edu.itba.paw.interfaces.PetDao;
import ar.edu.itba.paw.interfaces.PetService;
import ar.edu.itba.paw.models.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;

@Service
public class ImageServiceImpl implements ImageService {
    @Autowired
    private ImageDao imageDao;
    @Autowired
    private PetDao petDao;

    @Override
    public Stream<Image> findByPetId(long id) {
        return this.imageDao.findByPetId(id);
    }

    @Override
    public Optional<byte[]> getDataById(long id) {
        return this.imageDao.getDataById(id);
    }

    @Override
    public Optional<Image> create(long petId, byte[] bytes, long userId) {
        if (petDao.isPetOwner(petId, userId)) {
            return imageDao.create(petId, bytes);
        }
        return Optional.empty();
    }
}