package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.PetDao;
import ar.edu.itba.paw.models.*;

import ar.edu.itba.paw.persistence.mappers.PetMapExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class PetDaoImpl implements PetDao {

    private static final int PETS_PER_PAGE = 12;
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

    private static final RowMapper<Contact> CONTACT_MAPPER = (rs, rowNum) -> new Contact(rs.getString("mail"), rs.getString("username"));

    @Override
    public long getOwnerId(long petId){
        return jdbcTemplate.queryForObject("select ownerid from pets where id = "+ petId,Long.class);
    }

    @Override
    public Optional<Pet> findById(String language, long id) {

        Map<Pet, List<Long>> imageMap = jdbcTemplate.query("select pets.id as id, petName, location, vaccinated, gender, description, birthDate, uploadDate, price, ownerId, " +
                "species.id as speciesId," + "species." + language + " AS speciesName, " +
                "breeds.id as breedId, breeds.speciesId as breedSpeciesID, " + "breeds." + language + " AS breedName, " +
                "  images.id as imagesId, images.petId as petId " +
                "from (((pets inner join species on pets.species = species.id) inner join breeds on breed = breeds.id)inner join images on images.petId = pets.id) " +
                "WHERE pets.id = ?", new Object[] {id}, new PetMapExtractor());
        imageMap.forEach(Pet::setImages);
        return imageMap.keySet().stream().findFirst();
    }

    public Stream<Pet> list(String language, String page) {
        String offset = Integer.toString(PETS_PER_PAGE*(Integer.parseInt(page)-1));
        List<String> ids = jdbcTemplate.query("select id from pets limit "+ PETS_PER_PAGE + " offset " + offset, (resultSet, i) -> resultSet.getString("id"));
        if (ids.isEmpty()) return Stream.<Pet>builder().build();
        
        String pagePets = String.join(",", ids);

        String sql = "select pets.id as id, petName, location, vaccinated, gender, description, birthDate, uploadDate, price, ownerId, " +
                     "species.id as speciesId," + "species." + language + " AS speciesName, " +
                     "breeds.id as breedId, breeds.speciesId as breedSpeciesID, " + "breeds." + language + " AS breedName, " +
                     "  images.id as imagesId, images.petId as petId " +
                     "from (((pets inner join species on pets.species = species.id) inner join breeds on breed = breeds.id)inner join images on images.petid = pets.id) " +
                     "where pets.id in (" + pagePets + ") ";
        Map<Pet, List<Long>> imageMap = jdbcTemplate.query(sql,
                new PetMapExtractor());
        imageMap.forEach(Pet::setImages);
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

        String offset = Integer.toString(PETS_PER_PAGE*(Integer.parseInt(page)-1));
        String sql = "select pets.id as id, petName, location, vaccinated, gender, description, birthDate, uploadDate, price, ownerId, " +
                "species.id as speciesId," + "species." + language + " AS speciesName, " +
                "breeds.id as breedId, breeds.speciesId as breedSpeciesID, " + "breeds." + language + " AS breedName " +
                "from ((pets inner join species on pets.species = species.id) inner join breeds on breed = breeds.id)" +
                "WHERE LOWER(species." + language +") LIKE ?  " +
                "OR LOWER(breeds." + language + ") LIKE ? " +
                "OR LOWER(petName) LIKE ? OR LOWER(location) LIKE ? OR price = ? " +
                "limit "+ PETS_PER_PAGE + " offset " + offset;

        List<String> ids = jdbcTemplate.query(sql, new Object[] { modifiedValue ,modifiedValue,modifiedValue,modifiedValue,numValue}, (resultSet, i) -> resultSet.getString("id"));
        if(ids.size() == 0){
            return Stream.empty();
        }
        String pagePets = String.join(",", ids);

        sql =   "select pets.id as id, petName, location, vaccinated, gender, description, birthDate, uploadDate, price, ownerId, " +
                "species.id as speciesId," + "species." + language + " AS speciesName, " +
                "breeds.id as breedId, breeds.speciesId as breedSpeciesID, " + "breeds." + language + " AS breedName, " +
                " images.id as imagesId, images.petId as petId " +
                "from (((pets inner join species on pets.species = species.id) inner join breeds on breed = breeds.id)inner join images on images.petid = pets.id) " +
                "WHERE pets.id in (" + pagePets + ") AND LOWER(species." + language +") LIKE ?  " +
                "OR LOWER(breeds." + language + ") LIKE ? " +
                "OR LOWER(petName) LIKE ? OR LOWER(location) LIKE ? OR price = ? ";

        Map<Pet, List<Long>> imageMap = jdbcTemplate.query( sql,
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

        String offset = Integer.toString(PETS_PER_PAGE*(Integer.parseInt(page)-1));
        if(searchCriteria == null) {
            String sql = "select pets.id as id, petName, location, vaccinated, gender, description, birthDate, uploadDate, price, ownerId, " +
                    "species.id as speciesId," + "species." + language + " AS speciesName, " +
                    "breeds.id as breedId, breeds.speciesId as breedSpeciesID, " + "breeds." + language + " AS breedName " +
                    "from ((pets inner join species on pets.species = species.id) inner join breeds on breed = breeds.id)" +
                    "WHERE lower(species.id::text) LIKE ? " +
                    " AND lower(breeds.id::text) LIKE ? " +
                    "AND lower(gender) LIKE ? " +
                    "limit "+ PETS_PER_PAGE + " offset " + offset;

            List<String> ids = jdbcTemplate.query(sql, new Object[] { specieFilter, breedFilter, genderFilter}, (resultSet, i) -> resultSet.getString("id"));
            if(ids.size() == 0){
                return Stream.empty();
            }
            String pagePets = String.join(",", ids);

            Map<Pet, List<Long>> imageMap = jdbcTemplate.query(  "select pets.id as id, petName, location, vaccinated, gender, description, birthDate, uploadDate, price, ownerId, " +
                            "species.id as speciesId," + "species." + language + " AS speciesName, " +
                            "breeds.id as breedId, breeds.speciesId as breedSpeciesID, " + "breeds." + language + " AS breedName, " +
                            "  images.id as imagesId, images.petId as petId " +
                            "from (((pets inner join species on pets.species = species.id) inner join breeds on breed = breeds.id)inner join images on images.petid = pets.id) " +
                            "WHERE pets.id in (" + pagePets + ") AND lower(species.id::text) LIKE ? " +
                            " AND lower(breeds.id::text) LIKE ? " +
                            "AND lower(gender) LIKE ? ",
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
                searchCriteria = "species." + language;
            }
            if(searchCriteria.contains("breed")){
                searchCriteria = "breeds." + language;
            }
            if(searchOrder == null || searchOrder.contains("asc")) { searchOrder = "ASC";}
            else { searchOrder = "DESC";}

            searchCriteria = searchCriteria + " " + searchOrder;
            String sql = "select pets.id as id, petName, location, vaccinated, gender, description, birthDate, uploadDate, price, ownerId, " +
                    "species.id as speciesId," + "species." + language + " AS speciesName, " +
                    "breeds.id as breedId, breeds.speciesId as breedSpeciesID, " + "breeds." + language + " AS breedName " +
                    "from ((pets inner join species on pets.species = species.id) inner join breeds on breed = breeds.id)" +
                    "WHERE lower(species.id::text) LIKE ? " +
                    " AND lower(breeds.id::text) LIKE ? " +
                    "AND lower(gender) LIKE ? " +
                    "ORDER BY " +
                     searchCriteria +
                    " limit "+ PETS_PER_PAGE + " offset " + offset;

            List<String> ids = jdbcTemplate.query(sql, new Object[] { specieFilter, breedFilter, genderFilter}, (resultSet, i) -> resultSet.getString("id"));
            if(ids.size() == 0){
                return Stream.empty();
            }
            String pagePets = String.join(",", ids);

            sql = "select pets.id as id, petName, location, vaccinated, gender, description, birthDate, uploadDate, price, ownerId, " +
                    "species.id as speciesId," + "species." + language + " AS speciesName, " +
                    "breeds.id as breedId, breeds.speciesId as breedSpeciesID, " + "breeds." + language + " AS breedName, " +
                    "  images.id as imagesId, images.petId as petId " +
                    "from (((pets inner join species on pets.species = species.id) inner join breeds on breed = breeds.id)inner join images on images.petid = pets.id) " +
                    "WHERE  pets.id in (" + pagePets + ") AND  lower(species.id::text) LIKE ? " +
                    " AND lower(breeds.id::text) LIKE ? " +
                    "AND lower(gender) LIKE ? " +
                    "ORDER BY " +
                     searchCriteria;
            Map<Pet, List<Long>> imageMap = jdbcTemplate.query( sql,
                    new Object[] {specieFilter, breedFilter, genderFilter},
                    new PetMapExtractor());
            imageMap.forEach(Pet::setImages);
            return imageMap.keySet().stream();
        }

    }

    @Override
    public Stream<Pet> getByUserId(String language, long userId, String page){
        String offset = Integer.toString(PETS_PER_PAGE*(Integer.parseInt(page)-1));
        String sql = "select pets.id as id, petName, location, vaccinated, gender, description, birthDate, uploadDate, price, ownerId, " +
                "species.id as speciesId," + "species." + language + " AS speciesName, " +
                "breeds.id as breedId, breeds.speciesId as breedSpeciesID, " + "breeds." + language + " AS breedName, " +
                "  images.id as imagesId, images.petId as petId " +
                "from (((pets inner join species on pets.species = species.id) inner join breeds on breed = breeds.id)inner join images on images.petid = pets.id) ";
        List<String> ids = jdbcTemplate.query(sql + "where ownerid = " + userId + " limit " + PETS_PER_PAGE + " offset " + offset, (resultSet, i) -> resultSet.getString("id"));
        if (ids.isEmpty()) return Stream.<Pet>builder().build();

        String pagePets = String.join(",", ids);

        Map<Pet, List<Long>> imageMap = jdbcTemplate.query(sql + "WHERE  pets.id in (" + pagePets + ")",
                new PetMapExtractor());
        System.out.println(imageMap.size());
        imageMap.forEach(Pet::setImages);
        return imageMap.keySet().stream();
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

    @Override
    public String getMaxUserPetsPages(long userId){
        Integer pets = jdbcTemplate.queryForObject("select count(*) from pets where ownerId = " + userId ,Integer.class);
        pets = (int) Math.ceil((double) pets / PETS_PER_PAGE);
        return pets.toString();
    }

    @Override
    public Optional<Contact> getPetContact(long petId) {
        return jdbcTemplate.query("SELECT users.mail AS mail, users.username AS username " +
                "FROM pets INNER JOIN users ON users.id = pets.ownerId " +
                "WHERE pets.id = ?", new Object[] {petId}, CONTACT_MAPPER).stream().findFirst();
    }


}