package com.re4n.internalhub.service;

import com.re4n.internalhub.enums.RoleType;
import com.re4n.internalhub.model.User;

public interface AuthorizationService {
    boolean canCreateUser(User actor);
    boolean canDisableUser(User actor, User target);
    boolean canManageRole(User actor);
    boolean canReadRole(User actor);
    boolean canAssignRole(User actor, User target, RoleType roleToAssign);
    boolean canReadUser(User actor, User target);
    boolean canUpdateSalary(User actor, User target);
}
