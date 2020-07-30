package ar.edu.itba.paw.services;


import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.interfaces.exceptions.NotFoundException;
import ar.edu.itba.paw.interfaces.exceptions.PetException;
import ar.edu.itba.paw.interfaces.exceptions.UserException;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.constants.MailType;
import ar.edu.itba.paw.models.constants.RequestStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class RequestServiceImpl implements RequestService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestServiceImpl.class);

    @Autowired
    private RequestDao requestDao;
    @Autowired
    private PetService petService;
    @Autowired
    private MailService mailService;
    @Autowired
    private UserService userService;

    @Override
    public List<Request> list(int page, int pageSize) {
        return requestDao.list(page, pageSize);
    }

    @Override
    public List<Request> filteredList(Long userId, Long targetId, Long petId, List<String> find, RequestStatus status, String searchCriteria, String searchOrder, int page, int pageSize) {

        // Requests
        if (userId != null) {
            Optional<User> opUser = userService.findById(userId);
            if (!opUser.isPresent()) throw new NotFoundException("User " + userId + " not found.");
            User user = opUser.get();

            logRequestsAccess(user);
            return filteredListByRequestOwner(user, petId, find, status, searchCriteria, searchOrder, page, pageSize);
        }

        // Interests
        if (targetId != null) {
            Optional<User> opUser = userService.findById(targetId);
            if (!opUser.isPresent()) throw new NotFoundException("User " + userId + " not found.");
            User user = opUser.get();

            logInterestsAccess(user);
            return filteredListByPetOwner(user, petId, find, status, searchCriteria, searchOrder, page, pageSize);
        }

        return list(page, pageSize);
    }

    @Override
    public List<Request> filteredListByRequestOwner(User user, Long petId, List<String> find, RequestStatus status, String searchCriteria, String searchOrder, int page, int pageSize) {
        LOGGER.debug("Parameters for filteredList <Request>: user {}, pet {}, status {}, searchCriteria {}, searchOrder {}, page {}, pageSize {}",
                user, petId, status, searchCriteria, searchOrder, page, pageSize);

        Pet pet = parsePet(petId);
        return requestDao.searchList(user, pet, find, status, searchCriteria, searchOrder, page, pageSize);
    }

    @Override
    public List<RequestStatus> filteredStatusList(Long userId, Long petId, List<String> find, RequestStatus status) {
        Pet pet = parsePet(petId);
        Optional<User> opUser = userService.findById(userId);
        if (!opUser.isPresent()) throw new NotFoundException("User " + userId + " not found.");
        User user = opUser.get();
        Set<Integer> results = requestDao.searchStatusList(user, pet, find, status );
        List<RequestStatus> toReturn = new ArrayList<>();
        results.forEach(r->toReturn.add(RequestStatus.values()[r]));
        return toReturn;
    }

    @Override
    public List<Pet> filteredPetListByPetOwner(Long userId, Long petId, List<String> find, RequestStatus status) {
        Pet pet = parsePet(petId);
        Optional<User> opUser = userService.findById(userId);
        if (!opUser.isPresent()) throw new NotFoundException("User " + userId + " not found.");
        User user = opUser.get();
        if(pet != null) {
            List<Pet> pets = new ArrayList<>();
            pets.add(pet);
            return pets;
        }
        return requestDao.searchPetListByPetOwner(user, null, find, status);
    }

    @Override
    public List<Request> filteredListByPetOwner(User user, Long petId, List<String> find, RequestStatus status, String searchCriteria, String searchOrder, int page, int pageSize) {
        LOGGER.debug("Parameters for filteredListByPetOwner <Request>: user {}, pet {}, status {}, searchCriteria {}, searchOrder {}, page {}, pageSize {}",
                user, petId, status, searchCriteria, searchOrder, page, pageSize);

        Pet pet = parsePet(petId);
        return requestDao.searchListByPetOwner(user, pet, find, status, searchCriteria, searchOrder, page, pageSize);
    }

    @Override
    public List<RequestStatus> filteredStatusListByPetOwner(Long userId, Long petId, List<String> find, RequestStatus status) {
        Pet pet = parsePet(petId);
        Optional<User> opUser = userService.findById(userId);
        if (!opUser.isPresent()) throw new NotFoundException("User " + userId + " not found.");
        User user = opUser.get();
        Set<Integer> results = requestDao.searchStatusListByPetOwner(user, pet, find, status );
        List<RequestStatus> toReturn = new ArrayList<>();
        results.forEach(r->toReturn.add(RequestStatus.values()[r]));
        return toReturn;
    }

    @Override
    public int getListAmount() {
        return requestDao.getListAmount();
    }

    @Override
    public int getFilteredListAmount(Long userId, Long targetId, Long petId, List<String> find, RequestStatus status) {
        // Requests
        if (userId != null) {
            Optional<User> opUser = userService.findById(userId);
            if (!opUser.isPresent()) throw new NotFoundException("User " + userId + " not found.");
            User user = opUser.get();

            return getFilteredListByRequestOwnerAmount(user, petId, find, status);
        }

        // Interests
        if (targetId != null) {
            Optional<User> opUser = userService.findById(targetId);
            if (!opUser.isPresent()) throw new NotFoundException("User " + userId + " not found.");
            User user = opUser.get();

            return getFilteredListByPetOwnerAmount(user, petId, find, status);
        }

        return getListAmount();
    }

    @Override
    public int getFilteredListByRequestOwnerAmount(User user, Long petId, List<String> find, RequestStatus status) {
        Pet pet = parsePet(petId);
        return requestDao.getSearchListAmount(user, pet, find, status);
    }

    @Override
    public int getFilteredListByPetOwnerAmount(User user, Long petId, List<String> find, RequestStatus status) {
        Pet pet = parsePet(petId);
        return requestDao.getSearchListByPetOwnerAmount(user, pet, find, status);
    }

    private Pet parsePet(Long petId) {
        Pet pet = null;
        if (petId != null) {
            pet = petService.findById(petId).orElse(null);
        }
        return pet;
    }

    @Override
    public Optional<Request> findById(long id) {
        return requestDao.findById(id);
    }

    @Transactional
    @Override
    public Optional<Request> create(String locale, long userId, long petId, String contextURL) {
        Optional<User> opUser = userService.findById(userId);
        if (!opUser.isPresent()) {
            LOGGER.warn("User {} not found", userId);
            throw new UserException("Invalid user");
        }
        User user = opUser.get();

        Optional<Pet> opPet = petService.findById(locale, petId);
        if (!opPet.isPresent()) {
            LOGGER.warn("Pet {} not found", petId);
            throw new PetException("Invalid pet");
        }
        Pet pet = opPet.get();

        if (pet.getUser().getId().equals(user.getId())) {
            LOGGER.warn("User {} is pet {} owner, ignoring request", userId, petId);
            return Optional.empty();
        }

        List<Request> requestList = user.getRequestList();
        for (Request req: requestList) {
            if(req.getPet().getId().equals(pet.getId()) && !req.getStatus().equals(RequestStatus.CANCELED)) {
                LOGGER.warn("Request from user {} to pet {} already exists, ignoring request creation", user.getId(), pet.getId());
                return Optional.empty();
            }
        }

        Request request = requestDao.create(user, pet, RequestStatus.PENDING, LocalDateTime.now());

        Map<String, Object> arguments = new HashMap<>();

        arguments.put("requestURL", contextURL + "/interests");
        arguments.put("petURL", contextURL + "/pet/" + pet.getId());
        arguments.put("ownerUsername", request.getUser().getUsername());
        arguments.put("ownerURL", contextURL + "/user/" + user.getId());
        arguments.put("petName", pet.getPetName());

        String userLocale = pet.getUser().getLocale();

        mailService.sendMail(pet.getUser().getMail(), userLocale, arguments, MailType.REQUEST);

        return Optional.of(request);
    }

    @Transactional
    @Override
    public Optional<Request> update(Request request) {
        return requestDao.update(request);
    }

    @Transactional
    @Override
    public boolean cancel(long id, long userId, String contextURL) {
        LOGGER.debug("User {} attempting to cancel request {}", userId, id);

        Optional<User> opUser = userService.findById(userId);
        if (!opUser.isPresent()) throw new NotFoundException("User " + userId + " not found.");
        User user = opUser.get();

        Optional<Request> opRequest = requestDao.findById(id);
        if (!opRequest.isPresent()) {
            LOGGER.warn("Request {} not found", id);
            return false;
        }
        Request request = opRequest.get();

        if (!request.getUser().getId().equals(user.getId())) {
            LOGGER.warn("User {} is not Request {} owner, Request not canceled", user.getId(), request.getId());
            return false;
        }

        request.setStatus(RequestStatus.CANCELED);
        if(!requestDao.update(request).isPresent()) {
            LOGGER.warn("Request {} cancel by user {} failed", request.getId(), user.getId());
            return false;
        }

        Map<String, Object> arguments = new HashMap<>();

        Pet pet = request.getPet();
        User contact = request.getUser();
        User recipient = pet.getUser();

        arguments.put("petURL", contextURL + "/pet/" + pet.getId());
        arguments.put("ownerUsername", contact.getUsername());
        arguments.put("ownerURL", contextURL + "/user/" + + user.getId());
        arguments.put("petName", pet.getPetName());

        String userLocale = recipient.getLocale();

        mailService.sendMail(recipient.getMail(), userLocale, arguments, MailType.REQUEST_CANCEL);

        LOGGER.debug("Request {} canceled by user {}", request.getId(), user.getId());
        return true;
    }

    @Transactional
    @Override
    public boolean accept(long id, long userId, String contextURL) {
        LOGGER.debug("User {} attempting to accept request {}", userId, id);

        Optional<User> opUser = userService.findById(userId);
        if (!opUser.isPresent()) throw new NotFoundException("User " + userId + " not found.");
        User user = opUser.get();

        Optional<Request> opRequest = requestDao.findById(id);
        if (!opRequest.isPresent()) {
            LOGGER.warn("Request {} not found", id);
            return false;
        }
        Request request = opRequest.get();

        if (!request.getPet().getUser().getId().equals(user.getId())) {
            LOGGER.warn("User {} is not Request {} target, Request not accepted", user.getId(), request.getId());
            return false;
        }

        request.setStatus(RequestStatus.ACCEPTED);
        if(!requestDao.update(request).isPresent()) {
            LOGGER.warn("Request {} accept by user {} failed", request.getId(), user.getId());
            return false;
        }

        final User recipient = request.getUser();
        Pet pet = request.getPet();
        User contact = pet.getUser();

        Map<String, Object> arguments = new HashMap<>();

        arguments.put("petURL", contextURL + "/pet/" + pet.getId());
        arguments.put("ownerUsername", contact.getUsername());
        arguments.put("contactEmail", contact.getMail());
        arguments.put("ownerURL", contextURL +  "/user/" + recipient.getId());
        arguments.put("petName", pet.getPetName());

        String userLocale = recipient.getLocale();

        mailService.sendMail(recipient.getMail(), userLocale, arguments, MailType.REQUEST_ACCEPT);

        LOGGER.debug("Request {} accepted by user {}", request.getId(), user.getId());
        return true;
    }

    @Transactional
    @Override
    public boolean reject(long id, long userId, String contextURL) {
        LOGGER.debug("User {} attempting to reject request {}", userId, id);

        Optional<User> opUser = userService.findById(userId);
        if (!opUser.isPresent()) throw new NotFoundException("User " + userId + " not found.");
        User user = opUser.get();

        Optional<Request> opRequest = requestDao.findById(id);
        if (!opRequest.isPresent()) {
            LOGGER.warn("Request {} not found", id);
            return false;
        }
        Request request = opRequest.get();

        if (!request.getPet().getUser().getId().equals(user.getId())) {
            LOGGER.warn("User {} is not Request {} target, Request not rejected", user.getId(), request.getId());
            return false;
        }

        request.setStatus(RequestStatus.REJECTED);
        if(!requestDao.update(request).isPresent()) {
            LOGGER.warn("Request {} reject by user {} failed", request.getId(), user.getId());
            return false;
        }

        User recipient = request.getUser();
        Pet pet = request.getPet();
        User contact = pet.getUser();

        Map<String, Object> arguments = new HashMap<>();

        arguments.put("URL", contextURL );
        arguments.put("petURL", contextURL + "/pet/" + pet.getId());
        arguments.put("ownerUsername", contact.getUsername());
        arguments.put("ownerURL", contextURL + "/user/" + + user.getId());
        arguments.put("petName", pet.getPetName());

        String userLocale = recipient.getLocale();

        mailService.sendMail(recipient.getMail(), userLocale, arguments, MailType.REQUEST_REJECT);

        LOGGER.debug("Request {} rejected by user {}", request.getId(), user.getId());
        return true;
    }

    @Transactional
    @Override
    public boolean recover(long id, long userId, String contextURL){
        LOGGER.debug("User {} attempting to recover request {}", userId, id);

        Optional<User> opUser = userService.findById(userId);
        if (!opUser.isPresent()) throw new NotFoundException("User " + userId + " not found.");
        User user = opUser.get();

        Optional<Request> opRequest = requestDao.findById(id);
        if (!opRequest.isPresent()) {
            LOGGER.warn("Request {} not found", id);
            return false;
        }
        Request request = opRequest.get();

        if (!request.getUser().getId().equals(user.getId())) {
            LOGGER.warn("User {} is not Request {} target, Request not recovered", user.getId(), request.getId());
            return false;
        }

        request.setStatus(RequestStatus.PENDING);
        if (!requestDao.update(request).isPresent()) {
            LOGGER.warn("Request {} recover by user {} failed", request.getId(), user.getId());
            return false;
        }

        Pet pet = request.getPet();
        User contact = request.getUser();
        User recipient = pet.getUser();

        Map<String, Object> arguments = new HashMap<>();

        arguments.put("requestURL", contextURL + "/interests");
        arguments.put("petURL", contextURL + "/pet/" + pet.getId());
        arguments.put("ownerUsername", contact.getUsername());
        arguments.put("ownerURL", contextURL + "/user/" + + user.getId());
        arguments.put("petName", pet.getPetName());

        String userLocale = recipient.getLocale();

        mailService.sendMail(recipient.getMail(), userLocale, arguments, MailType.REQUEST_RECOVER);

//        final Contact contact = opContact.get();
//        final User recipient = request.getUser();

        LOGGER.debug("Request {} recovered by user {}", request.getId(), user.getId());
        return true;
    }

    @Transactional
    @Override
    public boolean sell(Pet pet, User user) {
        Optional<Request> opRequest = user.getRequestList().stream().filter(r -> r.getPet().getId().equals(pet.getId())).findFirst();
        if (!opRequest.isPresent()) {
            LOGGER.warn("No request from user {} to pet {} found", user.getId(), pet.getId());
            return false;
        }
        Request request = opRequest.get();
        request.setStatus(RequestStatus.SOLD);
        requestDao.update(request);
        return true;
    }

    @Transactional
    @Override
    public void adminUpdateStatus(long id, RequestStatus status) {
        Optional<Request> opRequest = requestDao.findById(id);
        if (!opRequest.isPresent()) {
            LOGGER.warn("Request {} not found", id);
            return;
        }
        Request request = opRequest.get();
        request.setStatus(status);
        requestDao.update(request);
    }

    @Transactional
    @Override
    public void adminCancel(long id) {
        adminUpdateStatus(id, RequestStatus.CANCELED);
    }

    @Transactional
    @Override
    public void adminAccept(long id) {
        adminUpdateStatus(id, RequestStatus.ACCEPTED);
    }

    @Transactional
    @Override
    public void adminReject(long id) {
        adminUpdateStatus(id, RequestStatus.REJECTED);
    }

    @Transactional
    @Override
    public void adminRecover(long id) {
        adminUpdateStatus(id, RequestStatus.PENDING);
    }

    @Transactional
    @Override
    public void cancelAllByUser(User user) {
        requestDao.updateByStatusAndUser(user, RequestStatus.PENDING, RequestStatus.CANCELED);
    }

    @Transactional
    @Override
    public void rejectAllByPetOwner(long petOwnerId) {
        Optional<User> opPetOwner = userService.findById(petOwnerId);
        if(!opPetOwner.isPresent()) {
            LOGGER.warn("User {} not found", petOwnerId);
            return;
        }
        User petOwner = opPetOwner.get();
        requestDao.updateByStatusAndPetOwner(petOwner, RequestStatus.PENDING, RequestStatus.REJECTED);
        requestDao.updateByStatusAndPetOwner(petOwner, RequestStatus.ACCEPTED, RequestStatus.REJECTED);
    }

    @Transactional
    @Override
    public void rejectAllByPet(long petId) {
        Optional<Pet> opPet = petService.findById(petId);
        if (!opPet.isPresent()) {
            LOGGER.warn("Pet {} not found", petId);
            return;
        }
        Pet pet = opPet.get();
        requestDao.updateByStatusAndPet(pet, RequestStatus.PENDING, RequestStatus.REJECTED);
        requestDao.updateByStatusAndPet(pet, RequestStatus.ACCEPTED, RequestStatus.REJECTED);
    }

    @Override
    public int interestNotifs(User user) {
        return requestDao.interestNotifs(user);
    }

    @Override
    public int requestNotifs(User user) {
        return requestDao.requestNotifs(user);
    }

    @Override
    public void logRequestsAccess(User user) {
        user.setRequestsDate(LocalDateTime.now());
        userService.update(user);
    }

    @Override
    public void logInterestsAccess(User user) {
        user.setInterestsDate(LocalDateTime.now());
        userService.update(user);
    }

}
