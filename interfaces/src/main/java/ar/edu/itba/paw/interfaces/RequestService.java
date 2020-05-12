package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Request;
import ar.edu.itba.paw.models.RequestList;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface RequestService {

    Optional<Request> findById(long id, String language);
    Stream<Request> listByOwner(String language, long ownerId);
    Stream<Request> listByPetOwner(String language, long petOwnerId);

    RequestList adminRequestList(String language, String findValue, String status, String searchCriteria, String searchOrder, String page);
    RequestList adminFilteredList(String language, String status, String searchCriteria, String searchOrder, String page);
    RequestList adminFind(String language, String find, String page);

    String getAdminRequestPages(String language);
    String getAdminMaxSearchPages(String language,String find);
    String getAdminMaxFilterPages(String language, String status);
    Optional<Request> create(long userId, long petId, String language);
    boolean requestExists(long petId, long ownerId, String language);
    Stream<Request> filterListByOwner(String language, long ownerId, String status, String searchCriteria, String searchOrder);
    Stream<Request> filterListByPetOwner(String language, long petOwnerId, String status, String searchCriteria, String searchOrder);
    boolean cancel(long id, long ownerId, String locale);
    boolean accept(long id, long ownerId, String locale);
    boolean reject(long id, long ownerId, String locale);
    void adminUpdateStatus(long id, String status);
    void cancelRequestAdmin(long requestId);
    void recoverRequestAdmin(long requestId);
    void cancelAllByOwner(long ownerId);
    void rejectAllByPetOwner(long petOwnerId);
    void rejectAllByPet(long petId);
}
