package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.LocationService;
import ar.edu.itba.paw.interfaces.SpeciesService;
import ar.edu.itba.paw.models.Department;
import ar.edu.itba.paw.models.Province;
import ar.edu.itba.paw.webapp.dto.DepartmentDto;
import ar.edu.itba.paw.webapp.dto.ProvinceDto;
import ar.edu.itba.paw.webapp.dto.SpeciesDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Component
@Path("/location")
public class LocationController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private LocationService locationService;

    @Context
    private UriInfo uriInfo;

    @GET
    @Path("/provinces")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getProvinces() {
        List<ProvinceDto> provinceList = locationService.provinceList().stream().map(ProvinceDto::fromProvince).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<ProvinceDto>>(provinceList) {}).build();
    }

    @GET
    @Path("/provinces/{provinceId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getProvince(@PathParam("provinceId") long provinceId) {
        Optional<Province> opProvince = locationService.findProvinceById(provinceId);
        if(!opProvince.isPresent()){
            LOGGER.debug("Province {} not found", provinceId);
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        }
        ProvinceDto province = ProvinceDto.fromProvince(opProvince.get());
        return Response.ok(new GenericEntity<ProvinceDto>(province) {}).build();
    }

    @GET
    @Path("/provinces/{provinceId}/departments")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getDepartmentsByProvince(@PathParam("provinceId") long provinceId) {
        Optional<Province> opProvince = locationService.findProvinceById(provinceId);
        if(!opProvince.isPresent()){
            LOGGER.debug("Province {} not found", provinceId);
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        }
        List<DepartmentDto> departmentList = opProvince.get().getDepartmentList().stream()
                .map(d -> DepartmentDto.fromDepartment(d, uriInfo)).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<DepartmentDto>>(departmentList) {}).build();
    }

    @GET
    @Path("/departments")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getDepartments() {
        List<DepartmentDto> departmentList = locationService.departmentList().stream()
                .map(d -> DepartmentDto.fromDepartment(d, uriInfo)).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<DepartmentDto>>(departmentList) {}).build();
    }

    @GET
    @Path("/departments/{departmentId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getDepartment(@PathParam("departmentId") long departmentId) {
        Optional<Department> opDepartment = locationService.findDepartmentById(departmentId);
        if(!opDepartment.isPresent()){
            LOGGER.debug("Department {} not found", departmentId);
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        }
        DepartmentDto department = DepartmentDto.fromDepartment(opDepartment.get(), uriInfo);
        return Response.ok(new GenericEntity<DepartmentDto>(department) {}).build();
    }
}
