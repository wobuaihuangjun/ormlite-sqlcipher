package com.example.huangzj.myapplication.way1;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

/**
 * Created by Stay on 29/10/15.
 * Powered by www.stay4it.com
 */
public abstract class BaseDAO<T> {

    public abstract Dao<T,String> getDAO() throws SQLException;

    public void addOrUpdate(T t) {
        try {
            getDAO().createOrUpdate(t);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public T queryById(String id){
        try {
            return getDAO().queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    public void delete(T t) {
        try {
            getDAO().delete(t);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(String id) {
        try {
            getDAO().deleteById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
