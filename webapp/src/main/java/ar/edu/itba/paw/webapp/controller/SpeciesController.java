package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.interfaces.SpeciesService;
import ar.edu.itba.paw.models.Species;
import ar.edu.itba.paw.webapp.dto.BreedDto;
import ar.edu.itba.paw.webapp.dto.PetDto;
import ar.edu.itba.paw.webapp.dto.SpeciesDto;
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
public class SpeciesController extends BaseController{

        private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

        @Autowired
        private SpeciesService speciesService;

        @Context
        private UriInfo uriInfo;

        @GET
        @Produces(value = {MediaType.APPLICATION_JSON})
        public Response getSpecies() {
            String locale = getLocale();
            List<SpeciesDto> speciesList = speciesService.speciesList(locale).stream().map(s -> SpeciesDto.fromSpecies(s, uriInfo)).collect(Collectors.toList());
            return Response.ok(new GenericEntity<List<SpeciesDto>>(speciesList) {}).build();
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
}
