package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Breed;

public class BreedDto {

    private Long id;
    private String en_us;
    private String es_ar;
    private String name;

    public static BreedDto fromBreed(Breed breed) {
        final BreedDto dto = new BreedDto();

        dto.id = breed.getId();
        dto.en_us = breed.getEn_us();
        dto.es_ar = breed.getEs_ar();
        dto.name = breed.getName();

        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEn_us() {
        return en_us;
    }

    public void setEn_us(String en_us) {
        this.en_us = en_us;
    }

    public String getEs_ar() {
        return es_ar;
    }

    public void setEs_ar(String es_ar) {
        this.es_ar = es_ar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
