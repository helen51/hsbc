package org.repository.impl;

import org.entity.User;
import org.repository.UserRepository;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class UserRepositoryImpl implements UserRepository {
    // user table in DB is userName as key and User as value
    private Map<String, User> userTable;

    public UserRepositoryImpl() {
        userTable = new ConcurrentHashMap();
    }

    @Override
    public void addUser(User user) {
        userTable.put(user.getUserName(), user);
    }

    @Override
    public Boolean deleteUser(String userName) {
        if(userExists(userName)) {
            userTable.remove(userName);
            return true;
        }
        return false;
    }

    @Override
    public User getUser(String userName) {
        return userTable.get(userName);
    }

    @Override
    public Boolean userExists(String userName) {
        if(Objects.isNull(userName)) return false;
        return userTable.containsKey(userName);
    }
}
