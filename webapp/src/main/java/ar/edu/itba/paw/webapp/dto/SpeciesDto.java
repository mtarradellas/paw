package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Species;

import java.util.List;
import java.util.stream.Collectors;

public class SpeciesDto {

    private Long id;
    private String en_us;
    private String es_ar;
    private String name;
    private List<BreedDto> breedList;

    public static SpeciesDto fromSpecies(Species species) {
        final SpeciesDto dto = new SpeciesDto();

        dto.id = species.getId();
        dto.en_us = species.getEn_us();
        dto.es_ar = species.getEs_ar();
        dto.name = species.getName();
        dto.breedList = species.getBreedList().stream().map(BreedDto::fromBreed).collect(Collectors.toList());

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

    public List<BreedDto> getBreedList() {
        return breedList;
    }

    public void setBreedList(List<BreedDto> breedList) {
        this.breedList = breedList;
    }
}
