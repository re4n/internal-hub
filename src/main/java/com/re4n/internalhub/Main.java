package com.re4n.internalhub;

import com.re4n.internalhub.dao.RoleDAO;
import com.re4n.internalhub.dao.UserDAO;
import com.re4n.internalhub.dto.AuthUserResult;
import com.re4n.internalhub.model.User;
import com.re4n.internalhub.service.*;
import com.re4n.internalhub.util.CredentialGenerator;

public class Main {
    public static void main(String[] args) {
        RoleDAO roleDAO = new RoleDAO();
        UserDAO userDAO = new UserDAO();
        CredentialGenerator generator = new CredentialGenerator();
        AuthorizationService authorizationService = new AuthorizationServiceImpl(roleDAO);
        AuthenticationService authenticationService = new AuthenticationServiceImpl(userDAO);
        UserService userService = new UserServiceImpl(userDAO, roleDAO, authorizationService,authenticationService, generator);

        User hrUser = userDAO.findById(2L); // password IH-Y6FX9G -> nwERBfMZ*U9E

        User adminUser = userDAO.findById(1L); // password IH-ADMIN01 -> root!admin1@

        User userTest =  userDAO.findById(3L); // password IH-MJG9BE -> uhG--ym!FqxV

        AuthUserResult loggedUser = authenticationService.login(adminUser.getCorporateEmail(), "root!admin1@");
        System.out.println(loggedUser);
    }
}