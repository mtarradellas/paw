package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Request;

import java.util.Optional;
import java.util.stream.Stream;

public interface RequestService {

    Optional<Request> findById(long id, String language);
    Stream<Request> listByOwner(String language, long ownerId);
    Stream<Request> listByPetOwner(String language, long petOwnerId);
    Optional<Request> create(long userId, long petId, String language);
    boolean requestExists(long petId, long ownerId, String language);
    Stream<Request> filterListByOwner(String language, long ownerId, String status, String searchCriteria, String searchOrder);
    Stream<Request> filterListByPetOwner(String language, long petOwnerId, String status, String searchCriteria, String searchOrder);
    boolean cancel(long id, long ownerId, String locale);
    boolean accept(long id, long ownerId, String locale);
    boolean reject(long id, long ownerId, String locale);
}
