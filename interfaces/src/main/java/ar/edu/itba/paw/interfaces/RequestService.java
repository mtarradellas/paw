package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Pet;
import ar.edu.itba.paw.models.Request;
import ar.edu.itba.paw.models.RequestList;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.constants.RequestStatus;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface RequestService {

    List<Request> list(int page, int pageSize);
    List<Request> filteredList(User user, Pet pet, String find, RequestStatus status, String searchCriteria, String searchOrder, int page, int pageSize);

    int getListAmount();
    int getFilteredListAmount(User user, Pet pet, String find, RequestStatus status);

    Optional<Request> findById(long id);

    Optional<Request> create(User user, Pet pet);
    Optional<Request> update(Request request);

    boolean  cancel(Request request, User user);
    boolean  accept(Request request, User user);
    boolean  reject(Request request, User user);
    boolean recover(Request request, User user);

    void adminUpdateStatus(Request request, RequestStatus status);
    void  adminCancel(Request request);
    void  adminAccept(Request request);
    void  adminReject(Request request);
    void adminRecover(Request request);

    void cancelAllByUser(User user);
    void rejectAllByPetOwner(User petOwner);
    void rejectAllByPet(Pet pet);
}
