package ar.edu.itba.paw.webapp.controller.admin;

import ar.edu.itba.paw.interfaces.PetService;
import ar.edu.itba.paw.interfaces.RequestService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.models.Request;
import ar.edu.itba.paw.models.constants.RequestStatus;
import ar.edu.itba.paw.webapp.controller.ParentController;
import ar.edu.itba.paw.webapp.exception.BadRequestException;
import ar.edu.itba.paw.webapp.form.AdminUploadRequestForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class AdminRequestController extends ParentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminRequestController.class);

    @Autowired
    private RequestService requestService;

    @Autowired
    private PetService petService;

    @Autowired
    private UserService userService;

    private static final int REQ_PAGE_SIZE = 25;

    /* TODO placeholders for max number (no pagination) */
    private static final int PAGE = 1;
    private static final int PAGE_MAX = 500;

    @RequestMapping(value = "/admin/requests")
    public ModelAndView getRequestsAdmin(@RequestParam(name = "status", required = false) String status,
                                         @RequestParam(name = "searchCriteria", required = false) String searchCriteria,
                                         @RequestParam(name = "searchOrder", required = false) String searchOrder,
                                         @RequestParam(name = "page", required = false) String page,
                                         @RequestParam(name = "find", required = false) String find) {

        ModelAndView mav = new ModelAndView("admin/admin_requests");

        int pageNum = parsePage(page);
        RequestStatus requestStatus = parseStatus(RequestStatus.class, status);
        searchCriteria = parseCriteria(searchCriteria);
        searchOrder = parseOrder(searchOrder);

        if (!isAllowedFind(find)) {
            mav.addObject("wrongSearch", true);
            find = null;
        } else {
            mav.addObject("wrongSearch", false);
        }
        List<String> findList = parseFind(find);

        List<Request> requestList = requestService.filteredList(null, null, findList, requestStatus,
                searchCriteria, searchOrder, pageNum, REQ_PAGE_SIZE);
        int amount = requestService.getFilteredListAmount(null, null, findList, requestStatus);

        mav.addObject("currentPage", pageNum);
        mav.addObject("maxPage", (int) Math.ceil((double) amount / REQ_PAGE_SIZE));
        mav.addObject("requestList", requestList);
        mav.addObject("amount", amount);

        return mav;
    }

    @RequestMapping(value ="/admin/upload-request", method = { RequestMethod.GET })
    public ModelAndView uploadRequestForm(@ModelAttribute("adminUploadRequestForm") final AdminUploadRequestForm requestForm) {
        String locale = getLocale();
        return new ModelAndView("admin/admin_upload_request")
                .addObject("petList", petService.list(locale, PAGE, PAGE_MAX))
                .addObject("userList",userService.list(PAGE, PAGE_MAX));
    }

    @RequestMapping(value = "/admin/upload-request", method = { RequestMethod.POST })
    public ModelAndView uploadRequest(@Valid @ModelAttribute("adminUploadRequestForm") final AdminUploadRequestForm requestForm,
                                      final BindingResult errors, HttpServletRequest request) {
        String locale = getLocale();

        if (errors.hasErrors()) {
            return uploadRequestForm(requestForm);
        }

        Optional<Request> optionalRequest;
        final String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        try {
            optionalRequest = requestService.create(locale, requestForm.getUserId(), requestForm.getPetId(), baseUrl);
        } catch (DataIntegrityViolationException ex) {
            LOGGER.warn("{}", ex.getMessage());
            return uploadRequestForm(requestForm).addObject("requestError", true);
        }

        if (!optionalRequest.isPresent()) {
            return uploadRequestForm(requestForm).addObject("requestError", true);
        }

        return new ModelAndView("redirect:/admin/requests");
    }

    @RequestMapping(value = "/admin/request/{id}/cancel", method = {RequestMethod.POST})
    public ModelAndView requestUpdateCanceled(@PathVariable("id") long id) {
        requestService.adminCancel(id);
        LOGGER.debug("Request {} updated as canceled", id);
        return new ModelAndView("redirect:/admin/requests");

    }

    @RequestMapping(value = "/admin/request/{id}/recover", method = {RequestMethod.POST})
    public ModelAndView requestUpdateRecover(@PathVariable("id") long id) {
        requestService.adminRecover(id);
        LOGGER.debug("Request {} updated as recovered", id);
        return new ModelAndView("redirect:/admin/requests");
    }

    @RequestMapping(value ="/admin/request/{id}/edit", method = { RequestMethod.GET })
    public ModelAndView editRequest(@PathVariable("id") long id) {

        Optional<Request> request = requestService.findById(id);
        if(!request.isPresent()){
            return new ModelAndView("error-views/404");
        }

        return new ModelAndView("admin/admin_edit_request")
                .addObject("request", request.get());
    }

    @RequestMapping(value = "/admin/request/{id}/edit", method = { RequestMethod.POST })
    public ModelAndView uploadRequest(@PathVariable("id") long id,
                                      @RequestParam(name = "newStatus", required = false) String status) {

        /* TODO better error handling */
        RequestStatus requestStatus = parseStatus(RequestStatus.class, status);
        if (requestStatus == null) new ModelAndView("redirect:/admin/requests").addObject("invalidStatus", true);

        try {
            requestService.adminUpdateStatus(id, requestStatus);
        } catch (DataIntegrityViolationException ex) {
            LOGGER.warn("{}", ex.getMessage());
            return new ModelAndView("redirect:/admin/requests").addObject("updateError", true);
        }

        return new ModelAndView("redirect:/admin/requests");
    }
}
