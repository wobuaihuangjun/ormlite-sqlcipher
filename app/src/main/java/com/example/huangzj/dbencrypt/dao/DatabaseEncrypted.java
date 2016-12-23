package com.example.huangzj.dbencrypt.dao;

import android.app.Application;
import android.database.Cursor;
import android.util.Log;

import net.sqlcipher.database.SQLiteDatabase;

import java.io.File;


/**
 * 数据库加密
 */
public class DatabaseEncrypted {

    private static final String TAG = "DatabaseEncrypted";

    /**
     * 将未加密数据库转换为加密数据库
     *
     * @param application       application
     * @param unencryptedDbName 未加密数据库名
     * @param encryptedDbName   加密数据库名
     * @param password          加密数据库密码
     */
    public static boolean importUnencryptedDatabase(Application application, String unencryptedDbName, String encryptedDbName, String password) {
        File unencryptedFile = application.getDatabasePath(unencryptedDbName);
        if (!unencryptedFile.exists()) {
            Log.i(TAG, "unencrypted database not exist");
            return true;
        }

        File encryptedFile = application.getDatabasePath(encryptedDbName);
        if (encryptedFile.exists()) {
            Log.i(TAG, "encrypted database is exist");
            return true;
        }

        Log.i(TAG, "encrypted database");
        SQLiteDatabase database = null;
        try {
            database = SQLiteDatabase.openOrCreateDatabase(unencryptedFile, "", null);

            Log.i(TAG, "未加密的数据库数据");
            Cursor cursor = database.rawQuery("select * from city", new String[]{});
            while (cursor.moveToNext()) {
                Log.i(TAG, "cityName：" + cursor.getString(cursor.getColumnIndex("cityName")));
            }
            cursor.close();

            database.rawExecSQL(String.format("ATTACH DATABASE '%s' AS encrypted KEY '%s'",
                    encryptedFile.getAbsolutePath(), password));
            database.rawExecSQL("select sqlcipher_export('encrypted')");
            database.rawExecSQL("DETACH DATABASE encrypted");

            // 测试数据是否拷贝成功
            database = SQLiteDatabase.openOrCreateDatabase(encryptedFile, password, null);
            Log.i(TAG, "加密的数据库数据");
            while (cursor.moveToNext()) {
                Log.i(TAG, "cityName：" + cursor.getString(cursor.getColumnIndex("cityName")));
            }
            cursor.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (database != null) {
                database.close();
            }
            application.deleteDatabase(unencryptedDbName);
            SQLiteDatabase.releaseMemory();
        }
    }

    /**
     * 将加密数据库转换为常规数据库
     *
     * @param application       application
     * @param encryptedDbName   加密数据库名
     * @param unencryptedDbName 未加密数据库名
     * @param password          加密数据库密码
     */
    public static boolean exportToUnencryptedDatabase(Application application, String encryptedDbName, String unencryptedDbName, String password) {
        File encryptedFile = application.getDatabasePath(encryptedDbName);
        if (!encryptedFile.exists()) {
            Log.i(TAG, "encrypted database not exist");
            return true;
        }

        File unencryptedFile = application.getDatabasePath(unencryptedDbName);
        if (unencryptedFile.exists()) {
            Log.i(TAG, "unencrypted database is exist");
            return true;
        }

        Log.i(TAG, "unencrypted database");
        SQLiteDatabase database = null;
        try {
            database = SQLiteDatabase.openOrCreateDatabase(encryptedFile, password, null);

            Log.i(TAG, "加密的数据库数据");
            Cursor cursor = database.rawQuery("select * from city", new String[]{});
            while (cursor.moveToNext()) {
                Log.i(TAG, "cityName：" + cursor.getString(cursor.getColumnIndex("cityName")));
            }
            cursor.close();

            application.deleteDatabase(unencryptedDbName);
            database.rawExecSQL(String.format("ATTACH DATABASE '%s' as plaintext KEY '';",
                    unencryptedFile.getAbsolutePath()));
            database.rawExecSQL("SELECT sqlcipher_export('plaintext');");
            database.rawExecSQL("DETACH DATABASE plaintext;");

            // 测试数据是否拷贝成功
            database = SQLiteDatabase.openOrCreateDatabase(unencryptedFile, "", null, null);
            Log.i(TAG, "解密的数据库数据");
            cursor = database.rawQuery("select * from city", new String[]{});
            while (cursor.moveToNext()) {
                Log.i(TAG, "cityName：" + cursor.getString(cursor.getColumnIndex("cityName")));
            }

            cursor.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (database != null) {
                database.close();
            }
            application.deleteDatabase(encryptedDbName);
            SQLiteDatabase.releaseMemory();
        }
    }

}
