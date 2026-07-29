package com.re4n.internalhub.service;
import com.re4n.internalhub.dao.RoleDAO;
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

        User actor = createUser(1L, hrRole.getId());
        User target = createUser(1L,null);

        boolean result = service.canUpdateSalary(actor, target);

        assertFalse(result, "HR doesn't update its own salaries.");
    }

    @Test
    void preventHRAssigningAdmin(){
        Role hrRole = createRole(1L, RoleType.HR);
        RoleDAO stub = new RoleDAOStub(hrRole);
        AuthorizationService service = new AuthorizationService(stub);

        User actor = createUser(1L, hrRole.getId());
        User target = createUser(2L, null);

        boolean result = service.canAssignRole(actor, target, RoleType.ADMIN);

        assertFalse(result, "HR should not be able to assign ADMIN role");
    }

    private Role createRole(Long id, RoleType type){
        Role role = new Role();
        role.setId(id);
        role.setRoleType(type);
        return role;
    }

    private User createUser(Long id, Long roleId){
        User user = new User();
        user.setId(id);
        user.setRoleId(roleId);
        return user;
    }
}
