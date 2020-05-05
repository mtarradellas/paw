package ar.edu.itba.paw.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AdminController extends ParentController{

//    @RequestMapping(value = "/admin/{id}")
//    public ModelAndView getAvailable(@PathVariable("id") long id) {
//
//        return new ModelAndView("views/admin");
//    }

    @RequestMapping(value = "/adm")
    public ModelAndView getAvailable() {

        return new ModelAndView("views/admin");
    }


}

