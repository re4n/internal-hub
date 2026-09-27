package com.re4n.internalhub;

import com.re4n.internalhub.dao.RoleDAO;
import com.re4n.internalhub.dao.UserDAO;
import com.re4n.internalhub.dto.AuthUserResult;
import com.re4n.internalhub.dto.NewUserResult;
import com.re4n.internalhub.dto.RoleResult;
import com.re4n.internalhub.enums.Department;
import com.re4n.internalhub.enums.RoleType;
import com.re4n.internalhub.enums.SeniorityLevel;
import com.re4n.internalhub.exception.AppException;
import com.re4n.internalhub.model.Role;
import com.re4n.internalhub.model.User;
import com.re4n.internalhub.service.*;
import com.re4n.internalhub.util.CredentialGenerator;

import java.math.BigDecimal;

public class ApplicationTestRunner {
    private static final RoleDAO roleDAO = new RoleDAO();
    private static final UserDAO userDAO = new UserDAO();
    private static final CredentialGenerator generator = new CredentialGenerator();
    private static final AuthorizationService authorizationService = new AuthorizationServiceImpl(roleDAO);
    private static final AuthenticationService authenticationService = new AuthenticationServiceImpl(userDAO, roleDAO);
    private static final RoleService roleService= new RoleServiceImpl(roleDAO, authorizationService);
    private static final UserService userService = new UserServiceImpl(userDAO, roleDAO, authorizationService, authenticationService, generator);

    public static void main(String[] args) {
        User hrUser = userDAO.findById(2L); // password IH-Y6FX9G -> px6r@W#vWyuv
        User adminUser = userDAO.findById(1L); // password IH-ADMIN01 -> root@admin!5?
        User uxUser = userDAO.findById(3L);

        System.out.println(testLoginUser(hrUser.getCorporateEmail(), "px6r@W#vWyuv"));

//        seedRole(adminUser);
        seedUser(hrUser);

    }

    private static RoleResult seedRole(User actor){
        System.out.println("Initiating creation.");
        Role newRole = new Role();
        newRole.setRoleName("UX/UI Designer");
        newRole.setRoleType(RoleType.EMPLOYEE);
        newRole.setSeniorityLevel(SeniorityLevel.MID);
        newRole.setMinSalary(BigDecimal.valueOf(5800));
        newRole.setMaxSalary(BigDecimal.valueOf(7200));
        newRole.setDescription("Defines the company's design system, mentors designers, and ensures user experience consistency across all fronts.");
        try{
            return roleService.createRole(actor, newRole);
        }catch (AppException e){
            System.out.println("Error: " + e.getErrorType() + " - " + e.getMessage());
        }
        return null;
    }

    private static NewUserResult seedUser(User actor){
        System.out.println("Initiating creation.");
        User newUser = new User();
        newUser.setFirstName("Terrance");
        newUser.setLastName("Grant");
        newUser.setPersonalEmail("terranceg@email.com");
        newUser.setDepartment(Department.PRODUCT);
        try {
            return userService.createUser(actor, newUser);
        }catch (AppException e){
            System.out.println("Error: " + e.getErrorType() + " - " + e.getMessage());
        }
        return null;
    }
    private static AuthUserResult testLoginUser(String email, String password){
        try {
            return authenticationService.login(email, password);
        }catch (AppException e){
            System.out.println("Error: " + e.getErrorType() + " - " + e.getMessage());
        }
        return null;
    }
}
