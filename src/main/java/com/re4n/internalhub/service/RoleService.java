package com.re4n.internalhub.service;

import com.re4n.internalhub.model.Role;
import com.re4n.internalhub.model.User;

import java.util.List;

public interface RoleService {
    Role createRole(User actor, Role newRole);
    void deleteRole(User actor, Long roleId);
    Role findRole(User actor, Long targetId);
    List<Role> findAllRoles(User actor);
    Role updateRole(User actor, Role role);
}
