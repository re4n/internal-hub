package com.re4n.internalhub.dao;

import com.re4n.internalhub.enums.WorkModel;
import com.re4n.internalhub.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class UserDAO extends BaseDAO<User>{
    @Override
    protected String getInsertSql() {
        return "INSERT INTO users (first_name, last_name, corporate_email, personal_email, salary, work_model, hire_date, is_active) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE users SET first_name = ?, last_name = ?, corporate_email = ?, personal_email = ?, salary = ?, work_model = ?, hire_date = ?, is_active = ? WHERE id = ?";
    }

    @Override
    protected String getDeleteSql() {
        return "DELETE FROM users WHERE id = ?";
    }

    @Override
    protected String getSelectByIdSql() {
        return "SELECT * FROM users WHERE id = ?";
    }

    @Override
    protected String getSelectAllSql() {
        return "SELECT * FROM users";
    }

    @Override
    protected User mapResultSetToEntity(ResultSet rs) throws SQLException {
        User user = new User();

        user.setId(rs.getLong("id"));
        user.setEmployeeId(rs.getString("employee_id"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setCorporateEmail(rs.getString("corporate_email"));
        user.setPersonalEmail(rs.getString("personal_email"));
        user.setSalary(rs.getBigDecimal("salary"));
        user.setWorkModel(WorkModel.valueOf(rs.getString("work_model")));
        user.setHireDate(rs.getObject("hire_date", LocalDate.class));
        user.setActive(rs.getBoolean("is_active"));
        user.setRoleId(rs.getLong("role_id"));
        return user;
    }

    @Override
    protected void bindSaveParameters(PreparedStatement stmt, User entity) throws SQLException {
        stmt.setString(1, entity.getFirstName());
        stmt.setString(2, entity.getLastName());
        stmt.setString(3, entity.getCorporateEmail());
        stmt.setString(4, entity.getPersonalEmail());
        stmt.setBigDecimal(5, entity.getSalary());
        stmt.setString(6, entity.getWorkModel().name());
        stmt.setObject(7, entity.getHireDate());
        stmt.setBoolean(8, entity.getActive());
    }

    @Override
    protected void bindUpdateParameters(PreparedStatement stmt, User entity) throws SQLException {
        bindSaveParameters(stmt, entity);
        stmt.setLong(9, entity.getId());
    }
}
