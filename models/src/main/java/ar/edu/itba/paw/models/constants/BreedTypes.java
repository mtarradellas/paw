package ar.edu.itba.paw.models.constants;

public enum BreedTypes {
    LABRADOR(0, "labrador", SpeciesTypes.DOG), GOLDEN(1, "golden", SpeciesTypes.DOG), SHEPHERD(2, "shepherd", SpeciesTypes.DOG),
    COLLIE(3, "collie", SpeciesTypes.DOG), FRENCH_BULLDOG(4, "frenchBulldog", SpeciesTypes.DOG), BULLDOG(5, "bulldog", SpeciesTypes.DOG),
    BEAGLE(4, "beagle", SpeciesTypes.DOG), POODLE(5, "poodle", SpeciesTypes.DOG), ROTTWEILER(6, "rottweiler", SpeciesTypes.DOG),
    POINTER(7, "pointer", SpeciesTypes.DOG), GERMAN_POINTER(8, "germanPointer", SpeciesTypes.DOG), YORK_TERRIER(9, "yorkTerrier", SpeciesTypes.DOG),
    TERRIER(10, "terrier", SpeciesTypes.DOG), BOXER(11, "boxer", SpeciesTypes.DOG), DACHSHUND(12, "dachshund", SpeciesTypes.DOG),
    CORGI(13, "corgi", SpeciesTypes.DOG), HUSKY(14, "husky", SpeciesTypes.DOG), AUSTRALIAN_SHEPHERD(15, "australianShepherd", SpeciesTypes.DOG),
    GREAT_DANES(16, "greatDanes", SpeciesTypes.DOG), DOBERMANN(17, "dobermann", SpeciesTypes.DOG), CAVALIER(18, "cavalier", SpeciesTypes.DOG),
    MINI_SCHNAUZERS(19, "miniSchnauzers", SpeciesTypes.DOG), BERNESE_MOUNTAIN(20, "berneseMountain", SpeciesTypes.DOG),
    POMERANIAN(21, "pomeranian", SpeciesTypes.DOG), HAVANESE(22, "havanese", SpeciesTypes.DOG), SHETLAND(23, "shetland", SpeciesTypes.DOG),
    BRITTANY(24, "brittany", SpeciesTypes.DOG), COCKER(25, "cocker", SpeciesTypes.DOG), SPRINGER(26, "springer", SpeciesTypes.DOG),
    MASTIFF(27, "mastiff", SpeciesTypes.DOG), CANE_CORSO(28, "caneCorso", SpeciesTypes.DOG), MINI_SHEPHERD(29, "miniShepherd", SpeciesTypes.DOG),
    WEIMARANERS(30, "weimaraners", SpeciesTypes.DOG), MALTESE(31, "maltese", SpeciesTypes.DOG), NEWFOUNDLAND(32, "newfoundland", SpeciesTypes.DOG),
    RHODESIAN(33, "rhodesian", SpeciesTypes.DOG), BELGIAN_MALINOIS(34, "belgianMalinois", SpeciesTypes.DOG), BICHONS(35, "bichons", SpeciesTypes.DOG),
    ST_BERNARD(36, "stBernard", SpeciesTypes.DOG), BLOODHOUND(37, "bloodhound", SpeciesTypes.DOG), WATER_DOG(38, "waterDog", SpeciesTypes.DOG),
    PAPILLONS(39, "papillons", SpeciesTypes.DOG), AUSTRALIAN_CATTLE(40, "australianCattle", SpeciesTypes.DOG), dalmatian(41, "dalmatian", SpeciesTypes.DOG),
    SCOTTISH_TERRIER(42, "scottishTerrier", SpeciesTypes.DOG), MALAMUTE(43, "malamute", SpeciesTypes.DOG), SAMOYED(44, "samoyed", SpeciesTypes.DOG),
    WIREHAIRED_GRIFFON(45, "wirehairedGriffon", SpeciesTypes.DOG), PYRENEES(46, "pyrenees", SpeciesTypes.DOG), BORDEAUX(47, "bordeaux", SpeciesTypes.DOG),
    CARDIGAN_CORGI(48, "cardiganCorgi", SpeciesTypes.DOG), MINI_PIN(49, "miniPin", SpeciesTypes.DOG),

    SIAMESE(100, "siamese", SpeciesTypes.CAT), ABYSSINIAN(101, "abyssinian", SpeciesTypes.CAT), SPHYNX(102, "sphynx", SpeciesTypes.CAT),
    EXOTIC(103, "exotic", SpeciesTypes.CAT), BENGAL(104, "bengal", SpeciesTypes.CAT), AMERICAN(105, "american", SpeciesTypes.CAT),
    BIRMAN(106, "birman", SpeciesTypes.CAT), SCOTTISH_FOLD(107, "scottishFold", SpeciesTypes.CAT), BURMESE(108, "burmese", SpeciesTypes.CAT),
    RUSSIAN_BLUE(109, "russianBlue", SpeciesTypes.CAT), NORWEGIAN(110, "norwegian", SpeciesTypes.CAT), PERSIAN(111, "persian", SpeciesTypes.CAT),
    SIBERIAN(112, "siberian", SpeciesTypes.CAT), HIMALAYAN(113, "himalayan", SpeciesTypes.CAT), ORIENTAL(114, "oriental", SpeciesTypes.CAT),
    TOKINESE(115, "tokinese", SpeciesTypes.CAT), TURKISH_ANGORA(116, "turkishAngora", SpeciesTypes.CAT), BOBTAIL(117, "bobtail", SpeciesTypes.CAT),
    JAP_BOBTAIL(118, "japBobtail", SpeciesTypes.CAT), MAU(119, "mau", SpeciesTypes.CAT), BALINESE(120, "balinese", SpeciesTypes.CAT),
    CURL(121, "curl", SpeciesTypes.CAT), TURKISH_VAN(122, "turkishVan", SpeciesTypes.CAT), SOMALI(123, "somali", SpeciesTypes.CAT),
    WIREHAIR(124, "wirehair", SpeciesTypes.CAT), AEGEAN(125, "aegean", SpeciesTypes.CAT);

    private int id;
    private String name;
    private SpeciesTypes speciesType;

    BreedTypes(int id, String name, SpeciesTypes speciesType){
        this.id = id;
        this.name = name;
        this.speciesType = speciesType;
    }

    public int getId() {
        return id;
    }

    public String getName(){
        return this.name;
    }

    public SpeciesTypes getSpeciesType(){
        return this.speciesType;
    }
}
