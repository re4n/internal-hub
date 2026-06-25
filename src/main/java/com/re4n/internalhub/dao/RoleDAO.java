package com.re4n.internalhub.dao;

import com.re4n.internalhub.enums.Department;
import com.re4n.internalhub.model.Role;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleDAO extends BaseDAO<Role> {
    @Override
    protected String getInsertSql() {
        return "INSERT INTO roles (role_name, department, access_level, min_salary, max_salary, description) VALUES (?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE roles SET role_name = ?, department = ?, access_level = ?, min_salary = ?, max_salary = ?, description = ? WHERE id = ?";
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
        role.setDepartment(Department.valueOf(rs.getString("department")));
        role.setAccessLevel(rs.getInt("access_level"));
        role.setMinSalary(rs.getDouble("min_salary"));
        role.setMaxSalary(rs.getDouble("max_salary"));
        role.setDescription(rs.getString("description"));
        return role;
    }

    @Override
    protected void bindSaveParameters(PreparedStatement stmt, Role entity) throws SQLException {
        stmt.setString(1, entity.getRoleName());
        stmt.setString(2, entity.getDepartment().name());
        stmt.setInt(3, entity.getAccessLevel());
        stmt.setDouble(4, entity.getMinSalary());
        stmt.setDouble(5, entity.getMaxSalary());
        stmt.setString(6, entity.getDescription());
    }

    @Override
    protected void bindUpdateParameters(PreparedStatement stmt, Role entity) throws SQLException {
        bindSaveParameters(stmt, entity);
        stmt.setLong(7, entity.getId());
    }
}
