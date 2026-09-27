package com.re4n.internalhub.dto;

import com.re4n.internalhub.dao.RoleDAO;
import com.re4n.internalhub.dao.UserDAO;
import com.re4n.internalhub.enums.Department;
import com.re4n.internalhub.model.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record AuthUserResult(
        String firstName,
        String lastName,
        String employeeId,
        LocalDate hireDate,
        Department department,
        String corporateEmail,
        RoleResult role
) {

    @Override
    public String toString() {
        String loginTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        return "LoggedUser -> " + loginTime + " [\n" +
                "  ├── firstName:      " + firstName + "\n" +
                "  ├── lastName:       " + lastName + "\n" +
                "  ├── employeeId:     " + employeeId + "\n" +
                "  ├── hireDate:       " + hireDate + "\n" +
                "  ├── department:     " + department + "\n" +
                "  ├── corporateEmail: " + corporateEmail + "\n" +
                "  ├── roleName:       " + (role != null ? role.roleName() : "N/A") + "\n" +
                "  ├── Seniority Level: " + (role != null ? role.seniorityLevel() : "N/A") + "\n" +
                "  └── roleType:       " + (role != null ? role.roleType() : "N/A") + "\n" +
                "]";
    }
}