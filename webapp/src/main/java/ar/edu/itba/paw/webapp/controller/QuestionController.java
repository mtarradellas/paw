package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.PetService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.models.Answer;
import ar.edu.itba.paw.models.Pet;
import ar.edu.itba.paw.models.Question;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.dto.AnswerDto;
import ar.edu.itba.paw.webapp.dto.QuestionDto;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Path("/questions")
public class QuestionController extends BaseController{

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private static final int QUESTION_PAGE_SIZE = 12;

    @Context
    private UriInfo uriInfo;

    @Autowired
    private PetService petService;

    @Autowired
    private UserService userService;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getQuestions(@QueryParam("petId") long petId, @QueryParam("page") @DefaultValue("1") int page) {
        String locale = getLocale();
        Optional<Pet> opPet = petService.findById(locale, petId);
        if(!opPet.isPresent()) {
            LOGGER.debug("Pet {} not found", petId);
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        }
        List<QuestionDto> questions = petService.listQuestions(petId, page, QUESTION_PAGE_SIZE).stream().map(q->QuestionDto.fromQuestion(q,uriInfo)).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<QuestionDto>>(questions) {}).build();
    }
    @GET
    @Path("/amount")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getQuestionAmount(@QueryParam("petId") long petId) {
        String locale = getLocale();
        Optional<Pet> opPet = petService.findById(locale, petId);
        if(!opPet.isPresent()) {
            LOGGER.debug("Pet {} not found", petId);
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        }
        Integer amount = petService.getListQuestionsAmount(petId);
        Map<String,Integer> response = new HashMap<>();
        response.put("amount", amount);
        return Response.ok(new Gson().toJson(response)).build();
    }

    @POST
    @Consumes(value = { MediaType.APPLICATION_JSON})
    public Response createQuestion(final QuestionDto question) {
        Optional<User> opUser = userService.findById(question.getUserId());
        if (!opUser.isPresent()) {
            LOGGER.warn("User {} not found", question.getUserId());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }
        final Optional<Question> opNewQuestion = petService.createQuestion(question.getContent(), opUser.get(), question.getPetId(), uriInfo.getBaseUri().toString());
        if (!opNewQuestion.isPresent()) {
            LOGGER.warn("Question creation failed");
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }
        final URI questionUri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(opNewQuestion.get().getId())).build();
        return Response.created(questionUri).build();
    }

    @GET
    @Path("/{questionId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getQuestion(@PathParam("questionId") long questionId) {
        Optional<Question> opQuestion = petService.findQuestionById(questionId);
        if(!opQuestion.isPresent()) {
            LOGGER.debug("Question {} not found", questionId);
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        }
        QuestionDto question = QuestionDto.fromQuestion(opQuestion.get(), uriInfo);
        return Response.ok(new GenericEntity<QuestionDto>(question) {}).build();
    }

    @GET
    @Path("/{questionId}/answer")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getAnswer(@PathParam("questionId") long questionId) {
        Optional<Question> opQuestion = petService.findQuestionById(questionId);
        if(!opQuestion.isPresent()) {
            LOGGER.debug("Question {} not found", questionId);
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        }
        if(opQuestion.get().getAnswer() == null) {
            LOGGER.debug("Answer for Question {} not found", questionId);
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        }
        AnswerDto answer = AnswerDto.fromAnswer(opQuestion.get().getAnswer(), uriInfo);
        return Response.ok(new GenericEntity<AnswerDto>(answer) {}).build();
    }

    @POST
    @Path("/{questionId}/answer")
    @Consumes(value = { MediaType.APPLICATION_JSON})
    public Response createAnswer(@PathParam("questionId") long questionId, final AnswerDto answer) {
        Optional<Question> opQuestion = petService.findQuestionById(questionId);
        if(!opQuestion.isPresent()) {
            LOGGER.debug("Question {} not found", questionId);
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        }
        Optional<User> opUser = userService.findById(answer.getUserId());
        if (!opUser.isPresent()) {
            LOGGER.warn("User {} not found", answer.getUserId());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }
        final Optional<Answer> opNewAnswer = petService.createAnswer(questionId,answer.getContent(), opUser.get(), uriInfo.getBaseUri().toString());
        if (!opNewAnswer.isPresent()) {
            LOGGER.warn("Answer creation failed");
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }
        final URI answerUri = uriInfo.getAbsolutePathBuilder().build();
        return Response.created(answerUri).build();
    }
}
