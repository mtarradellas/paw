package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Pet;
import ar.edu.itba.paw.models.Request;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.constants.RequestStatus;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface RequestDao {

    List<Request> list(int page, int pageSize);
    List<Request> searchList(User user, Pet pet, String find, int page, int pageSize);
    List<Request> filteredList(User user, Pet pet, RequestStatus status, String searchCriteria, String searchOrder, int page, int pageSize);

    List<Request> searchListByPetOwner(User user, Pet pet, String find, int page, int pageSize);
    List<Request> filteredListByPetOwner(User user, Pet pet, RequestStatus status, String searchCriteria, String searchOrder, int page, int pageSize);

    int getListAmount();
    int getSearchListAmount(User user, Pet pet, String find);
    int getFilteredListAmount(User user, Pet pet, RequestStatus status);

    int getSearchListByPetOwnerAmount(User user, Pet pet, String find);
    int getFilteredListByPetOwnerAmount(User user, Pet pet, RequestStatus status);

    Optional<Request> findById(long id);

    Request create(User user, Pet pet, RequestStatus status);
    Optional<Request> update(Request request);
    void updateByStatusAndUser(User user, RequestStatus oldStatus, RequestStatus newStatus);
    void updateByStatusAndPetOwner(User petOwner, RequestStatus oldStatus, RequestStatus newStatus);
    void updateByStatusAndPet(Pet pet, RequestStatus oldStatus, RequestStatus newStatus);

    boolean isRequestTarget(Request request, User user);
}

