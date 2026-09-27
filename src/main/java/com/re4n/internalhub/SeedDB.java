package com.re4n.internalhub;

import com.re4n.internalhub.dao.RoleDAO;
import com.re4n.internalhub.dao.UserDAO;
import com.re4n.internalhub.dto.NewUserResult;
import com.re4n.internalhub.enums.Department;
import com.re4n.internalhub.enums.RoleType;
import com.re4n.internalhub.exception.AppException;
import com.re4n.internalhub.model.Role;
import com.re4n.internalhub.model.User;
import com.re4n.internalhub.service.*;
import com.re4n.internalhub.util.CredentialGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SeedDB {
    public static void main(String[] args) {
        RoleDAO roleDAO = new RoleDAO();
        UserDAO userDAO = new UserDAO();
        CredentialGenerator generator = new CredentialGenerator();
        AuthorizationService authorizationService = new AuthorizationServiceImpl(roleDAO);
        AuthenticationService authenticationService = new AuthenticationServiceImpl(userDAO);
        UserService userService = new UserServiceImpl(userDAO, roleDAO, authorizationService, authenticationService, generator);
        RoleService roleService = new RoleServiceImpl(roleDAO, authorizationService);

        User actor = userDAO.findById(2L);

        User newUser = new User();
        newUser.setFirstName("Ryan");
        newUser.setLastName("Rouxinol");
        newUser.setPersonalEmail("ryanrouxinol@email.com");
        newUser.setDepartment(Department.ENGINEERING);

        try {
            NewUserResult created = userService.createUser(actor, newUser);
            System.out.println(created);
        }catch (AppException e){
            System.out.println("Error: " + e.getErrorType() + " - " + e.getMessage());
            e.printStackTrace();
        }
    }
}
