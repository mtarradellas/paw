package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Request;
import ar.edu.itba.paw.models.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface RequestDao {
    Optional<Request> findById(long id, String language);
    Stream<Request> listByOwner(String language, long ownerId);
    Stream<Request> listByPetOwner(String language, long petOwnerId);
    Stream<Request> adminRequestList(String language, String page);
    Stream<Request> adminSearchList(String language, String find, String page);
    Stream<Request> adminFilteredList(String language, String status, String searchCriteria, String searchOrder, String page);
    Optional<Request> create(long ownerId, long petId, int status, String language);
    Optional<Request> getRequestByOwnerAndPetId(long ownerId, long petId, String language);
    Stream<Request> filterListByOwner(String language, long ownerId, String status, String searchCriteria, String searchOrder);
    Stream<Request> filterListByPetOwner(String language, long petOwnerId, String status, String searchCriteria, String searchOrder);
    String getAdminRequestPages(String language);
    String getAdminMaxSearchPages(String language, String find);
    String getAdminMaxFilterPages(String language, String status);
    void updateStatus(long id, long newStatus);
    boolean isRequestOwner(long id, long userId);
    boolean isRequestTarget(long id, long userId);
    Stream<Long> findIdByStatus(long petId, long ownerId, List<Integer> statusList);
    void updateAllByOwner(long ownerId, int oldStatus, int newStatus);
    void updateAllByPetOwner(long petOwnerId, int oldStatus, int newStatus);
}

