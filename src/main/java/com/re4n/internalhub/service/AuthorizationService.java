package com.re4n.internalhub.service;

import com.re4n.internalhub.dao.RoleDAO;
import com.re4n.internalhub.enums.RoleType;
import com.re4n.internalhub.model.Role;
import com.re4n.internalhub.model.User;

public class AuthorizationService {
    private final RoleDAO roleDAO;

    public AuthorizationService(RoleDAO roleDAO){
        this.roleDAO = roleDAO;
    }

    public boolean canCreateUser(User actor){
        RoleType actorType = resolveActorType(actor);
        if (actorType == null){return false;}

        if(actorType == RoleType.HR) {return true;}
        return false;
    }

    public boolean canDisableUser(User actor, User target){
        RoleType actorType = resolveActorType(actor);
        if (actorType == null){return false;}

        if(actorType == RoleType.HR && !actor.getId().equals(target.getId())) {return true;}
        return false;
    }

    public boolean canManageRoles(User actor){
        RoleType actorType = resolveActorType(actor);
        if (actorType == null){return false;}

        if(actorType == RoleType.ADMIN){ return true;}
        return false;
    }

    public boolean canAssignRole(User actor, User target, RoleType roleToAssign){
        RoleType actorType = resolveActorType(actor);
        if (actorType == null){return false;}
        if(actor.getId().equals(target.getId())) return false;
        if(roleToAssign == RoleType.ADMIN && actorType == RoleType.ADMIN) {return true;}
        if(roleToAssign != RoleType.ADMIN){
            return actorType == RoleType.ADMIN || actorType == RoleType.HR;
        }
        return false;
    }

    public boolean canReadUser(User actor, User target){
        RoleType actorType = resolveActorType(actor);
        if (actorType == null){return false;}

        if(actor.getId().equals(target.getId())){return true;}
        if(actorType == RoleType.ADMIN || actorType == RoleType.HR){return true;}
        if(actorType == RoleType.MANAGER && actor.getDepartment().equals(target.getDepartment())){return true;}
        return false;
    }
    public boolean canUpdateSalary(User actor, User target){
        RoleType actorType = resolveActorType(actor);
        if (actorType == null){return false;}
        if (actor.getId().equals(target.getId())){return false;}
        if(actorType == RoleType.HR){return true;}
        return false;
    }

   private RoleType resolveActorType(User actor){
       if (actor.getRoleId() == null){return null;}
       Role actorRole = roleDAO.findById(actor.getRoleId());
       if(actorRole == null){return null;}
       return actorRole.getRoleType();
 }
}
