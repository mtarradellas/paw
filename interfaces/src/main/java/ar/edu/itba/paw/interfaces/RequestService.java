package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Request;

import java.util.Date;
import java.util.Optional;
import java.util.stream.Stream;

public interface RequestService {

    Optional<Request> findById(long id, String language);
    Stream<Request> listByOwner(String language, long ownerId);
    Stream<Request> listByPetOwner(String language, long petOwnerId);
    Request create(long ownerId, long petId, Date creationDate, String language);
    Request update(long id, int status, String language);
}
