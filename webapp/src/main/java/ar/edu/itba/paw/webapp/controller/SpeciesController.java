package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.SpeciesService;
import ar.edu.itba.paw.models.Breed;
import ar.edu.itba.paw.models.Species;
import ar.edu.itba.paw.webapp.dto.BreedDto;
import ar.edu.itba.paw.webapp.dto.SpeciesDto;
import ar.edu.itba.paw.webapp.util.ApiUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Path("/species")
public class SpeciesController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private SpeciesService speciesService;

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getSpecies() {
        String locale = ApiUtils.getLocale();
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
    public Response getBreeds() {
        String locale = ApiUtils.getLocale();
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
