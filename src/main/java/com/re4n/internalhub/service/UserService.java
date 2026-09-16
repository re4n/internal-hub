package com.re4n.internalhub.service;

import com.re4n.internalhub.dao.UserDAO;
import com.re4n.internalhub.enums.AppError;
import com.re4n.internalhub.exception.AppException;
import com.re4n.internalhub.model.User;
import com.re4n.internalhub.util.CredentialGenerator;

public class UserService {
    private static final int MAX_ATTEMPTS = 10;
    private final UserDAO userDAO;
    private final AuthorizationService authService;
    private final CredentialGenerator generator;

    public  UserService(UserDAO userDAO, AuthorizationService authService, CredentialGenerator generator){
        this.userDAO = userDAO;
        this.authService = authService;
        this.generator = generator;
    }

    public User createUser(User actor, User newUser){

        if (!authService.canCreateUser(actor)){
            throw new AppException(AppError.AUTHORIZATION_DENIED, null);
        }
        if(newUser.getFirstName() == null || newUser.getFirstName().isBlank()
                || newUser.getLastName() == null || newUser.getLastName().isBlank()
                || newUser.getPersonalEmail() == null
                || newUser.getDepartment() == null){
            throw new AppException(AppError.VALIDATION_FAILED, null);
        }

        for(int attempts=0; attempts < MAX_ATTEMPTS; attempts++){
            newUser.setEmployeeId(generator.genEmployeeId());
            newUser.setCorporateEmail(generator.genEmployeeCorporateEmail(newUser.getFirstName(), newUser.getLastName(), attempts));
            try {
                userDAO.save(newUser);
                return newUser;
            }catch (AppException e){
                if (e.getErrorType() == AppError.DUPLICATE_IDENTITY){
                    continue;
                }
                throw e;
            }
        }

        throw new AppException(AppError.DB_PERSISTENCE_VIOLATION, null);
    }

    public User findUser(User actor, Long targetId){
        User target = userDAO.findById(targetId);
        if (target == null){ throw new AppException(AppError.RESOURCE_NOT_FOUND, null); }

        if(!authService.canReadUser(actor, target)){
            throw new AppException(AppError.AUTHORIZATION_DENIED, null);
        }
            return target;

    }

    





}
