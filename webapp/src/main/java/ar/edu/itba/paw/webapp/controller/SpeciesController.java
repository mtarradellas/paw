package ar.edu.itba.paw.webapp.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.edu.itba.paw.interfaces.SpeciesService;
import ar.edu.itba.paw.models.Breed;
import ar.edu.itba.paw.models.Species;
import ar.edu.itba.paw.webapp.dto.BreedDto;
import ar.edu.itba.paw.webapp.dto.SpeciesDto;
import ar.edu.itba.paw.webapp.util.ApiUtils;

@Component
@Path("/species")
public class SpeciesController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpeciesController.class);

    @Autowired
    private SpeciesService speciesService;

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getSpecies(@Context HttpServletRequest request) {
        String locale = ApiUtils.getLocale(request);
        List<SpeciesDto> speciesList = speciesService.speciesList(locale).stream().map(s -> SpeciesDto.fromSpecies(s, uriInfo)).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<SpeciesDto>>(speciesList) {}).build();
    }

    @GET
    @Path("/{speciesId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getSpeciesById(@PathParam("speciesId") long speciesId) {
        Optional<Species> opSpecies = speciesService.findSpeciesById(speciesId);
        if(!opSpecies.isPresent()) {
            LOGGER.debug("Species {} not found", speciesId);
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        }
        SpeciesDto species = SpeciesDto.fromSpecies(opSpecies.get(), uriInfo);
        return Response.ok(new GenericEntity<SpeciesDto>(species) {}).build();
    }

    @GET
    @Path("/{speciesId}/breeds")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getBreedsBySpecies(@PathParam("speciesId") long speciesId) {
        Optional<Species> opSpecies = speciesService.findSpeciesById(speciesId);
        if(!opSpecies.isPresent()) {
            LOGGER.debug("Species {} not found", speciesId);
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        }
        List<BreedDto> breedList = opSpecies.get().getBreedList().stream().map(BreedDto::fromBreed).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<BreedDto>>(breedList) {}).build();
    }

    @GET
    @Path("/breeds")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getBreeds(@Context HttpServletRequest request) {
        String locale = ApiUtils.getLocale(request);
        List<BreedDto> breedList = speciesService.breedList(locale).stream().map(BreedDto::fromBreed).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<BreedDto>>(breedList) {}).build();
    }

    @GET
    @Path("/breeds/{breedId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getBreedById(@PathParam("breedId") long breedId) {
        Optional<Breed> opBreed = speciesService.findBreedById(breedId);
        if(!opBreed.isPresent()) {
            LOGGER.debug("Breed {} not found", breedId);
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        }
        BreedDto breed = BreedDto.fromBreed(opBreed.get());
        return Response.ok(new GenericEntity<BreedDto>(breed) {}).build();
    }

}
