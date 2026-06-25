package com.re4n.internalhub.model;

import com.re4n.internalhub.enums.Department;

public class Role {
    private Long id;
    private String roleName;
    private Department department;
    private Integer accessLevel;
    private Double minSalary;
    private Double maxSalary;
    private String description;

    public Role(){}

    public Role(Long id, String roleName, Department department, Integer accessLevel, Double minSalary, Double maxSalary, String description) {
        this.id = id;
        this.roleName = roleName;
        this.department = department;
        this.accessLevel = accessLevel;
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
        this.description = description;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getRoleName() {return roleName;}
    public void setRoleName(String roleName) {this.roleName = roleName;}

    public Department getDepartment() {return department;}
    public void setDepartment(Department department) {this.department = department;}

    public Integer getAccessLevel() {return accessLevel;}
    public void setAccessLevel(Integer accessLevel) {this.accessLevel = accessLevel;}

    public Double getMinSalary() {return minSalary;}
    public void setMinSalary(Double minSalary) {this.minSalary = minSalary;}

    public Double getMaxSalary() {return maxSalary;}
    public void setMaxSalary(Double maxSalary) {this.maxSalary = maxSalary;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
}
