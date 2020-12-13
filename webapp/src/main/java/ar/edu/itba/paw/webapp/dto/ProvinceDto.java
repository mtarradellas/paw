package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Province;

public class ProvinceDto {

    private Long id;
    private String name;

    public static ProvinceDto fromProvince(Province province) {
        final ProvinceDto dto = new ProvinceDto();

        dto.id = province.getId();
        dto.name = province.getName();

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
}
