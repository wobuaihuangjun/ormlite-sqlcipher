package com.example.huangzj.myapplication.way2;

import android.content.Context;

import com.example.huangzj.myapplication.LogUtil;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.sqlcipher.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import net.sqlcipher.database.SQLiteDatabase;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * ormlite操作数据库Helper
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    /**
     * 数据库名称
     */
    private static final String DATABASE_NAME = "DB-way2";

    private static final String PASSWORD = "DB-way2-password";

    /**
     * 数据库版本号
     */
    private static final int DATABASE_VERSION = 1;

    private static DatabaseHelper instance;

    /**
     * dao缓存
     */
    private Map<String, Dao> daoMap;

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, PASSWORD, null, DATABASE_VERSION);
        daoMap = new HashMap<>();
    }

    public static DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (DatabaseHelper.class) {
                if (instance == null) {
                    SQLiteDatabase.loadLibs(context);
                    instance = new DatabaseHelper(context);
                }
            }
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        createTables(connectionSource);
    }

    /**
     * 数据库升级，注意控制好数据库版本号，不然此方法将不会被调用到
     *
     * @param sqLiteDatabase
     * @param connectionSource
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        dropTables(connectionSource);
        onCreate(sqLiteDatabase, connectionSource);
    }

    private void createTables(ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, City.class);
        } catch (SQLException e) {
            LogUtil.e(e);
        }
    }

    private void dropTables(ConnectionSource connectionSource) {
        try {
            TableUtils.dropTable(connectionSource, City.class, true);
        } catch (SQLException e) {
            LogUtil.e(e);
        }
    }

    public synchronized Dao getDao(Class cls) {
        Dao dao = null;
        String clsName = cls.getSimpleName();
        if (daoMap.containsKey(clsName)) {
            dao = daoMap.get(clsName);
        } else {
            try {
                dao = super.getDao(cls);
            } catch (SQLException e) {
                LogUtil.e(e);
                return null;
            }
            daoMap.put(clsName, dao);
        }
        return dao;
    }

    @Override
    public void close() {
        super.close();
        synchronized (this) {
            Iterator<Map.Entry<String, Dao>> it = daoMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Dao> entry = it.next();
                Dao dao = entry.getValue();
                dao = null;
                it.remove();
            }
        }
    }
}
