package com.re4n.internalhub.service;

import com.re4n.internalhub.dao.RoleDAO;
import com.re4n.internalhub.dao.UserDAO;
import com.re4n.internalhub.enums.AppError;
import com.re4n.internalhub.exception.AppException;
import com.re4n.internalhub.model.Role;
import com.re4n.internalhub.model.User;
import com.re4n.internalhub.util.CredentialGenerator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private static final int MAX_ATTEMPTS = 10;
    private final UserDAO userDAO;
    private final RoleDAO roleDAO;
    private final AuthorizationService authService;
    private final CredentialGenerator generator;

    public UserService(UserDAO userDAO, RoleDAO roleDAO, AuthorizationService authService, CredentialGenerator generator) {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
        this.authService = authService;
        this.generator = generator;
    }

    public User createUser(User actor, User newUser) {
        if (!authService.canCreateUser(actor)) {
            throw new AppException(AppError.AUTHORIZATION_DENIED, null);
        }
        if (newUser.getFirstName() == null || newUser.getFirstName().isBlank()
                || newUser.getLastName() == null || newUser.getLastName().isBlank()
                || newUser.getPersonalEmail() == null
                || newUser.getDepartment() == null) {
            throw new AppException(AppError.VALIDATION_FAILED, null);
        }

        for (int attempts = 0; attempts < MAX_ATTEMPTS; attempts++) {
            newUser.setEmployeeId(generator.genEmployeeId());
            newUser.setCorporateEmail(generator.genEmployeeCorporateEmail(newUser.getFirstName(), newUser.getLastName(), attempts));
            try {
                userDAO.save(newUser);
                return newUser;
            } catch (AppException e) {
                if (e.getErrorType() == AppError.DUPLICATE_IDENTITY) {
                    continue;
                }
                throw e;
            }
        }

        throw new AppException(AppError.DB_PERSISTENCE_VIOLATION, null);
    }

    public User findUser(User actor, Long targetId) {
        User target = userDAO.findById(targetId);
        if (target == null) {
            throw new AppException(AppError.RESOURCE_NOT_FOUND, null);
        }

        if (!authService.canReadUser(actor, target)) {
            throw new AppException(AppError.AUTHORIZATION_DENIED, null);
        }
        return target;

    }

    public User disableUser(User actor, Long targetId) {
        User target = userDAO.findById(targetId);
        if (target == null){
            throw new AppException(AppError.RESOURCE_NOT_FOUND, null);
        }
        if (!authService.canDisableUser(actor, target)) {
            throw new AppException(AppError.AUTHORIZATION_DENIED, null);
        }
        target.setActive(false);
        userDAO.update(target);
        return target;
    }

    public User updateSalary(User actor, Long targetId, BigDecimal newSalary){
        User target = userDAO.findById(targetId);
        if(target == null){
            throw new AppException(AppError.RESOURCE_NOT_FOUND, null);
        }

        if (!authService.canUpdateSalary(actor, target)){
            throw new AppException(AppError.AUTHORIZATION_DENIED, null);
        }

        if(target.getRoleId() == null){
            throw new AppException(AppError.VALIDATION_FAILED, null);
        }

        Role role = roleDAO.findById(target.getRoleId());

        if(role == null){
            throw new AppException(AppError.RESOURCE_NOT_FOUND, null);
        }

        if(newSalary.compareTo(role.getMinSalary()) < 0 || newSalary.compareTo(role.getMaxSalary()) > 0){
            throw new AppException(AppError.VALIDATION_FAILED, null);
        }

        target.setSalary(newSalary);
        userDAO.update(target);
        return target;
    }

    public User assignRole(User actor, Long targetId, Long roleId){
        User target = userDAO.findById(targetId);
        if(target == null){
            throw new AppException(AppError.RESOURCE_NOT_FOUND, null);
        }

        Role role = roleDAO.findById(roleId);
        if(role == null){
            throw new AppException(AppError.RESOURCE_NOT_FOUND, null);
        }

        if(!authService.canAssignRole(actor, target, role.getRoleType())){
            throw new AppException(AppError.AUTHORIZATION_DENIED, null);
        }
        target.setRoleId(roleId);
        userDAO.update(target);
        return target;
    }

    public List<User> findAllUsers(User actor){
        List<User> allUsers = userDAO.findAll();
        List<User> visibleUsers = new ArrayList<>();

        for(User u: allUsers){
            if(authService.canReadUser(actor, u)){
                visibleUsers.add(u);
            }
        }
        return visibleUsers;
    }


}
