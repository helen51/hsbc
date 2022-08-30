package org.entity;


import java.util.HashSet;
import java.util.Set;

public class User {
    private String userName;

    // Password in encrypted form
    private String password;
    private Set<String> roles;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.roles = new HashSet<>();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public void addRole(String roleName) {
        this.roles.add(roleName);
    }

}
