package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Pet;
import ar.edu.itba.paw.models.Request;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.constants.RequestStatus;
import java.util.List;
import java.util.Optional;

public interface RequestService {

    List<Request> list(int page, int pageSize);
    List<Request> filteredList(Long userId, Long targetId, Long petId, List<String> find, RequestStatus status, String searchCriteria, String searchOrder, int page, int pageSize);
    List<Request> filteredListByRequestOwner(User user, Long petId, List<String> find, RequestStatus status, String searchCriteria, String searchOrder, int page, int pageSize);
    List<RequestStatus> filteredStatusList(Long userId, Long petId, List<String> find, RequestStatus status);
    List<Pet> filteredPetListByPetOwner(Long userId, Long petId, List<String> find, RequestStatus status);
    List<Request> filteredListByPetOwner(User user, Long petId, List<String> find, RequestStatus status, String searchCriteria, String searchOrder, int page, int pageSize);
    List<RequestStatus> filteredStatusListByPetOwner(Long userId, Long petId, List<String> find, RequestStatus status);
    int getListAmount();
    int getFilteredListAmount(Long userId, Long targetId, Long petId, List<String> find, RequestStatus status);
    int getFilteredListByRequestOwnerAmount(User user, Long petId, List<String> find, RequestStatus status);
    int getFilteredListByPetOwnerAmount(User user, Long petId, List<String> find, RequestStatus status);

    Optional<Request> findById(long id);

    Optional<Request> create(String locale, long user, long pet, String contextURL);
    Optional<Request> update(Request request);

    boolean  cancel(long id, long userId, String contextURL);
    boolean  accept(long id, long userId, String contextURL);
    boolean  reject(long id, long userId, String contextURL);
    boolean recover(long id, long userId, String contextURL);
    boolean sell(Pet pet, User user);

    void adminUpdateStatus(long id, RequestStatus status);
    void  adminCancel(long id);
    void  adminAccept(long id);
    void  adminReject(long id);
    void adminRecover(long id);

    void cancelAllByUser(User user);
    void rejectAllByPetOwner(long petOwnerId);
    void rejectAllByPet(long petId);

    int interestNotifs(User user);
    int requestNotifs(User user);

    void logRequestsAccess(User user);
    void logInterestsAccess(User user);
}
