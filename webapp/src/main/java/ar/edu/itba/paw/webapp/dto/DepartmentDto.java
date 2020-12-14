package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Department;

import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class DepartmentDto {

    private Long id;
    private String name;
    private URI province;

    public static DepartmentDto fromDepartment(Department department, UriInfo uriInfo) {
        final DepartmentDto dto = new DepartmentDto();

        dto.id = department.getId();
        dto.name = department.getName();
        dto.province = uriInfo.getAbsolutePathBuilder().path("location").path("provinces").path(String.valueOf(department.getProvince().getId())).build();

        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public URI getProvince() {
        return province;
    }

    public void setProvince(URI province) {
        this.province = province;
    }
}
