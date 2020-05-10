package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.interfaces.exception.DuplicateUserException;
import ar.edu.itba.paw.models.*;

import ar.edu.itba.paw.persistence.mappers.PetMapExtractor;
import ar.edu.itba.paw.persistence.mappers.UserMapExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

import java.util.*;
import java.util.stream.Stream;

@Repository
public class UserDaoImpl implements UserDao {

    private static final int ADMIN_SHOWCASE_ITEMS= 25;

    private static final String USER_TABLE = "users";
    private static final String TOKEN_TABLE = "tokens";

    private static final String DUPLICATE_USERNAME_ERROR = "Duplicate key exception: username already exists";
    private static final String DUPLICATE_MAIL_ERROR = "Duplicate key exception: mail already exists";

    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final SimpleJdbcInsert jdbcInsertToken;

//    private static final RowMapper<User> USER_MAPPER = (rs, rowNum) -> new User(
//            rs.getLong("id"),
//            rs.getString("username"),
//            rs.getString("password"),
//            rs.getString("mail"),
//            rs.getString("phone"),
//            new Status(rs.getInt("status"),rs.getString("statusName"))
//    );

    private static final RowMapper<Token> TOKEN_MAPPER = (rs, rowNum) -> new Token(
            rs.getLong("id"),
            (UUID)rs.getObject("token"),
            rs.getLong("userId"),
            rs.getDate("expirationDate"));

    @Autowired
    public UserDaoImpl(final DataSource dataSource) {

        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(USER_TABLE)
                .usingGeneratedKeyColumns("id");
        jdbcInsertToken = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(TOKEN_TABLE)
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Optional<User> findById(String language, long id) {
        String sql = "SELECT users.id AS id, username, password, mail, phone, users.status AS statusId, user_status." + language + " AS statusName, " +
                        "requests.id AS requestId, requests.creationDate AS requestCreationDate, requests.status AS requestStatusId, request_status." + language + " AS requestStatusName, " +
                        "pets.id AS petId, pets.petName as petName " +
                     "FROM (users INNER JOIN user_status ON users.status = user_status.id) LEFT JOIN ( " +
                        "(requests INNER JOIN request_status ON requests.status = request_status.id) INNER JOIN pets ON requests.petId = pets.id) " +
                        "ON users.id = requests.ownerId " +
                     "WHERE users.id = ?";

        Map<User, List<Request>> requestMap = jdbcTemplate.query(sql, new Object[] {id}, new UserMapExtractor());
        requestMap.forEach(User::setRequestList);
        return requestMap.keySet().stream().findFirst();
    }

    @Override
    public Optional<User> findByUsername(String language, String username) {
        String sql = "SELECT users.id AS id, username, password, mail, phone, users.status AS statusId, user_status." + language + " AS statusName, " +
                "requests.id AS requestId, requests.creationDate AS requestCreationDate, requests.status AS requestStatusId, request_status." + language + " AS requestStatusName, " +
                "pets.id AS petId, pets.petName as petName " +
                "FROM (users INNER JOIN user_status ON users.status = user_status.id) LEFT JOIN ( " +
                "(requests INNER JOIN request_status ON requests.status = request_status.id) INNER JOIN pets ON requests.petId = pets.id) " +
                "ON users.id = requests.ownerId " +
                "WHERE users.username = ?";

        Map<User, List<Request>> requestMap = jdbcTemplate.query(sql, new Object[] {username}, new UserMapExtractor());
        requestMap.forEach(User::setRequestList);

        return requestMap.keySet().stream().findFirst();
    }

    @Override
    public Optional<User> findByMail(String language, String mail) {
        String sql = "SELECT users.id AS id, username, password, mail, phone, users.status AS statusId, user_status." + language + " AS statusName, " +
                "requests.id AS requestId, requests.creationDate AS requestCreationDate, requests.status AS requestStatusId, request_status." + language + " AS requestStatusName, " +
                "pets.id AS petId, pets.petName as petName " +
                "FROM (users INNER JOIN user_status ON users.status = user_status.id) LEFT JOIN ( " +
                "(requests INNER JOIN request_status ON requests.status = request_status.id) INNER JOIN pets ON requests.petId = pets.id) " +
                "ON users.id = requests.ownerId " +
                "WHERE users.mail = ?";

        Map<User, List<Request>> requestMap = jdbcTemplate.query(sql, new Object[] {mail}, new UserMapExtractor());
        requestMap.forEach(User::setRequestList);
        return requestMap.keySet().stream().findFirst();
    }

    @Override
    public boolean updatePassword(String newPassword, long id) {
        return jdbcTemplate.update("UPDATE users SET password = ? WHERE id = ?", new Object[] {newPassword, id}) == 1;
    }

    @Override
    public boolean createToken(UUID uuid, long userId, Date expirationDate) {
        final Map<String, Object> values = new HashMap<>();
        values.put("token", uuid);
        values.put("userId", userId);
        values.put("expirationDate", expirationDate);
        return jdbcInsertToken.executeAndReturnKey(values).intValue() > 0 ;

    }
    @Override
    public Optional<Token> getToken(UUID uuid) {
        return jdbcTemplate.query("SELECT * FROM tokens WHERE token = ? ", new Object[] {uuid},TOKEN_MAPPER)
                .stream().findFirst();
    }

    @Override
    public boolean deleteToken(UUID uuid) {
        return jdbcTemplate.update("DELETE FROM tokens WHERE token = ?", new Object[] {uuid}) == 1;
    }

    @Override
    public Optional<User> findByToken(String language, UUID uuid) {
        String sql = "SELECT users.id AS id, username, password, mail, phone, users.status AS statusId, user_status." + language + " AS statusName, " +
                "requests.id AS requestId, requests.creationDate AS requestCreationDate, requests.status AS requestStatusId, request_status." + language + " AS requestStatusName, " +
                "pets.id AS petId, pets.petName as petName " +
                "FROM ((users INNER JOIN user_status ON users.status = user_status.id) LEFT JOIN ( " +
                "(requests INNER JOIN request_status ON requests.status = request_status.id) INNER JOIN pets ON requests.petId = pets.id) " +
                "ON users.id = requests.ownerId) INNER JOIN tokens ON users.id = tokens.userId " +
                "WHERE tokens.token = ?";

        Map<User, List<Request>> requestMap = jdbcTemplate.query(sql, new Object[] {uuid}, new UserMapExtractor());
        requestMap.forEach(User::setRequestList);
        return requestMap.keySet().stream().findFirst();
    }

    @Override
    public Stream<User> list(String language) {
        String sql = "SELECT users.id AS id, username, password, mail, phone, users.status AS statusId, user_status." + language + " AS statusName, " +
                "requests.id AS requestId, requests.creationDate AS requestCreationDate, requests.status AS requestStatusId, request_status." + language + " AS requestStatusName, " +
                "pets.id AS petId, pets.petName as petName " +
                "FROM (users INNER JOIN user_status ON users.status = user_status.id) LEFT JOIN ( " +
                "(requests INNER JOIN request_status ON requests.status = request_status.id) INNER JOIN pets ON requests.petId = pets.id) " +
                "ON users.id = requests.ownerId";

        Map<User, List<Request>> requestMap = jdbcTemplate.query(sql, new UserMapExtractor());
        requestMap.forEach(User::setRequestList);
        return requestMap.keySet().stream();
    }

    @Override
    public Stream<User> adminUserList(String language, String page){
        String offset = Integer.toString(ADMIN_SHOWCASE_ITEMS*(Integer.parseInt(page)-1));

        String sql = "SELECT users.id AS id, username, password, mail, phone, users.status AS statusId, user_status." + language + " AS statusName, " +
                "requests.id AS requestId, requests.creationDate AS requestCreationDate, requests.status AS requestStatusId, request_status." + language + " AS requestStatusName, " +
                "pets.id AS petId, pets.petName as petName " +
                "FROM (users INNER JOIN user_status ON users.status = user_status.id) LEFT JOIN ( " +
                "(requests INNER JOIN request_status ON requests.status = request_status.id) INNER JOIN pets ON requests.petId = pets.id) " +
                "ON users.id = requests.ownerId " +
                "WHERE users.id = ? " +
                "limit " + ADMIN_SHOWCASE_ITEMS + " offset " + offset;

        Map<User, List<Request>> requestMap = jdbcTemplate.query(sql, new UserMapExtractor());
        requestMap.forEach(User::setRequestList);
        return requestMap.keySet().stream();
    }

    @Override
    public Stream<User> adminSearchList(String language, String findValue, String page) {
        if(findValue.equals("")){
            return adminUserList(language, page);
        }

        String modifiedValue = "%" + findValue.toLowerCase() + "%";
        String offset = Integer.toString(ADMIN_SHOWCASE_ITEMS*(Integer.parseInt(page)-1));

        String sql = "SELECT users.id AS id, username, password, mail, phone, users.status AS statusId, user_status." + language + " AS statusName, " +
                "requests.id AS requestId, requests.creationDate AS requestCreationDate, requests.status AS requestStatusId, request_status." + language + " AS requestStatusName, " +
                "pets.id AS petId, pets.petName as petName " +
                "FROM (users INNER JOIN user_status ON users.status = user_status.id) LEFT JOIN ( " +
                "(requests INNER JOIN request_status ON requests.status = request_status.id) INNER JOIN pets ON requests.petId = pets.id) " +
                "ON users.id = requests.ownerId " +
                "WHERE (LOWER(username) LIKE ? ) OR (LOWER(mail) LIKE ? ) OR (LOWER(phone) LIKE ? ) " +
                "limit " + ADMIN_SHOWCASE_ITEMS + " offset " + offset;

        Map<User, List<Request>> requestMap = jdbcTemplate.query(sql, new Object[] {modifiedValue, modifiedValue, modifiedValue},
                new UserMapExtractor());
        requestMap.forEach(User::setRequestList);
        return requestMap.keySet().stream();
    }

    @Override
    public String getAdminPages(){
        Integer users = jdbcTemplate.queryForObject("select count(*) from users" ,
                Integer.class);

        users = (int) Math.ceil((double) users / ADMIN_SHOWCASE_ITEMS);
        return users.toString();
    }

    @Override
    public String getAdminSearchPages(String language, String findValue) {
        if(findValue.equals("")){
            return getAdminPages();
        }

        String modifiedValue = "%"+findValue.toLowerCase()+"%";

        Integer users = jdbcTemplate.queryForObject("select count(*) from users " +
                        "WHERE (LOWER(username) LIKE ? ) OR (LOWER(mail) LIKE ? ) OR (LOWER(phone) LIKE ? ) " ,
                new Object[] { modifiedValue ,modifiedValue,modifiedValue},
                Integer.class);

        users = (int) Math.ceil((double) users / ADMIN_SHOWCASE_ITEMS);
        return users.toString();
    }

    @Override
    public boolean updateStatus(long id, int status) {
        String sql = "UPDATE users " +
                "SET status = ? " +
                "WHERE id = ? ";
        return jdbcTemplate.update(sql, status, id) == 1;
    }

    @Override
    public boolean isAdmin(long userId) {
        return jdbcTemplate.queryForObject("select count(*) from admins " +
                        "WHERE id = ?" ,
                new Object[] { userId},
                Integer.class) == 1;
    }

    @Override
    public Optional<User> create(String language, String username, String password, String mail, String phone, int status) throws DuplicateUserException {
        final Map<String, Object> values = new HashMap<>();
        values.put("username", username);
        values.put("password", password);
        values.put("mail", mail);
        values.put("phone", phone);
        values.put("status", status);
        Number key;

        try {
            key = jdbcInsert.executeAndReturnKey(values);
        } catch (DuplicateKeyException ex) {
            if (ex.getMessage().contains("users_username_key")) throw new DuplicateUserException(DUPLICATE_USERNAME_ERROR, true, false);
            if (ex.getMessage().contains("users_mail_key")) throw new DuplicateUserException(DUPLICATE_MAIL_ERROR, false, true);
            return Optional.empty();
        }

        return findById(language, key.longValue());
    }
}

