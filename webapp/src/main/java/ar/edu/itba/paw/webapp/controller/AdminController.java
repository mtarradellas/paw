package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Pet;
import ar.edu.itba.paw.models.Request;
import ar.edu.itba.paw.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class AdminController extends ParentController{

    //TODO: cambiar el path del mapping para que te redireccion correctamente
    @RequestMapping(value = "/admi")
    public ModelAndView getPetsAdmin(@RequestParam(name = "page", required = false) String page) {
        if(page == null){
            page = "1";
        }

        ModelAndView mav = new ModelAndView("admin/admin");
        mav.addObject("currentPage", page);

        String maxPage = petService.getAdminPetPages();
        mav.addObject("maxPage", maxPage);
        List<Pet> petList = petService.adminPetList(getLocale(), page);
        mav.addObject("pets_list", petList);

        return mav;
    }

    @RequestMapping(value = "/admi/users")
    public ModelAndView getUsersAdmin(@RequestParam(name = "page", required = false) String page) {
        if(page == null){
            page = "1";
        }

        ModelAndView mav = new ModelAndView("admin/admin_users");
        mav.addObject("currentPage", page);

        String maxPage = userService.getAdminUserPages();
        mav.addObject("maxPage", maxPage);
        List<User> userList = userService.adminUserList(page);
        mav.addObject("users_list", userList);

        return mav;
    }

    @RequestMapping(value = "/admi/requests")
    public ModelAndView getRequestsAdmin(@RequestParam(name = "page", required = false) String page) {
        if(page == null){
            page = "1";
        }

        ModelAndView mav = new ModelAndView("admin/admin_requests");
        mav.addObject("currentPage", page);

        String maxPage = requestService.getAdminRequestPages(getLocale());
        mav.addObject("maxPage", maxPage);
        List<Request> requestList = requestService.adminRequestList(getLocale(), page);
        mav.addObject("requests_list", requestList);

        return mav;
    }

}

