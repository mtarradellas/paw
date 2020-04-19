package ar.edu.itba.paw.models;

public class Species {

    private long id;
    private String es_AR;
    private String en_US;

    public Species(long id, String es_AR, String en_US) {
        this.id = id;
        this.es_AR = es_AR;
        this.en_US = en_US;
    }

    public Species() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
