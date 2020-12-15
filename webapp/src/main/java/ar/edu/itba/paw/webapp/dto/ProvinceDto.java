package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Province;

import java.util.ArrayList;
import java.util.List;

public class ProvinceDto {

    private Long id;
    private String name;
    private List<Long> departmentIds;

    public static ProvinceDto fromProvince(Province province) {
        final ProvinceDto dto = new ProvinceDto();
        dto.departmentIds = new ArrayList<>();

        dto.id = province.getId();
        dto.name = province.getName();
        province.getDepartmentList().forEach(d -> dto.departmentIds.add(d.getId()));

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

    public List<Long> getDepartmentIds() {
        return departmentIds;
    }

    public void setDepartmentIds(List<Long> departmentIds) {
        this.departmentIds = departmentIds;
    }
}
