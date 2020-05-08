package ar.edu.itba.paw.webapp.controller;
import ar.edu.itba.paw.models.Contact;
import ar.edu.itba.paw.models.Request;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.exception.UserNotRequestOwnerException;
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

        final ModelAndView mav = new ModelAndView("views/single_user");
        final String locale = getLocale();

        if (page == null){
            page = "1";
        }
        mav.addObject("currentPage", page);
        mav.addObject("maxPage", petService.getMaxUserPetsPages(id));
        Optional<User> opUser = userService.findById(id);
        if (!opUser.isPresent()) throw new UserNotFoundException("User " + id + " not found");
        mav.addObject("user", opUser.get());
        mav.addObject("userPets", petService.getByUserId(locale, id, page));
        return mav;
    }

    @RequestMapping(value = "/requests")
    public ModelAndView getRequests(@RequestParam(name = "status", required = false) String status,
                                    @RequestParam(name = "searchCriteria", required = false) String searchCriteria,
                                    @RequestParam(name = "searchOrder", required = false) String searchOrder) {

        final ModelAndView mav = new ModelAndView("views/requests");
        final String locale = getLocale();
        final User user = loggedUser();

        status = (status == null || status.equals("any") ? null : status);
        searchCriteria = (searchCriteria == null || searchCriteria.equals("any") ? null : searchCriteria);

        /* Filtered request list */
        if (status != null || searchCriteria != null) {
            mav.addObject("requests_list",
                    requestService.filterListByOwner(locale, user.getId(), status, searchCriteria, searchOrder).toArray());
        }
        /* Default request list */
        else {
            mav.addObject("requests_list",
                    requestService.listByOwner(locale, user.getId()).toArray());
        }
        return mav;
    }

    @RequestMapping(value = "/requests/{id}/cancel", method = {RequestMethod.POST})
    public ModelAndView cancelRequest(@PathVariable("id") long id) {
        final User user = loggedUser();
        final String locale = getLocale();

        if (requestService.cancel(id, user.getId(), locale)) {
            return new ModelAndView("redirect:/requests" );
        }
        return new ModelAndView("redirect:/403" );
    }

    @RequestMapping(value = "/interests")
    public ModelAndView getInterested(@RequestParam(name = "status", required = false) String status,
                                      @RequestParam(name = "searchCriteria", required = false) String searchCriteria,
                                      @RequestParam(name = "searchOrder", required = false) String searchOrder) {

        final ModelAndView mav = new ModelAndView("views/interests");
        final User user = loggedUser();
        final String locale = getLocale();

        status = (status == null || status.equals("any") ? null : status);
        searchCriteria = (searchCriteria == null || searchCriteria.equals("any") ? null : searchCriteria);

        /* Filtered interest list */
        if(status != null || searchCriteria != null) {
            mav.addObject("interests_list",
                    requestService.filterListByPetOwner(locale, user.getId(), status, searchCriteria, searchOrder).toArray());
        }
        /* Default interest list */
        else{
            mav.addObject("interests_list",
                    requestService.listByPetOwner(locale, user.getId()).toArray());
        }
        return mav;
    }

    @RequestMapping(value = "/interests/{id}/accept", method = {RequestMethod.POST})
    public ModelAndView acceptInterest(@PathVariable("id") long id) {
        final User user = loggedUser();
        final String locale = getLocale();

        if (requestService.accept(id, user.getId(), locale)) {
            return new ModelAndView("redirect:/interests" );
        }
        return new ModelAndView("redirect:/403" );
    }

    @RequestMapping(value = "/interests/{id}/reject", method = {RequestMethod.POST})
    public ModelAndView rejectInterest(@PathVariable("id") long id) {
        final User user = loggedUser();
        final String locale = getLocale();

        if (requestService.reject(id, user.getId(), locale)) {
            return new ModelAndView("redirect:/interests" );
        }
        return new ModelAndView("redirect:/403" );
    }
}