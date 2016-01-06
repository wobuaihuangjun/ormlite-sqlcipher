package com.example.huangzj.myapplication.way1;

import android.content.Context;

import com.example.huangzj.myapplication.way1.User;
import com.j256.ormlite.sqlcipher.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import net.sqlcipher.database.SQLiteDatabase;

import java.sql.SQLException;

/**
 * Created by Stay on 29/10/15.
 * Powered by www.stay4it.com
 */
public class CipherDbHelper extends OrmLiteSqliteOpenHelper {
    public static final int databaseVersion = 1;

    public CipherDbHelper(Context context, String databaseName, String password) {
        super(context, databaseName, password, null, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, User.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }
}
