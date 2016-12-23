package com.j256.ormlite.sqlcipher;

import android.app.Application;
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
    public static boolean importUnencryptedDatabase(Application application, String unencryptedDbName,
                                                    String encryptedDbName, String password) {
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

            database.rawExecSQL(String.format("ATTACH DATABASE '%s' AS encrypted KEY '%s'",
                    encryptedFile.getAbsolutePath(), password));
            database.rawExecSQL("select sqlcipher_export('encrypted')");
            database.rawExecSQL("DETACH DATABASE encrypted");

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

            application.deleteDatabase(unencryptedDbName);
            database.rawExecSQL(String.format("ATTACH DATABASE '%s' as plaintext KEY '';",
                    unencryptedFile.getAbsolutePath()));
            database.rawExecSQL("SELECT sqlcipher_export('plaintext');");
            database.rawExecSQL("DETACH DATABASE plaintext;");

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
