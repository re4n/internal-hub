package com.re4n.internalhub.model;

import com.re4n.internalhub.enums.Department;
import com.re4n.internalhub.enums.WorkModel;

import java.math.BigDecimal;
import java.time.LocalDate;

public class User {
    private Long id;
    private String employeeId;
    private String firstName;
    private String lastName;
    private String corporateEmail;
    private String personalEmail;
    private BigDecimal salary;
    private Department department;
    private WorkModel workModel = WorkModel.ONSITE;
    private LocalDate hireDate;
    private Boolean isActive = true;

    private Long roleId;

    public User(){}

    public User(Long id, String employeeId, String firstName, String lastName, String corporateEmail, String personalEmail, BigDecimal salary, Department department, WorkModel workModel, LocalDate hireDate, Boolean isActive, Long roleId) {
        this.id = id;
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.corporateEmail = corporateEmail;
        this.personalEmail = personalEmail;
        this.salary = salary;
        this.department = department;
        this.workModel = workModel;
        this.hireDate = hireDate;
        this.isActive = isActive;
        this.roleId = roleId;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getEmployeeId() {return employeeId;}
    public void setEmployeeId(String employeeId) {this.employeeId = employeeId;}

    public String getFirstName() {return firstName;}
    public void setFirstName(String firstName) {this.firstName = firstName;}

    public String getLastName() {return lastName;}
    public void setLastName(String lastName) {this.lastName = lastName;}

    public String getCorporateEmail() {return corporateEmail;}
    public void setCorporateEmail(String corporateEmail) {this.corporateEmail = corporateEmail;}

    public String getPersonalEmail() {return personalEmail;}

    public void setPersonalEmail(String personalEmail) {this.personalEmail = personalEmail;}

    public BigDecimal getSalary() {return salary;}
    public void setSalary(BigDecimal salary) {this.salary = salary;}

    public Department getDepartment() {return department;}
    public void setDepartment(Department department) {this.department = department;}

    public WorkModel getWorkModel() {return workModel;}
    public void setWorkModel(WorkModel workModel) {this.workModel = workModel;}

    public LocalDate getHireDate() {return hireDate;}
    public void setHireDate(LocalDate hireDate) {this.hireDate = hireDate;}

    public Boolean getActive() {return isActive;}
    public void setActive(Boolean active) {isActive = active;}

    public Long getRoleId() {return roleId;}
    public void setRoleId(Long roleId) {this.roleId = roleId;}
}
