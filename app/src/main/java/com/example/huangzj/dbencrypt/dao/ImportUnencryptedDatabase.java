package com.example.huangzj.dbencrypt.dao;

import android.database.Cursor;
import android.util.Log;

import com.example.huangzj.dbencrypt.LogUtil;
import com.example.huangzj.dbencrypt.MyApplication;

import net.sqlcipher.database.SQLiteDatabase;

import java.io.File;


/**
 * 导入一个未加密的数据库
 */
public class ImportUnencryptedDatabase {
    public static final String TAG = "ImportUnencrypted";

    private SQLiteDatabase database;

    public boolean execute() {
        File unencryptedDatabase = MyApplication.getContext().getDatabasePath("unencrypted");
        if (!unencryptedDatabase.exists()) {
            Log.i(TAG, "unencryptedDatabase not exist");
            return true;
        }

        File encryptedDatabase = MyApplication.getContext().getDatabasePath("encrypted");
        if (encryptedDatabase.exists()) {
            Log.i(TAG, "encryptedDatabase is exist");
        }

        try {
            database = SQLiteDatabase.openOrCreateDatabase(unencryptedDatabase, "", null);

            Log.i(TAG, "未加密的数据库数据");
            Cursor cursor = database.rawQuery("select * from city", new String[]{});
            cursor.moveToFirst();
            String a = cursor.getString(cursor.getColumnIndex("cityName"));
            LogUtil.d(a);
            cursor.close();

            database.rawExecSQL(String.format("ATTACH DATABASE '%s' AS encrypted KEY '%s'",
                    encryptedDatabase.getAbsolutePath(), DatabaseHelper.PASSWORD));
            database.rawExecSQL("select sqlcipher_export('encrypted')");
            database.rawExecSQL("DETACH DATABASE encrypted");
            database.close();

            // 测试数据是否拷贝成功
            database = SQLiteDatabase.openOrCreateDatabase(encryptedDatabase, DatabaseHelper.PASSWORD, null);
            Log.i(TAG, "加密的数据库数据");
            cursor = database.rawQuery("select * from city", new String[]{});
            cursor.moveToFirst();
            a = cursor.getString(cursor.getColumnIndex("cityName"));
            LogUtil.d(a);
            cursor.close();
            return true;
        } catch (Exception e) {
            LogUtil.e(TAG, e);
            return false;
        } finally {
            if (database != null) {
                database.close();
            }
            unencryptedDatabase.delete();
//            encryptedDatabase.delete();
            SQLiteDatabase.releaseMemory();
        }
    }

}
