package com.example.huangzj.myapplication;

import android.app.Application;
import android.util.Log;

import com.example.huangzj.myapplication.way1.DBManager;
import com.example.huangzj.myapplication.way2.City;
import com.example.huangzj.myapplication.way2.CityDao;

import net.sqlcipher.database.SQLiteDatabase;

import java.util.List;

/**
 * Created by huangzj on 2015/12/21.
 */
public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

//        DBManager.init(this, "DB-way1", "DB-way1-password");
        SQLiteDatabase.loadLibs(this);
        testCity();
    }

    private void testCity() {

        Log.e("MyApplication", "testCity");
        CityDao cityDao = new CityDao(this);
        List<City> list = cityDao.queryForAll();
        if (list != null)
            Log.e("MyApplication", "数据count：" + list.size());
    }
}
