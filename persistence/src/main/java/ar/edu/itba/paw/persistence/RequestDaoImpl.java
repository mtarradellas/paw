package ar.edu.itba.paw.persistence;


import ar.edu.itba.paw.interfaces.RequestDao;
import ar.edu.itba.paw.models.Request;
import ar.edu.itba.paw.models.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;
import java.util.stream.Stream;

@Repository
public class RequestDaoImpl implements RequestDao {

    private static final String REQUESTS_TABLE = "requests";

    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private static final RowMapper<Request> REQUEST_MAPPER = (rs, rowNum) -> new Request(
            rs.getLong("id"),
            rs.getLong("ownerId"),
            rs.getString("ownerUsername"),
            new Status(rs.getInt("statusId"), rs.getString("statusName")),
            rs.getLong("petId"),
            rs.getString("petName"),
            rs.getDate("creationDate")
    );

    @Autowired
    public RequestDaoImpl(final DataSource dataSource) {

        jdbcTemplate = new JdbcTemplate(dataSource);
        String[] colNames = {"ownerId", "petId", "status"};
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(REQUESTS_TABLE)
                .usingGeneratedKeyColumns("id")
                .usingColumns(colNames);
    }

    @Override
    public Optional<Request> findById(long id, String language) {
        return jdbcTemplate.query("SELECT requests.id as id,  requests.ownerId as ownerId, users.username as ownerUsername, petId,  " +
                        "creationDate, status.id as statusId , status." + language + " as statusName, pets.petname as petName " +
                        "FROM (((requests inner join status on requests.status = status.id) inner join users on requests.ownerid = users.id)inner join pets on pets.id = requests.petId) " +
                        "WHERE requests.id = ? "
                , new Object[]{id}, REQUEST_MAPPER)
                .stream().findFirst();
    }

    @Override
    public Stream<Request> listByOwner(String language, long ownerId) {
        return jdbcTemplate.query("SELECT requests.id as id,  requests.ownerId as ownerId, users.username as ownerUsername, petId, " +
                        "creationDate, status.id as statusId , status." + language + " as statusName, pets.petname as petName " +
                        "FROM (((requests inner join status on requests.status = status.id) inner join users on requests.ownerid = users.id)inner join pets on pets.id = requests.petId) " +
                        "WHERE requests.ownerId = ? "
                , new Object[]{ownerId}, REQUEST_MAPPER)
                .stream();
    }

    @Override
    public Stream<Request> listByPetOwner(String language, long petOwnerId) {
        return jdbcTemplate.query("SELECT requests.id as id,  requests.ownerId as ownerId, users.username as ownerUsername, petId, " +
                        "creationDate, status.id as statusId , status." + language + " as statusName, pets.petname as petName " +
                        "FROM (((requests inner join status on requests.status = status.id) inner join users on requests.ownerid = users.id)inner join pets on pets.id = requests.petId) " +
                        "WHERE pets.ownerId = ?"
                , new Object[]{petOwnerId}, REQUEST_MAPPER)
                .stream();
    }

    @Override
    public Optional<Request> create(long ownerId, long petId, int status, String language) {

        final Map<String, Object> values = new HashMap<>();
        values.put("ownerId", ownerId);
        values.put("petId", petId);
        values.put("status", status);
        final Number key = jdbcInsert.executeAndReturnKey(values);
        return findById(key.longValue(), language);

    }

    @Override
    public Optional<Request> updateStatus(long id, String status, String language) {
        int newStatus = 1;
        if (status.contains("accepted")) {
            newStatus = 2;
        }
        if (status.contains("rejected")) {
            newStatus = 3;
        }
        jdbcTemplate.update("UPDATE requests " +
                "SET status = ? " +
                "WHERE id = ? ", new Object[]{newStatus, id});
        return findById(id, language);
    }

    public Optional<Request> getRequestByOwnerAndPetId(long ownerId, long petId, String language) {
        return jdbcTemplate.query("SELECT requests.id as id,  requests.ownerId as ownerId, users.username as ownerUsername, petId,  " +
                        "creationDate, status.id as statusId , status." + language + " as statusName, pets.petname as petName " +
                        "FROM (((requests inner join status on requests.status = status.id) inner join users on requests.ownerid = users.id)inner join pets on pets.id = requests.petId) " +
                        "WHERE requests.ownerId = ? AND petId = ?"
                , new Object[]{ownerId, petId}, REQUEST_MAPPER)
                .stream().findFirst();
    }

    @Override
    public Stream<Request> filterListByOwner(String language, long ownerId, String status, String searchCriteria, String searchOrder) {
        //USERNAME???
        String userIdFilter = "requests.ownerId";
        return filterList(language,userIdFilter,ownerId,status,searchCriteria,searchOrder);
    }

    @Override
    public Stream<Request> filterListByPetOwner(String language, long petOwnerId, String status, String searchCriteria, String searchOrder) {
        String userIdFilter = "pets.ownerId";
        return filterList(language,userIdFilter,petOwnerId,status,searchCriteria,searchOrder);
    }

    public Stream<Request> filterList (String language, String userIdFilter, long userId, String status, String searchCriteria, String searchOrder){
        Stream<Request> result;
        if (status == null) {
            status = "%";
        }
        String sql = "SELECT requests.id as id,  requests.ownerId as ownerId, users.username as ownerUsername, petId, " +
                "creationDate, status.id as statusId , status." + language + " as statusName, pets.petname as petName " +
                "FROM (((requests inner join status on requests.status = status.id) inner join users on requests.ownerid = users.id)inner join pets on pets.id = requests.petId) " +
                "WHERE " + userIdFilter +" = ? AND status." + language + " = ? ";
        if (searchCriteria == null) {
            result = jdbcTemplate.query(sql, new Object[]{userId, status}, REQUEST_MAPPER).stream();
        } else {
            if (searchCriteria.contains("date")) {
                searchCriteria = "requests.creationDate";
            }
            if (searchCriteria.contains("petName")) {
                searchCriteria = "pets.petName";
            }
            if (searchOrder.contains("asc")) {
                searchOrder = "ASC";
            } else {
                searchOrder = "DESC";
            }
            searchCriteria = searchCriteria + " " + searchOrder;

            result = jdbcTemplate.query(sql + "ORDER BY " + searchCriteria, new Object[]{}, REQUEST_MAPPER).stream();
        }

        return result;
    }

    }

