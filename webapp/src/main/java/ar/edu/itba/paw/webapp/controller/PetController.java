package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.ImageService;
import ar.edu.itba.paw.interfaces.MailService;
import ar.edu.itba.paw.interfaces.PetService;
import ar.edu.itba.paw.interfaces.SpeciesService;
import ar.edu.itba.paw.webapp.exception.PetNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Locale;

@Controller
public class PetController {

    @Autowired
    PetService petService;
    @Autowired
    ImageService imageService;
    @Autowired
    SpeciesService speciesService;

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
        mav.addObject("species",species);
        mav.addObject("breed",breed);
        mav.addObject("gender",gender);
        mav.addObject("searchCriteria",searchCriteria);
        mav.addObject("searchOrder",searchOrder);
        mav.addObject("find",findValue);

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
        mav.addObject("pet",
                petService.findById(getLocale(),id).orElseThrow(PetNotFoundException::new));
        mav.addObject("species_list", speciesService.speciesList(getLocale()).toArray());
        mav.addObject("breeds_list", speciesService.breedsList(getLocale()).toArray());
        return mav;
    }

    @RequestMapping(value = "/img/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] getImageWithMediaType(@PathVariable("id") long id) {
        return imageService.getDataById(id).get();
    }

    protected String getLocale() {
        Locale locale = LocaleContextHolder.getLocale();
        String lang = locale.getLanguage() + "_" + locale.getCountry();
        if (lang.startsWith("en")) return "en_US";
        else return "es_AR";
    }

    @ExceptionHandler(PetNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView noSuchPet() {
        return new ModelAndView("error-views/404_pet");
    }

}
