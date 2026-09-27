package com.re4n.internalhub.service;

import com.re4n.internalhub.dao.UserDAO;
import com.re4n.internalhub.dto.AuthUserResult;
import com.re4n.internalhub.dto.NewUserResult;
import com.re4n.internalhub.enums.AppError;
import com.re4n.internalhub.exception.AppException;
import com.re4n.internalhub.model.User;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class AuthenticationServiceImpl implements AuthenticationService {
    private static final Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
    private final UserDAO userDAO;
    public static final int ITERATIONS = 2;
    public static final int MEMORY_KB = 19456;
    public static final int PARALLELISM = 1;

    public AuthenticationServiceImpl(UserDAO userDAO){
        this.userDAO = userDAO;
    }

    @Override
    public String hashPassword(String plainPassword){
        if(plainPassword == null || plainPassword.isBlank() || plainPassword.length() < 12){
            throw new AppException(AppError.VALIDATION_FAILED, null);
        }
        char[] passwordChars = plainPassword.toCharArray();
        try{
            return argon2.hash(ITERATIONS, MEMORY_KB, PARALLELISM, passwordChars);
        } finally {
            java.util.Arrays.fill(passwordChars, '\u0000');
        }
    }

    @Override
    public AuthUserResult login(String email, String plainPassword){
        User user = userDAO.findByEmail(email);
        if(user == null){
            throw new AppException(AppError.INVALID_CREDENTIALS, null);
        }
        if(!verifyPassword(plainPassword, user.getPasswordHash())){
            throw new AppException(AppError.INVALID_CREDENTIALS, null);
        }
        if(!user.getActive()){
            throw new AppException(AppError.INVALID_CREDENTIALS,null);
        }
        return new AuthUserResult(
                user.getFirstName(),
                user.getLastName(),
                user.getEmployeeId(),
                user.getHireDate(),
                user.getDepartment(),
                user.getCorporateEmail());
    }

    private boolean verifyPassword(String plainPassword, String hash){
        if(plainPassword == null || plainPassword.isBlank()){
            return false;
        }
        if(hash == null || hash.isBlank()){
            return false;
        }
        char[] passwordChars = plainPassword.toCharArray();
        try{
            return argon2.verify(hash, passwordChars);
        }finally {
            java.util.Arrays.fill(passwordChars, '\u0000');
        }
    }
}
