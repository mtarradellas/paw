package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Pet;
import ar.edu.itba.paw.models.Request;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.constants.RequestStatus;
import java.util.List;
import java.util.Optional;

public interface RequestService {

    List<Request> list(int page, int pageSize);
    List<Request> filteredList(User user, Long petId, List<String> find, RequestStatus status, String searchCriteria, String searchOrder, int page, int pageSize);
    List<RequestStatus> filteredStatusList(User user, Long petId, List<String> find, RequestStatus status);
    List<Pet> filteredPetListByPetOwner(User user, Long petId, List<String> find, RequestStatus status);
    List<Request> filteredListByPetOwner(User user, Long petId, List<String> find, RequestStatus status, String searchCriteria, String searchOrder, int page, int pageSize);
    List<RequestStatus> filteredStatusListByPetOwner(User user, Long petId, List<String> find, RequestStatus status);
    int getListAmount();
    int getFilteredListAmount(User user, Long petId, List<String> find, RequestStatus status);
    int getFilteredListByPetOwnerAmount(User user, Long petId, List<String> find, RequestStatus status);

    Optional<Request> findById(long id);

    Optional<Request> create(String locale, long user, long pet, String contextURL);
    Optional<Request> update(Request request);

    boolean  cancel(long id, User user, String contextURL);
    boolean  accept(long id, User user, String contextURL);
    boolean  reject(long id, User user, String contextURL);
    boolean recover(long id, User user, String contextURL);

    void adminUpdateStatus(long id, RequestStatus status);
    void  adminCancel(long id);
    void  adminAccept(long id);
    void  adminReject(long id);
    void adminRecover(long id);

    void cancelAllByUser(User user);
    void rejectAllByPetOwner(long petOwnerId);
    void rejectAllByPet(String locale, long petId);

    int interestNotifs(User user);
    int requestNotifs(User user);

    void logRequestsAccess(User user);
    void logInterestsAccess(User user);
}
