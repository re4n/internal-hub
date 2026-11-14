package com.re4n.internalhub.service;

import com.re4n.internalhub.dao.RoleDAO;
import com.re4n.internalhub.model.Role;

public class RoleDAOStub extends RoleDAO {
    private Role roleToReturn;

    public RoleDAOStub(Role roleToReturn) {
        this.roleToReturn = roleToReturn;
    }

    @Override
    public Role findById(Long id) {
        return roleToReturn;
    }
}
