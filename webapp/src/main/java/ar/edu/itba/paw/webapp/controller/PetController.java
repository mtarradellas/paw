package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.ImageService;
import ar.edu.itba.paw.interfaces.PetService;
import ar.edu.itba.paw.models.ImageDTO;
import ar.edu.itba.paw.models.Pet;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.dto.PetDto;
import ar.edu.itba.paw.webapp.dto.QuestionDto;
import ar.edu.itba.paw.webapp.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Path("/pets")
public class PetController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private static final int PET_PAGE_SIZE = 12;
    private static final int QUESTION_PAGE_SIZE = 12;

    @Autowired
    private PetService petService;

    @Autowired
    private ImageService imageService;

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getPets(@QueryParam("ownerId") long ownerId, @QueryParam("species") long species, @QueryParam("breed") long breed,
                            @QueryParam("gender") String gender, @QueryParam("searchCriteria") String searchCriteria,
                            @QueryParam("find") String find, @QueryParam("searchOrder") String searchOrder,
                            @QueryParam("priceRange") int priceRange, @QueryParam("province") long province,
                            @QueryParam("department") long department, @QueryParam("page") @DefaultValue("1") int page) {

        final String locale = getLocale();
        final List<PetDto> petList = petService.filteredList(locale,null, ownerId,null,null,null,
                null,null,null, 0,-1,null,null, 1, PET_PAGE_SIZE)
                .stream().map(p -> PetDto.fromPetForList(p, uriInfo)).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<PetDto>>(petList) {}).build();
    }
//    @POST
//    @Consumes(value = { MediaType.APPLICATION_JSON})
//    public Response create(final PetDto pet) {
//        final String locale = getLocale();
//
//        final Optional<User> opNewUser = userService.create(user.getUsername(), user.getPassword(), user.getMail(), locale, uriInfo.getPath());
//        if (!opNewUser.isPresent()) {
//            LOGGER.warn("User creation failed");
//            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
//        }
//        final URI userUri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(opNewUser.get().getId())).build();
//        return Response.created(userUri).build();
//    }

    @GET
    @Path("/{petId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getPet(@PathParam("petId") long petId) {
        String locale = getLocale();
        Optional<Pet> opPet = petService.findById(locale, petId);
        if(!opPet.isPresent()) {
            LOGGER.debug("Pet {} not found", petId);
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        }
        PetDto pet = PetDto.fromPet(opPet.get(), uriInfo);
        return Response.ok(new GenericEntity<PetDto>(pet) {}).build();
    }

    @GET
    @Path("/{petId}/images")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getImages(@PathParam("petId") long petId) {
        String locale = getLocale();
        Optional<Pet> opPet = petService.findById(locale, petId);
        if(!opPet.isPresent()) {
            LOGGER.debug("Pet {} not found", petId);
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        }
        List<ImageDTO> images = opPet.get().getImages();
        return Response.ok(new GenericEntity<List<ImageDTO>>(images) {}).build();
    }

    @GET
    @Path("/{petId}/images/{imageId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getImage(@PathParam("petId") long petId, @PathParam("imageId") long imageId) {
        String locale = getLocale();
        Optional<Pet> opPet = petService.findById(locale, petId);
        if(!opPet.isPresent()) {
            LOGGER.debug("Pet {} not found", petId);
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        }
        //get image from service... how to deliver it?
        return Response.ok().build();
    }

}
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(PetController.class);
//
//    @Autowired
//    private PetService petService;
//
//    @Autowired
//    private SpeciesService speciesService;
//
//    @Autowired
//    private ImageService imageService;
//
//    @Autowired
//    private LocationService locationService;
//
//    @Autowired
//    private RequestService requestService;
//
//    private static final int PET_PAGE_SIZE = 12;
//    private static final int COMMENTS_PAGE_SIZE = 5;
//
//    @RequestMapping(value = "/", method = { RequestMethod.GET})
//    public ModelAndView getHome(@RequestParam(name = "species", required = false) String species,
//                                @RequestParam(name = "breed", required = false) String breed,
//                                @RequestParam(name = "gender", required = false) String gender,
//                                @RequestParam(name = "searchCriteria", required = false) String searchCriteria,
//                                @RequestParam(name = "searchOrder", required = false) String searchOrder,
//                                @RequestParam(name = "find", required = false) String find,
//                                @RequestParam(name = "page", required = false) String page,
//                                @RequestParam(name = "priceRange", required = false) String priceRange,
//                                @RequestParam(name = "province", required = false) String province,
//                                @RequestParam(name = "department", required = false) String department) {
//
//        final ModelAndView mav = new ModelAndView("index");
//        final String locale = getLocale();
//
//        int pageNum = ParseUtils.parsePage(page);
//        Long speciesId = ParseUtils.parseSpecies(species);
//        Long breedId = ParseUtils.parseSpecies(breed);
//        gender = ParseUtils.parseGender(gender);
//        searchCriteria = ParseUtils.parseCriteria(searchCriteria);
//        searchOrder = ParseUtils.parseOrder(searchOrder);
//        int[] price = ParseUtils.parseRange(priceRange);
//        int minPriceNum = price[0];
//        int maxPriceNum = price[1];
//        Long provinceId = ParseUtils.parseProvince(province);
//        Long departmentId = ParseUtils.parseDepartment(department);
//
//        if (!ParseUtils.isAllowedFind(find)) {
//            mav.addObject("wrongSearch", true);
//            find = null;
//        } else {
//            mav.addObject("wrongSearch", false);
//        }
//        if(find != null && (find.equals("") || find.trim().length() == 0)){
//            find = null;
//        }
//        List<String> findList = ParseUtils.parseFind(find);
//
//        List<Pet> petList = petService.filteredList(locale, findList, null, speciesId, breedId, gender, PetStatus.AVAILABLE,
//                searchCriteria, searchOrder, minPriceNum, maxPriceNum, provinceId, departmentId, pageNum, PET_PAGE_SIZE);
//        int amount = petService.getFilteredListAmount(locale, findList, null, speciesId, breedId, gender, PetStatus.AVAILABLE, minPriceNum,
//                maxPriceNum, provinceId, departmentId);
//
//        List<Breed> breedList = petService.filteredBreedList(locale, findList, null, speciesId, breedId, gender, PetStatus.AVAILABLE,
//                 minPriceNum, maxPriceNum, provinceId, departmentId);
//        Object[] speciesList = breedList.stream().map(Breed::getSpecies).distinct().sorted(Species::compareTo).toArray();
//        List<Department> departmentList = petService.filteredDepartmentList(locale, findList, null, speciesId, breedId, gender, PetStatus.AVAILABLE,
//                minPriceNum, maxPriceNum, provinceId, departmentId);
//        Object[] provinceList = departmentList.stream().map(Department::getProvince).distinct().sorted(Province::compareTo).toArray();
//        Object[] ranges = petService.filteredRangesList(locale, findList, null, speciesId, breedId, gender, PetStatus.AVAILABLE,
//                minPriceNum, maxPriceNum, provinceId, departmentId).toArray();
//        Object[] genders = petService.filteredGenderList(locale, findList, null, speciesId, breedId, gender, PetStatus.AVAILABLE,
//                minPriceNum, maxPriceNum, provinceId, departmentId).toArray();
//
//        mav.addObject("currentPage", pageNum);
//        mav.addObject("maxPage", (int) Math.ceil((double) amount / PET_PAGE_SIZE));
//        mav.addObject("homePetList", petList.toArray());
//        mav.addObject("amount", amount);
//
//        mav.addObject("speciesList", speciesList);
//        mav.addObject("breedList", breedList.toArray());
//        mav.addObject("provinceList", provinceList);
//        mav.addObject("departmentList", departmentList.toArray());
//        mav.addObject("ranges", ranges);
//        mav.addObject("genders", genders);
//
//        mav.addObject("find", find);
//        return mav;
//    }
//
//    @RequestMapping(value = "/search", method = RequestMethod.GET, headers="Accept=*/*")
//    @ResponseBody
//    public void search(HttpServletRequest request, final HttpServletResponse response) throws IOException {
//        List<String> searchValues = petService.autocompleteFind(getLocale(),request.getParameter("term"));
//        response.setContentType("application/json");
//
//        final String param = request.getParameter("term");
//        final List<AutoCompleteData> result = new ArrayList<>();
//        for (final String country : searchValues) {
//            if (country.toLowerCase().contains(param.toLowerCase())) {
//                result.add(new AutoCompleteData(country, country));
//            }
//        }
//        response.getWriter().write(new Gson().toJson(result));
//    }
//
//
//    @RequestMapping(value = "/pet/{id}")
//    public ModelAndView getIdPet(@PathVariable("id") long id) {
//        final ModelAndView mav = new ModelAndView("views/single_pet");
//        User user = loggedUser();
//        String locale = getLocale();
//
//        RequestStatus lastRequest = null;
//        boolean requestExists = false;
//        boolean acquired = false;
//        List<User> availableUsers = null;
//        int availableAmount = 0;
//
//        Pet pet = petService.findById(locale, id).orElseThrow(PetNotFoundException::new);
//
//        if (user != null) {
//            /* Check if user has already requested pet */
//            if (!user.getRequestList().isEmpty()) {
//                Optional<Request> opRequest = user.getRequestList().stream()
//                        .filter(request -> request.getPet().getId() == id)
//                        .max(Comparator.comparing(Request::getCreationDate));
//                if (opRequest.isPresent()) {
//                    LOGGER.debug("User {} last request status for pet {} is {}", user.getId(), id, opRequest.get().getId());
//                    lastRequest = opRequest.get().getStatus();
//                    requestExists = true;
//                } else {
//                    LOGGER.debug("User {} has no request for pet {}", user.getId(), id);
//                }
//            } else {
//                LOGGER.debug("User {} has no request for pet {}", user.getId(), id);
//            }
//
//            if (pet.getUser().getId().equals(user.getId())) {
//                availableUsers = user.getInterestList().stream()
//                        .filter(r -> (r.getStatus() == RequestStatus.ACCEPTED) && r.getPet().getId().equals(pet.getId()))
//                        .map(Request::getUser).collect(Collectors.toList());
//                availableAmount = availableUsers.size();
//            }
//
//            if (user.getNewPets().contains(pet)) acquired = true;
//        }
//
//        mav.addObject("pet", pet);
//        mav.addObject("lastRequest", lastRequest);
//        mav.addObject("requestExists", requestExists);
//        mav.addObject("availableUsers", availableUsers);
//        mav.addObject("availableAmount", availableAmount);
//        mav.addObject("acquired", acquired);
//        return mav;
//    }
//
//    @RequestMapping(value = "/pet/{id}/request", method = {RequestMethod.POST})
//    public ModelAndView requestPet(@PathVariable("id") final long id) {
//        final ModelAndView mav = new ModelAndView("redirect:/pet/" + id);
//        final User user = loggedUser();
//        final String locale = getLocale();
//
//        if (user == null) {
//            LOGGER.warn("User not authenticated, ignoring request");
//            return new ModelAndView("redirect:/403");
//        }
//
//        Optional<Request> opRequest;
//        final String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
//        try {
//             opRequest = requestService.create(locale, user.getId(), id, baseUrl);
//        } catch (DataIntegrityViolationException ex) {
//            LOGGER.warn("{}", ex.getMessage());
//            return mav.addObject("requestError", true);
//        }
//
//        if (!opRequest.isPresent()) {
//            mav.addObject("requestError", true);
//        }
//
//        return new ModelAndView("redirect:/pet/" + id );
//    }
//
//    @RequestMapping(value = "/pet/{id}/sell-adopt", method = {RequestMethod.POST})
//    public ModelAndView petUpdateSold(@PathVariable("id") long id,
//                                      @RequestParam(name = "newowner", required = false) String newOwner) {
//        User user = loggedUser();
//        Long newOwnerId = ParseUtils.parseUser(newOwner);
//        final String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
//
//        if (user != null && newOwner != null && petService.sellPet(id, user, newOwnerId, baseUrl)) {
//            LOGGER.debug("Pet {} updated as sold", id);
//            return new ModelAndView("redirect:/interests");
//        }
//        LOGGER.warn("User is not pet owner, pet status not updated");
//        return new ModelAndView("redirect:/403");
//    }
//
//    @RequestMapping(value = "/pet/{id}/remove", method = {RequestMethod.POST})
//    public ModelAndView petUpdateRemoved(@PathVariable("id") long id) {
//        User user = loggedUser();
//        if (user != null && petService.removePet(id, user)) {
//            LOGGER.debug("Pet {} updated as removed", id);
//            return new ModelAndView("redirect:/user/" + user.getId());
//        }
//        LOGGER.warn("User is not pet owner, pet status not updated");
//        return new ModelAndView("redirect:/403");
//    }
//
//    @RequestMapping(value = "/pet/{id}/recover", method = {RequestMethod.POST})
//    public ModelAndView petUpdateRecover(@PathVariable("id") long id) {
//        User user = loggedUser();
//        Optional<Pet> pet = petService.findById(id);
//        if(!pet.isPresent()){
//            return new ModelAndView("redirect:/403");
//        }
//
//        if (user != null && pet.get().getNewOwner() == null  && petService.recoverPet(id, user)) {
//            LOGGER.debug("Pet {} updated as recovered", id);
//            return new ModelAndView("redirect:/pet/{id}");
//        }
//        LOGGER.warn("User is not pet owner, pet status not updated");
//        return new ModelAndView("redirect:/403");
//    }
//
//    @RequestMapping(value = "/img/{id}", produces = MediaType.IMAGE_PNG_VALUE)
//    public @ResponseBody
//    byte[] getImageWithMediaType(@PathVariable("id") long id) throws IOException {
//
//        byte[] byteImage = imageService.getDataById(id).orElse(null);
//        if(byteImage == null){
//            return null;
//        }
//
//        ByteArrayInputStream bis = new ByteArrayInputStream(byteImage);
//        BufferedImage bufferedImage = ImageIO.read(bis);
//        int height = bufferedImage.getHeight(), width = bufferedImage.getWidth();
//
//        BufferedImage cropped = bufferedImage;
//        int diff = Math.abs(height-width);
//        if(width>height){
//            cropped = bufferedImage.getSubimage(diff/2, 0, width-diff, height);
//        }else{ if(width<height)
//            cropped = bufferedImage.getSubimage(0, diff/2, width, height-diff);
//        }
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        ImageIO.write(cropped, "jpg", baos );
//        baos.flush();
//        byte[] imageInByte = baos.toByteArray();
//        baos.close();
//        return imageInByte;
//    }
//
//    @RequestMapping(value ="/upload-pet", method = { RequestMethod.GET })
//    public ModelAndView uploadPetForm(@ModelAttribute ("uploadPetForm") final UploadPetForm petForm) {
//        ModelAndView mav = new ModelAndView("views/upload_pet");
//        String locale = getLocale();
//
//        List<Species> speciesList = speciesService.speciesList(locale);
//        List<Breed> breedList = speciesService.breedList(locale);
//        List<Province> provinceList = locationService.provinceList();
//        List<Department> departmentList = locationService.departmentList();
//
//        mav.addObject("provinceList", provinceList);
//        mav.addObject("departmentList", departmentList);
//        mav.addObject("speciesList", speciesList);
//        mav.addObject("breedList", breedList);
//        return mav;
//    }
//
//    @RequestMapping(value = "/upload-pet", method = { RequestMethod.POST })
//    public ModelAndView uploadPet(@Valid @ModelAttribute("uploadPetForm") final UploadPetForm petForm,
//                                  final BindingResult errors, HttpServletRequest request) {
//        if (errors.hasErrors()) {
//            return uploadPetForm(petForm);
//        }
//        User user = loggedUser();
//        if (user == null) throw new UserNotFoundException();
//
//        List<byte[]> photos = new ArrayList<>();
//        try {
//            for (MultipartFile photo : petForm.getPhotos()) {
//                try {
//                    photos.add(photo.getBytes());
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                    throw new ImageLoadException(ex);
//                }
//            }
//        } catch (ImageLoadException ex) {
//            LOGGER.warn("Image bytes load from pet form failed");
//            return uploadPetForm(petForm).addObject("imageError", true);
//        }
//
//        Optional<Pet> opPet;
//        try{
//            LocalDateTime birthDate = petForm.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
//            opPet = petService.create(getLocale(), petForm.getPetName(), birthDate, petForm.getGender(),
//                    petForm.getVaccinated(), petForm.getPrice(), petForm.getDescription(), PetStatus.AVAILABLE, user.getId(),
//                    petForm.getSpeciesId(), petForm.getBreedId(), petForm.getProvince(), petForm.getDepartment(), photos);
//        } catch (DataIntegrityViolationException ex) {
//            LOGGER.warn("{}", ex.getMessage());
//            return uploadPetForm(petForm).addObject("petError", true);
//        }
//
//        if (!opPet.isPresent()) {
//            LOGGER.warn("Pet could not be created");
//            return uploadPetForm(petForm).addObject("petError", true);
//        }
//
//        return new ModelAndView("redirect:/pet/" + opPet.get().getId());
//    }
//
//
//    @RequestMapping(value = "/edit-pet/{id}", method = { RequestMethod.GET })
//    public ModelAndView editPetGet(@ModelAttribute("editPetForm") final EditPetForm petForm, @PathVariable("id") long id) {
//
//        Pet pet = petService.findById(getLocale(), id).orElseThrow(PetNotFoundException::new);
//
//        if(pet.getUser().getId().equals(loggedUser().getId())) {
//
//            List<Department> departmentList = locationService.departmentList();
//            List<Province> provinceList = locationService.provinceList();
//
//            petForm.setBirthDate(java.util.Date.from(pet.getBirthDate().atZone(ZoneId.systemDefault()).toInstant()));
//            petForm.setBreedId(pet.getBreed().getId());
//            petForm.setDescription(pet.getDescription());
//            petForm.setGender(pet.getGender());
//            petForm.setProvince(pet.getProvince().getId());
//            petForm.setDepartment(pet.getDepartment().getId());
//            petForm.setPrice(pet.getPrice());
//            petForm.setPetName(pet.getPetName());
//            petForm.setSpeciesId(pet.getSpecies().getId());
//            petForm.setVaccinated(pet.isVaccinated());
//
//            return editPetForm(petForm, id)
//                    .addObject("provinceList", provinceList)
//                    .addObject("departmentList", departmentList);
//        }
//        return new ModelAndView("redirect:/403" );
//
//    }
//
//    private ModelAndView editPetForm(@ModelAttribute("editPetForm") final EditPetForm editPetForm, long id) {
//        String locale = getLocale();
//
//        Pet pet = petService.findById(getLocale(), id).orElseThrow(PetNotFoundException::new);
//        List<Species> speciesList = speciesService.speciesList(locale);
//        List<Breed> breedList = speciesService.breedList(locale);
//        List<Department> departmentList = locationService.departmentList();
//        List<Province> provinceList = locationService.provinceList();
//
//        return new ModelAndView("views/pet_edit")
//                .addObject("speciesList", speciesList)
//                .addObject("breedList", breedList)
//                .addObject("provinceList", provinceList)
//                .addObject("departmentList", departmentList)
//                .addObject("pet", pet)
//                .addObject("id", id);
//    }
//
//    @RequestMapping(value = "/edit-pet/{id}", method = { RequestMethod.POST })
//    public ModelAndView editPet(@Valid @ModelAttribute("editPetForm") final EditPetForm editPetForm,
//                                  final BindingResult errors, HttpServletRequest request,
//                                @PathVariable("id") long id) {
//        User user = loggedUser();
//        if (user == null) throw new UserNotFoundException();
//        String locale = getLocale();
//
//        if (errors.hasErrors()) {
//            return editPetForm(editPetForm, id);
//        }
//        List<byte[]> photos = new ArrayList<>();
//        try {
//            for (MultipartFile photo : editPetForm.getPhotos()) {
//                if(!photo.isEmpty()) {
//                    try {
//                        photos.add(photo.getBytes());
//                    } catch (IOException ex) {
//                        ex.printStackTrace();
//                        throw new ImageLoadException(ex);
//                    }
//                }
//            }
//        } catch (ImageLoadException ex) {
//            LOGGER.warn("Image bytes load from pet form failed");
//            return editPetForm(editPetForm, id).addObject("imageError", true);
//        }
//
//        Optional<Pet> opPet;
//        try {
//            LocalDateTime birthDate = editPetForm.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
//             opPet = petService.update(locale, id, user.getId(), editPetForm.getPetName(), birthDate,
//                     editPetForm.getGender(), editPetForm.getVaccinated(), editPetForm.getPrice(), editPetForm.getDescription(),
//                     null, editPetForm.getSpeciesId(), editPetForm.getBreedId(), editPetForm.getProvince(),
//                     editPetForm.getDepartment(), photos, editPetForm.getImagesIdToDelete());
//
//        } catch (InvalidImageQuantityException ex) {
//            LOGGER.warn("{}", ex.getMessage());
//            return editPetForm(editPetForm, id).addObject("imageQuantityError", true);
//
//        } catch (DataIntegrityViolationException ex) {
//            LOGGER.warn("{}", ex.getMessage());
//            return editPetForm(editPetForm, id).addObject("petError", true);
//        }
//
//        if(!opPet.isPresent()){
//            LOGGER.warn("Pet could not be updated");
//            return editPetForm(editPetForm, id).addObject("petError", true);
//        }
//        return new ModelAndView("redirect:/pet/" + opPet.get().getId());
//    }
//
//    @RequestMapping(value = "/pet/{id}/comments")
//    public @ResponseBody
//    Map<String, Object> petComments(@PathVariable("id") long id,
//                                    @RequestParam(name = "page", required = false) String page) {
//
//        int pageNum = ParseUtils.parsePage(page);
//
//        List<Question> questionList = petService.listQuestions(id, pageNum, COMMENTS_PAGE_SIZE);
//        int amount = petService.getListQuestionsAmount(id);
//
//        Map<String, Object> response = new HashMap<>();
//        List<Map<String, Object>> comments = questionList.stream().map(question -> {
//            Map<String, Object> comm = new HashMap<>();
//            comm.put("question", question.toCommentJson());
//            if (question.getAnswer() != null) comm.put("answer", question.getAnswer().toCommentJson());
//            return comm;
//        }).collect(Collectors.toList());
//
//        response.put("currentPage", pageNum);
//        response.put("maxPage", (int) Math.ceil((double) amount / COMMENTS_PAGE_SIZE));
//        response.put("commentList", comments);
//        response.put("amount", amount);
//
//        return response;
//    }
//
//    @RequestMapping(value = "/pet/{id}/question", method = RequestMethod.POST)
//    public ModelAndView petQuestion(@PathVariable("id") long id,
//                                    @Valid QuestionAnswerForm questionAnswerForm,
//                                    final BindingResult errors) {
//        if (errors.hasErrors()) {
//            return getIdPet(id);
//        }
//
//        User user = loggedUser();
//        if (user == null) {
//            LOGGER.warn("User not logged int");
//            return new ModelAndView("redirect:/403");
//        }
//        final String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
//        boolean success = petService.createQuestion(questionAnswerForm.getContent(), user, id, baseUrl).isPresent();
//        return new ModelAndView("redirect:/pet/" + id).addObject("error", !success);
//    }
//
//    @RequestMapping(value = "/pet/{id}/answer", method = RequestMethod.POST)
//    public ModelAndView petAnswer(@PathVariable("id") long id,
//                                  @Valid final QuestionAnswerForm questionAnswerForm,
//                                  final BindingResult errors) {
//        if (errors.hasErrors()) {
//            return getIdPet(id);
//        }
//
//        User user = loggedUser();
//        if (user == null) {
//            LOGGER.warn("User not logged int");
//            return new ModelAndView("redirect:/403");
//        }
//
//        boolean success = false;
//        if (questionAnswerForm.getAnswerId() > 0) {
//            final String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
//            success = petService.createAnswer(questionAnswerForm.getAnswerId(), questionAnswerForm.getContent(), user, baseUrl).isPresent();
//        }
//        return new ModelAndView("redirect:/pet/" + id).addObject("error", !success);
//    }
//
//}
