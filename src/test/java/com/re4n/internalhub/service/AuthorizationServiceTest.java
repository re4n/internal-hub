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

        assertFalse(result, "HR doesn't update its own salaries.");
    }

    @Test
    void preventHRAssigningAdmin(){
        Role hrRole = createRole(1L, RoleType.HR);
        RoleDAO stub = new RoleDAOStub(hrRole);
        AuthorizationService service = new AuthorizationService(stub);

        User actor = createUser(1L, hrRole.getId(), Department.IT);
        User target = createUser(2L, null, Department.IT);

        boolean result = service.canAssignRole(actor, target, RoleType.ADMIN);

        assertFalse(result, "HR should not be able to assign ADMIN role");
    }

    @Test
    void preventSelfRoleAssignment(){
        Role hrRole = createRole(1L, RoleType.HR);
        RoleDAO stub = new RoleDAOStub(hrRole);
        AuthorizationService service = new AuthorizationService(stub);

        User actor = createUser(1L, hrRole.getId(), Department.IT);

        boolean result = service.canAssignRole(actor, actor, RoleType.ADMIN);

        assertFalse(result, "You cannot self-assign");
    }

    @Test
    void preventManagerReadingOtherDepartment(){
        Role managerRole = createRole(1L, RoleType.MANAGER);
        RoleDAO stub = new RoleDAOStub(managerRole);
        AuthorizationService service = new AuthorizationService(stub);

        User actor = createUser(1L, managerRole.getId(), Department.IT);
        User target = createUser(2L, managerRole.getId(), Department.FINANCE);

        boolean result = service.canReadUser(actor, target);

        assertFalse(result, "You cannot view users outside your department.");
    }

    @Test
    void allowHRUpdatingOtherSalary(){
        Role hrRole = createRole(1L, RoleType.HR);
        RoleDAO stub = new RoleDAOStub(hrRole);
        AuthorizationService service = new AuthorizationService(stub);

        User actor = createUser(1L, hrRole.getId(), Department.HR);
        User target = createUser(2L, null, Department.ENGINEERING);

        boolean result = service.canUpdateSalary(actor, target);

        assertTrue(result, "You cannot update other employee's salaries.");
    }

    @Test
    void allowUserReadingOwnProfile(){
        Role employeeRole = createRole(1L, RoleType.EMPLOYEE);
        RoleDAO stub = new RoleDAOStub(employeeRole);
        AuthorizationService service = new AuthorizationService(stub);

        User actor = createUser(1L, employeeRole.getId(), Department.SUPPORT);

        boolean result = service.canReadUser(actor, actor);

        assertTrue(result, "You cannot view other users's profiles.");
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
