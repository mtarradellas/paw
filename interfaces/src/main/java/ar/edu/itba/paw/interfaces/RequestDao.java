package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Request;

import java.util.Optional;
import java.util.stream.Stream;

public interface RequestDao {
    Optional<Request> findById(long id, String language);
    Stream<Request> listByOwner(String language, long ownerId);
    Stream<Request> listByPetOwner(String language, long petOwnerId);
    Optional<Request> create(long ownerId, long petId, int status, String language);
    Optional<Request> updateStatus(long id, long petOwnerId, String status, String language);
    Optional<Request> getRequestByOwnerAndPetId(long ownerId, long petId, String language);
    Stream<Request> filterListByOwner(String language, long ownerId, String status, String searchCriteria, String searchOrder);
    Stream<Request> filterListByPetOwner(String language, long petOwnerId, String status, String searchCriteria, String searchOrder);
    boolean delete(long id, long ownerId);
    //cancel request
}
