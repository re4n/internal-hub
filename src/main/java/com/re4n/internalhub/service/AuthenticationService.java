package com.re4n.internalhub.service;

import com.re4n.internalhub.model.User;

public interface AuthenticationService {
    String hashPassword(String plainPassword);
    User login(String email, String plainPassword);
}

