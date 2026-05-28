package com.re4n.internalhub.dao;

import com.re4n.internalhub.enums.Department;
import com.re4n.internalhub.model.Role;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleDAO extends BaseDAO<Role> {
    @Override
    protected String getInsertSql() {
        return "";
    }

    @Override
    protected String getUpdateSql() {
        return "";
    }

    @Override
    protected String getDeleteSql() {
        return "";
    }

    @Override
    protected String getSelectByIdSql() {
        return "";
    }

    @Override
    protected String getSelectAllSql() {
        return "";
    }

    @Override
    protected Role mapResultSetToEntity(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    protected void bindSaveParameters(PreparedStatement stmt, Role entity) throws SQLException {

    }

    @Override
    protected void bindUpdateParameters(PreparedStatement stmt, Role entity) throws SQLException {

    }
}
