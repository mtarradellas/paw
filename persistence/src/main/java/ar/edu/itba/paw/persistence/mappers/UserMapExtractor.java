package ar.edu.itba.paw.persistence.mappers;

import ar.edu.itba.paw.models.*;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UserMapExtractor implements ResultSetExtractor<Map<User, List<Request>>> {
    @Override
    public Map<User, List<Request>> extractData(ResultSet rs) throws SQLException {
        Map<User, List<Request>> requestMap = new LinkedHashMap<>();
        while (rs.next()) {
            User user = new User(
                    rs.getLong("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("mail"),
                    new Status(rs.getInt("statusId"), rs.getString("statusName"))
            );
            Request request = null;
            if (rs.getLong("requestId") != 0) {
                request = new Request(
                        rs.getLong("requestId"),
                        rs.getLong("id"),
                        rs.getString("username"),
                        new Status(rs.getInt("requestStatusId"), rs.getString("requestStatusName")),
                        rs.getLong("petId"),
                        rs.getString("petName"),
                        rs.getDate("requestCreationDate")
                );
            }
            List<Request> requests = requestMap.get(user);
            if (requests == null) {
                List<Request> newRequests = new ArrayList<>();
                requestMap.put(user, newRequests);
                requests = requestMap.get(user);
            }
            if (request != null){
                requests.add(request);
            }
        }
        return requestMap;
    }
}
