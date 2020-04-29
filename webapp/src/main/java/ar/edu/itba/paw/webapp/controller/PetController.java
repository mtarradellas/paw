package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.interfaces.exception.DuplicateUserException;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.exception.PetNotFoundException;
import ar.edu.itba.paw.webapp.form.UploadPetForm;
import ar.edu.itba.paw.webapp.form.UserForm;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
        return new ModelAndView("redirect:/pet/" + id );
    }

    @RequestMapping(value = "/pet/{id}/sell-adopt", method = {RequestMethod.POST})
    public ModelAndView petUpdateSold(@PathVariable("id") long id) {
        User user = loggedUser();
        /* TODO change sold status ID hardcoded*/
        if (user != null && petService.updateStatus(id, user.getId(), 3)) {
            return new ModelAndView("redirect:/");
        }
        return new ModelAndView("redirect:/403");
    }

    @RequestMapping(value = "/pet/{id}/remove", method = {RequestMethod.POST})
    public ModelAndView petUpdateRemoved(@PathVariable("id") long id) {
        User user = loggedUser();
        /* TODO change removed status ID hardcoded*/
        if (user != null && petService.updateStatus(id, user.getId(), 2)) {
            return new ModelAndView("redirect:/");
        }
        return new ModelAndView("redirect:/403");
    }


    @RequestMapping(value = "/img/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] getImageWithMediaType(@PathVariable("id") long id) {
        return imageService.getDataById(id).orElse(null);
    }

    @RequestMapping(value ="/upload-pet", method = { RequestMethod.GET })
    public ModelAndView uploadPetForm(@ModelAttribute ("uploadPetForm") final UploadPetForm userForm) {
        return new ModelAndView("views/upload_pet")
                .addObject("species_list", speciesService.speciesList(getLocale()).toArray())
                .addObject("breeds_list", speciesService.breedsList(getLocale()).toArray());
    }

    @RequestMapping(value = "/upload-pet", method = { RequestMethod.POST })
    public ModelAndView uploadPet(@Valid @ModelAttribute("uploadPetForm") final UploadPetForm petForm,
                                   final BindingResult errors, HttpServletRequest request) {


        if (errors.hasErrors()) {
            return uploadPetForm(petForm);
        }

        Date currentDate = new java.sql.Date(System.currentTimeMillis());
        Date birthDate = java.sql.Date.valueOf(String.valueOf(petForm.getBirthDate()));

        Optional<Pet> opPet = petService.create(getLocale(), petForm.getPetName(), petForm.getSpeciesId(), petForm.getBreedId(),
                          petForm.getLocation(), petForm.isVaccinated(), petForm.getGender(), petForm.getDescription(),
                          birthDate, currentDate, petForm.getPrice(), loggedUser().getId());

        if (!opPet.isPresent()) {
            System.out.println("pet not created");
            return uploadPetForm(petForm).addObject("error", true);
        }

        Optional<Image> opImage = imageService.create(opPet.get().getId(), petForm.getPhoto(), loggedUser().getId());

        if (!opImage.isPresent()) {
            System.out.println("img not created");
            return uploadPetForm(petForm).addObject("error", true);
        }

        //TODO: redireccionar a la view del pet?
        return new ModelAndView("redirect:/");
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
