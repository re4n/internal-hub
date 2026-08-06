package com.re4n.internalhub.service;

import com.re4n.internalhub.dao.UserDAO;
import com.re4n.internalhub.enums.AppError;
import com.re4n.internalhub.exception.AppException;
import com.re4n.internalhub.model.User;

public class UserService {
    private final UserDAO userDAO;
    private final AuthorizationService authService;

    public  UserService(UserDAO userDAO, AuthorizationService authService){
        this.userDAO = userDAO;
        this.authService = authService;
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
