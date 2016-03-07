package com.example.huangzj.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.huangzj.myapplication.way2.City;
import com.example.huangzj.myapplication.way2.CityDao;
import com.example.huangzj.myapplication.way1.DBManager;
import com.example.huangzj.myapplication.way1.User;
import com.example.huangzj.myapplication.way1.UserDAO;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView way1;
    private TextView way2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        way1 = (TextView) findViewById(R.id.way1);
        way2 = (TextView) findViewById(R.id.way2);

//        testWay1();
        testWay2();
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
        city.setProvinceNo("2");
        city.setCityName("东莞");

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

    private void testWay1() {
        User user = new User();
        user.id = "1000000";
        user.age = 18;
        user.name = "antush";
        way1.append("存入记录：");
        way1.append(user.toString());
        UserDAO.getInstance().addOrUpdate(user);

        User user1 = UserDAO.getInstance().queryById(user.id);
        way1.append("\n");
        way1.append("查询记录：");
        way1.append(user1.toString());
    }

}
