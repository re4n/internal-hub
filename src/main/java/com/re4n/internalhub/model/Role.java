package com.re4n.internalhub.model;

import com.re4n.internalhub.enums.Department;

import java.time.LocalDate;

public class Role {
    private Long id;
    private String roleName;
    private Department department;
    private Integer accessLevel;
    private Double salary;
    private String description;
    private LocalDate creationDate;

    public Role(){}

    public Role(Long id, String roleName, Department department, Integer accessLevel, Double salary, String description, LocalDate creationDate) {
        this.id = id;
        this.roleName = roleName;
        this.department = department;
        this.accessLevel = accessLevel;
        this.salary = salary;
        this.description = description;
        this.creationDate = creationDate;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getRoleName() {return roleName;}
    public void setRoleName(String roleName) {this.roleName = roleName;}

    public Department getDepartment() {return department;}
    public void setDepartment(Department department) {this.department = department;}

    public Integer getAccessLevel() {return accessLevel;}
    public void setAccessLevel(Integer accessLevel) {this.accessLevel = accessLevel;}

    public Double getSalary() {return salary;}
    public void setSalary(Double salary) {this.salary = salary;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public LocalDate getCreationDate() {return creationDate;}
    public void setCreationDate(LocalDate creationDate) {this.creationDate = creationDate;}
}
