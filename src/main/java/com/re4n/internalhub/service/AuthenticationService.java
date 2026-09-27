package com.re4n.internalhub.service;

import com.re4n.internalhub.dto.AuthUserResult;

public interface AuthenticationService {
    String hashPassword(String plainPassword);
    AuthUserResult login(String email, String plainPassword);
}

