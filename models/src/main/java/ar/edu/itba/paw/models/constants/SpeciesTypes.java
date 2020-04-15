package ar.edu.itba.paw.models.constants;

public enum SpeciesTypes {
    /**
     * All species currently supported
     * Note: if you add a new species please add an entry on the i18n files
     */
    DOG(0, "dog"), CAT(1, "cat");

    private int id;
    private String name;

    SpeciesTypes(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName(){
        return this.name;
    }
}