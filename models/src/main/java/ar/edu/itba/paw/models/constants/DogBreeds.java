package ar.edu.itba.paw.models.constants;

public enum DogBreeds {
    /**
     * All breeds currently supported
     * Note: if you add a new breed please add an entry on the i18n files
     */
    LABRADOR(0, "labrador"), GOLDEN(1, "golden"), SHEPHERD(2, "shepherd"),
    COLLIE(3, "collie"), FRENCH_BULLDOG(4, "frenchBulldog"), BULLDOG(5, "bulldog"),
    BEAGLE(4, "beagle"), POODLE(5, "poodle"), ROTTWEILER(6, "rottweiler"),
    POINTER(7, "pointer"), GERMAN_POINTER(8, "germanPointer"), YORK_TERRIER(9, "yorkTerrier"),
    TERRIER(10, "terrier"), BOXER(11, "boxer"), DACHSHUND(12, "dachshund"),
    CORGI(13, "corgi"), HUSKY(14, "husky"), AUSTRALIAN_SHEPHERD(15, "australianShepherd"),
    GREAT_DANES(16, "greatDanes"), DOBERMANN(17, "dobermann"), CAVALIER(18, "cavalier"),
    MINI_SCHNAUZERS(19, "miniSchnauzers"), BERNESE_MOUNTAIN(20, "berneseMountain"),
    POMERANIAN(21, "pomeranian"), HAVANESE(22, "havanese"), SHETLAND(23, "shetland"),
    BRITTANY(24, "brittany"), COCKER(25, "cocker"), SPRINGER(26, "springer"),
    MASTIFF(27, "mastiff"), CANE_CORSO(28, "caneCorso"), MINI_SHEPHERD(29, "miniShepherd"),
    WEIMARANERS(30, "weimaraners"), MALTESE(31, "maltese"), NEWFOUNDLAND(32, "newfoundland"),
    RHODESIAN(33, "rhodesian"), BELGIAN_MALINOIS(34, "belgianMalinois"), BICHONS(35, "bichons"),
    ST_BERNARD(36, "stBernard"), BLOODHOUND(37, "bloodhound"), WATER_DOG(38, "waterDog"),
    PAPILLONS(39, "papillons"), AUSTRALIAN_CATTLE(40, "australianCattle"), dalmatian(41, "dalmatian"),
    SCOTTISH_TERRIER(42, "scottishTerrier"), MALAMUTE(43, "malamute"), SAMOYED(44, "samoyed"),
    WIREHAIRED_GRIFFON(45, "wirehairedGriffon"), PYRENEES(46, "pyrenees"), BORDEAUX(47, "bordeaux"),
    CARDIGAN_CORGI(48, "cardiganCorgi"), MINI_PIN(49, "miniPin");

    private int id;
    private String name;

    DogBreeds(int id, String name){
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
