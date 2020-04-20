package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.PetDao;
import ar.edu.itba.paw.models.Breed;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.Pet;

import ar.edu.itba.paw.models.Species;
import ar.edu.itba.paw.persistence.mappers.PetMapExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.Date;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@Repository
public class PetDaoImpl implements PetDao {

    private static final String PET_TABLE = "pets";

    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private static final RowMapper<Pet> PET_MAPPER = (rs, rowNum) -> new Pet(
            rs.getLong("id"),
            rs.getString("petName"),
            new Species(rs.getLong("speciesId"), rs.getString("speciesEs"), rs.getString("speciesEn")),
            new Breed(rs.getLong("breedId"), rs.getLong("breedSpeciesId"), rs.getString("breedEs"), rs.getString("breedEn")),
            rs.getString("location"),
            rs.getBoolean("vaccinated"),
            rs.getString("gender"),
            rs.getString("description"),
            rs.getDate("birthDate"),
            rs.getDate("uploadDate"),
            rs.getInt("price"),
            rs.getLong("ownerId")
    );



    @Autowired
    public PetDaoImpl(final DataSource dataSource) {

        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(PET_TABLE)
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Optional<Pet> findById(long id) {
        Map<Pet, List<Image>> imageMap = jdbcTemplate.query("select pets.id as id, petName, location, vaccinated, gender, description, birthDate, uploadDate, price, ownerId, " +
                "species.id as speciesID, species.es_ar as speciesEs, species.en_us as speciesEn, " +
                "breeds.id as breedID, breeds.speciesId as breedSpeciesID, breeds.es_ar as breedEs, breeds.en_us as breedEn, img, images.id as imagesId, images.petId as petId " +
                "from (((pets inner join species on pets.species = species.id) inner join breeds on breed = breeds.id)inner join images on images.petId = pets.id) " +
                "WHERE pets.id = ?", new Object[] {id}, new PetMapExtractor());
        imageMap.forEach(Pet::setImages);
        return imageMap.keySet().stream().findFirst();
    }

    @Override
    public Stream<Pet> list() {
        Map<Pet, List<Image>> imageMap = jdbcTemplate.query("select pets.id as id, petName, location, vaccinated, gender, description, birthDate, uploadDate, price, ownerId, " +
                "species.id as speciesID, species.es_ar as speciesEs, species.en_us as speciesEn, " +
                "breeds.id as breedID, breeds.speciesId as breedSpeciesID, breeds.es_ar as breedEs, breeds.en_us as breedEn, img, images.id as imagesId, images.petId as petId " +
                "from (((pets inner join species on pets.species = species.id) inner join breeds on breed = breeds.id)inner join images on images.petid = pets.id)",
                new PetMapExtractor());
        imageMap.forEach(Pet::setImages);
        return imageMap.keySet().stream();
    }

    @Override
    public Stream<Pet> find(String findValue){
        if(findValue.equals("")){
            return list();
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
                "species.id as speciesID, species.es_ar as speciesEs, species.en_us as speciesEn, " +
                "breeds.id as breedID, breeds.speciesId as breedSpeciesID, breeds.es_ar as breedEs, breeds.en_us as breedEn, img, images.id as imagesId, images.petId as petId " +
                "from (((pets inner join species on pets.species = species.id) inner join breeds on breed = breeds.id)inner join images on images.petid = pets.id) " +
                "WHERE LOWER(species.en_US) LIKE ? OR LOWER(species.es_AR) LIKE ? " +
                "OR LOWER(breeds.en_US) LIKE ? OR LOWER(breeds.es_ar) LIKE ? " +
                "OR LOWER(petName) LIKE ? OR LOWER(location) LIKE ? OR price = ?";
        Map<Pet, List<Image>> imageMap = jdbcTemplate.query( sql,
                new Object[] {modifiedValue, modifiedValue, modifiedValue ,modifiedValue,modifiedValue,modifiedValue,numValue},
                new PetMapExtractor());
        imageMap.forEach(Pet::setImages);
        return imageMap.keySet().stream();
    }

    @Override
    public Stream<Pet> filteredList(String specieFilter, String breedFilter, String genderFilter, String searchCriteria, String searchOrder) {
        if(specieFilter == null) {
            specieFilter = "%";
            breedFilter = "%";
        }
        else{ specieFilter = "%" + specieFilter + "%";}
        if(breedFilter == null) { breedFilter = "%";}
        else{ breedFilter = "%" + breedFilter + "%";}
        if(genderFilter == null) { genderFilter = "%"; }
        if(searchCriteria == null) {
            Map<Pet, List<Image>> imageMap = jdbcTemplate.query(  "select pets.id as id, petName, location, vaccinated, gender, description, birthDate, uploadDate, price, ownerId, " +
                            "species.id as speciesID, species.es_ar as speciesEs, species.en_us as speciesEn, " +
                            "breeds.id as breedID, breeds.speciesId as breedSpeciesID, breeds.es_ar as breedEs, breeds.en_us as breedEn, img, images.id as imagesId, images.petId as petId " +
                            "from (((pets inner join species on pets.species = species.id) inner join breeds on breed = breeds.id) inner join images on images.petid = pets.id)" +
                            "WHERE (lower(species.es_AR) LIKE ? OR lower(species.en_US) LIKE ? ) AND (lower(breeds.es_AR) LIKE ? OR lower(breeds.en_US) LIKE ? )AND lower(gender) LIKE ? ",
                    new Object[] {specieFilter, specieFilter, breedFilter, breedFilter, genderFilter},
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
                    "species.id as speciesID, species.es_ar as speciesEs, species.en_us as speciesEn, " +
                    "breeds.id as breedID, breeds.speciesId as breedSpeciesID, breeds.es_ar as breedEs, breeds.en_us as breedEn, img, images.id as imagesId, images.petId as petId " +
                    "from (((pets inner join species on pets.species = species.id) inner join breeds on breed = breeds.id) inner join images on images.petid = pets.id) " +
                    "WHERE (species.es_AR LIKE ? OR species.en_US LIKE ? ) AND (breeds.es_AR LIKE ? OR breeds.en_US LIKE ? )AND gender LIKE ? " +
                    "ORDER BY " +
                    searchCriteria;
            Map<Pet, List<Image>> imageMap = jdbcTemplate.query( sql,
                    new Object[] {specieFilter, specieFilter, breedFilter, breedFilter, genderFilter},
                    new PetMapExtractor());
            imageMap.forEach(Pet::setImages);
            return imageMap.keySet().stream();
        }

    }

//    @Override
//    public Pet create(String petName, long speciesId, long breedId, String location, boolean vaccinated, String gender, String description, Date birthDate, Date uploadDate, int price, long ownerId) {
//        final Map<String, Object> values = new HashMap<String, Object>() {{
//            put("petName", petName);
//            put("species", species);
//            put("breed", breed);
//            put("location", location);
//            put("vaccinated", vaccinated);
//            put("gender", gender);
//            put("description", description);
//            put("birthDate", birthDate);
//            put("uploadDate", uploadDate);
//            put("price", price);
//            put("ownerId", ownerId);
//        }};
//        final Number key = jdbcInsert.executeAndReturnKey(values);
//
//        return new Pet(key.longValue(), petName, species, breed,location,vaccinated,gender,description,birthDate,uploadDate,price,ownerId);
//    }
}
