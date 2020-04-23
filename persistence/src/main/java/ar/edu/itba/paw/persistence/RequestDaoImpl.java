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
            rs.getString("ownerUsername"),
            new Status(rs.getInt("statusId"),rs.getString("statusName")),
            rs.getLong("petId"),
            rs.getDate("creationDate")
    );

    @Autowired
    public RequestDaoImpl(final DataSource dataSource) {

        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(REQUESTS_TABLE)
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Optional<Request> findById(long id, String language) {
        return jdbcTemplate.query("SELECT requests.id as id, users.username as ownerUsername, petId, creationDate, status.id as statusId , status."+ language +" as statusName " +
                        "FROM (requests inner join status on requests.status = status.id) inner join users on requests.ownerid = users.id " +
                        "WHERE requests.id = ? "
                , new Object[] {id}, REQUEST_MAPPER)
                .stream().findFirst();
    }

    @Override
    public Stream<Request> listByOwner(String language, long ownerId) {
        return jdbcTemplate.query("SELECT requests.id as id, users.username as ownerUsername, petId, creationDate, status.id as statusId, status."+ language +" as statusName " +
                        "FROM (requests inner join status on requests.status = status.id) inner join users on requests.ownerid = users.id " +
                        "WHERE requests.ownerId = ? "
                , new Object[] {ownerId}, REQUEST_MAPPER)
                .stream();
    }

    @Override
    public Stream<Request> listByPetOwner(String language, long petOwnerId) {
        return jdbcTemplate.query("SELECT requests.id as id, users.username as ownerUsername, petId, creationDate, status.id as statusId , status."+ language +" as statusName " +
                        "FROM (requests inner join status on requests.status = status.id) inner join users on requests.ownerid = users.id " +
                        "WHERE pets.ownerId = ?"
                , new Object[] {petOwnerId}, REQUEST_MAPPER)
                .stream();
    }

    @Override
    public Optional<Request> create(long ownerId, long petId, int status, String language) {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2020);
        cal.set(Calendar.MONTH, 2);
        cal.set(Calendar.DATE, 2);

        Date date = new java.sql.Date(cal.getTimeInMillis());
        final Map<String, Object> values = new HashMap<>();
        values.put("ownerId", ownerId);
        values.put("petId", petId);
        values.put("status", status);
        values.put("creationDate", date);
        final Number key = jdbcInsert.executeAndReturnKey(values);
        return findById(key.longValue(),language);

    }

    @Override
    public Optional<Request> updateStatus(long id, int status, String language) {

        jdbcTemplate.update("UPDATE requests " +
                "SET status = ? " +
                "WHERE id = ? ",new Object[] {status, id});
        return findById(id, language);
    }
}
