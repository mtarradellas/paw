package ar.edu.itba.paw.services;


import ar.edu.itba.paw.interfaces.RequestDao;
import ar.edu.itba.paw.interfaces.RequestService;
import ar.edu.itba.paw.models.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class RequestServiceImpl implements RequestService {

    @Autowired
    private RequestDao requestDao;

    @Override
    public Optional<Request> findById(long id, String language) {
        return requestDao.findById(id, language);
    }

    @Override
    public Stream<Request> listByOwner(String language, long ownerId) {
        return requestDao.listByOwner(language, ownerId);
    }

    @Override
    public Stream<Request> listByPetOwner(String language, long petOwnerId) {
        return requestDao.listByPetOwner(language, petOwnerId);
    }

    @Override
    public Request create(long ownerId, long petId, Date creationDate, String language) {
        return null;
    }

    @Override
    public Request update(long id, int status, String language) {
        return null;
    }
}
