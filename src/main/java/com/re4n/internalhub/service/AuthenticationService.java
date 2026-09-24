package com.re4n.internalhub.service;

import com.re4n.internalhub.dao.UserDAO;
import com.re4n.internalhub.enums.AppError;
import com.re4n.internalhub.exception.AppException;
import com.re4n.internalhub.model.User;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class AuthenticationService {
    private static final Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
    private final UserDAO userDAO;
    public static final int ITERATIONS = 2;
    public static final int MEMORY_KB = 19456;
    public static final int PARALLELISM = 1;

    public AuthenticationService(UserDAO userDAO){
        this.userDAO = userDAO;
    }

    public String hashPassword(String plainPassword){
        if(plainPassword == null || plainPassword.isBlank() || plainPassword.length() < 12){
            throw new AppException(AppError.VALIDATION_FAILED, null);
        }
        return argon2.hash(ITERATIONS, MEMORY_KB, PARALLELISM, plainPassword);
    }

    public boolean verifyPassword(String plainPassword, String hash){
        if(plainPassword == null || plainPassword.isBlank()){
            return false;
        }
        if(hash == null || hash.isBlank()){
           return false;
        }
        return argon2.verify(hash, plainPassword);
    }

    public User login(String email, String plainPassword){
        User user = userDAO.findByEmail(email);
        if(user == null){
            throw new AppException(AppError.INVALID_CREDENTIALS, null);
        }
        if(!user.getActive()){
            throw new AppException(AppError.INVALID_CREDENTIALS,null);
        }
        if(!verifyPassword(plainPassword, user.getPasswordHash())){
            throw new AppException(AppError.INVALID_CREDENTIALS, null);
        }
        return  user;
    }
}
