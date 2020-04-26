package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.models.Request;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.exception.PetNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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
            mav.addObject("home_pet_list", petService.filteredList(getLocale(), species, breed, gender, searchCriteria, searchOrder,page));
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
            mav.addObject("currentUserID", loggedUser().getId());
        }else{
            mav.addObject("requestExists", false);
            mav.addObject("currentUserID", -1);
        }

        mav.addObject("pet",
                petService.findById(getLocale(),id).orElseThrow(PetNotFoundException::new));
        mav.addObject("species_list", speciesService.speciesList(getLocale()).toArray());
        mav.addObject("breeds_list", speciesService.breedsList(getLocale()).toArray());
        return mav;
    }

    @RequestMapping(value = "/pet/{id}/request", method = {RequestMethod.POST})
    public ModelAndView requestPet(@PathVariable("id") long id) {

        if(!requestService.requestExists(id,loggedUser().getId(),getLocale())){
            Optional<Request> newRequest =  requestService.create(loggedUser().getId(),id,getLocale());
            if(newRequest.isPresent()){
//                Optional<User> user = ;
//                String mailBody = "User " + newRequest.get().getOwnerUsername() + " is interested in "+ newRequest.get().getPetName() + "." +
//                        " Go to our web page to accept or reject his request!!";
//                mailService.sendMail( ,"A User showed interest in one of your pets!", mailBody);
            }
        }
        return getIdPet(id);
    }

    @RequestMapping(value = "/img/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] getImageWithMediaType(@PathVariable("id") long id) {
        System.out.println("IMG ID: " + id );
        return imageService.getDataById(id).orElse(null);
    }

    @RequestMapping(value = "/test")
    public ModelAndView getIdPet() {
        final ModelAndView mav = new ModelAndView("views/test");

        mav.addObject("contact",
                petService.getPetContact(4).get());

        return mav;
    }
}
