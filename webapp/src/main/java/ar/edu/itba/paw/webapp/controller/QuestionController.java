package ar.edu.itba.paw.webapp.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import ar.edu.itba.paw.interfaces.PetService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.interfaces.exceptions.NotFoundException;
import ar.edu.itba.paw.interfaces.exceptions.QuestionException;
import ar.edu.itba.paw.models.Answer;
import ar.edu.itba.paw.models.Question;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.dto.AnswerDto;
import ar.edu.itba.paw.webapp.dto.QuestionDto;
import ar.edu.itba.paw.webapp.exception.BadRequestException;
import ar.edu.itba.paw.webapp.util.ApiUtils;
import ar.edu.itba.paw.webapp.util.ParseUtils;

@Component
@Path("/questions")
public class QuestionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuestionController.class);

    private static final int QUESTION_PAGE_SIZE = 12;

    @Context
    private UriInfo uriInfo;

    @Autowired
    private PetService petService;

    @Autowired
    private UserService userService;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getQuestions(@QueryParam("petId") @DefaultValue("0") Long petId, @QueryParam("page") @DefaultValue("1") int page) {
        try {
            petId = ParseUtils.parsePetId(petId);
            ParseUtils.parsePage(page);
        } catch (BadRequestException ex) {
            LOGGER.warn(ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }
        if(petId == null) {
            LOGGER.warn("Invalid parameter: petId");
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }
        List<QuestionDto> questions;
        int amount;
        try {
            questions = petService.listQuestions(petId, page, QUESTION_PAGE_SIZE)
                    .stream().map(q -> QuestionDto.fromQuestion(q, uriInfo)).collect(Collectors.toList());
            amount = petService.getListQuestionsAmount(petId);
        } catch (NotFoundException ex) {
            LOGGER.warn(ex.getMessage());
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        }

        return ApiUtils.paginatedListResponse(amount, QUESTION_PAGE_SIZE, page, uriInfo, questions, null);
    }

    @POST
    @Consumes(value = { MediaType.APPLICATION_JSON})
    public Response createQuestion(@Context HttpServletRequest httpRequest, final QuestionDto question) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User loggedUser = ApiUtils.loggedUser(httpRequest, userService, auth);
        Optional<Question> opNewQuestion;
        if (question == null || question.getPetId() == null) return Response.status(Status.BAD_REQUEST.getStatusCode()).build();
        try {
            question.setContent(ParseUtils.parseQuestion(question.getContent()));
        } catch (BadRequestException ex) {
            LOGGER.warn(ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }
        try {
            opNewQuestion = petService.createQuestion(question.getContent(), loggedUser.getId(), question.getPetId(), uriInfo.getBaseUri().toString());
        } catch(NotFoundException ex) {
            LOGGER.warn(ex.getMessage());
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        } catch(QuestionException ex) {
            LOGGER.warn(ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }
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
    public Response createAnswer(@Context HttpServletRequest httpRequest, @PathParam("questionId") Long questionId, final AnswerDto answer) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User loggedUser = ApiUtils.loggedUser(httpRequest, userService, auth);
        try {
            questionId = ParseUtils.parseQuestionId(questionId);
            answer.setContent(ParseUtils.parseQuestion(answer.getContent()));
        } catch (BadRequestException ex) {
            LOGGER.warn(ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }
        if(questionId == null) {
            LOGGER.warn("Invalid parameter: questionId {}", questionId);
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }
        Optional<Answer> opNewAnswer;
        try {
            opNewAnswer = petService.createAnswer(questionId, answer.getContent(), loggedUser.getId(), uriInfo.getBaseUri().toString());
        } catch(NotFoundException ex) {
            LOGGER.warn(ex.getMessage());
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        } catch(QuestionException ex) {
            LOGGER.warn(ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }
        if (!opNewAnswer.isPresent()) {
            LOGGER.warn("Answer creation failed");
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }
        final URI answerUri = uriInfo.getAbsolutePathBuilder().build();
        return Response.created(answerUri).build();
    }
}
