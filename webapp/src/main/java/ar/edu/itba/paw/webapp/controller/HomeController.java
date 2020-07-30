package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.models.Breed;
import ar.edu.itba.paw.models.Species;
import ar.edu.itba.paw.webapp.dto.PetDto;
import ar.edu.itba.paw.webapp.dto.SpeciesDto;
import ar.edu.itba.paw.webapp.util.ApiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Path("/")
public class HomeController {

    @Autowired
    private SpeciesService speciesService;

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getAvailable() {

        final String locale = ApiUtils.getLocale();
        final List<SpeciesDto> speciesList = speciesService.speciesList(locale).stream().map(s -> SpeciesDto.fromSpecies(s, uriInfo)).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<SpeciesDto>>(speciesList) {}).build();
    }

//    public ModelAndView getAdminHome() {
//        return new ModelAndView("admin/admin");
//    }

}
