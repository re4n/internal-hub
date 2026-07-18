package com.re4n.internalhub.model;

import com.re4n.internalhub.enums.RoleType;

import java.math.BigDecimal;

public class Role {
    private Long id;
    private String roleName;
    private RoleType roleType;
    private BigDecimal minSalary;
    private BigDecimal maxSalary;
    private String description;

    public Role(){}

    public Role(Long id, String roleName, RoleType roleType, BigDecimal minSalary, BigDecimal maxSalary, String description) {
        this.id = id;
        this.roleName = roleName;
        this.roleType = roleType;
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
        this.description = description;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getRoleName() {return roleName;}
    public void setRoleName(String roleName) {this.roleName = roleName;}

    public RoleType getRoleType() {return roleType;}
    public void setRoleType(RoleType roleType) {this.roleType = roleType;}

    public BigDecimal getMinSalary() {return minSalary;}
    public void setMinSalary(BigDecimal minSalary) {this.minSalary = minSalary;}

    public BigDecimal getMaxSalary() {return maxSalary;}
    public void setMaxSalary(BigDecimal maxSalary) {this.maxSalary = maxSalary;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
}
