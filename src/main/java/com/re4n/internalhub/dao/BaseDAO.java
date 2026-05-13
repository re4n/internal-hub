package com.re4n.internalhub.dao;

import com.re4n.internalhub.config.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public abstract class BaseDAO<T> implements GenericDAO<T> {

    protected Connection getConnection() throws SQLException{
        return DbConnection.createConnection();
    }

    protected abstract String getInsertSql();
    protected abstract String getUpdateSql();
    protected abstract String getDeleteSql();
    protected abstract String getSelectByIdSql();
    protected abstract String getSelectAllSql();
    protected abstract T mapResultSetToEntity(ResultSet rs ) throws SQLException;
    protected  abstract void bindParameters(PreparedStatement stmt, T entity) throws SQLException;

    @Override
    public void save(T entity) {
        String sql = getInsertSql();

        try(Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            bindParameters(stmt, entity);
            stmt.executeUpdate();
        }catch (SQLException e){
            System.err.println("[ERROR] - " + e.getMessage());
        }
    }

    @Override
    public void update(T entity) {

    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public T findById(Long id) {
        return null;
    }

    @Override
    public List<T> findAll() {
        return List.of();
    }
}
