package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.PetDao;
import ar.edu.itba.paw.models.*;

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
    private static final int PETS_IN_USER_PAGE = 4;
    private static final String PET_TABLE = "pets";
    private static final String HIDDEN_PETS_STATUS = " (2, 3) "; // Pets Removed or Sold are hidden from usual queries

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
        return jdbcTemplate.queryForObject("select ownerid from pets where id = ? AND pets.status NOT IN" + HIDDEN_PETS_STATUS,
                new Object[] {petId}, Long.class);
    }

    @Override
    public Optional<Pet> findById(String language, long id) {

        Map<Pet, List<Long>> imageMap = jdbcTemplate.query("select pets.id as id, petName, location, vaccinated, gender, description, birthDate, uploadDate, price, ownerId, " +
                "species.id as speciesId," + "species." + language + " AS speciesName, " +
                "breeds.id as breedId, breeds.speciesId as breedSpeciesID, " + "breeds." + language + " AS breedName, " +
                "images.id as imagesId, images.petId as petId, " +
                "pet_status.id as statusId, pet_status." + language + " as statusName " +
                "from (((pets inner join species on pets.species = species.id) inner join breeds on breed = breeds.id) " +
                    "inner join images on images.petId = pets.id) inner join pet_status on pet_status.id = status " +
                "WHERE pets.id = ? AND pets.status NOT IN" + HIDDEN_PETS_STATUS, new Object[] {id}, new PetMapExtractor());
        imageMap.forEach(Pet::setImages);
        return imageMap.keySet().stream().findFirst();
    }

    public Stream<Pet> list(String language, String page) {
        String offset = Integer.toString(PETS_PER_PAGE*(Integer.parseInt(page)-1));
        List<String> ids = jdbcTemplate.query("select id from pets " +
                                                    " WHERE pets.status NOT IN " + HIDDEN_PETS_STATUS +
                                                    " limit " + PETS_PER_PAGE + " offset " + offset,
                                            (resultSet, i) -> resultSet.getString("id"));
        if (ids.isEmpty()) return Stream.<Pet>builder().build();
        
        String pagePets = String.join(",", ids);

        String sql = "select pets.id as id, petName, location, vaccinated, gender, description, birthDate, uploadDate, price, ownerId, " +
                     "species.id as speciesId," + "species." + language + " AS speciesName, " +
                     "breeds.id as breedId, breeds.speciesId as breedSpeciesID, " + "breeds." + language + " AS breedName, " +
                     "images.id as imagesId, images.petId as petId, " +
                     "pet_status.id as statusId, pet_status." + language + " as statusName " +
                     "from (((pets inner join species on pets.species = species.id) inner join breeds on breed = breeds.id) inner join images on images.petid = pets.id) inner join pet_status on pet_status.id = status " +
                     "where pets.id in (" + pagePets + ") AND pets.status NOT IN " + HIDDEN_PETS_STATUS;
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
        try {
            numValue = Integer.parseInt(findValue);
        } catch (NumberFormatException ignored) {}

        String modifiedValue = "%"+findValue.toLowerCase()+"%";

        String offset = Integer.toString(PETS_PER_PAGE*(Integer.parseInt(page)-1));
        String sql = "select pets.id as id, petName, location, vaccinated, gender, description, birthDate, uploadDate, price, ownerId, " +
                "species.id as speciesId," + "species." + language + " AS speciesName, " +
                "breeds.id as breedId, breeds.speciesId as breedSpeciesID, " + "breeds." + language + " AS breedName, " +
                "pet_status.id as statusId, pet_status." + language + " as statusName " +
                "from ((pets inner join species on pets.species = species.id) inner join breeds on breed = breeds.id) inner join pet_status on pet_status.id = status " +
                "WHERE (LOWER(species." + language +") LIKE ? " +
                "OR LOWER(breeds." + language + ") LIKE ? " +
                "OR LOWER(petName) LIKE ? OR LOWER(location) LIKE ? OR price = ? ) " +
                "AND pets.status NOT IN " + HIDDEN_PETS_STATUS +
                " limit "+ PETS_PER_PAGE + " offset " + offset;

        List<String> ids = jdbcTemplate.query(sql, new Object[] { modifiedValue ,modifiedValue,modifiedValue,modifiedValue,numValue}, (resultSet, i) -> resultSet.getString("id"));
        if(ids.size() == 0){
            return Stream.empty();
        }
        String pagePets = String.join(",", ids);

        sql =   "select pets.id as id, petName, location, vaccinated, gender, description, birthDate, uploadDate, price, ownerId, " +
                "species.id as speciesId," + "species." + language + " AS speciesName, " +
                "breeds.id as breedId, breeds.speciesId as breedSpeciesID, " + "breeds." + language + " AS breedName, " +
                "images.id as imagesId, images.petId as petId, " +
                "pet_status.id as statusId, pet_status." + language + " as statusName " +
                "from (((pets inner join species on pets.species = species.id) inner join breeds on breed = breeds.id)inner join images on images.petid = pets.id) inner join pet_status on pet_status.id = status " +
                "WHERE (pets.id in (" + pagePets + ") AND LOWER(species." + language +") LIKE ?  " +
                "OR LOWER(breeds." + language + ") LIKE ? " +
                "OR LOWER(petName) LIKE ? OR LOWER(location) LIKE ? OR price = ? ) " +
                "AND pets.status NOT IN " + HIDDEN_PETS_STATUS;

        Map<Pet, List<Long>> imageMap = jdbcTemplate.query( sql,
                new Object[] { modifiedValue ,modifiedValue,modifiedValue,modifiedValue,numValue},
                new PetMapExtractor());
        imageMap.forEach(Pet::setImages);
        return imageMap.keySet().stream();
    }

    @Override
    public Stream<Pet> filteredList(String language, String specieFilter, String breedFilter, String genderFilter, String searchCriteria, String searchOrder, String minPrice, String maxPrice, String page) {

        if(specieFilter == null) {
            specieFilter = "%";
            breedFilter = "%";
        }

        int minP = -1;
        int maxP = -1;
        try {
            minP = Integer.parseInt(minPrice);
            maxP = Integer.parseInt(maxPrice);
        } catch (NumberFormatException ignored) { }

        if(breedFilter == null) { breedFilter = "%";}
        if(genderFilter == null) { genderFilter = "%"; }

        //Query to get the petids for the current page
        List<String> ids;
        String offset = Integer.toString(PETS_PER_PAGE*(Integer.parseInt(page)-1));
        String limit = " limit "+ PETS_PER_PAGE + " offset " + offset;

        String sql = "SELECT pets.id as id " +
                "FROM (((pets inner join species on pets.species = species.id) " +
                        "inner join breeds on pets.breed = breeds.id) " +
                        "inner join pet_status on pets.status = pet_status.id) " +
                "WHERE lower(cast(species.id as char(20))) LIKE ? " +
                "AND lower(cast(breeds.id as char(20))) LIKE ? " +
                "AND lower(gender) LIKE ? " +
                "AND pets.status NOT IN " + HIDDEN_PETS_STATUS ;

        if(minP != -1 && maxP != -1) {
            sql += "  AND price >= ? AND price <= ?  " ;
            ids = jdbcTemplate.query(sql + limit, new Object[] { specieFilter, breedFilter, genderFilter, minP, maxP}, (resultSet, i) -> resultSet.getString("id"));
        }
        else {
            ids = jdbcTemplate.query((sql + limit), new Object[]{specieFilter, breedFilter, genderFilter}, (resultSet, i) -> resultSet.getString("id"));
        }
        if(ids.size() == 0){
            return Stream.empty();
        }
        String pagePets = String.join(",", ids);

        //query to get the pets for the current page
        String sqlWithPages = "select pets.id as id, petName, location, vaccinated, gender, description, birthDate, uploadDate, price, ownerId, " +
                "species.id as speciesId," + "species." + language + " AS speciesName, " +
                "breeds.id as breedId, breeds.speciesId as breedSpeciesID, " + "breeds." + language + " AS breedName, " +
                "images.id as imagesId, images.petId as petId, " +
                "pet_status.id as statusId, pet_status." + language + " as statusName " +
                "from ((((pets inner join species on pets.species = species.id) inner join breeds on breed = breeds.id)" +
                "inner join images on images.petid = pets.id) inner join pet_status on pet_status.id = status ) " +
                "WHERE (pets.id in (" + pagePets + ") ) " ;


        if(searchCriteria == null) {

            Map<Pet, List<Long>> imageMap = jdbcTemplate.query(  sqlWithPages, new PetMapExtractor());
            imageMap.forEach(Pet::setImages);
            return imageMap.keySet().stream();
        }
        else {
            if(searchCriteria.toLowerCase().contains("upload")){
                searchCriteria = "uploadDate";
            }
            else if(searchCriteria.equalsIgnoreCase("gender")){
                searchCriteria = "gender";
            }
            else if(searchCriteria.equalsIgnoreCase("price")){
                searchCriteria = "price";
            }
            else if(searchCriteria.equalsIgnoreCase("species")){
                searchCriteria = "species." + language;
            }
            else if(searchCriteria.equalsIgnoreCase("breed")){
                searchCriteria = "breeds." + language;
            } else { /* Default criteria */
                searchCriteria = "breeds." + language;
            }
            if(searchOrder == null || searchOrder.equalsIgnoreCase("asc")) { searchOrder = "ASC";}
            else { searchOrder = "DESC";}

            searchCriteria = searchCriteria + " " + searchOrder;

            sql += "ORDER BY " + searchCriteria;

            if(minP != -1 && maxP != -1) {
                ids = jdbcTemplate.query(sql + limit, new Object[] { specieFilter, breedFilter, genderFilter, minP, maxP}, (resultSet, i) -> resultSet.getString("id"));
            }
            else {
                ids = jdbcTemplate.query((sql + limit), new Object[]{specieFilter, breedFilter, genderFilter}, (resultSet, i) -> resultSet.getString("id"));
            }
            if(ids.size() == 0){
                return Stream.empty();
            }
            pagePets = String.join(",", ids);
            sqlWithPages = "select pets.id as id, petName, location, vaccinated, gender, description, birthDate, uploadDate, price, ownerId, " +
                    "species.id as speciesId," + "species." + language + " AS speciesName, " +
                    "breeds.id as breedId, breeds.speciesId as breedSpeciesID, " + "breeds." + language + " AS breedName, " +
                    "images.id as imagesId, images.petId as petId, " +
                    "pet_status.id as statusId, pet_status." + language + " as statusName " +
                    "from ((((pets inner join species on pets.species = species.id) inner join breeds on breed = breeds.id)" +
                    "inner join images on images.petid = pets.id) inner join pet_status on pet_status.id = status ) " +
                    "WHERE (pets.id in (" + pagePets + ") ) " ;

            sqlWithPages += "ORDER BY " + searchCriteria;
            Map<Pet, List<Long>> imageMap = jdbcTemplate.query( sqlWithPages, new PetMapExtractor());
            imageMap.forEach(Pet::setImages);
            return imageMap.keySet().stream();
        }

    }

    @Override
    public Stream<Pet> getByUserId(String language, long userId, String page){
        String offset = Integer.toString(PETS_IN_USER_PAGE*(Integer.parseInt(page)-1));
        String sql = "select pets.id as id, petName, location, vaccinated, gender, description, birthDate, uploadDate, price, ownerId, " +
                "species.id as speciesId," + "species." + language + " AS speciesName, " +
                "breeds.id as breedId, breeds.speciesId as breedSpeciesID, " + "breeds." + language + " AS breedName, " +
                "pet_status.id as statusId, pet_status." + language + " as statusName, " +
                "images.id as imagesId, images.petId as petId " +
                "from (((pets inner join species on pets.species = species.id) inner join breeds on breed = breeds.id)inner join images on images.petid = pets.id) inner join pet_status on pet_status.id = status ";

        List<String> ids = jdbcTemplate.query(sql + " WHERE ownerid = ? AND pets.status NOT IN " + HIDDEN_PETS_STATUS +
                                                        " limit " + PETS_IN_USER_PAGE + " offset " + offset,
                                            new Object[] {userId},
                                            (resultSet, i) -> resultSet.getString("id"));

        if (ids.isEmpty()) return Stream.<Pet>builder().build();

        String pagePets = String.join(",", ids);
        Map<Pet, List<Long>> imageMap = jdbcTemplate.query(sql +
                                                        " WHERE  (pets.id in (" + pagePets + ")) AND pets.status NOT IN " + HIDDEN_PETS_STATUS,
                                                        new PetMapExtractor());
        imageMap.forEach(Pet::setImages);
        return imageMap.keySet().stream();
    }

    @Override
    public Pet create(String petName, Species species, Breed breed, String location, boolean vaccinated, String gender, String description, Date birthDate, Date uploadDate, int price, long ownerId, Status status) {
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
            put("status", status);
        }};
        final Number key = jdbcInsert.executeAndReturnKey(values);

        return new Pet(key.longValue(), petName, species, breed, location, vaccinated, gender, description, birthDate, uploadDate, price, ownerId, status);
    }

    @Override
    public String maxPages() {
        Integer pets = jdbcTemplate.queryForObject("select count(*) from pets WHERE pets.status NOT IN " + HIDDEN_PETS_STATUS,
                                                    Integer.class);

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

        String sql = "select count(distinct pets.id) " +
                "from (((pets inner join species on pets.species = species.id) inner join breeds on breed = breeds.id)inner join images on images.petid = pets.id) " +
                "WHERE (LOWER(species." + language +") LIKE ?  " +
                "OR LOWER(breeds." + language + ") LIKE ? " +
                "OR LOWER(petName) LIKE ? OR LOWER(location) LIKE ? OR price = ? ) " +
                "AND pets.status NOT IN " + HIDDEN_PETS_STATUS;


        Integer pets = jdbcTemplate.queryForObject(sql, new Object[] { modifiedValue ,modifiedValue,modifiedValue,modifiedValue,numValue} ,Integer.class);

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
            Integer pets = jdbcTemplate.queryForObject(  "select count( distinct pets.id) "+
                            "from (((pets inner join species on pets.species = species.id) inner join breeds on breed = breeds.id)inner join images on images.petid = pets.id) " +
                            "WHERE (lower(species.id::text) LIKE ? " +
                            "AND lower(breeds.id::text) LIKE ? " +
                            "AND lower(gender) LIKE ? ) " +
                            "AND pets.status NOT IN " + HIDDEN_PETS_STATUS,
                    new Object[] {specieFilter, breedFilter, genderFilter},
                    Integer.class);

            pets = (int) Math.ceil((double) pets / PETS_PER_PAGE);
            return pets.toString();
    }

    @Override
    public String getMaxUserPetsPages(long userId){
        Integer pets = jdbcTemplate.queryForObject("select count(*) from pets where ownerId = ? " +
                                                        "AND pets.status NOT IN " + HIDDEN_PETS_STATUS, new Object[] {userId}, Integer.class);
        pets = (int) Math.ceil((double) pets / PETS_IN_USER_PAGE);
        return pets.toString();
    }

    @Override
    public Optional<Contact> getPetContact(long petId) {
        return jdbcTemplate.query("SELECT users.mail AS mail, users.username AS username " +
                    "FROM pets INNER JOIN users ON users.id = pets.ownerId " +
                    "WHERE pets.id = ? AND pets.status NOT IN " + HIDDEN_PETS_STATUS,
                new Object[] {petId}, CONTACT_MAPPER).stream().findFirst();
    }

    @Override
    public void updateStatus(long id, long newStatus) {
        String sql = "UPDATE pets " +
                     "SET status = ? " +
                     "WHERE id = ? ";
        jdbcTemplate.update(sql, newStatus, id);
    }

    @Override
    public boolean isPetOwner(long petId, long userId) {
        String sql = "SELECT COUNT(id) " +
                     "FROM pets " +
                     "WHERE id = ? AND ownerId = ? ";
        Integer owner = jdbcTemplate.queryForObject(sql, new Object[] {petId, userId}, Integer.class);
        return owner == 1;
    }
}