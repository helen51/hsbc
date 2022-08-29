package org.repository;

import org.entity.Role;

public interface RoleRepository {
    void addRole(Role role);
    Boolean deleteRole(String roleName);
    Role getRole(String roleName);
    Boolean roleExists(String roleName);
}
