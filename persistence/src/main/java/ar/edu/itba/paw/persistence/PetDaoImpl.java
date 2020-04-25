package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.PetDao;
import ar.edu.itba.paw.models.Breed;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.Pet;

import ar.edu.itba.paw.models.Species;
import ar.edu.itba.paw.persistence.mappers.PetMapExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public class PetDaoImpl implements PetDao {

    private static final int PETS_PER_PAGE = 4;
    private static final String PET_TABLE = "pets";

    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;


    @Autowired
    public PetDaoImpl(final DataSource dataSource) {

        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(PET_TABLE)
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Optional<Pet> findById(String language, long id) {

        Map<Pet, List<Image>> imageMap = jdbcTemplate.query("select pets.id as id, petName, location, vaccinated, gender, description, birthDate, uploadDate, price, ownerId, " +
                "species.id as speciesId," + "species." + language + " AS speciesName, " +
                "breeds.id as breedId, breeds.speciesId as breedSpeciesID, " + "breeds." + language + " AS breedName, " +
                " img, images.id as imagesId, images.petId as petId " +
                "from (((pets inner join species on pets.species = species.id) inner join breeds on breed = breeds.id)inner join images on images.petId = pets.id) " +
                "WHERE pets.id = ?", new Object[] {id}, new PetMapExtractor());
        imageMap.forEach(Pet::setImages);
        return imageMap.keySet().stream().findFirst();
    }

    @Override
    public Stream<Pet> list(String language, String page) {
        String offset = Integer.toString(PETS_PER_PAGE*(Integer.parseInt(page)-1)) ;
        String sql = "select pets.id as id, petName, location, vaccinated, gender, description, birthDate, uploadDate, price, ownerId, " +
                "species.id as speciesId," + "species." + language + " AS speciesName, " +
                "breeds.id as breedId, breeds.speciesId as breedSpeciesID, " + "breeds." + language + " AS breedName, " +
                " img, images.id as imagesId, images.petId as petId " +
                "from (((pets inner join species on pets.species = species.id) inner join breeds on breed = breeds.id) inner join images on images.petid = pets.id) "
                + "limit "+ PETS_PER_PAGE +" offset " + offset;

        Map<Pet, List<Image>> imageMap = jdbcTemplate.query(sql,
                new PetMapExtractor());
        imageMap.forEach(Pet::setImages);
        System.out.println(sql);
        return imageMap.keySet().stream();
    }

    @Override
    public Stream<Pet> find(String language, String findValue, String page){
        if(findValue.equals("")){
            return list(language,"1");
        }

        int numValue = -1;
        boolean number = true;
        for(int i = 0; i < findValue.length();i++){
            if(!Character.isDigit(findValue.charAt(i))){
                number = false;
            }
        }
        if(number){
            numValue = Integer.parseInt(findValue);
        }
        String modifiedValue = "%"+findValue.toLowerCase()+"%";

        String sql = "select pets.id as id, petName, location, vaccinated, gender, description, birthDate, uploadDate, price, ownerId, " +
                "species.id as speciesId," + "species." + language + " AS speciesName, " +
                "breeds.id as breedId, breeds.speciesId as breedSpeciesID, " + "breeds." + language + " AS breedName, " +
                " img, images.id as imagesId, images.petId as petId " +
                "from (((pets inner join species on pets.species = species.id) inner join breeds on breed = breeds.id)inner join images on images.petid = pets.id) " +
                "WHERE LOWER(species." + language +") LIKE ?  " +
                "OR LOWER(breeds." + language + ") LIKE ? " +
                "OR LOWER(petName) LIKE ? OR LOWER(location) LIKE ? OR price = ? " +
                "limit "+ PETS_PER_PAGE + " offset "+ PETS_PER_PAGE*(Integer.parseInt(page)-1);
        Map<Pet, List<Image>> imageMap = jdbcTemplate.query( sql,
                new Object[] { modifiedValue ,modifiedValue,modifiedValue,modifiedValue,numValue},
                new PetMapExtractor());
        imageMap.forEach(Pet::setImages);
        return imageMap.keySet().stream();
    }

    @Override
    public Stream<Pet> filteredList(String language, String specieFilter, String breedFilter, String genderFilter, String searchCriteria, String searchOrder, String page) {
        if(specieFilter == null) {
            specieFilter = "%";
            breedFilter = "%";
        }
        if(breedFilter == null) { breedFilter = "%";}

        if(genderFilter == null) { genderFilter = "%"; }
        if(searchCriteria == null) {
            Map<Pet, List<Image>> imageMap = jdbcTemplate.query(  "select pets.id as id, petName, location, vaccinated, gender, description, birthDate, uploadDate, price, ownerId, " +
                            "species.id as speciesId," + "species." + language + " AS speciesName, " +
                            "breeds.id as breedId, breeds.speciesId as breedSpeciesID, " + "breeds." + language + " AS breedName, " +
                            " img, images.id as imagesId, images.petId as petId " +
                            "from (((pets inner join species on pets.species = species.id) inner join breeds on breed = breeds.id)inner join images on images.petid = pets.id) " +
                            "WHERE lower(species.id::text) LIKE ? " +
                            " AND lower(breeds.id::text) LIKE ? " +
                            "AND lower(gender) LIKE ? " +
                            "limit "+ PETS_PER_PAGE + " offset "+ PETS_PER_PAGE*(Integer.parseInt(page)-1),
                    new Object[] {specieFilter, breedFilter, genderFilter},
                    new PetMapExtractor());
            imageMap.forEach(Pet::setImages);
            return imageMap.keySet().stream();
        }
        else {
            if(searchCriteria.contains("upload")){
                searchCriteria = "uploadDate";
            }
            if(searchCriteria.contains("gender")){
                searchCriteria = "gender";
            }
            if(searchCriteria.contains("price")){
                searchCriteria = "price";
            }
            if(searchCriteria.contains("species")){
                searchCriteria = "species";
            }
            if(searchCriteria.contains("breed")){
                searchCriteria = "breed";
            }
            if(searchOrder.contains("asc")) { searchOrder = "ASC";}
            else { searchOrder = "DESC";}

            searchCriteria = searchCriteria + " " + searchOrder;
            String sql = "select pets.id as id, petName, location, vaccinated, gender, description, birthDate, uploadDate, price, ownerId, " +
                    "species.id as speciesId," + "species." + language + " AS speciesName, " +
                    "breeds.id as breedId, breeds.speciesId as breedSpeciesID, " + "breeds." + language + " AS breedName, " +
                    " img, images.id as imagesId, images.petId as petId " +
                    "from (((pets inner join species on pets.species = species.id) inner join breeds on breed = breeds.id)inner join images on images.petid = pets.id) " +
                    "WHERE lower(species.id::text) LIKE ? " +
                    " AND lower(breeds.id::text) LIKE ? " +
                    "AND lower(gender) LIKE ? " +
                    "ORDER BY " +
                    searchCriteria;
            Map<Pet, List<Image>> imageMap = jdbcTemplate.query( sql,
                    new Object[] {specieFilter, breedFilter, genderFilter},
                    new PetMapExtractor());
            imageMap.forEach(Pet::setImages);
            return imageMap.keySet().stream();
        }

    }

    @Override
    public Pet create(String petName, Species species, Breed breed, String location, boolean vaccinated, String gender, String description, Date birthDate, Date uploadDate, int price, long ownerId) {
        final Map<String, Object> values = new HashMap<String, Object>() {{
            put("petName", petName);
            put("species", species.getId());
            put("breed", breed.getId());
            put("location", location);
            put("vaccinated", vaccinated);
            put("gender", gender);
            put("description", description);
            put("birthDate", birthDate);
            put("uploadDate", uploadDate);
            put("price", price);
            put("ownerId", ownerId);
        }};
        final Number key = jdbcInsert.executeAndReturnKey(values);

        return new Pet(key.longValue(), petName, species, breed, location, vaccinated, gender, description, birthDate, uploadDate, price, ownerId);
    }

    @Override
    public String maxPages() {
        Integer pets = jdbcTemplate.queryForObject("select count(*) from pets",Integer.class);
        pets = (int) Math.ceil((double) pets / PETS_PER_PAGE);
        return pets.toString();
    }

    @Override
    public String maxSearchPages(String language, String findValue) {
        if(findValue.equals("")){
            return maxPages();
        }

        int numValue = -1;
        boolean number = true;
        for(int i = 0; i < findValue.length();i++){
            if(!Character.isDigit(findValue.charAt(i))){
                number = false;
            }
        }
        if(number){
            numValue = Integer.parseInt(findValue);
        }
        String modifiedValue = "%"+findValue.toLowerCase()+"%";

        String sql = "select count(distinct pets.id)" +
                "from (((pets inner join species on pets.species = species.id) inner join breeds on breed = breeds.id)inner join images on images.petid = pets.id) " +
                "WHERE LOWER(species." + language +") LIKE ?  " +
                "OR LOWER(breeds." + language + ") LIKE ? " +
                "OR LOWER(petName) LIKE ? OR LOWER(location) LIKE ? OR price = ? ";
        Integer pets = jdbcTemplate.queryForObject(sql,new Object[] { modifiedValue ,modifiedValue,modifiedValue,modifiedValue,numValue} ,Integer.class);
        pets = (int) Math.ceil((double) pets / PETS_PER_PAGE);
        return pets.toString();
    }

    @Override
    public String maxFilterPages(String language, String specieFilter, String breedFilter, String genderFilter) {
        if(specieFilter == null) {
            specieFilter = "%";
            breedFilter = "%";
        }
        if(breedFilter == null) { breedFilter = "%";}

        if(genderFilter == null) { genderFilter = "%"; }
            Integer pets = jdbcTemplate.queryForObject(  "select count( distinct pets.id)"+
                            "from (((pets inner join species on pets.species = species.id) inner join breeds on breed = breeds.id)inner join images on images.petid = pets.id) " +
                            "WHERE lower(species.id::text) LIKE ? " +
                            " AND lower(breeds.id::text) LIKE ? " +
                            "AND lower(gender) LIKE ? ",
                    new Object[] {specieFilter, breedFilter, genderFilter},
                    Integer.class);

            pets = (int) Math.ceil((double) pets / PETS_PER_PAGE);
            return pets.toString();
    }


}