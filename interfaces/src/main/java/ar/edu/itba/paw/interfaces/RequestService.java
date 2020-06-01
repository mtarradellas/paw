package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Pet;
import ar.edu.itba.paw.models.Request;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.constants.RequestStatus;
import java.util.List;
import java.util.Optional;

public interface RequestService {

    List<Request> list(int page, int pageSize);
    List<Request> filteredList(User user, Pet pet, String find, RequestStatus status, String searchCriteria, String searchOrder, int page, int pageSize);
    List<Request> filteredListByPetOwner(User user, Pet pet, String find, RequestStatus status, String searchCriteria, String searchOrder, int page, int pageSize);

    int getListAmount();
    int getFilteredListAmount(User user, Pet pet, String find, RequestStatus status);
    int getFilteredListByPetOwnerAmount(User user, Pet pet, String find, RequestStatus status);

    Optional<Request> findById(long id);

    Optional<Request> create(String locale, long user, long pet);
    Optional<Request> update(Request request);

    boolean  cancel(long id, User user);
    boolean  accept(long id, User user);
    boolean  reject(long id, User user);
    boolean recover(long id, User user);

    void adminUpdateStatus(long id, RequestStatus status);
    void  adminCancel(long id);
    void  adminAccept(long id);
    void  adminReject(long id);
    void adminRecover(long id);

    void cancelAllByUser(User user);
    void rejectAllByPetOwner(long petOwnerId);
    void rejectAllByPet(String locale, long petId);
}
