package com.example.huangzj.dbencrypt;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.huangzj.dbencrypt.dao.DatabaseHelper;
import com.example.huangzj.dbencrypt.dao.bean.City;
import com.example.huangzj.dbencrypt.dao.bean.CityDao;

import net.sqlcipher.database.SQLiteDatabase;

import java.io.File;
import java.util.List;

public class MainActivity extends Activity {

    public static final String TAG = "MainActivity";

    private TextView way1;
    private TextView way2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        way1 = (TextView) findViewById(R.id.way1);
        way2 = (TextView) findViewById(R.id.way2);

        testWay2();

        dbIsUpdate();
    }

    private void dbIsUpdate() {
        File unencryptedDatabase = MyApplication.getContext().getDatabasePath(DatabaseHelper.DATABASE_NAME);
        if (unencryptedDatabase.exists()) {
            Log.e(TAG, "unencrypted database is exist");
        }

        File encryptedDatabase = MyApplication.getContext().getDatabasePath(DatabaseHelper.ENCRYPTED_DATABASE_NAME);
        if (encryptedDatabase.exists()) {
            Log.e(TAG, "encrypted database is exist");
        }

//        Log.i(TAG, "数据库名：" + DatabaseHelper.DATABASE_NAME);
//        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(
//                DatabaseHelper.DATABASE_NAME, "", null);
//        Log.i(TAG, "加密的数据库数据");
//        Cursor cursor = database.rawQuery("select * from city", new String[]{});
//        cursor.moveToFirst();
//        String a = cursor.getString(cursor.getColumnIndex("cityName"));
//        Log.d(TAG, a);
//        cursor.close();
    }

    private void testWay2() {
        CityDao cityDao = new CityDao(this);

        List<City> list = cityDao.queryForAll();

        String cityNo = "0";

        if (list != null) {
            cityNo = list.size() + "";
        }

        City city = new City();
        city.setCityNo(cityNo);
        city.setProvinceNo("5");
        city.setCityName("深圳");

        if (cityDao.insert(city)) {
            way2.append("\n");
            way2.append("存入记录成功：" + city.toString());
        }


        city = cityDao.queryByCityNo(cityNo);
        way2.append("\n");
        way2.append(city.toString());


        list = cityDao.queryForAll();
        if (list != null) {
            way2.append("\n");
            way2.append("数据count：" + list.size());
        }

    }

}
