package com.example.huangzj.dbencrypt;

import android.app.Application;
import android.util.Log;

import com.example.huangzj.dbencrypt.dao.ImportUnencryptedDatabase;
import com.example.huangzj.dbencrypt.dao.bean.City;
import com.example.huangzj.dbencrypt.dao.bean.CityDao;

import net.sqlcipher.database.SQLiteDatabase;

import java.util.List;


/**
 * Created by huangzj on 2015/12/21.
 */
public class MyApplication extends Application {

    public static final String TAG = "MyApplication";
    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "3");
        instance = this;

        initDb();

        testCity();
    }

    private void initDb() {
        SQLiteDatabase.loadLibs(this);
        new ImportUnencryptedDatabase().execute();

    }

    private void testCity() {
        Log.e(TAG, "testCity");
        CityDao cityDao = new CityDao(this);
        List<City> list = cityDao.queryForAll();
        if (list != null)
            Log.e(TAG, "数据count：" + list.size());
    }

    public static MyApplication getContext() {
        return instance;
    }
}
