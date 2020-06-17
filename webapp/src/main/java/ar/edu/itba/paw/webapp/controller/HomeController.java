package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.models.Answer;
import ar.edu.itba.paw.models.Question;
import ar.edu.itba.paw.models.Request;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.exception.PetNotFoundException;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
public class HomeController extends ParentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParentController.class);

    @Autowired
    private SpeciesService speciesService;
    @Autowired
    private PetService petService;
    @Autowired
    private UserService userService;
    @Autowired
    private RequestService requestService;

    @Autowired
    private LocationService locationService;

    @RequestMapping("/available")
    public ModelAndView getAvailable() {
        final String locale = getLocale();
        return new ModelAndView("views/available")
                    .addObject("speciesList", speciesService.speciesList(locale).toArray())
                    .addObject("breedList", speciesService.breedList(locale).toArray());
    }

    @RequestMapping(value = "/admin")
    public ModelAndView getAdminHome() {
        return new ModelAndView("admin/admin");
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ModelAndView test() {
        ModelAndView mav = new ModelAndView("views/test");
//        List<Question> questionList = petService.listQuestions(1, 1, 50);
//        int amount = petService.getListQuestionsAmount(1);
//        mav.addObject("questionList", questionList);
//        mav.addObject("amount", amount);
        List<Request> reqList = requestService.filteredList(null,petService.findById(12L).get(),
                null,null,null,null,1,12 );
        reqList.forEach(System.out::println);
        mav.addObject("filteredRe", reqList);
        return mav;
    }

//    @RequestMapping(value = "/test", method = RequestMethod.POST)
//    public ModelAndView testPost() {
//        User user = userService.findById(1L).orElseThrow(UserNotFoundException::new);
//        System.out.println("\nCREATE ANSWER\n");
//        Optional<Answer> optionalAnswer = petService.createAnswer(2, "TEST", user);
//        if (!optionalAnswer.isPresent()) {
//            LOGGER.warn("ANSWER FALLUTA");
//            throw new PetNotFoundException();
//        }
//        return new ModelAndView("redirect:/test");
//    }
}
