package com.example.huangzj.myapplication.way1;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import net.sqlcipher.database.SQLiteDatabase;

import java.sql.SQLException;

/**
 * Created by Stay on 29/10/15.
 * Powered by www.stay4it.com
 */
public class DBManager {
    private static DBManager instance;
    private CipherDbHelper dbHelper;

    private DBManager() {

    }

    private DBManager(Context context, String databaseName, String password) {
        SQLiteDatabase.loadLibs(context);
        dbHelper = new CipherDbHelper(context, databaseName, password);
    }

    public static void init(Context context, String databaseName, String password) {
        instance = new DBManager(context, databaseName, password);
    }

    public static DBManager getInstance() {
        if (instance == null) {
            throw new IllegalArgumentException("you should call DBManager.init(context) first");
        }
        return instance;
    }

    public void closeDatabase() {
        dbHelper.close();
        dbHelper = null;
        instance = null;
    }

    public <D extends Dao<T, String>, T> D getDAO(Class<T> clz) throws SQLException {
        return dbHelper.getDao(clz);
    }


}
