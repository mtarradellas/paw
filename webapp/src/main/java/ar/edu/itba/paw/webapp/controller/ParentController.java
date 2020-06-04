package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.exception.BadRequestException;
import ar.edu.itba.paw.webapp.exception.PetNotFoundException;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Controller
public class ParentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParentController.class);

    @Autowired
    private UserService userService;

    protected String getLocale() {
        Locale locale = LocaleContextHolder.getLocale();
        String lang = locale.getLanguage() + "_" + locale.getCountry();
        if (lang.startsWith("en")) return "en_US";
        else return "es_AR";
    }

    @ModelAttribute("loggedUser")
    public User loggedUser() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return null;
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))) {
            return null;
        }
        return userService.findByUsername(auth.getName()).orElse(null);
    }

    public int parsePage(String page) {
        int pageNum = 1;
        if(page != null) {
            try {
                pageNum = Integer.parseInt(page);
            } catch (NumberFormatException ex) {
                LOGGER.debug("Invalid page ({}) parameter", page);
            }
        }
        return pageNum;
    }

    public <E extends Enum<E>> E parseStatus(Class<E> enumClass, String statusStr) {
        E status = null;
        E[] values = enumClass.getEnumConstants();
        if(statusStr != null) {
            try {
                int idx = Integer.parseInt(statusStr);
                status = values[idx];
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
                LOGGER.debug("Invalid status ({}) parameter", statusStr);
            }
        }
        return status;
    }

    public Long parseSpecies(String speciesStr) {
        Long species = null;
        if (speciesStr != null && !speciesStr.equalsIgnoreCase("any")) {
            try {
                species = Long.parseLong(speciesStr);
            } catch (NumberFormatException ex) {
                LOGGER.debug("Invalid species ({}) parameter", speciesStr);
            }
        }
        return species;
    }

    public Long parseBreed(String breedStr) {
        Long breed = null;
        if (breedStr != null && !breedStr.equalsIgnoreCase("any")) {
            try {
                breed = Long.parseLong(breedStr);
            } catch (NumberFormatException ex) {
                LOGGER.debug("Invalid breed ({}) parameter", breedStr);
            }
        }
        return breed;
    }

    public Long parseProvince(String provinceStr) {
        Long province = null;
        if (provinceStr != null && !provinceStr.equalsIgnoreCase("any")) {
            try {
                province = Long.parseLong(provinceStr);
            } catch (NumberFormatException ex) {
                LOGGER.debug("Invalid province ({}) parameter", provinceStr);
            }
        }
        return province;
    }

    public Long parseDepartment(String departmentStr) {
        Long department = null;
        if (departmentStr != null && !departmentStr.equalsIgnoreCase("any")) {
            try {
                department = Long.parseLong(departmentStr);
            } catch (NumberFormatException ex) {
                LOGGER.debug("Invalid department ({}) parameter", departmentStr);
            }
        }
        return department;
    }

    public boolean isAllowedFind(String find) {
        return find == null || find.matches("^[a-zA-Z \u00C1\u00C9\u00CD\u00D3\u00DA\u00D1\u00DC\u00E1\u00E9\u00ED" +
                                                "\u00F3\u00FA\u00F1\u00FC]*$");
    }

    public List<String> parseFind(String find) {
        if(find == null) return null;
        String[] splitStr = find.trim().split("\\s+");
        return Arrays.asList(splitStr);
    }

    public String parseGender(String gender) {
        if (gender == null) return null;
        if (gender.equalsIgnoreCase("male")) return "male";
        if (gender.equalsIgnoreCase("female")) return "female";
        return null;
    }

    public String parseCriteria(String criteria) {
        if (criteria == null || criteria.equalsIgnoreCase("any")) return null;
        return criteria;
    }

    public String parseOrder(String order) {
        if (order != null && order.equalsIgnoreCase("desc")) return "desc";
        return "asc";
    }

    public int[] parseRange(String range) {
        if (range == null) return new int[]{0, -1};
        int[] price;
        switch (range) {
            case "0" : price = new int[]{0, 0}; break;
            case "1" : price = new int[]{1, 5000}; break;
            case "2" : price = new int[]{5000, 10000}; break;
            case "3" : price = new int[]{10000, 15000}; break;
            case "4" : price = new int[]{15000, 20000}; break;
            case "5" : price = new int[]{20000, 25000}; break;
            case "6" : price = new int[]{25000, -1}; break;
            default: price = new int[]{0, -1}; break;
        }
        return price;
    }

    public int[] parsePrice(String minPrice, String maxPrice) {
        int minPriceNum = parseMinPrice(minPrice);
        int maxPriceNum = parseMaxPrice(maxPrice);

        if (maxPriceNum != -1 && minPriceNum > maxPriceNum) return new int[]{0, -1};
        return new int[]{minPriceNum, maxPriceNum};
    }

    public int parseMinPrice(String price) {
        if (price == null) return 0;
        int priceNum = 0;
        try {
            priceNum = Integer.parseInt(price);
        } catch (NumberFormatException ex) {
            LOGGER.debug("Invalid min price ({}) parameter", price);
        }
        if (priceNum < 0) priceNum = 0;
        return priceNum;
    }

    public int parseMaxPrice(String price) {
        if (price == null) return -1;
        int priceNum = -1;
        try {
            priceNum = Integer.parseInt(price);
        } catch (NumberFormatException ex) {
            LOGGER.debug("Invalid max price ({}) parameter", price);
        }
        if (priceNum < -1) priceNum = -1;
        return priceNum;
    }

    @ExceptionHandler(PetNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView noSuchPet() {
        return new ModelAndView("error-views/404_pet");
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView noSuchUser() {
        return new ModelAndView("error-views/404_user");
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ModelAndView badRequest() {
        return new ModelAndView("error-views/400");
    }

}
