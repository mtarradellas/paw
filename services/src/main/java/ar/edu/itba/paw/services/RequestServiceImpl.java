package ar.edu.itba.paw.services;


import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.constants.RequestStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.Context;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RequestServiceImpl implements RequestService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestServiceImpl.class);

    private final String DEFAULT_LOCALE = "es_AR";

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
        if (find == null) return requestDao.filteredList(user, pet, status, searchCriteria, searchOrder, page, pageSize);
        return requestDao.searchList(user, pet, find, page, pageSize);
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
    public Optional<Request> findById(long id) {
        return requestDao.findById(id);
    }

    @Override
    public Optional<Request> create(User user, Pet pet) {
        List<Request> requestList = user.getRequestList();
        long pendingRequests = requestList.stream().filter(req -> !req.getStatus().equals(RequestStatus.CANCELED)).count();

        if (pendingRequests > 0) {
            LOGGER.warn("Request from user {} to pet {} already exists, ignoring request creation", user.getId(), pet.getId());
            return Optional.empty();
        }
        if (petService.isPetOwner(pet.getId(), user.getId())) {
            LOGGER.warn("User {} is the owner of the requested pet {}, ignoring request creation", user.getId(), pet.getId());
            return Optional.empty();
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

    @Override
    public Optional<Request> update(Request request) {
        return requestDao.update(request);
    }

    @Override
    public boolean cancel(Request request, User user) {
        LOGGER.debug("User {} attempting to cancel request {}", user.getId(), request.getId());

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

    @Override
    public boolean accept(Request request, User user) {
        LOGGER.debug("User {} attempting to accept request {}", user.getId(), request.getId());

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

    @Override
    public boolean reject(Request request, User user) {
        LOGGER.debug("User {} attempting to reject request {}", user.getId(), request.getId());

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

    @Override
    public boolean recover(Request request, User user){
        LOGGER.debug("User {} attempting to recover request {}", user.getId(), request.getId());

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

    @Override
    public void adminUpdateStatus(Request request, RequestStatus status) {
        request.setStatus(status);
        requestDao.update(request);
    }

    @Override
    public void adminCancel(Request request) {
        adminUpdateStatus(request, RequestStatus.CANCELED);
    }

    @Override
    public void adminAccept(Request request) {
        adminUpdateStatus(request, RequestStatus.ACCEPTED);
    }

    @Override
    public void adminReject(Request request) {
        adminUpdateStatus(request, RequestStatus.REJECTED);
    }

    @Override
    public void adminRecover(Request request) {
        adminUpdateStatus(request, RequestStatus.PENDING);
    }

    @Override
    public void cancelAllByUser(User user) {
        requestDao.updateByStatusAndUser(user, RequestStatus.PENDING, RequestStatus.CANCELED);
    }

    @Override
    public void rejectAllByPetOwner(User petOwner) {
        requestDao.updateByStatusAndPetOwner(petOwner, RequestStatus.PENDING, RequestStatus.REJECTED);
    }

    @Override
    public void rejectAllByPet(Pet pet) {
        requestDao.updateByStatusAndPet(pet, RequestStatus.PENDING, RequestStatus.REJECTED);
    }

}
