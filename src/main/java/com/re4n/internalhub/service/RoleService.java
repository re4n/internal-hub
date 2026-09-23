package com.re4n.internalhub.service;

import com.re4n.internalhub.dao.RoleDAO;
import com.re4n.internalhub.dao.UserDAO;
import com.re4n.internalhub.enums.AppError;
import com.re4n.internalhub.exception.AppException;
import com.re4n.internalhub.model.Role;
import com.re4n.internalhub.model.User;

import java.util.List;

public class RoleService {
    private final RoleDAO roleDAO;
    private final AuthorizationService authService;

    public RoleService(RoleDAO roleDAO, AuthorizationService authService){
        this.roleDAO = roleDAO;
        this.authService = authService;
    }

    public Role createRole(User actor, Role newRole){
        if(!authService.canManageRole(actor)){
           throw new AppException(AppError.AUTHORIZATION_DENIED, null);
        }

        if(newRole.getRoleName() == null || newRole.getRoleName().isBlank()
                || newRole.getRoleType() == null
                || newRole.getMinSalary() == null
                || newRole.getMaxSalary() == null
                || newRole.getMinSalary().compareTo(newRole.getMaxSalary()) > 0
                || newRole.getDescription() == null){
            throw new AppException(AppError.VALIDATION_FAILED, null);
        }
            roleDAO.save(newRole);
            return newRole;
        }

    public Role findRole(User actor, Long targetId){
        if(!authService.canReadRole(actor)){
            throw new AppException(AppError.AUTHORIZATION_DENIED, null);
        }
        Role target = roleDAO.findById(targetId);
        if(target == null){
            throw new AppException(AppError.RESOURCE_NOT_FOUND, null);
        }
        return target;
    }

    public List<Role> findAllRoles(User actor){
        if(!authService.canReadRole(actor)) {
            throw new AppException(AppError.AUTHORIZATION_DENIED, null);
        }
        return roleDAO.findAll();
    }

    public Role updateRole(User actor, Role role){
        if(!authService.canManageRole(actor)){
            throw new AppException(AppError.AUTHORIZATION_DENIED, null);
        }
        if(role.getRoleName() == null || role.getRoleName().isBlank()
                || role.getRoleType() == null
                || role.getMinSalary() == null
                || role.getMaxSalary() == null
                || role.getMinSalary().compareTo(role.getMaxSalary()) > 0
                || role.getDescription() == null){
            throw new AppException(AppError.VALIDATION_FAILED, null);
        }
        roleDAO.update(role);
        return role;
    }
}
