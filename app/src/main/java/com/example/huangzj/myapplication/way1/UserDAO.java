package com.example.huangzj.myapplication.way1;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

/**
 * Created by Stay on 29/10/15.
 * Powered by www.stay4it.com
 */
public class UserDAO extends BaseDAO<User> {
    private static UserDAO instance;

    private UserDAO() {
    }

    public synchronized static UserDAO getInstance() {
        if (instance == null) {
            instance = new UserDAO();
        }
        return instance;
    }

    @Override
    public Dao<User, String> getDAO() throws SQLException {
        return DBManager.getInstance().getDAO(User.class);
    }
}
