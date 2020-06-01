package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.interfaces.exception.InvalidImageQuantityException;
import ar.edu.itba.paw.models.Pet;
import ar.edu.itba.paw.models.Request;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.constants.PetStatus;
import ar.edu.itba.paw.webapp.exception.ImageLoadException;
import ar.edu.itba.paw.webapp.exception.PetNotFoundException;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.EditPetForm;
import ar.edu.itba.paw.webapp.form.UploadPetForm;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;


@Controller
public class PetController extends ParentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PetController.class);

    @Autowired
    PetService petService;

    @Autowired
    private SpeciesService speciesService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private RequestService requestService;

    private static final int PET_PAGE_SIZE = 12;

    @RequestMapping(value = "/", method = { RequestMethod.GET})
    public ModelAndView getHome(@RequestParam(name = "species", required = false) String species,
                                @RequestParam(name = "breed", required = false) String breed,
                                @RequestParam(name = "gender", required = false) String gender,
                                @RequestParam(name = "searchCriteria", required = false) String searchCriteria,
                                @RequestParam(name = "searchOrder", required = false) String searchOrder,
                                @RequestParam(name = "find", required = false) String find,
                                @RequestParam(name = "page", required = false) String page,
                                @RequestParam(name = "status", required = false) String status,
                                @RequestParam(name = "priceRange", required = false) String priceRange,
                                @RequestParam(name = "province", required = false) String province,
                                @RequestParam(name = "department", required = false) String department) {

        final ModelAndView mav = new ModelAndView("index");
        final String locale = getLocale();

        int pageNum = parsePage(page);
        PetStatus petStatus = parseStatus(PetStatus.class, status);
        Long speciesId = parseSpecies(species);
        Long breedId = parseSpecies(breed);
        gender = parseGender(gender);
        searchCriteria = parseCriteria(searchCriteria);
        searchOrder = parseOrder(searchOrder);
        int[] price = parseRange(priceRange);
        int minPriceNum = price[0];
        int maxPriceNum = price[1];
        Long provinceId = parseProvince(province);
        Long departmentId = parseDepartment(department);

        if (!parseFind(find)) {
            mav.addObject("wrongSearch", true);
            find = null;
        } else {
            mav.addObject("wrongSearch", false);
        }

        List<Pet> petList = petService.filteredList(locale, find, null, speciesId, breedId, gender, PetStatus.AVAILABLE,
                searchCriteria, searchOrder, minPriceNum, maxPriceNum, provinceId, departmentId, pageNum, PET_PAGE_SIZE);
        int amount = petService.getFilteredListAmount(find, null, speciesId, breedId, gender, PetStatus.AVAILABLE, minPriceNum,
                maxPriceNum, provinceId, departmentId);

        Object[] departments = petList.stream().map(Pet::getDepartment).distinct().sorted(Department::compareTo).toArray();
        Object[] provinces = petList.stream().map(Pet::getProvince).distinct().sorted(Province::compareTo).toArray();
        Object[] breeds = petList.stream().map(Pet::getBreed).distinct().sorted(Breed::compareTo).toArray();
        Object[] speciesL = petList.stream().map(Pet::getSpecies).distinct().sorted(Species::compareTo).toArray();

        mav.addObject("currentPage", pageNum);
        mav.addObject("maxPage", (int) Math.ceil((double) amount / PET_PAGE_SIZE));
        mav.addObject("homePetList", petList.toArray());
        mav.addObject("amount", amount);

        mav.addObject("speciesList", speciesL);
        mav.addObject("breedList", breeds);
        mav.addObject("provinceList", provinces);
        mav.addObject("departmentList", departments);

        mav.addObject("find", find);
        return mav;
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET, headers="Accept=*/*")
    @ResponseBody
    public void search(HttpServletRequest request, final HttpServletResponse response) throws IOException {
        List<String> searchValues = petService.autocompleteFind(getLocale(),request.getParameter("term"));
        response.setContentType("application/json");

        final String param = request.getParameter("term");
        final List<AutoCompleteData> result = new ArrayList<>();
        for (final String country : searchValues) {
            if (country.toLowerCase().contains(param.toLowerCase())) {
                result.add(new AutoCompleteData(country, country));
            }
        }
        response.getWriter().write(new Gson().toJson(result));
    }


    @RequestMapping(value = "/pet/{id}")
    public ModelAndView getIdPet(@PathVariable("id") long id) {
        final ModelAndView mav = new ModelAndView("views/single_pet");
        User user = loggedUser();
        String locale = getLocale();
        /* Check if user has already requested pet */
        if (user != null && !user.getRequestList().isEmpty()) {
            Optional<Request> opRequest = user.getRequestList().stream().filter(request -> request.getPet().getId() == id).max(Comparator.comparing(Request::getCreationDate));
            if (!opRequest.isPresent()) {
                LOGGER.debug("User {} has no request for pet {}", user.getId(), id);
                mav.addObject("lastRequest", null);
                mav.addObject("requestExists", false);
            }
            else {
                LOGGER.debug("User {} last request status for pet {} is {}", user.getId(), id, opRequest.get().getId());
                mav.addObject("lastRequest", opRequest.get().getStatus());
                mav.addObject("requestExists", true);
            }
        } else {
            LOGGER.debug("User is not authenticated or has no requests");
            mav.addObject("lastRequest", null);
            mav.addObject("requestExists", false);
        }
        Pet pet = petService.findById(locale, id).orElseThrow(PetNotFoundException::new);
        mav.addObject("pet", pet);

        return mav;
    }

    @RequestMapping(value = "/pet/{id}/request", method = {RequestMethod.POST})
    public ModelAndView requestPet(@PathVariable("id") final long id) {
        final ModelAndView mav = new ModelAndView("redirect:/pet/" + id);
        final User user = loggedUser();
        final String locale = getLocale();

        if (user == null) {
            LOGGER.warn("User not authenticated, ignoring request");
            return new ModelAndView("redirect:/403");
        }

        /* TODO Generate exceptions for error handling */

        Optional<Request> opRequest =  requestService.create(locale, user.getId(), id);
        if (!opRequest.isPresent()) {
            mav.addObject("requestError", true);
        }

        return new ModelAndView("redirect:/pet/" + id );
    }

    @RequestMapping(value = "/pet/{id}/sell-adopt", method = {RequestMethod.POST})
    public ModelAndView petUpdateSold(@PathVariable("id") long id) {
        User user = loggedUser();
        if (user != null && petService.sellPet(id, user)) {
            LOGGER.debug("Pet {} updated as sold", id);
            return new ModelAndView("redirect:/");
        }
        LOGGER.warn("User is not pet owner, pet status not updated");
        return new ModelAndView("redirect:/403");
    }

    @RequestMapping(value = "/pet/{id}/remove", method = {RequestMethod.POST})
    public ModelAndView petUpdateRemoved(@PathVariable("id") long id) {
        User user = loggedUser();
        if (user != null && petService.removePet(id, user)) {
            LOGGER.debug("Pet {} updated as removed", id);
            return new ModelAndView("redirect:/");
        }
        LOGGER.warn("User is not pet owner, pet status not updated");
        return new ModelAndView("redirect:/403");
    }

    @RequestMapping(value = "/pet/{id}/recover", method = {RequestMethod.POST})
    public ModelAndView petUpdateRecover(@PathVariable("id") long id) {
        User user = loggedUser();
        if (user != null && petService.recoverPet(id, user)) {
            LOGGER.debug("Pet {} updated as recovered", id);
            return new ModelAndView("redirect:/pet/{id}");
        }
        LOGGER.warn("User is not pet owner, pet status not updated");
        return new ModelAndView("redirect:/403");
    }

    @RequestMapping(value = "/img/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody
    byte[] getImageWithMediaType(@PathVariable("id") long id) throws IOException {
        byte[] byteImage = imageService.getDataById(id).orElse(null);
        if(byteImage == null){
            return null;

        }

        ByteArrayInputStream bis = new ByteArrayInputStream(byteImage);
        BufferedImage bufferedImage = ImageIO.read(bis);
        int height = bufferedImage.getHeight(), width = bufferedImage.getWidth();

        BufferedImage cropped = bufferedImage;
        int diff = Math.abs(height-width);
        if(width>height){
            cropped = bufferedImage.getSubimage(diff/2, 0, width-diff, height);
        }else{ if(width<height)
            cropped = bufferedImage.getSubimage(0, diff/2, width, height-diff);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(cropped, "jpg", baos );
        baos.flush();
        byte[] imageInByte = baos.toByteArray();
        baos.close();
        return imageInByte;
    }

    @RequestMapping(value ="/upload-pet", method = { RequestMethod.GET })
    public ModelAndView uploadPetForm(@ModelAttribute ("uploadPetForm") final UploadPetForm petForm) {
        ModelAndView mav = new ModelAndView("views/upload_pet");
        String locale = getLocale();

        List<Species> speciesList = speciesService.speciesList(locale);
        List<Breed> breedList = speciesService.breedList(locale);
        List<Province> provinceList = locationService.provinceList();
        List<Department> departmentList = locationService.departmentList();

        mav.addObject("provinceList", provinceList);
        mav.addObject("departmentList", departmentList);
        mav.addObject("speciesList", speciesList);
        mav.addObject("breedList", breedList);
        return mav;
    }

    @RequestMapping(value = "/upload-pet", method = { RequestMethod.POST })
    public ModelAndView uploadPet(@Valid @ModelAttribute("uploadPetForm") final UploadPetForm petForm,
                                  final BindingResult errors, HttpServletRequest request) {
        if (errors.hasErrors()) {
            return uploadPetForm(petForm);
        }

        User user = loggedUser();
        if (user == null) throw new UserNotFoundException();

        List<byte[]> photos = new ArrayList<>();
        try {
            for (MultipartFile photo : petForm.getPhotos()) {
                try {
                    photos.add(photo.getBytes());
                } catch (IOException ex) {
                    ex.printStackTrace();
                    throw new ImageLoadException(ex);
                }
            }
        } catch (ImageLoadException ex) {
            LOGGER.warn("Image bytes load from pet form failed");
            return uploadPetForm(petForm).addObject("imageError", true);
        }

        Optional<Pet> opPet = petService.create(getLocale(), petForm.getPetName(), petForm.getBirthDate(), petForm.getGender(),
                petForm.getVaccinated(), petForm.getPrice(), petForm.getDescription(), PetStatus.AVAILABLE, user.getId(),
                petForm.getSpeciesId(), petForm.getBreedId(), petForm.getProvince(), petForm.getDepartment(), photos);

        if (!opPet.isPresent()) {
            LOGGER.warn("Pet could not be created");
            return uploadPetForm(petForm).addObject("petError", true);
        }

        return new ModelAndView("redirect:/pet/" + opPet.get().getId());
    }


    @RequestMapping(value = "/edit-pet/{id}", method = { RequestMethod.GET })
    public ModelAndView editPetGet(@ModelAttribute("editPetForm") final EditPetForm petForm, @PathVariable("id") long id) {

        Pet pet = petService.findById(getLocale(), id).orElseThrow(PetNotFoundException::new);

        if(pet.getUser().equals(loggedUser())) {

            List<Department> departmentList = locationService.departmentList();
            List<Province> provinceList = locationService.provinceList();

            petForm.setBirthDate(pet.getBirthDate());
            petForm.setBreedId(pet.getBreed().getId());
            petForm.setDescription(pet.getDescription());
            petForm.setGender(pet.getGender());
            petForm.setProvince(pet.getProvince().getId());
            petForm.setDepartment(pet.getDepartment().getId());
            petForm.setPrice(pet.getPrice());
            petForm.setPetName(pet.getPetName());
            petForm.setSpeciesId(pet.getSpecies().getId());
            petForm.setVaccinated(pet.isVaccinated());

            return editPetForm(petForm, id)
                    .addObject("provinceList", provinceList)
                    .addObject("departmentList", departmentList);
        }
        return new ModelAndView("redirect:/403" );

    }

    private ModelAndView editPetForm(@ModelAttribute("editPetForm") final EditPetForm editPetForm, long id) {
        String locale = getLocale();

        Pet pet = petService.findById(getLocale(), id).orElseThrow(PetNotFoundException::new);
        List<Species> speciesList = speciesService.speciesList(locale);
        List<Breed> breedList = speciesService.breedList(locale);
        List<Department> departmentList = locationService.departmentList();
        List<Province> provinceList = locationService.provinceList();

        return new ModelAndView("views/pet_edit")
                .addObject("speciesList", speciesList)
                .addObject("breedList", breedList)
                .addObject("provinceList", provinceList)
                .addObject("departmentList", departmentList)
                .addObject("pet", pet)
                .addObject("id", id);
    }

    @RequestMapping(value = "/edit-pet/{id}", method = { RequestMethod.POST })
    public ModelAndView editPet(@Valid @ModelAttribute("editPetForm") final EditPetForm editPetForm,
                                  final BindingResult errors, HttpServletRequest request,
                                @PathVariable("id") long id) {
        User user = loggedUser();
        if (user == null) throw new UserNotFoundException();
        String locale = getLocale();

        if (errors.hasErrors()) {
            return editPetForm(editPetForm, id);
        }
        List<byte[]> photos = new ArrayList<>();
        try {
            for (MultipartFile photo : editPetForm.getPhotos()) {
                if(!photo.isEmpty()) {
                    try {
                        photos.add(photo.getBytes());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        throw new ImageLoadException(ex);
                    }
                }
            }
        } catch (ImageLoadException ex) {
            LOGGER.warn("Image bytes load from pet form failed");
            return editPetForm(editPetForm, id).addObject("imageError", true);
        }

        Optional<Pet> opPet;
        try {
             opPet = petService.update(locale, id, user.getId(), editPetForm.getPetName(), editPetForm.getBirthDate(),
                     editPetForm.getGender(), editPetForm.getVaccinated(), editPetForm.getPrice(), editPetForm.getDescription(),
                     null, editPetForm.getSpeciesId(), editPetForm.getBreedId(), editPetForm.getProvince(),
                     editPetForm.getDepartment(), photos, editPetForm.getImagesIdToDelete());
        }
        catch(InvalidImageQuantityException ex) {
            LOGGER.warn(ex.getMessage());
            return editPetForm(editPetForm, id).addObject("imageQuantityError", true);
        }
        if(!opPet.isPresent()){
            LOGGER.warn("Pet could not be updated");
            return new ModelAndView("redirect:/");
        }
        return new ModelAndView("redirect:/pet/" + opPet.get().getId());
    }
}
