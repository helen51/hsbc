package org.repository.impl;

import org.entity.Role;
import org.repository.RoleRepository;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class RoleRepositoryImpl implements RoleRepository {
    // role table in DB is roleName as key and role as value
    private Map<String, Role> roleTable;

    public RoleRepositoryImpl() {
        roleTable = new ConcurrentHashMap();
    }

    @Override
    public void addRole(Role role) {
        roleTable.put(role.getRoleName(), role);
    }

    @Override
    public Boolean deleteRole(String roleName) {
        if(roleExists(roleName)) {
            roleTable.remove(roleName);
            return true;
        }
        return false;
    }

    @Override
    public Role getRole(String roleName) {
        return roleTable.get(roleName);
    }

    @Override
    public Boolean roleExists(String roleName) {
        if(Objects.isNull(roleName)) return false;
        return roleTable.containsKey(roleName);
    }
}
