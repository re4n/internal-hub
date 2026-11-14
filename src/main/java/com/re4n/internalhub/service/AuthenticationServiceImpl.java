package com.re4n.internalhub.service;

import com.re4n.internalhub.dao.RoleDAO;
import com.re4n.internalhub.dao.UserDAO;
import com.re4n.internalhub.dto.AuthUserResult;
import com.re4n.internalhub.dto.RoleResult;
import com.re4n.internalhub.enums.AppError;
import com.re4n.internalhub.exception.AppException;
import com.re4n.internalhub.model.Role;
import com.re4n.internalhub.model.User;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import  javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;


public class AuthenticationServiceImpl implements AuthenticationService {
    private static final Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
    private final UserDAO userDAO;
    private final RoleDAO roleDAO;
    public static final int ITERATIONS = 2;
    public static final int MEMORY_KB = 19456;
    public static final int PARALLELISM = 1;
    private final SecretKey jwtSecretKey;
    private static final long EXPIRATION_TIME_MS = 2 * 60 * 60 * 1000; // 2 hours


    public AuthenticationServiceImpl(UserDAO userDAO, RoleDAO roleDAO, String secret){
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
        this.jwtSecretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
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

        Role role = user.getRoleId() != null ? roleDAO.findById(user.getRoleId()) : null;
        RoleResult roleResult = (role != null) ? new RoleResult(role.getRoleName(), role.getSeniorityLevel().name(), role.getRoleType().name()) : null;
        String token = generateToken(user);

        return new AuthUserResult(
                token,
                user.getFirstName(),
                user.getLastName(),
                user.getEmployeeId(),
                user.getHireDate(),
                user.getDepartment(),
                user.getCorporateEmail(),
                roleResult);
    }

    @Override
    public String generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME_MS);
        return Jwts.builder()
                .subject(String.valueOf(user.getId()))
                .claim("email", user.getCorporateEmail())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(jwtSecretKey)
                .compact();
    }

    @Override
    public Long userIdExtractor(String token) {
        try{
            Claims claims = Jwts.parser()
                    .verifyWith(jwtSecretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return Long.parseLong(claims.getSubject());
        }catch (JwtException | IllegalArgumentException e){
            throw new AppException(AppError.INVALID_CREDENTIALS, e);
        }
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
