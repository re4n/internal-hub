package com.re4n.internalhub.dao;

import com.re4n.internalhub.enums.AppError;
import com.re4n.internalhub.enums.Department;
import com.re4n.internalhub.enums.WorkModel;
import com.re4n.internalhub.exception.AppException;
import com.re4n.internalhub.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class UserDAO extends BaseDAO<User>{
    @Override
    protected String getInsertSql() {
        return "INSERT INTO users (employee_id, first_name, last_name, corporate_email, password_hash, personal_email, salary, department, work_model, hire_date, is_active) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE users SET first_name = ?, last_name = ?, corporate_email = ?, personal_email = ?, salary = ?, department = ?, work_model = ?, hire_date = ?, is_active = ? WHERE id = ?";
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
        user.setPasswordHash(rs.getString("password_hash"));
        user.setSalary(rs.getBigDecimal("salary"));
        user.setDepartment(Department.valueOf(rs.getString("department")));
        user.setWorkModel(WorkModel.valueOf(rs.getString("work_model")));
        user.setHireDate(rs.getObject("hire_date", LocalDate.class));
        user.setActive(rs.getBoolean("is_active"));
        user.setRoleId(rs.getLong("role_id"));
        return user;
    }

    @Override
    protected void bindSaveParameters(PreparedStatement stmt, User entity) throws SQLException {
        stmt.setString(1, entity.getEmployeeId());
        stmt.setString(2, entity.getFirstName());
        stmt.setString(3, entity.getLastName());
        stmt.setString(4, entity.getCorporateEmail());
        stmt.setString(5, entity.getPasswordHash());
        stmt.setString(6, entity.getPersonalEmail());
        stmt.setBigDecimal(7, entity.getSalary());
        stmt.setString(8, entity.getDepartment().name());
        stmt.setString(9, entity.getWorkModel().name());
        stmt.setObject(10, entity.getHireDate());
        stmt.setBoolean(11, entity.getActive());
    }

    @Override
    protected void bindUpdateParameters(PreparedStatement stmt, User entity) throws SQLException {
        stmt.setString(1, entity.getFirstName());
        stmt.setString(2, entity.getLastName());
        stmt.setString(3, entity.getCorporateEmail());
        stmt.setString(4, entity.getPersonalEmail());
        stmt.setBigDecimal(5, entity.getSalary());
        stmt.setString(6, entity.getDepartment().name());
        stmt.setString(7, entity.getWorkModel().name());
        stmt.setObject(8, entity.getHireDate());
        stmt.setBoolean(9, entity.getActive());
        stmt.setLong(10, entity.getId());
    }

    public User findByEmail(String email){
        String sql = "SELECT * FROM users WHERE corporate_email = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, email);
            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    return mapResultSetToEntity(rs);
                }
                return null;
            }
        }catch(SQLException e){
            throw new AppException(AppError.DB_PERSISTENCE_VIOLATION, e);
        }
    }
}
