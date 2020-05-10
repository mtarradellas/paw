package ar.edu.itba.paw.services;


import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.models.Contact;
import ar.edu.itba.paw.models.Request;
import ar.edu.itba.paw.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RequestServiceImpl implements RequestService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestServiceImpl.class);

    private final int PENDING_STATUS = 1;
    private final int ACCEPTED_STATUS = 2;
    private final int REJECTED_STATUS = 3;
    private final int CANCELED_STATUS = 4;

    private final String DEFAULT_LOCALE = "es_AR";

    @Autowired
    private RequestDao requestDao;

    @Autowired
    private PetService petService;
    @Autowired
    private MailService mailService;
    @Autowired
    private UserService userService;

    private final int PENDING = 1;

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
    public List<Request> adminRequestList(String language,String page){
        return requestDao.adminRequestList(language, page).collect(Collectors.toList());
    }

    @Override
    public List<Request> adminSearchList(String language, String find, String page) {
        return requestDao.adminSearchList(language, find, page).collect(Collectors.toList());
    }

    @Override
    public List<Request> adminFilteredList(String language, String status, String searchCriteria, String searchOrder, String page) {
        return requestDao.adminFilteredList(language,status,searchCriteria,searchOrder,page).collect(Collectors.toList());
    }

    @Override
    public Optional<Request> create(long userId, long petId, String locale) {
        ArrayList<Integer> statusList = new ArrayList<Integer>() {{
            add(ACCEPTED_STATUS);
            add(REJECTED_STATUS);
            add(PENDING_STATUS);
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

        Optional<Request> opRequest = requestDao.create(userId, petId, PENDING, locale);
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

        mailService.sendMail(contact.getEmail(), getMailMessage(locale, "subject", request),
                getMailMessage(locale, "body", request));

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

        requestDao.updateStatus(id, CANCELED_STATUS);
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

        mailService.sendMail(recipient.getMail(), getMailMessage("subjectCancel", request, contact, locale),
                getMailMessage("bodyCancel", request, contact, locale));
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

        requestDao.updateStatus(id, ACCEPTED_STATUS);

        final Optional<Request> opRequest = requestDao.findById(id, locale);
        if (!opRequest.isPresent()) {
            LOGGER.warn("Request {} not found after successfully updating status to {} by user {}", id, ACCEPTED_STATUS, ownerId);
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

        mailService.sendMail(recipient.getMail(), getMailMessage("subjectAccept", request, contact, locale),
                getMailMessage("bodyAccept", request, contact, locale));

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

        requestDao.updateStatus(id, REJECTED_STATUS);

        final Optional<Request> opRequest = requestDao.findById(id, locale);
        if (!opRequest.isPresent()) {
            LOGGER.warn("Request {} not found after successfully updating status to {} by user {}", id, REJECTED_STATUS, ownerId);
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

        mailService.sendMail(recipient.getMail(), getMailMessage("subjectReject", request, contact, locale),
                getMailMessage("bodyReject", request, contact, locale));

        LOGGER.debug("Request {} rejected by user {}", id, ownerId);
        return true;
    }

    @Override
    public void cancelRequestAdmin(long requestId) {
        requestDao.updateStatus(requestId, CANCELED_STATUS);
    }

    @Override
    public void recoverRequestAdmin(long requestId) {
        requestDao.updateStatus(requestId, PENDING_STATUS);
    }

    @Override
    public void cancelAllByOwner(long ownerId) {
        requestDao.updateAllByOwner(ownerId, PENDING_STATUS, CANCELED_STATUS);
    }

    @Override
    public void cancelAllByPetOwner(long petOwnerId) {
        requestDao.updateAllByPetOwner(petOwnerId, PENDING_STATUS, REJECTED_STATUS);
    }

    private String getMailMessage( String part, Request request, Contact contact, String locale){
        String url = "http://pawserver.it.itba.edu.ar/paw-2020a-7";
        switch(part){
            case "subjectAccept":
                if(locale.equals("en_US")){
                    return "Hooray! Your request was accepted";
                }else{
                    return "¡Genial! Su solicitud fue aceptada";
                }
            case "bodyAccept":
                if(locale.equals("en_US")){
                    return "User " + contact.getUsername() + " has accepted your request for "+ request.getPetName() + "." +
                            " To begin the process for getting your new pet, please contact " + contact.getEmail() + " and ask about " + request.getPetName() + "." +
                            "\nFor more information, contact us at petsociety.contact@gmail.com or go to " + url + "." +
                            "\nSincerely,\nPet Society Team.";
                }else{
                    return "El usuario " + contact.getUsername() + " ha aceptado su solicitud de "+ request.getPetName() + "." +
                            "\nPara iniciar el proceso de conseguir su mascota, contáctese con " + contact.getEmail() + " y pregunte por " + request.getPetName() + "." +
                            "\nPara más información, contáctese con petsociety.contact@gmail.com o vaya a " + url + "." +
                            "\nSinceramente,\nEl equipo de Pet Society." ;
                }
            case "subjectReject":
                if(locale.equals("en_US")){
                    return "We're sorry, your request was rejected";
                }else{
                    return "Lo sentimos, su solicitud fue rechazada";
                }
            case "bodyReject":
                if(locale.equals("en_US")){
                    return "User " + contact.getUsername() + " has rejected your request for "+ request.getPetName() + "." +
                            "\nFor more information, contact us at petsociety.contact@gmail.com or go to " + url + "." +
                            "\nSincerely,\nPet Society Team.";
                }else{
                    return "El usuario " + contact.getUsername() + " ha rechazado su solicitud de "+ request.getPetName() + "." +
                            "\nPara más información, contáctese con petsociety.contact@gmail.com o vaya a " + url + "." +
                            "\nSinceramente,\nEl equipo de Pet Society." ;
                }
        }
        return "";
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

    private String getMailMessage(String locale, String part, Request request){
        String url = "http://pawserver.it.itba.edu.ar/paw-2020a-7";
        switch(part){
            case "subject":
                if(locale.equals("en_US")){
                    return "A user showed interest in one of your pets!";
                }else{
                    return "¡Un usuario mostró interés en una de sus mascotas!";
                }
            case "body":
                if(locale.equals("en_US")){
                    return "User " + request.getOwnerUsername() + " is interested in "+ request.getPetName() + "." +
                            " Go to " + url + " to accept or reject his request!!" +
                            "\nSincerely,\nPet Society Team.";
                }else{
                    return "El usuario " + request.getOwnerUsername() + " está interesado/a en "+ request.getPetName() +
                            "¡¡Vaya a " + url + " para aceptar o rechazar su solicitud!!" +
                            "\nSinceramente,\nEl equipo de Pet Society.";
                }
        }
        return "";
    }

}
