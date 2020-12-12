package ar.edu.itba.paw.webapp.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.edu.itba.paw.interfaces.SpeciesService;
import ar.edu.itba.paw.webapp.dto.SpeciesDto;
import ar.edu.itba.paw.webapp.util.ApiUtils;

@Component
@Path("/")
public class HomeController {

    @Autowired
    private SpeciesService speciesService;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getAvailable() {
        final String locale = ApiUtils.getLocale();
        final List<SpeciesDto> speciesList = speciesService.speciesList(locale).stream().map(SpeciesDto::fromSpecies).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<SpeciesDto>>(speciesList) {}).build();
    }

//    public ModelAndView getAdminHome() {
//        return new ModelAndView("admin/admin");
//    }

}
