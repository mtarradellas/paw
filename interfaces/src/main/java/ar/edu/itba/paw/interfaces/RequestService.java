package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Request;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface RequestService {

    Optional<Request> findById(long id, String language);
    List<Request> listByOwner(String language, long ownerId);
    List<Request> listByPetOwner(String language, long petOwnerId);
    Optional<Request> create(long ownerId, long petId, String language);
    Optional<Request> updateStatus(long id, String status, String language);
    boolean requestExists(long petId, long ownerId, String language);
}
