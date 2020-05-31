package ar.edu.itba.paw.services;


import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.constants.RequestStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.Context;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public List<Request> filteredList(User user, Pet pet, String find, RequestStatus status, String searchCriteria, String searchOrder, int page, int pageSize) {
        LOGGER.debug("Parameters for filteredList <Request>: user {}, pet {}, status {}, searchCriteria {}, searchOrder {}", user, pet,status,searchCriteria,searchOrder);
        if (find == null) return requestDao.filteredList(user, pet, status, searchCriteria, searchOrder, page, pageSize);
        return requestDao.searchList(user, pet, find, page, pageSize);
    }

    @Override
    public List<Request> filteredListByPetOwner(User user, Pet pet, String find, RequestStatus status, String searchCriteria, String searchOrder, int page, int pageSize) {
        if (find == null) return requestDao.filteredListByPetOwner(user, pet, status, searchCriteria, searchOrder, page, pageSize);
        return requestDao.searchListByPetOwner(user, pet, find, page, pageSize);
    }

    @Override
    public int getListAmount() {
        return requestDao.getListAmount();
    }

    @Override
    public int getFilteredListAmount(User user, Pet pet, String find, RequestStatus status) {
        if (find == null) return requestDao.getFilteredListAmount(user, pet, status);
        return requestDao.getSearchListAmount(user, pet, find);
    }

    @Override
    public int getFilteredListByPetOwnerAmount(User user, Pet pet, String find, RequestStatus status) {
        if (find == null) return requestDao.getFilteredListByPetOwnerAmount(user, pet, status);
        return requestDao.getSearchListByPetOwnerAmount(user, pet, find);
    }

    @Override
    public Optional<Request> findById(long id) {
        return requestDao.findById(id);
    }

    @Transactional
    @Override
    public Optional<Request> create(String locale, long userId, long petId) {
        Optional<User> opUser = userService.findById(userId);
        if (!opUser.isPresent()) {
            LOGGER.warn("User {} not found", userId);
            return Optional.empty();
     Optional<Contact> opContact = petService.getPetContact(pet.getId());
        if (!opContact.isPresent()) {
            LOGGER.warn("Contact info for pet {} not found", pet.getId());
            return Optional.of(request);
        }
        final Contact contact = opContact.get();     }
        User user = opUser.get();

        Optional<Pet> opPet = petService.findById(locale, petId);
        if (!opPet.isPresent()) {
            LOGGER.warn("Pet {} not found", petId);
            return Optional.empty();
        }
        Pet pet = opPet.get();

        if (pet.getOwnerId() == user.getId()) {
            LOGGER.warn("User {} is pet {} owner, ignoring request", userId, petId);
            return Optional.empty();
        }

        List<Request> requestList = user.getRequestList();
        for (Request req: requestList) {
            if(req.getPetId() == pet.getId() && !req.getStatus().equals(RequestStatus.CANCELED)) {
                LOGGER.warn("Request from user {} to pet {} already exists, ignoring request creation", user.getId(), pet.getId());
                return Optional.empty();
            }
        }

        Request request = requestDao.create(user, pet, RequestStatus.PENDING);

        Optional<Contact> opContact = petService.getPetContact(pet.getId());
        if (!opContact.isPresent()) {
            LOGGER.warn("Contact info for pet {} not found", pet.getId());
            return Optional.of(request);
        }
        final Contact contact = opContact.get();

        Map<String, Object> arguments = new HashMap<>();
        String url = "http://pawserver.it.itba.edu.ar/paw-2020a-7";

        arguments.put("requestURL", url + "/interests");
        arguments.put("petURL", url + "/pet/" + pet.getId());
        arguments.put("ownerUsername", user.getUsername());
        arguments.put("ownerURL", url + "/user/" + user.getId());
        arguments.put("petName", pet.getPetName());

        mailService.sendMail(contact.getEmail(), arguments, "request");

        return Optional.of(request);
    }

    @Transactional
    @Override
    public Optional<Request> update(Request request) {
        return requestDao.update(request);
    }

    @Transactional
    @Override
    public boolean cancel(long id, User user) {
        LOGGER.debug("User {} attempting to cancel request {}", user.getId(), id);

        Optional<Request> opRequest = requestDao.findById(id);
        if (!opRequest.isPresent()) {
            LOGGER.warn("Request {} not found", id);
            return false;
        }
        Request request = opRequest.get();

        if (!request.getUser().equals(user)) {
            LOGGER.warn("User {} is not Request {} owner, Request not canceled", user.getId(), request.getId());
            return false;
        }

        request.setStatus(RequestStatus.CANCELED);
        if(!requestDao.update(request).isPresent()) {
            LOGGER.warn("Request {} cancel by user {} failed", request.getId(), user.getId());
            return false;
        }

        LOGGER.debug("Request {} canceled by user {}", request.getId(), user.getId());
        return true;
    }

    @Transactional
    @Override
    public boolean accept(long id, User user) {
        LOGGER.debug("User {} attempting to accept request {}", user.getId(), id);

        Optional<Request> opRequest = requestDao.findById(id);
        if (!opRequest.isPresent()) {
            LOGGER.warn("Request {} not found", id);
            return false;
        }
        Request request = opRequest.get();

        if (!requestDao.isRequestTarget(request, user)) {
            LOGGER.warn("User {} is not Request {} target, Request not accepted", user.getId(), request.getId());
            return false;
        }

        request.setStatus(RequestStatus.ACCEPTED);
        if(!requestDao.update(request).isPresent()) {
            LOGGER.warn("Request {} accept by user {} failed", request.getId(), user.getId());
            return false;
        }

        final Optional<Contact> opContact = petService.getPetContact(request.getPetId());
        if (!opContact.isPresent()) {
            LOGGER.warn("Contact information for pet {} through request {} not found", request.getPetId(), request.getId());
            return false;
        }

        final Contact contact = opContact.get();
        final User recipient = request.getUser();

        Map<String, Object> arguments = new HashMap<>();
        String url = "http://pawserver.it.itba.edu.ar/paw-2020a-7";

        arguments.put("URL", url );
        arguments.put("petURL", url + "/pet/" + request.getPetId());
        arguments.put("ownerUsername", contact.getUsername());
        arguments.put("contactEmail", contact.getEmail());
        arguments.put("ownerURL", url +  "/user/" + recipient.getId());
        arguments.put("petName", request.getPetName());

        mailService.sendMail(recipient.getMail(), arguments, "request_accept");

        LOGGER.debug("Request {} accepted by user {}", request.getId(), user.getId());
        return true;
    }

    @Transactional
    @Override
    public boolean reject(long id, User user) {
        LOGGER.debug("User {} attempting to reject request {}", user.getId(), id);

        Optional<Request> opRequest = requestDao.findById(id);
        if (!opRequest.isPresent()) {
            LOGGER.warn("Request {} not found", id);
            return false;
        }
        Request request = opRequest.get();

        if (!requestDao.isRequestTarget(request, user)) {
            LOGGER.warn("User {} is not Request {} target, Request not rejected", user.getId(), request.getId());
            return false;
        }

        request.setStatus(RequestStatus.REJECTED);
        if(!requestDao.update(request).isPresent()) {
            LOGGER.warn("Request {} reject by user {} failed", request.getId(), user.getId());
            return false;
        }

        final Optional<Contact> opContact = petService.getPetContact(request.getPetId());
        if (!opContact.isPresent()) {
            LOGGER.warn("Contact information for pet {} through request {} not found", request.getPetId(), request.getId());
            return false;
        }

        final Contact contact = opContact.get();
        final User recipient = request.getUser();

        Map<String, Object> arguments = new HashMap<>();
        String url = "http://pawserver.it.itba.edu.ar/paw-2020a-7";

        arguments.put("URL", url );
        arguments.put("petURL", url + "/pet/" + request.getPetId());
        arguments.put("ownerUsername", contact.getUsername());
        arguments.put("ownerURL", url + "/user/" + + user.getId());
        arguments.put("petName", request.getPetName());

        mailService.sendMail(recipient.getMail(), arguments, "request_reject");

        LOGGER.debug("Request {} rejected by user {}", request.getId(), user.getId());
        return true;
    }

    @Transactional
    @Override
    public boolean recover(long id, User user){
        LOGGER.debug("User {} attempting to recover request {}", user.getId(), id);

        Optional<Request> opRequest = requestDao.findById(id);
        if (!opRequest.isPresent()) {
            LOGGER.warn("Request {} not found", id);
            return false;
        }
        Request request = opRequest.get();

        if (!request.getUser().equals(user)) {
            LOGGER.warn("User {} is not Request {} target, Request not recovered", user.getId(), request.getId());
            return false;
        }

        request.setStatus(RequestStatus.PENDING);
        if (!requestDao.update(request).isPresent()) {
            LOGGER.warn("Request {} recover by user {} failed", request.getId(), user.getId());
            return false;
        }

        final Optional<Contact> opContact = petService.getPetContact(request.getPetId());
        if (!opContact.isPresent()) {
            LOGGER.warn("Contact information for pet {} through request {} not found", request.getPetId(), request.getId());
            return false;
        }

        final Contact contact = opContact.get();
        final User recipient = request.getUser();

        LOGGER.debug("Request {} recovered by user {}", request.getId(), user.getId());
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
    }

    @Transactional
    @Override
    public void rejectAllByPet(String locale, long petId) {
        Optional<Pet> opPet = petService.findById(locale, petId);
        if (!opPet.isPresent()) {
            LOGGER.warn("Pet {} not found", petId);
            return;
        }
        Pet pet = opPet.get();
        requestDao.updateByStatusAndPet(pet, RequestStatus.PENDING, RequestStatus.REJECTED);
    }

}
