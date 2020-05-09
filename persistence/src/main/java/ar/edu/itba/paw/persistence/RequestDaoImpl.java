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

    private static final int ADMIN_SHOWCASE_ITEMS= 25;

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
                        "creationDate, request_status.id as statusId , request_status." + language + " as statusName, pets.petname as petName " +
                        "FROM (((requests inner join request_status on requests.status = request_status.id) inner join users on requests.ownerid = users.id)inner join pets on pets.id = requests.petId) " +
                        "WHERE requests.id = ? "
                , new Object[]{id}, REQUEST_MAPPER)
                .stream().findFirst();
    }

    @Override
    public Stream<Request> listByOwner(String language, long ownerId) {
        return jdbcTemplate.query("SELECT requests.id as id,  requests.ownerId as ownerId, users.username as ownerUsername, petId, " +
                        "creationDate, request_status.id as statusId , request_status." + language + " as statusName, pets.petname as petName " +
                        "FROM (((requests inner join request_status on requests.status = request_status.id) inner join users on requests.ownerid = users.id)inner join pets on pets.id = requests.petId) " +
                        "WHERE requests.ownerId = ? "
                , new Object[]{ownerId}, REQUEST_MAPPER)
                .stream();
    }

    @Override
    public Stream<Request> listByPetOwner(String language, long petOwnerId) {
        return jdbcTemplate.query("SELECT requests.id as id,  requests.ownerId as ownerId, users.username as ownerUsername, petId, " +
                        "creationDate, request_status.id as statusId , request_status." + language + " as statusName, pets.petname as petName " +
                        "FROM (((requests inner join request_status on requests.status = request_status.id) inner join users on requests.ownerid = users.id)inner join pets on pets.id = requests.petId) " +
                        "WHERE pets.ownerId = ?"
                , new Object[]{petOwnerId}, REQUEST_MAPPER)
                .stream();
    }

    @Override
    public Stream<Request> adminRequestList(String language, String page){
        int numValue = 1;
        try {
            numValue = Integer.parseInt(page);
        } catch (NumberFormatException ignored) {
        }

        String offset = Integer.toString(ADMIN_SHOWCASE_ITEMS*(numValue-1));
        return jdbcTemplate.query("SELECT requests.id as id,  requests.ownerId as ownerId, users.username as ownerUsername, petId, " +
                "creationDate, request_status.id as statusId , request_status." + language + " as statusName, pets.petname as petName " +
                "FROM (((requests inner join request_status on requests.status = request_status.id) inner join users on requests.ownerid = users.id)inner join pets on pets.id = requests.petId) " +
                " limit " + ADMIN_SHOWCASE_ITEMS + " offset " + offset, REQUEST_MAPPER)
                .stream();
    }

    @Override
    public Stream<Request> adminSearchList(String language, String findValue, String page) {
        int numValue = 1;
        try {
            numValue = Integer.parseInt(page);
        } catch (NumberFormatException ignored) {
        }

        String offset = Integer.toString(ADMIN_SHOWCASE_ITEMS*(numValue-1));
        if(findValue.equals("")){
            return adminRequestList(language, page);
        }

        String modifiedValue = "%"+findValue.toLowerCase()+"%";
        return jdbcTemplate.query("SELECT requests.id as id,  requests.ownerId as ownerId, users.username as ownerUsername, petId, " +
                "creationDate, request_status.id as statusId , request_status." + language + " as statusName, pets.petname as petName " +
                "FROM (((requests inner join request_status on requests.status = request_status.id) inner join users on requests.ownerid = users.id)inner join pets on pets.id = requests.petId) " +
                " WHERE  (LOWER(request_status."+ language +") LIKE ?) OR" +
                "(LOWER(users.username) LIKE ?) OR (LOWER(pets.petname) LIKE ?)" +
                " limit " + ADMIN_SHOWCASE_ITEMS + " offset " + offset,
                new Object[] { modifiedValue ,modifiedValue, modifiedValue},
                REQUEST_MAPPER)
                .stream();

    }

    @Override
    public Stream<Request> adminFilteredList(String language, String status, String searchCriteria, String searchOrder, String page) {
        int numValue = 1;
        try {
            numValue = Integer.parseInt(page);
        } catch (NumberFormatException ignored) {
        }

        if (status == null) {
            status = "(1,2,3,4)";
        } else if (status.equals("pending")) {
            status = "(1)";
        } else if (status.equals("accepted")) {
            status = "(2)";
        } else if (status.equals("rejected")) {
            status = "(3)";
        } else if (status.equals("canceled")) {
            status = "(4)";
        } else{
            status = "(100)";
        }

        Stream<Request> result;

        String offset = Integer.toString(ADMIN_SHOWCASE_ITEMS * (numValue - 1));
        String limit = " limit " + ADMIN_SHOWCASE_ITEMS + " offset " + offset;

        String sql = "SELECT requests.id as id,  requests.ownerId as ownerId, users.username as ownerUsername, petId, " +
                "creationDate, request_status.id as statusId , request_status." + language + " as statusName, pets.petname as petName " +
                "FROM (((requests inner join request_status on requests.status = request_status.id) inner join users on requests.ownerid = users.id)inner join pets on pets.id = requests.petId) " +
                "WHERE  requests.status IN " + status + limit;
        if (searchCriteria == null) {
            result = jdbcTemplate.query(sql, REQUEST_MAPPER).stream();
        } else {
            if (searchCriteria.contains("date")) {
                searchCriteria = "requests.creationDate";
            }
            else if (searchCriteria.contains("petName")) {
                searchCriteria = "pets.petName";
            }
            else { /* Default criteria */
                searchCriteria = "requests.creationDate";
            }
            if (searchOrder == null || searchOrder.toLowerCase().contains("asc")) {
                searchOrder = "ASC";
            } else {
                searchOrder = "DESC";
            }
            searchCriteria = searchCriteria + " " + searchOrder;
            sql = sql + " ORDER BY " + searchCriteria;

            result = jdbcTemplate.query(sql ,  REQUEST_MAPPER).stream();
        }

        return result;

    }

    @Override
    public Stream<Long> findIdByStatus(long petId, long ownerId, List<Integer> statusList) {
        if (statusList.isEmpty()) {
            return Stream.empty();
        }
        StringBuilder status = new StringBuilder().append(" (").append(statusList.get(0));
        statusList.remove(0);
        for (Integer statusId : statusList) {
            status.append(",").append(statusId);
        }
        status.append(") ");

        String sql = "SELECT id " +
                     "FROM requests " +
                     "WHERE petId = ? AND ownerId = ? AND status IN " + status;

        return jdbcTemplate.query(sql, new Object[] {petId, ownerId}, (rs, rowNum) -> new Long(rs.getLong("id"))).stream();
    }

    @Override
    public void updateAllByOwner(long ownerId, int oldStatus, int newStatus) {

        System.out.println("\n\n\n\n"+ownerId+"oldstatus: "+oldStatus+"new status "+newStatus+"\n\n\n\n\n");
        String sql = "UPDATE requests " +
                "SET status = ? " +
                "WHERE status = ? AND ownerId = ? ";
        jdbcTemplate.update(sql, newStatus, oldStatus, ownerId);
    }

    @Override
    public void updateAllByPetOwner(long petOwnerId, int oldStatus, int newStatus) {
        String sql = "UPDATE requests " +
                "SET status = ? " +
                "FROM pets " +
                "WHERE pets.id = requests.petId AND requests.status = ? AND pets.ownerId = ? ";
        jdbcTemplate.update(sql, newStatus, oldStatus, petOwnerId);
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
    public void updateStatus(long id, long newStatus) {
        String sql = "UPDATE requests " +
                "SET status = ? " +
                "WHERE id = ? ";
        jdbcTemplate.update(sql, newStatus, id);
    }

    public Optional<Request> getRequestByOwnerAndPetId(long ownerId, long petId, String language) {
        return jdbcTemplate.query("SELECT requests.id as id,  requests.ownerId as ownerId, users.username as ownerUsername, petId,  " +
                        "creationDate, request_status.id as statusId , request_status." + language + " as statusName, pets.petname as petName " +
                        "FROM (((requests inner join request_status on requests.status = request_status.id) inner join users on requests.ownerid = users.id)inner join pets on pets.id = requests.petId) " +
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

    private Stream<Request> filterList (String language, String userIdFilter, long userId, String status, String searchCriteria, String searchOrder){
        Stream<Request> result;
        if (status == null) {
            status = "%";
        }
        else if(status.contains("accepted")){
            status = "Accepted";
        }
        else if(status.contains("pending")){
            status = "Pending";
        }
        else {
            status = "Rejected";
        }
        String sql = "SELECT requests.id as id,  requests.ownerId as ownerId, users.username as ownerUsername, petId, " +
                "creationDate, request_status.id as statusId , request_status." + language + " as statusName, pets.petname as petName " +
                "FROM (((requests inner join request_status on requests.status = request_status.id) inner join users on requests.ownerid = users.id)inner join pets on pets.id = requests.petId) " +
                "WHERE " + userIdFilter +" = ? AND request_status.en_US  LIKE ? ";
        if (searchCriteria == null) {
            result = jdbcTemplate.query(sql, new Object[]{userId, status}, REQUEST_MAPPER).stream();
        } else {
            if (searchCriteria.contains("date")) {
                searchCriteria = "requests.creationDate";
            }
            else if (searchCriteria.contains("petName")) {
                searchCriteria = "pets.petName";
            }
            else { /* Default criteria */
                searchCriteria = "requests.creationDate";
            }
            if (searchOrder == null || searchOrder.toLowerCase().contains("asc")) {
                searchOrder = "ASC";
            } else {
                searchOrder = "DESC";
            }
            searchCriteria = searchCriteria + " " + searchOrder;
            sql = sql + "ORDER BY " + searchCriteria;

            result = jdbcTemplate.query(sql , new Object[]{userId, status}, REQUEST_MAPPER).stream();
        }

        return result;
    }

    @Override
    public String getAdminRequestPages(String language){
        Integer requests = jdbcTemplate.queryForObject("select count(*) from requests" ,
                Integer.class);

        requests = (int) Math.ceil((double) requests / ADMIN_SHOWCASE_ITEMS);
        return requests.toString();
    }

    @Override
    public String getAdminMaxSearchPages(String language, String findValue){
            if (findValue.equals("")) {
                return getAdminRequestPages(language);
            }

            String modifiedValue = "%" + findValue.toLowerCase() + "%";

            Integer requests = jdbcTemplate.queryForObject("select count(*)  " +
                            "FROM (((requests inner join request_status on requests.status = request_status.id) inner join users on requests.ownerid = users.id) inner join pets on pets.id = requests.petId)" +
                            " WHERE  (LOWER(request_status." + language + ") LIKE ?) OR" +
                            "(LOWER(users.username) LIKE ?) OR (LOWER(pets.petname) LIKE ?)",
                    new Object[]{modifiedValue, modifiedValue, modifiedValue},
                    Integer.class);
            requests = (int) Math.ceil((double) requests / ADMIN_SHOWCASE_ITEMS);
            return requests.toString();
    }

    @Override
    public String getAdminMaxFilterPages(String language, String status) {
        if (status == null) {
            status = "(1,2,3,4)";
        } else if (status.equals("pending")) {
            status = "(1)";
        } else if (status.equals("accepted")) {
            status = "(2)";
        } else if (status.equals("rejected")) {
            status = "(3)";
        } else if (status.equals("canceled")) {
            status = "(4)";
        } else{
            status = "(100)";
        }

        Integer requests = jdbcTemplate.queryForObject("select count(*) " +
                        "FROM (((requests inner join request_status on requests.status = request_status.id) " +
                        "inner join users on requests.ownerid = users.id) inner join pets on pets.id = requests.petId) " +
                        "WHERE requests.status IN " + status,
                Integer.class);

        requests = (int) Math.ceil((double) requests / ADMIN_SHOWCASE_ITEMS);
        return requests.toString();
    }

    @Override
    public boolean isRequestOwner(long id, long userId) {
        String sql = "SELECT COUNT(id) " +
                "FROM requests " +
                "WHERE id = ? AND ownerId = ? ";
        Integer owner = jdbcTemplate.queryForObject(sql, new Object[] {id, userId}, Integer.class);
        return owner == 1;
    }

    @Override
    public boolean isRequestTarget(long id, long userId) {
        String sql = "SELECT COUNT(pets.ownerid) " +
                "FROM requests inner join pets on requests.petId = pets.id " +
                "WHERE requests.id = ? AND pets.ownerId = ?";
        Integer owner = jdbcTemplate.queryForObject(sql, new Object[] {id, userId}, Integer.class);
        return owner == 1;
    }
}

