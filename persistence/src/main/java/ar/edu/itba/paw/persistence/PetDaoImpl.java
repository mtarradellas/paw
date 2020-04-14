package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.PetDao;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.Pet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
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
            rs.getString("species"),
            rs.getString("breed"),
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
        return jdbcTemplate.query("SELECT * FROM pets WHERE id = ?", new Object[] {id}, PET_MAPPER)
                .stream().findFirst();
    }

    @Override
    public Stream<Pet> list() {
        return jdbcTemplate.query("SELECT * FROM pets", PET_MAPPER)
                .stream();
    }

    @Override
    public Stream<Pet> find(String findValue){
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

        String sql = "SELECT * " +
                "FROM pets " +
                "WHERE species LIKE ? OR breed LIKE ? OR petName LIKE ? OR location LIKE ? OR price = ?";
        return jdbcTemplate.query( sql,
                new Object[] {findValue.toLowerCase(),findValue.toLowerCase(),findValue,findValue,numValue},
                PET_MAPPER)
                .stream();
    }

    @Override
    public Stream<Pet> filteredList(String specieFilter, String breedFilter, String genderFilter, String searchCriteria, String searchOrder) {
        if(specieFilter == null) {
            specieFilter = "%";
            breedFilter = "%";
        }
        if(breedFilter == null) { breedFilter = "%";}
        if(genderFilter == null) { genderFilter = "%"; }
        if(searchCriteria == null) {
            return jdbcTemplate.query(  "SELECT * " +
                            "FROM pets " +
                            "WHERE species LIKE ? AND breed LIKE ? AND gender LIKE ? ",
                    new Object[] {specieFilter, breedFilter, genderFilter},
                    PET_MAPPER)
                    .stream();
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
            String sql = "SELECT * " +
                    "FROM pets " +
                    "WHERE species LIKE ? AND breed LIKE ? AND gender LIKE ? " +
                    "ORDER BY " +
                    searchCriteria;
            return jdbcTemplate.query( sql,
                    new Object[] {specieFilter, breedFilter, genderFilter},
                    PET_MAPPER)
                    .stream();
        }

    }

    @Override
    public Pet create(String petName, String species, String breed, String location, boolean vaccinated, String gender, String description, Date birthDate, Date uploadDate, int price, long ownerId) {
        final Map<String, Object> values = new HashMap<String, Object>() {{
            put("petName", petName);
            put("species", species);
            put("breed", breed);
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

        return new Pet(key.longValue(), petName, species, breed,location,vaccinated,gender,description,birthDate,uploadDate,price,ownerId);
    }
}
