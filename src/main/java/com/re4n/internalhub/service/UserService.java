package com.re4n.internalhub.service;

import com.re4n.internalhub.model.User;

import java.math.BigDecimal;
import java.util.List;

public interface UserService {
    User createUser(User actor, User newUser);
    User findUser(User actor, Long targetId);
    List<User> findAllUsers(User actor);
    User disableUser(User actor, Long targetId);
    User updateSalary(User actor, Long targetId, BigDecimal newSalary);
    User assignRole(User actor, Long targetId, Long roleId);

}
