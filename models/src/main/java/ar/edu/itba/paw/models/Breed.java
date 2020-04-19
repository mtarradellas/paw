package ar.edu.itba.paw.models;

public class Breed {
    private long id;
    private long speciesId;
    private String es_AR;
    private String en_US;

    public Breed(long id, long speciesId, String es_AR, String en_US) {
        this.id = id;
        this.speciesId = speciesId;
        this.es_AR = es_AR;
        this.en_US = en_US;
    }

    public Breed() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSpeciesId() {
        return speciesId;
    }

    public void setSpeciesId(long speciesId) {
        this.speciesId = speciesId;
    }

    public String getEs_AR() {
        return es_AR;
    }

    public void setEs_AR(String es_AR) {
        this.es_AR = es_AR;
    }

    public String getEn_US() {
        return en_US;
    }

    public void setEn_US(String en_US) {
        this.en_US = en_US;
    }
}
