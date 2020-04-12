package ar.edu.itba.paw.models.constants;

public enum CatBreeds {
    /**
     * All breeds currently supported
     * Note: if you add a new breed please add an entry on the i18n files
     */
    SIAMESE(0, "siamese"), ABYSSINIAN(1, "abyssinian"), SPHYNX(2, "sphynx"),
    EXOTIC(3, "exotic"), BENGAL(4, "bengal"), AMERICAN(5, "american"),
    BIRMAN(6, "birman"), SCOTTISH_FOLD(7, "scottishFold"), BURMESE(8, "burmese"),
    RUSSIAN_BLUE(9, "russianBlue"), NORWEGIAN(10, "norwegian"), PERSIAN(11, "persian"),
    SIBERIAN(12, "siberian"), HIMALAYAN(13, "himalayan"), ORIENTAL(14, "oriental"),
    TOKINESE(15, "tokinese"), TURKISH_ANGORA(16, "turkishAngora"), BOBTAIL(17, "bobtail"),
    JAP_BOBTAIL(18, "japBobtail"), MAU(19, "mau"), BALINESE(20, "balinese"),
    CURL(21, "curl"), TURKISH_VAN(22, "turkishVan"), SOMALI(23, "somali"),
    WIREHAIR(24, "wirehair"), AEGEAN(25, "aegean");

    private int id;
    private String name;

    CatBreeds(int id, String name){
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
