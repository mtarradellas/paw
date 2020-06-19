package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Pet;
import ar.edu.itba.paw.models.Request;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.constants.RequestStatus;
import org.hibernate.search.backend.impl.LocalBackendQueueProcessor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public interface RequestDao {

    List<Request> list(int page, int pageSize);
    List<Request> searchList(User user, Pet pet, List<String> find, RequestStatus status, String searchCriteria, String searchOrder, int page, int pageSize);
    Set<Integer> searchStatusList(User user, Pet pet, List<String> find, RequestStatus status);
    List<Pet> searchPetListByPetOwner(User user, Pet pet, List<String> find, RequestStatus status);
    List<Request> filteredList(User user, Pet pet, RequestStatus status, String searchCriteria, String searchOrder, int page, int pageSize);

    List<Request> searchListByPetOwner(User user, Pet pet, List<String> find, RequestStatus status, String searchCriteria, String searchOrder,  int page, int pageSize);
    Set<Integer> searchStatusListByPetOwner(User user, Pet pet, List<String> find, RequestStatus status);
    List<Request> filteredListByPetOwner(User user, Pet pet, RequestStatus status, String searchCriteria, String searchOrder, int page, int pageSize);

    int getListAmount();
    int getSearchListAmount(User user, Pet pet, List<String> find, RequestStatus status);
    int getFilteredListAmount(User user, Pet pet, RequestStatus status);

    int getSearchListByPetOwnerAmount(User user, Pet pet, List<String> find, RequestStatus status);
    int getFilteredListByPetOwnerAmount(User user, Pet pet, RequestStatus status);

    Optional<Request> findById(long id);

    Request create(User user, Pet pet, RequestStatus status, LocalDateTime creationDate);
    Optional<Request> update(Request request);
    void updateByStatusAndUser(User user, RequestStatus oldStatus, RequestStatus newStatus);
    void updateByStatusAndPetOwner(User petOwner, RequestStatus oldStatus, RequestStatus newStatus);
    void updateByStatusAndPet(Pet pet, RequestStatus oldStatus, RequestStatus newStatus);

    int interestNotifs(User user);
    int requestNotifs(User user);
}

