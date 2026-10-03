package com.re4n.internalhub.service;

import com.re4n.internalhub.dao.RoleDAO;
import com.re4n.internalhub.enums.RoleType;
import com.re4n.internalhub.enums.SeniorityLevel;
import com.re4n.internalhub.model.Role;
import com.re4n.internalhub.model.User;

public class AuthorizationServiceImpl implements AuthorizationService {
    private final RoleDAO roleDAO;

    public AuthorizationServiceImpl(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    @Override
    public boolean canCreateUser(User actor) {
        RoleType actorType = resolveActorType(actor);
        if (actorType == null) {return false;}
        if (actorType != RoleType.HR) {return false;}
        return hasSeniorHRPrivileges(actor);
    }

    @Override
    public boolean canDisableUser(User actor, User target) {
        RoleType actorType = resolveActorType(actor);
        if (actorType == null) {return false;}
        if(actorType != RoleType.HR && actor.getId().equals(target.getId())){return false;}
        return hasSeniorHRPrivileges(actor);
    }

    @Override
    public boolean canManageRole(User actor) {
        RoleType actorType = resolveActorType(actor);
        if (actorType == null) {return false;}
        return actorType == RoleType.ADMIN;
    }

    @Override
    public boolean canReadRole(User actor) {
        RoleType actorType = resolveActorType(actor);
        if (actorType == null) {return false;}
        return actorType == RoleType.ADMIN || actorType == RoleType.HR;
    }

    @Override
    public boolean canAssignRole(User actor, User target, RoleType roleToAssign) {
        RoleType actorType = resolveActorType(actor);
        RoleType targetType = resolveActorType(target);
        if (actorType == null || targetType == null) {return false;}
        if (actor.getId().equals(target.getId())) {return false;
        }
        if(actorType == RoleType.ADMIN){return roleToAssign == RoleType.ADMIN;}
        if(actorType == RoleType.HR){
            if(roleToAssign == RoleType.ADMIN){return false;}
            if(targetType == RoleType.ADMIN) {return false;}
            return hasSeniorHRPrivileges(actor);
        }
        return false;
    }


    @Override
    public boolean canReadUser(User actor, User target) {
        RoleType actorType = resolveActorType(actor);
        if (actorType == null) {return false;}
        if (actor.getId().equals(target.getId())) {return true;}
        if (actorType == RoleType.ADMIN || actorType == RoleType.HR) {
            return true;
        }
        return actorType == RoleType.MANAGER && actor.getDepartment().equals(target.getDepartment());
    }

    @Override
    public boolean canUpdateSalary(User actor, User target) {
        RoleType actorType = resolveActorType(actor);
        if (actorType == null) {return false;}
        if (actorType != RoleType.HR) {return false;}
        if (actor.getId().equals(target.getId())) {return false;}
        return hasSeniorHRPrivileges(actor);

    }

    private RoleType resolveActorType(User actor) {
        if (actor.getRoleId() == null) {
            return null;
        }
        Role actorRole = roleDAO.findById(actor.getRoleId());
        if (actorRole == null) {
            return null;
        }
        return actorRole.getRoleType();
    }

    private boolean hasSeniorHRPrivileges(User actor) {
        if(actor.getRoleId() == null) {
            return false;
        }
        Role actorRole = roleDAO.findById(actor.getRoleId());
        if(actorRole.getRoleType() != RoleType.HR) {
            return false;
        }
        SeniorityLevel seniorityLevel = actorRole.getSeniorityLevel();
        if(seniorityLevel == null) {return false;}
        return seniorityLevel == SeniorityLevel.SENIOR ||
                seniorityLevel == SeniorityLevel.LEAD ||
                seniorityLevel == SeniorityLevel.EXECUTIVE;

    }
}
