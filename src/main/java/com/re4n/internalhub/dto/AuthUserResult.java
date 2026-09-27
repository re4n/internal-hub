package com.re4n.internalhub.dto;

import com.re4n.internalhub.enums.Department;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record AuthUserResult(
        String firstName,
        String lastName,
        String employeeId,
        LocalDate hireDate,
        Department department,
        String corporateEmail
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
                "  └── corporateEmail: " + corporateEmail + "\n" +
                "]";
    }
}
