package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Pet;
import ar.edu.itba.paw.models.Request;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.exception.PetNotFoundException;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class AdminController extends ParentController{

    //TODO: cambiar el path del mapping para que te redireccion correctamente
    @RequestMapping(value = "/admi")
    public ModelAndView getAdminHome() {
        return new ModelAndView("admin/admin");
    }

    @RequestMapping(value = "/admi/pets")
    public ModelAndView getPetsAdmin(@RequestParam(name = "page", required = false) String page,
                                     @RequestParam(name = "find", required = false) String find) {
        if(page == null){
            page = "1";
        }

        ModelAndView mav = new ModelAndView("admin/admin_pets");
        mav.addObject("currentPage", page);

        if(find != null){
            String maxPage = petService.getAdminMaxSearchPages(getLocale(),find);
            mav.addObject("maxPage", maxPage);
            List<Pet> petList = petService.adminSearchList(getLocale(), find, page);
            mav.addObject("pets_list", petList);

        }else{
            String maxPage = petService.getAdminMaxPages();
            mav.addObject("maxPage", maxPage);
            List<Pet> petList = petService.adminList(getLocale(), page);
            mav.addObject("pets_list", petList);
        }


        return mav;
    }

    @RequestMapping(value = "/admi/pet/{id}")
    public ModelAndView getSinglePet(@PathVariable("id") long id){

        final ModelAndView mav = new ModelAndView("/admin/admin_single_pet");


        mav.addObject("pet", petService.findById(getLocale(), id).orElseThrow(PetNotFoundException::new));

        return mav;
    }

    @RequestMapping(value = "/admi/users")
    public ModelAndView getUsersAdmin(@RequestParam(name = "page", required = false) String page,
                                      @RequestParam(name = "find", required = false) String find) {
        if(page == null){
            page = "1";
        }

        ModelAndView mav = new ModelAndView("admin/admin_users");
        mav.addObject("currentPage", page);

        if(find != null){
            String maxPage = userService.getAdminMaxSearchPages(getLocale(), find);
            mav.addObject("maxPage", maxPage);
            List<User> userList = userService.adminSearchList(getLocale(), find, page);
            mav.addObject("users_list", userList);

        }else{
            String maxPage = userService.getAdminUserPages();
            mav.addObject("maxPage", maxPage);
            List<User> userList = userService.adminUserList(page);
            mav.addObject("users_list", userList);
        }

        return mav;
    }

    @RequestMapping(value = "/admi/user/{id}")
    public ModelAndView getSingleUser(@PathVariable("id") long id,
                                      @RequestParam(name = "page", required = false) String page){

        final ModelAndView mav = new ModelAndView("/admin/admin_single_user");

        if(page == null){
            page = "1";
        }

        mav.addObject("user", userService.findById(id).orElseThrow(UserNotFoundException::new));
        mav.addObject("maxPage", petService.getMaxUserPetsPages(id));
        mav.addObject("currentPage", page);
        mav.addObject("userPets", petService.getByUserId(getLocale(), id, page));

        return mav;
    }

    @RequestMapping(value = "/admi/requests")
    public ModelAndView getRequestsAdmin(@RequestParam(name = "page", required = false) String page,
                                         @RequestParam(name = "find", required = false) String find) {
        if(page == null){
            page = "1";
        }

        ModelAndView mav = new ModelAndView("admin/admin_requests");
        mav.addObject("currentPage", page);

        if(find != null){
            String maxPage = requestService.getAdminMaxSearchPages(getLocale(), find);
            mav.addObject("maxPage", maxPage);
            List<Request> requestList = requestService.adminSearchList(getLocale(), find, page);
            mav.addObject("requests_list", requestList);

        }else{
            String maxPage = requestService.getAdminRequestPages(getLocale());
            mav.addObject("maxPage", maxPage);
            List<Request> requestList = requestService.adminRequestList(getLocale(), page);
            mav.addObject("requests_list", requestList);
        }

        return mav;
    }

}

