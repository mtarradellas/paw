package ar.edu.itba.paw.webapp.controller;
import ar.edu.itba.paw.models.Contact;
import ar.edu.itba.paw.models.Pet;
import ar.edu.itba.paw.models.Request;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.exception.PetNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;
import java.util.Optional;


@Controller
public class PetController extends ParentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PetController.class);

    @RequestMapping(value = "/", method = { RequestMethod.GET})
    public ModelAndView getHome(@RequestParam(name = "species", required = false) String species,
                                @RequestParam(name = "breed", required = false) String breed,
                                @RequestParam(name = "gender", required = false) String gender,
                                @RequestParam(name = "searchCriteria", required = false) String searchCriteria,
                                @RequestParam(name = "searchOrder", required = false) String searchOrder,
                                @RequestParam(name = "find", required = false) String findValue,
                                @RequestParam(name = "page", required = false) String page,
                                @RequestParam(name = "minPrice", required = false) String minPrice,
                                @RequestParam(name = "maxPrice", required = false) String maxPrice) {

        final ModelAndView mav = new ModelAndView("index");
        final String locale = getLocale();

        if (page == null) {
            page = "1";
        }

        mav.addObject("currentPage", page);

        species = species == null || species.equals("any") ? null : species;
        breed = breed == null || breed.equals("any") ? null : breed;
        gender = gender == null || gender.equals("any") ? null : gender;
        searchCriteria = searchCriteria == null || searchCriteria.equals("any") ? null : searchCriteria;
//check price
        /* Filtered pet list */
        if (species != null || gender != null || searchCriteria != null || minPrice != null || maxPrice != null) {
            String maxPage = petService.getMaxFilterPages(locale, species, breed, gender);
            mav.addObject("maxPage", maxPage);

            LOGGER.debug("Requesting filtered pet list of parameters: locale: {}, spec: {}, breed: {}, gender: {}, sCriteria: {}, sOrder: {}, mPrice: {}, mPrice: {}, page: {}",
                    locale, species, breed, gender, searchCriteria, searchOrder, minPrice, maxPrice, page);
            List<Pet> petList = petService.filteredList(locale, species, breed, gender, searchCriteria,
                    searchOrder, minPrice, maxPrice, page);
            mav.addObject("home_pet_list", petList);
        }
        /* Search input pet list */
        else if (findValue != null) {
            String maxPage = petService.getMaxSearchPages(locale, findValue);
            mav.addObject("maxPage", maxPage);

            LOGGER.debug("Requesting search pet list of parameters: {}, {}, {}", locale, findValue, page);
            mav.addObject("home_pet_list", petService.find(locale, findValue, page).toArray());
        }
        /* Default home pet list */
        else {
            String maxPage = petService.getMaxPages();
            mav.addObject("maxPage", maxPage);

            LOGGER.debug("Requesting full pet list");
            mav.addObject("home_pet_list", petService.list(locale, page));
        }
        mav.addObject("species_list", speciesService.speciesList(locale).toArray());
        mav.addObject("breeds_list", speciesService.breedsList(locale).toArray());
        return mav;
    }

    @RequestMapping(value = "/pet/{id}")
    public ModelAndView getIdPet(@PathVariable("id") long id) {
        final ModelAndView mav = new ModelAndView("views/single_pet");
        User user = loggedUser();
        String locale = getLocale();
        /* Check if user has already requested pet */
        if (user != null) {
            mav.addObject("requestExists", requestService.requestExists(id, user.getId(), locale));
        } else {
            mav.addObject("requestExists", false);
        }
        mav.addObject("pet",
                petService.findById(locale, id).orElseThrow(PetNotFoundException::new));
        return mav;
    }

    @RequestMapping(value = "/pet/{id}/request", method = {RequestMethod.POST})
    public ModelAndView requestPet(@PathVariable("id") final long id) {
        final User user = loggedUser();
        final String locale = getLocale();

        if (user == null) {
            LOGGER.warn("User not authenticated, ignoring request");
            return new ModelAndView("redirect:/403");
        }

        /* TODO Generate exceptions for error handling */
        Optional<Request> opRequest =  requestService.create(user.getId(), id, locale);
        if (!opRequest.isPresent()) {
            LOGGER.warn("Request creation from user {} to pet {} failed", user.getId(), id);
        }
        else {
            final Request request = opRequest.get();
            Optional<Contact> opContact = petService.getPetContact(id);
            if (!opContact.isPresent()) {
                LOGGER.warn("Contact info for pet {} not found", id);
            }
            else {
                final Contact contact = opContact.get();
                mailService.sendMail(contact.getEmail(), getMailMessage("subject", request),
                        getMailMessage("body", request));
            }
        }

        return new ModelAndView("redirect:/pet/" + id );
    }

    @RequestMapping(value = "/pet/{id}/sell-adopt", method = {RequestMethod.POST})
    public ModelAndView petUpdateSold(@PathVariable("id") long id) {
        User user = loggedUser();
        if (user != null && petService.sellPet(id, user.getId())) {
            LOGGER.debug("Pet {} updated as sold", id);
            return new ModelAndView("redirect:/");
        }
        LOGGER.warn("User is not pet owner, pet status not updated");
        return new ModelAndView("redirect:/403");
    }

    @RequestMapping(value = "/pet/{id}/remove", method = {RequestMethod.POST})
    public ModelAndView petUpdateRemoved(@PathVariable("id") long id) {
        User user = loggedUser();
        if (user != null && petService.removePet(id, user.getId())) {
            LOGGER.debug("Pet {} updated as removed", id);
            return new ModelAndView("redirect:/");
        }
        LOGGER.warn("User is not pet owner, pet status not updated");
        return new ModelAndView("redirect:/403");
    }

    @RequestMapping(value = "/img/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] getImageWithMediaType(@PathVariable("id") long id) {
        return imageService.getDataById(id).orElse(null);
    }

    private String getMailMessage( String part, Request request){
        String locale = getLocale();
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
