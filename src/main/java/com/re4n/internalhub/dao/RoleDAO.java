package com.re4n.internalhub.dao;

import com.re4n.internalhub.enums.RoleType;
import com.re4n.internalhub.enums.SeniorityLevel;
import com.re4n.internalhub.model.Role;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleDAO extends BaseDAO<Role> {
    @Override
    protected String getInsertSql() {
        return "INSERT INTO roles (role_name, role_type, seniority_level, min_salary, max_salary, description) VALUES (?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE roles SET role_name = ?, role_type = ?, seniority_level = ?, min_salary = ?, max_salary = ?, description = ? WHERE id = ?";
    }

    @Override
    protected String getDeleteSql() {
        return "DELETE FROM roles WHERE id = ?";
    }

    @Override
    protected String getSelectByIdSql() {
        return "SELECT * FROM roles WHERE id = ?";
    }

    @Override
    protected String getSelectAllSql() {
        return "SELECT * FROM roles";
    }

    @Override
    protected Role mapResultSetToEntity(ResultSet rs) throws SQLException {
        Role role = new Role();

        role.setId(rs.getLong("id"));
        role.setRoleName(rs.getString("role_name"));
        role.setRoleType(RoleType.valueOf(rs.getString("role_type")));
        role.setSeniorityLevel(SeniorityLevel.valueOf(rs.getString("seniority_level")));
        role.setMinSalary(rs.getBigDecimal("min_salary"));
        role.setMaxSalary(rs.getBigDecimal("max_salary"));
        role.setDescription(rs.getString("description"));
        return role;
    }

    @Override
    protected void bindSaveParameters(PreparedStatement stmt, Role entity) throws SQLException {
        stmt.setString(1, entity.getRoleName());
        stmt.setString(2, entity.getRoleType().name());
        stmt.setString(3, entity.getSeniorityLevel().name());
        stmt.setBigDecimal(4, entity.getMinSalary());
        stmt.setBigDecimal(5, entity.getMaxSalary());
        stmt.setString(6, entity.getDescription());
    }

    @Override
    protected void bindUpdateParameters(PreparedStatement stmt, Role entity) throws SQLException {
        stmt.setString(1, entity.getRoleName());
        stmt.setString(2, entity.getRoleType().name());
        stmt.setString(3, entity.getSeniorityLevel().name());
        stmt.setBigDecimal(4, entity.getMinSalary());
        stmt.setBigDecimal(5, entity.getMaxSalary());
        stmt.setString(6, entity.getDescription());
        stmt.setLong(7, entity.getId());
    }
}
