package com.re4n.internalhub.service;
import com.re4n.internalhub.dao.RoleDAO;
import com.re4n.internalhub.enums.Department;
import com.re4n.internalhub.enums.RoleType;
import com.re4n.internalhub.model.Role;
import com.re4n.internalhub.model.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuthorizationServiceTest {

    @Test
    void preventHRSelfSalaryUpdate(){
        Role hrRole = createRole(1L, RoleType.HR);
        RoleDAO stub = new RoleDAOStub(hrRole);
        AuthorizationService service = new AuthorizationService(stub);

        User actor = createUser(1L, hrRole.getId(), Department.IT);
        User target = createUser(1L,null, Department.IT);

        boolean result = service.canUpdateSalary(actor, target);

        assertFalse(result, "HR must not update their own salary");
    }

    @Test
    void preventHRAssigningAdmin(){
        Role hrRole = createRole(1L, RoleType.HR);
        RoleDAO stub = new RoleDAOStub(hrRole);
        AuthorizationService service = new AuthorizationService(stub);

        User actor = createUser(1L, hrRole.getId(), Department.IT);
        User target = createUser(2L, null, Department.IT);

        boolean result = service.canAssignRole(actor, target, RoleType.ADMIN);

        assertFalse(result, "HR must not assign the ADMIN role");
    }

    @Test
    void preventSelfRoleAssignment(){
        Role hrRole = createRole(1L, RoleType.HR);
        RoleDAO stub = new RoleDAOStub(hrRole);
        AuthorizationService service = new AuthorizationService(stub);

        User actor = createUser(1L, hrRole.getId(), Department.IT);

        boolean result = service.canAssignRole(actor, actor, RoleType.ADMIN);

        assertFalse(result, "No user may assign a role to themselves");
    }

    @Test
    void preventManagerReadingOtherDepartment(){
        Role managerRole = createRole(1L, RoleType.MANAGER);
        RoleDAO stub = new RoleDAOStub(managerRole);
        AuthorizationService service = new AuthorizationService(stub);

        User actor = createUser(1L, managerRole.getId(), Department.IT);
        User target = createUser(2L, managerRole.getId(), Department.FINANCE);

        boolean result = service.canReadUser(actor, target);

        assertFalse(result, "MANAGER must not read users from another department");
    }

    @Test
    void allowHRUpdatingOtherSalary(){
        Role hrRole = createRole(1L, RoleType.HR);
        RoleDAO stub = new RoleDAOStub(hrRole);
        AuthorizationService service = new AuthorizationService(stub);

        User actor = createUser(1L, hrRole.getId(), Department.HR);
        User target = createUser(2L, null, Department.ENGINEERING);

        boolean result = service.canUpdateSalary(actor, target);

        assertTrue(result, "HR should be able to update another employee's salary");
    }

    @Test
    void allowUserReadingOwnProfile(){
        Role employeeRole = createRole(1L, RoleType.EMPLOYEE);
        RoleDAO stub = new RoleDAOStub(employeeRole);
        AuthorizationService service = new AuthorizationService(stub);

        User actor = createUser(1L, employeeRole.getId(), Department.SUPPORT);

        boolean result = service.canReadUser(actor, actor);

        assertTrue(result, "Any user should be able to read their own profile");
    }

    @Test
    void denyEmployeeReadRole(){
        Role employeeRole = createRole(1L, RoleType.EMPLOYEE);
        RoleDAO stub = new RoleDAOStub(employeeRole);
        AuthorizationService service = new AuthorizationService(stub);

        User actor = createUser(1L, employeeRole.getId(), Department.ENGINEERING);

        boolean result = service.canReadRole(actor);

        assertFalse(result, "EMPLOYEE must not be able to read roles");
    }

    @Test
    void denyManagerManageRole(){
        Role employeeRole = createRole(1L, RoleType.MANAGER);
        RoleDAO stub = new RoleDAOStub(employeeRole);
        AuthorizationService service = new AuthorizationService(stub);

        User actor = createUser(1L, employeeRole.getId(), Department.IT);

        boolean result = service.canManageRole(actor);

        assertFalse(result, "MANAGER must not manage roles");
    }

    @Test
    void hrCanReadRole(){
        Role employeeRole = createRole(1L, RoleType.HR);
        RoleDAO stub = new RoleDAOStub(employeeRole);
        AuthorizationService service = new AuthorizationService(stub);

        User actor = createUser(1L, employeeRole.getId(), Department.HR);

        boolean result = service.canReadRole(actor);

        assertTrue(result, "HR should be able to read roles");
    }

    @Test
    void hrCannotManageRole(){
        Role employeeRole = createRole(1L, RoleType.HR);
        RoleDAO stub = new RoleDAOStub(employeeRole);
        AuthorizationService service = new AuthorizationService(stub);

        User actor = createUser(1L, employeeRole.getId(), Department.HR);

        boolean result = service.canManageRole(actor);

        assertFalse(result, "HR must not manage roles");
    }

    @Test
    void adminCanReadRole(){
        Role employeeRole = createRole(1L, RoleType.ADMIN);
        RoleDAO stub = new RoleDAOStub(employeeRole);
        AuthorizationService service = new AuthorizationService(stub);

        User actor = createUser(1L, employeeRole.getId(), Department.IT);

        boolean result = service.canReadRole(actor);

        assertTrue(result, "ADMIN should be able to read roles");
    }

    @Test
    void adminCanManageRole(){
        Role employeeRole = createRole(1L, RoleType.ADMIN);
        RoleDAO stub = new RoleDAOStub(employeeRole);
        AuthorizationService service = new AuthorizationService(stub);

        User actor = createUser(1L, employeeRole.getId(), Department.IT);

        boolean result = service.canManageRole(actor);

        assertTrue(result, "ADMIN should be able to manage roles");
    }

    private Role createRole(Long id, RoleType type){
        Role role = new Role();
        role.setId(id);
        role.setRoleType(type);
        return role;
    }

    private User createUser(Long id, Long roleId, Department department){
        User user = new User();
        user.setId(id);
        user.setRoleId(roleId);
        user.setDepartment(department);
        return user;
    }
}
