package com.re4n.internalhub.dao;

import com.re4n.internalhub.config.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
    protected abstract void bindSaveParameters(PreparedStatement stmt, T entity) throws SQLException;
    protected abstract void bindUpdateParameters(PreparedStatement stmt, T entity) throws SQLException;

    @Override
    public void save(T entity) {
        String sql = getInsertSql();

        try(Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            bindSaveParameters(stmt, entity);
            stmt.executeUpdate();
        }catch (SQLException e){
            System.err.println("[ERROR] - " + e.getMessage());
        }
    }

    @Override
    public void update(T entity) {
        String sql = getUpdateSql();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            bindUpdateParameters(stmt, entity);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[ERROR] - " + e.getMessage());
        }
    }

    @Override
    public void delete(Long id) {
        String sql = getDeleteSql();

        try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setLong(1, id);

            int affectedRows = stmt.executeUpdate();

            if(affectedRows > 0){
                System.out.println("Success!");
            }else{
                System.out.println("Fail! ");
            }
        }catch(SQLException e){
            System.err.println("[ERROR] - " + e.getMessage());
        }
    }

    @Override
    public T findById(Long id) {
        String sql = getSelectByIdSql();
        T entity = null;

        try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()){
                if (rs.next()){
                   entity = mapResultSetToEntity(rs);
                }
            }
        }catch (SQLException e){
            System.err.println("[ERROR] - " + e.getMessage());
        }
        return entity;
    }

    @Override
    public List<T> findAll() {
        String sql = getSelectAllSql();
        List<T> entityList = new java.util.ArrayList<>();

        try(Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            try(ResultSet rs = stmt.executeQuery()){
                while (rs.next()){
                    T entity  = mapResultSetToEntity(rs);
                    entityList.add(entity);
                }
            }
        }catch (SQLException e){
            System.err.println("[ERROR] - " + e.getMessage());
        }
        return entityList;
    }
}
