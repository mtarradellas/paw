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
    public Optional<Request> findById(long id, String language) {
        return requestDao.findById(id, language);
    }

    @Override
    public Stream<Request> listByOwner(String language, long ownerId) {
        return requestDao.listByOwner(language, ownerId);
    }

    @Override
    public Stream<Request> listByPetOwner(String language, long petOwnerId) {
        return requestDao.listByPetOwner(language, petOwnerId);
    }

    @Override
    public RequestList adminRequestList(String language, String findValue, String status, String searchCriteria, String searchOrder, String page){
        if (findValue == null) return adminFilteredList(language, status, searchCriteria, searchOrder, page);
        return adminFind(language, findValue, page);
    }

    @Override
    public RequestList adminFind(String language, String findValue, String page) {
        List<Request> list = requestDao.adminSearchList(language, findValue, page).collect(Collectors.toList()); //1 is admin
        String maxPage = getAdminMaxSearchPages(language, findValue);
        return new RequestList(list, maxPage);
    }

    @Override
    public RequestList adminFilteredList(String language, String status, String searchCriteria, String searchOrder, String page) {
        List<Request> list = requestDao.adminFilteredList(language, status, searchCriteria, searchOrder, page).collect(Collectors.toList());
        String maxPage = getAdminMaxFilterPages(language, status);
        return new RequestList(list, maxPage);
    }

    @Override
    public Optional<Request> create(long userId, long petId, String locale) {
        ArrayList<Integer> statusList = new ArrayList<Integer>() {{
            add(RequestStatus.ACCEPTED.getValue());
            add(RequestStatus.REJECTED.getValue());
            add(RequestStatus.PENDING.getValue());
        }};
        if (requestDao.findIdByStatus(petId, userId, statusList).count() > 0) {
            LOGGER.warn("Request from user {} to pet {} already exists, ignoring request creation", userId, petId);
            return Optional.empty();
        }
        /* TODO change to petService.isPetOwner */
        if (petService.getOwnerId(petId) == userId) {
            LOGGER.warn("User {} is the owner of the requested pet {}, ignoring request creation", userId, petId);
            return Optional.empty();
        }

        Optional<Request> opRequest = requestDao.create(userId, petId, RequestStatus.PENDING.getValue(), locale);
        if (!opRequest.isPresent()) {
            LOGGER.warn("Request creation from user {} to pet {} failed", userId, petId);
            return Optional.empty();
        }
        Request request = opRequest.get();

        Optional<Contact> opContact = petService.getPetContact(petId);
        if (!opContact.isPresent()) {
            LOGGER.warn("Contact info for pet {} not found", petId);
            return opRequest;
        }
        final Contact contact = opContact.get();

        Map<String, Object> arguments = new HashMap<>();
        String url = "http://pawserver.it.itba.edu.ar/paw-2020a-7";

        arguments.put("requestURL", url + "/interests");
        arguments.put("petURL", url + "/pet/" + request.getPetId());
        arguments.put("ownerUsername", request.getOwnerUsername());
        arguments.put("ownerURL", url + "/user/" + request.getOwnerId());
        arguments.put("petName", request.getPetName());

        mailService.sendMail(contact.getEmail(), arguments, "request");

        return opRequest;
    }

    @Override
    public boolean requestExists(long petId, long ownerId, String language){
        Optional<Request> request = requestDao.getRequestByOwnerAndPetId(ownerId, petId, language);
        return request.isPresent();
    }

    @Override
    public Stream<Request> filterListByOwner(String language, long ownerId, String status, String searchCriteria, String searchOrder) {
        return requestDao.filterListByOwner(language, ownerId, status, searchCriteria, searchOrder);
    }

    @Override
    public Stream<Request> filterListByPetOwner(String language, long petOwnerId, String status, String searchCriteria, String searchOrder) {
        return requestDao.filterListByPetOwner(language, petOwnerId, status, searchCriteria, searchOrder);
    }

    @Override
    public boolean cancel(long id, long ownerId, String locale) {
        LOGGER.debug("User {} attempting to cancel request {}", ownerId, id);

        if (!requestDao.isRequestOwner(id, ownerId)) {
            LOGGER.warn("User {} is not Request {} owner, Request not canceled", ownerId, id);
            return false;
        }

        requestDao.updateStatus(id, RequestStatus.CANCELED.getValue());
/*
        final Optional<Request> opRequest = requestDao.findById(id, locale);
        if (!opRequest.isPresent()) {
            LOGGER.warn("Request {} not found after successfully updating status to {} by user {}", id, CANCELED_STATUS, ownerId);
            return false;
        }
        final Request request = opRequest.get();

        final Optional<Contact> opContact = petService.getPetContact(request.getPetId());
        if (!opContact.isPresent()) {
            LOGGER.warn("Contact information for pet {} through request {} not found", request.getPetId(), request.getId());
            return false;
        }

        final Optional<User> opRecipient = userService.findById(request.getOwnerId());
        if (!opRecipient.isPresent()) {
            LOGGER.warn("Recipient user {} through request {} not found", request.getOwnerId(), request.getId());
            return false;
        }

        final Contact contact = opContact.get();
        final User recipient = opRecipient.get();

        Map<String, Object> arguments = new HashMap<>();
        String url = "http://pawserver.it.itba.edu.ar/paw-2020a-7";

        arguments.put("URL", url );
        arguments.put("petURL", url + "/pet/" + request.getPetId());
        arguments.put("ownerUsername", request.getOwnerUsername());
        arguments.put("ownerURL", url +  "/user/" + request.getOwnerId());
        arguments.put("petName", request.getPetName());

        mailService.sendMail(recipient.getMail(), arguments, "request_cancel");
*/

        LOGGER.debug("Request {} canceled by user {}", id, ownerId);
        return true;
    }

    /*  TODO throw exceptions on errors */
    @Override
    public boolean accept(long id, long ownerId, String locale) {
        LOGGER.debug("User {} attempting to accept request {}", ownerId, id);

        if (!requestDao.isRequestTarget(id, ownerId)) {
            LOGGER.warn("User {} is not Request {} target, Request not accepted", ownerId, id);
            return false;
        }

        requestDao.updateStatus(id, RequestStatus.ACCEPTED.getValue());

        final Optional<Request> opRequest = requestDao.findById(id, locale);
        if (!opRequest.isPresent()) {
            LOGGER.warn("Request {} not found after successfully updating status to {} by user {}", id, RequestStatus.ACCEPTED.getValue(), ownerId);
            return false;
        }
        final Request request = opRequest.get();

        final Optional<Contact> opContact = petService.getPetContact(request.getPetId());
        if (!opContact.isPresent()) {
            LOGGER.warn("Contact information for pet {} through request {} not found", request.getPetId(), request.getId());
            return false;
        }

        final Optional<User> opRecipient = userService.findById(DEFAULT_LOCALE, request.getOwnerId());
        if (!opRecipient.isPresent()) {
            LOGGER.warn("Recipient user {} through request {} not found", request.getOwnerId(), request.getId());
            return false;
        }

        final Contact contact = opContact.get();
        final User recipient = opRecipient.get();

        Map<String, Object> arguments = new HashMap<>();
        String url = "http://pawserver.it.itba.edu.ar/paw-2020a-7";

        arguments.put("URL", url );
        arguments.put("petURL", url + "/pet/" + request.getPetId());
        arguments.put("ownerUsername", contact.getUsername());
        arguments.put("contactEmail", contact.getEmail());
        arguments.put("ownerURL", url +  "/user/" + request.getOwnerId());
        arguments.put("petName", request.getPetName());

        mailService.sendMail(recipient.getMail(), arguments, "request_accept");

        LOGGER.debug("Request {} accepted by user {}", id, ownerId);
        return true;
    }

    @Override
    public boolean reject(long id, long ownerId, String locale) {
        LOGGER.debug("User {} attempting to reject request {}", ownerId, id);

        if (!requestDao.isRequestTarget(id, ownerId)) {
            LOGGER.warn("User {} is not Request {} target, Request not rejected", ownerId, id);
            return false;
        }

        requestDao.updateStatus(id, RequestStatus.REJECTED.getValue());

        final Optional<Request> opRequest = requestDao.findById(id, locale);
        if (!opRequest.isPresent()) {
            LOGGER.warn("Request {} not found after successfully updating status to {} by user {}", id, RequestStatus.REJECTED.getValue(), ownerId);
            return false;
        }
        final Request request = opRequest.get();

        final Optional<Contact> opContact = petService.getPetContact(request.getPetId());
        if (!opContact.isPresent()) {
            LOGGER.warn("Contact information for pet {} through request {} not found", request.getPetId(), request.getId());
            return false;
        }

        final Optional<User> opRecipient = userService.findById(DEFAULT_LOCALE, request.getOwnerId());
        if (!opRecipient.isPresent()) {
            LOGGER.warn("Recipient user {} through request {} not found", request.getOwnerId(), request.getId());
            return false;
        }

        final Contact contact = opContact.get();
        final User recipient = opRecipient.get();

        Map<String, Object> arguments = new HashMap<>();
        String url = "http://pawserver.it.itba.edu.ar/paw-2020a-7";

        arguments.put("URL", url );
        arguments.put("petURL", url + "/pet/" + request.getPetId());
        arguments.put("ownerUsername", contact.getUsername());
        arguments.put("ownerURL", url + "/user/" + + request.getOwnerId());
        arguments.put("petName", request.getPetName());

        mailService.sendMail(recipient.getMail(), arguments, "request_reject");

        LOGGER.debug("Request {} rejected by user {}", id, ownerId);
        return true;
    }

    @Override
    public boolean recover(long id, long ownerId, String locale){
        LOGGER.debug("User {} attempting to recover request {}", ownerId, id);

        if (!requestDao.isRequestOwner(id, ownerId)) {
            LOGGER.warn("User {} is not Request {} target, Request not recovered", ownerId, id);
            return false;
        }

        requestDao.updateStatus(id, RequestStatus.PENDING.getValue());

        final Optional<Request> opRequest = requestDao.findById(id, locale);
        if (!opRequest.isPresent()) {
            LOGGER.warn("Request {} not found after successfully updating status to {} by user {}", id, RequestStatus.PENDING.getValue(), ownerId);
            return false;
        }
        final Request request = opRequest.get();

        final Optional<Contact> opContact = petService.getPetContact(request.getPetId());
        if (!opContact.isPresent()) {
            LOGGER.warn("Contact information for pet {} through request {} not found", request.getPetId(), request.getId());
            return false;
        }

        final Optional<User> opRecipient = userService.findById(DEFAULT_LOCALE, request.getOwnerId());
        if (!opRecipient.isPresent()) {
            LOGGER.warn("Recipient user {} through request {} not found", request.getOwnerId(), request.getId());
            return false;
        }

        LOGGER.debug("Request {} recovered by user {}", id, ownerId);
        return true;
    }

    @Override
    public void adminUpdateStatus(long id, String status) {
        if(status.equals("pending")){
            requestDao.updateStatus(id, RequestStatus.PENDING.getValue());
        }else if(status.equals("accepted")){
            requestDao.updateStatus(id, RequestStatus.ACCEPTED.getValue());
        }else if(status.equals("rejected")){
            requestDao.updateStatus(id, RequestStatus.REJECTED.getValue());
        }else if(status.equals("canceled")){
            requestDao.updateStatus(id, RequestStatus.CANCELED.getValue());
        }
    }

    @Override
    public void cancelRequestAdmin(long requestId) {
        requestDao.updateStatus(requestId, RequestStatus.CANCELED.getValue());
    }

    @Override
    public void recoverRequestAdmin(long requestId) {
        requestDao.updateStatus(requestId, RequestStatus.PENDING.getValue());
    }

    @Override
    public void cancelAllByOwner(long ownerId) {
        requestDao.updateAllByOwner(ownerId, RequestStatus.PENDING.getValue(), RequestStatus.CANCELED.getValue());
    }

    @Override
    public void rejectAllByPetOwner(long petOwnerId) {
        requestDao.updateAllByPetOwner(petOwnerId, RequestStatus.PENDING.getValue(), RequestStatus.REJECTED.getValue());
    }

    @Override
    public void rejectAllByPet(long petId) {
        requestDao.updateAllByPet(petId, RequestStatus.PENDING.getValue(), RequestStatus.REJECTED.getValue());
    }

    @Override
    public String getAdminRequestPages(String language){
        return requestDao.getAdminRequestPages(language);
    }

    @Override
    public String getAdminMaxSearchPages(String language, String find) {
        return requestDao.getAdminMaxSearchPages(language,find);
    }

    @Override
    public String getAdminMaxFilterPages(String language, String status) {
        return requestDao.getAdminMaxFilterPages(language, status);
    }


}
