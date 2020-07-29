package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Department;

public class DepartmentDto {

    private Long id;
    private String name;
    private ProvinceDto province;

    public static DepartmentDto fromDepartment(Department department) {
        final DepartmentDto dto = new DepartmentDto();

        dto.id = department.getId();
        dto.name = department.getName();
        dto.province = ProvinceDto.fromProvince(department.getProvince());

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

    public ProvinceDto getProvince() {
        return province;
    }

    public void setProvince(ProvinceDto province) {
        this.province = province;
    }
}
