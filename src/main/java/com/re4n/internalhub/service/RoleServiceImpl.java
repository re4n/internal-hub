package com.re4n.internalhub.service;

import com.re4n.internalhub.dao.RoleDAO;
import com.re4n.internalhub.dto.RoleResult;
import com.re4n.internalhub.enums.AppError;
import com.re4n.internalhub.exception.AppException;
import com.re4n.internalhub.model.Role;
import com.re4n.internalhub.model.User;

import java.util.List;

public class RoleServiceImpl implements RoleService {
    private final RoleDAO roleDAO;
    private final AuthorizationService authorizationService;

    public RoleServiceImpl(RoleDAO roleDAO, AuthorizationService authorizationService){
        this.roleDAO = roleDAO;
        this.authorizationService = authorizationService;
    }

    @Override
    public RoleResult createRole(User actor, Role newRole){
        if(!authorizationService.canManageRole(actor)){
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
            return new RoleResult(
                    newRole.getRoleName(),
                    newRole.getSeniorityLevel().name(),
                    newRole.getDescription()
            );
        }
    @Override
    public void deleteRole(User actor, Long roleId){
            if(!authorizationService.canManageRole(actor)){
                throw new AppException(AppError.AUTHORIZATION_DENIED, null);
            }
            Role target = roleDAO.findById(roleId);
            if(target == null){
                throw new AppException(AppError.RESOURCE_NOT_FOUND, null);
            }
            roleDAO.delete(roleId);
        }

    @Override
    public Role findRole(User actor, Long targetId){
        if(!authorizationService.canReadRole(actor)){
            throw new AppException(AppError.AUTHORIZATION_DENIED, null);
        }
        Role target = roleDAO.findById(targetId);
        if(target == null){
            throw new AppException(AppError.RESOURCE_NOT_FOUND, null);
        }
        return target;
    }

    @Override
    public List<Role> findAllRoles(User actor){
        if(!authorizationService.canReadRole(actor)) {
            throw new AppException(AppError.AUTHORIZATION_DENIED, null);
        }
        return roleDAO.findAll();
    }

    @Override
    public Role updateRole(User actor, Role role){
        if(!authorizationService.canManageRole(actor)){
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
