package org.service;

import lombok.NonNull;

import java.util.Set;

public interface AuthService {
    public void createUser(@NonNull String userName, @NonNull String password);

    public void deleteUser(@NonNull String userName);

    public void createRole(@NonNull String roleName);

    public void deleteRole(@NonNull String roleName);

    public void addRoleToUser(@NonNull String userName, @NonNull String roleName);

    public String authenticate(@NonNull String userName, @NonNull String password);

    public void invalidate(@NonNull String tokenName);

    public Boolean checkRole(@NonNull String tokenName, @NonNull String roleName);

    public Set<String> allRoles(@NonNull String tokenName);

    void setTokenExpireSecond(long seconds);
}
