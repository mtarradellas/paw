package ar.edu.itba.paw.services;


import ar.edu.itba.paw.interfaces.RequestDao;
import ar.edu.itba.paw.interfaces.RequestService;
import ar.edu.itba.paw.models.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;

@Service
public class RequestServiceImpl implements RequestService {

    @Autowired
    private RequestDao requestDao;

    private final int PENDING = 1;

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
    public Optional<Request> create(long ownerId, long petId, String language) {
        return requestDao.create(ownerId, petId, PENDING, language);
    }

    @Override
    public Optional<Request> updateStatus(long id, String status, String language) {
        return requestDao.updateStatus(id, status, language);
    }

    @Override
    public boolean requestExists(long petId, long ownerId, String language){
        Optional<Request> request = requestDao.getRequestByOwnerAndPetId(petId, ownerId, language);
        return request.equals(Optional.empty());
    }

}
