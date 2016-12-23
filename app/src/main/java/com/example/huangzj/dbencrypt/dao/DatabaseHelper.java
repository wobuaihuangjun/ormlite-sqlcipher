package com.example.huangzj.dbencrypt.dao;

import android.content.Context;
import android.database.Cursor;

import com.example.huangzj.dbencrypt.LogUtil;
import com.example.huangzj.dbencrypt.dao.bean.City;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.JavaxPersistence;
import com.j256.ormlite.sqlcipher.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTable;
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
    public static final String DATABASE_NAME = "unencrypted";
    public static final String ENCRYPTED_DATABASE_NAME = "encrypted";

    public static final String PASSWORD = "DB-way2-password";

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
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        daoMap = new HashMap<>();
    }

    public static DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (DatabaseHelper.class) {
                if (instance == null) {
                    instance = new DatabaseHelper(context);
                }
            }
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        createTables(sqLiteDatabase, connectionSource);
    }

    /**
     * 数据库升级，注意控制好数据库版本号，不然此方法将不会被调用到
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        dropTables(connectionSource);
        onCreate(sqLiteDatabase, connectionSource);
    }

    private void createTables(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        if (isTableExist(sqLiteDatabase, City.class)) {
            LogUtil.d("数据表已经存在了");
            return;
        }
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
        Dao dao;
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

    private <T> boolean isTableExist(SQLiteDatabase database, Class<T> clazz) {
        boolean isTableExist = false;
        String tableName = extractTableName(clazz);
        Cursor cursor = database.rawQuery("select * from sqlite_master where type = ? AND name = ?",
                new String[]{"table", tableName});
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex("sql");
            if (-1 != columnIndex && cursor.getCount() > 0) {
                isTableExist = true;
            }
            cursor.close();
        }
        return isTableExist;
    }

    /**
     * 获取表名
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> String extractTableName(Class<T> clazz) {
        DatabaseTable databaseTable = clazz.getAnnotation(DatabaseTable.class);
        String name;
        if (databaseTable != null && databaseTable.tableName() != null && databaseTable.tableName().length() > 0) {
            name = databaseTable.tableName();
        } else {
            name = JavaxPersistence.getEntityName(clazz);
            if (name == null) {
                name = clazz.getSimpleName().toLowerCase();
            }
        }
        return name;
    }
}
