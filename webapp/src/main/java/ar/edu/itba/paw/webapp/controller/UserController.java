package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.models.Contact;
import ar.edu.itba.paw.models.Request;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class UserController extends ParentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    String  page = "1";

    @RequestMapping(value = "/user/{id}")
    public ModelAndView user(@PathVariable("id") long id,
                             @RequestParam(name = "page", required = false) String page) {

        if(page == null){
            page = "1";
        }

        final ModelAndView mav = new ModelAndView("views/single_user");
        mav.addObject("currentPage",page);
        mav.addObject("maxPage",petService.getMaxUserPetsPages(id));

        mav.addObject("user",
                userService.findById(id).orElseThrow(UserNotFoundException::new));
        mav.addObject("userPets", petService.getByUserId(getLocale(),id,page));
        LOGGER.debug("Loading user {} page", id);
        return mav;
    }

    @RequestMapping(value = "/requests")
    public ModelAndView getRequests(@RequestParam(name = "status", required = false) String status,
                                    @RequestParam(name = "searchCriteria", required = false) String searchCriteria,
                                    @RequestParam(name = "searchOrder", required = false) String searchOrder) {

        status = (status == null || status.equals("any") ? null : status);
        searchCriteria = (searchCriteria == null || searchCriteria.equals("any") ? null : searchCriteria);
        final ModelAndView mav = new ModelAndView("views/requests");

        if(status != null || searchCriteria != null) {
            mav.addObject("requests_list",
                    requestService.filterListByOwner(getLocale(), loggedUser().getId(), status, searchCriteria, searchOrder).toArray());
        }
        else{
            mav.addObject("requests_list",
                    requestService.listByOwner(getLocale(),loggedUser().getId()).toArray());
        }

        return mav;
    }

    @RequestMapping(value = "/requests-cancel/{id}", method = {RequestMethod.POST})
    public ModelAndView cancelRequest(@RequestParam(name = "newStatus", required = false) String status,
                                      @PathVariable("id") long id) {

        if(loggedUser() != null){
            requestService.delete(id, loggedUser().getId());
        }

        return new ModelAndView("redirect:/requests" );
    }


    @RequestMapping(value = "/interests")
    public ModelAndView getInterested(@RequestParam(name = "status", required = false) String status,
                                      @RequestParam(name = "searchCriteria", required = false) String searchCriteria,
                                      @RequestParam(name = "searchOrder", required = false) String searchOrder) {

        status = (status == null || status.equals("any") ? null : status);
        searchCriteria = (searchCriteria == null || searchCriteria.equals("any") ? null : searchCriteria);
        final ModelAndView mav = new ModelAndView("views/interests");

        if(status != null || searchCriteria != null) {
            mav.addObject("interests_list",
                    requestService.filterListByPetOwner(getLocale(), loggedUser().getId(), status, searchCriteria, searchOrder).toArray());
        }
        else{
            mav.addObject("interests_list",
                    requestService.listByPetOwner(getLocale(),loggedUser().getId()).toArray());
        }
        return mav;
    }

    @RequestMapping(value = "/interests-accept-reject/{id}", method = {RequestMethod.POST})
    public ModelAndView changeStatus(@RequestParam(name = "newStatus", required = false) String status,
                                     @PathVariable("id") long id) {

            if(status.equals("accept")){
                Optional<Request> newRequest = requestService.updateStatus(id,loggedUser().getId(),"accepted",getLocale());
                if(newRequest.isPresent()){
                    Optional<Contact> contact = petService.getPetContact(newRequest.get().getPetId());
                    Optional<User> recipient = userService.findById(newRequest.get().getOwnerId());
                    if(contact.isPresent() && recipient.isPresent()){
                        mailService.sendMail(recipient.get().getMail(), getMailMessage(getLocale(), "subjectAccept", newRequest.get(), contact.get()),  getMailMessage(getLocale(), "bodyAccept", newRequest.get(), contact.get()));
                    }
                }
            }else if (status.equals("reject")){
                Optional<Request> newRequest = requestService.updateStatus(id,loggedUser().getId(),"rejected",getLocale());
                if(newRequest.isPresent()){
                    Optional<Contact> contact = petService.getPetContact(newRequest.get().getPetId());
                    Optional<User> recipient = userService.findById(newRequest.get().getOwnerId());
                    if(contact.isPresent() && recipient.isPresent()){
                        mailService.sendMail(recipient.get().getMail(), getMailMessage(getLocale(), "subjectReject", newRequest.get(), contact.get()),  getMailMessage(getLocale(), "bodyReject", newRequest.get(), contact.get()));
                    }
                }
            }

        return new ModelAndView("redirect:/interests");
    }

    private String getMailMessage(String locale, String part, Request request, Contact contact){
        switch(part){
            case "subjectAccept":
                if(locale.equals("en_US")){
                    return "Hooray! Your request was accepted";
                }else{
                    return "¡Genial! Tu solicitud fue aceptada";
                }
            case "bodyAccept":
                if(locale.equals("en_US")){
                    return "User " + contact.getUsername() + " has accepted your request for "+ request.getPetName() + ". To begin the process for getting your new pet, please contact " + contact.getEmail() + " and ask about " + request.getPetName() + ".";
                }else{
                    return "El usuario " + contact.getUsername() + " ha aceptado tu solicitud de "+ request.getPetName() + ". Para iniciar el proceso de conseguir tu mascota, contáctese con " + contact.getEmail() + " y pregunte por " + request.getPetName() + ".";
                }
            case "subjectReject":
                if(locale.equals("en_US")){
                    return "We're sorry, your request was rejected";
                }else{
                    return "Lo sentimos, tu solicitud fue rechazada";
                }
            case "bodyReject":
                if(locale.equals("en_US")){
                    return "User " + contact.getUsername() + " has rejected your request for "+ request.getPetName() + ".";
                }else{
                    return "El usuario " + contact.getUsername() + " ha rechazado tu solicitud de "+ request.getPetName() + ".";
                }
        }
        return "";
    }

    @RequestMapping(value = "/test")
    public ModelAndView getIdPet() {
        final ModelAndView mav = new ModelAndView("views/test");

        mav.addObject("bool",
                requestService.delete(2,1));

        return mav;
    }

}

