package com.re4n.internalhub;

import com.re4n.internalhub.dao.RoleDAO;
import com.re4n.internalhub.dao.UserDAO;
import com.re4n.internalhub.enums.Department;
import com.re4n.internalhub.exception.AppException;
import com.re4n.internalhub.model.User;
import com.re4n.internalhub.service.*;
import com.re4n.internalhub.util.CredentialGenerator;

public class Main {
    public static void main(String[] args) {
        RoleDAO roleDAO = new RoleDAO();
        UserDAO userDAO = new UserDAO();
        CredentialGenerator generator = new CredentialGenerator();
        AuthorizationService authService = new AuthorizationServiceImpl(roleDAO);
        AuthenticationService authenticationService = new AuthenticationServiceImpl(userDAO);
        UserService userService = new UserServiceImpl(userDAO, roleDAO, authService, generator);

        User actor = userDAO.findById(1L);

        User newUser = new User();
        newUser.setFirstName("Ryan");
        newUser.setLastName("Rouxinol");
        newUser.setPersonalEmail("ryanrouxinol@email.com");
        newUser.setDepartment(Department.ENGINEERING);

        try {
            User created = userService.createUser(actor, newUser);
            System.out.println("Created: " + created.getEmployeeId());
            System.out.println("Email: " + created.getCorporateEmail());
        }catch (AppException e){
            System.out.println("Error: " + e.getErrorType() + " - " + e.getMessage());
            e.printStackTrace();
        }
    }
}