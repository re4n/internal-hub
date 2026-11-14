package com.re4n.internalhub.dto;

import com.re4n.internalhub.enums.Department;

import java.time.LocalDate;

public record NewUserResult(
        String firstName,
        String lastName,
        String employeeId,
        LocalDate hireDate,
        Department department,
        String corporateEmail,
        String temporaryPassword
) {}
