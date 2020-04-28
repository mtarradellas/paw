package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.models.Contact;
import ar.edu.itba.paw.models.Pet;
import ar.edu.itba.paw.models.Request;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.exception.PetNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
public class PetController extends ParentController {

    @RequestMapping(value = "/", method = { RequestMethod.GET})
    public ModelAndView getHome(@RequestParam(name = "species", required = false) String species,
                                @RequestParam(name = "breed", required = false) String breed,
                                @RequestParam(name = "gender", required = false) String gender,
                                @RequestParam(name = "searchCriteria", required = false) String searchCriteria,
                                @RequestParam(name = "searchOrder", required = false) String searchOrder,
                                @RequestParam(name = "find", required = false) String findValue,
                                @RequestParam(name = "page", required = false) String page){

        if(page == null){
            page = "1";
        }

        final ModelAndView mav = new ModelAndView("index");

        mav.addObject("currentPage", page);

        species = species == null || species.equals("any") ? null : species;
        breed = breed == null || breed.equals("any") ? null : breed;
        gender = gender == null || gender.equals("any") ? null : gender;
        searchCriteria = searchCriteria == null || searchCriteria.equals("any") ? null : searchCriteria;

        if(species != null || gender != null || searchCriteria != null){
            String maxPage = petService.getMaxFilterPages(getLocale(), species, breed, gender);
            mav.addObject("maxPage", maxPage);
            List<Pet> petList = petService.filteredList(getLocale(), species, breed, gender, searchCriteria, searchOrder,page);
            mav.addObject("home_pet_list", petList);
        }
        else if(findValue != null){
            String maxPage = petService.getMaxSearchPages(getLocale(),findValue);
            mav.addObject("maxPage", maxPage);
            mav.addObject("home_pet_list", petService.find(getLocale(),findValue, page).toArray());
        }
        else {
            String maxPage = petService.getMaxPages();
            mav.addObject("maxPage", maxPage);
            mav.addObject("home_pet_list", petService.list(getLocale(),page));
        }

        mav.addObject("species_list", speciesService.speciesList(getLocale()).toArray());
        mav.addObject("breeds_list", speciesService.breedsList(getLocale()).toArray());
        return mav;
    }

    @RequestMapping(value = "/pet/{id}")
    public ModelAndView getIdPet(@PathVariable("id") long id) {
        final ModelAndView mav = new ModelAndView("views/single_pet");

        if(loggedUser() != null){
            mav.addObject("requestExists", requestService.requestExists(id,loggedUser().getId(),getLocale()));
        }else{
            mav.addObject("requestExists", false);
        }

        mav.addObject("pet",
                petService.findById(getLocale(),id).orElseThrow(PetNotFoundException::new));
        mav.addObject("species_list", speciesService.speciesList(getLocale()).toArray());
        mav.addObject("breeds_list", speciesService.breedsList(getLocale()).toArray());
        return mav;
    }

    @RequestMapping(value = "/pet/{id}/request", method = {RequestMethod.POST})
    public ModelAndView requestPet(@PathVariable("id") long id) {
        long ownerId = petService.getOwnerId(id);

        if( loggedUser()!= null && ownerId != loggedUser().getId() && !requestService.requestExists(id,loggedUser().getId(),getLocale())){
            Optional<Request> newRequest =  requestService.create(loggedUser().getId(),id,getLocale());
            if(newRequest.isPresent()){
                Optional<Contact> contact = petService.getPetContact(newRequest.get().getPetId());
                contact.ifPresent(value -> mailService.sendMail(value.getEmail(), getMailMessage(getLocale(), "subject", newRequest.get()), getMailMessage(getLocale(), "body", newRequest.get())));
            }
        }
        return getIdPet(id);
    }

    @RequestMapping(value = "/img/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] getImageWithMediaType(@PathVariable("id") long id) {
        return imageService.getDataById(id).orElse(null);
    }

    private String getMailMessage(String locale, String part, Request request){
        switch(part){
            case "subject":
                if(locale.equals("en_US")){
                    return "A user showed interest in one of your pets!";
                }else{
                    return "¡Un usuario mostró interés en una de tus mascotas!";
                }
            case "body":
                if(locale.equals("en_US")){
                    return "User " + request.getOwnerUsername() + " is interested in "+ request.getPetName() + ". Go to our web page to accept or reject his request!!";
                }else{
                    return "El usuario " + request.getOwnerUsername() + " está interesado/a en "+ request.getPetName() + "¡¡Vaya a nuestro sitio web para aceptar o rechazar su solicitud!!";
                }
        }
        return "";
    }

}
