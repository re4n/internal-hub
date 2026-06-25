package com.re4n.internalhub.service;

import com.re4n.internalhub.dao.RoleDAO;
import com.re4n.internalhub.enums.AppError;
import com.re4n.internalhub.exception.AppException;
import com.re4n.internalhub.model.Role;

public class RoleService {
    private final RoleDAO roleDAO = new RoleDAO();

    public void registerRole(Role role){
        if (role.getAccessLevel() < 1 || role.getAccessLevel() > 6){
            throw new AppException(AppError.VALIDATION_FAILED, null);
        }
        roleDAO.save(role);
    }

}
