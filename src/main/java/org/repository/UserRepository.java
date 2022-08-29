package org.repository;

import org.entity.User;

public interface UserRepository {
    void addUser(User user);
    Boolean deleteUser(String userName);
    User getUser(String userName);
    Boolean userExists(String userName);
}
