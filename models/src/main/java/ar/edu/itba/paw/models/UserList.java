package ar.edu.itba.paw.models;

import java.util.AbstractList;
import java.util.List;
import java.util.TreeSet;

public class UserList extends AbstractList<User> {
    private final User[] userList;
    private final String maxPage;

    public UserList(List<User> userArray, String maxPage) {
        super();
        this.maxPage = maxPage;
        userList = userArray.toArray(new User[0]);
    }

    public User get(int index) {
        return userList[index];
    }

    public User set(int index, User user) {
        User oldValue = userList[index];
        userList[index] = user;
        return oldValue;
    }

    public int size() {
        return userList.length;
    }

    public String getMaxPage() {
        return maxPage;
    }
}
