package com.example.huangzj.myapplication.way2;

import android.content.Context;

import java.util.List;


public class CityDao extends OrmLiteDao<City> {

    public CityDao(Context context) {
        super(context, City.class);
    }

    public boolean createForBatch(List<City> list) {
        return super.insertForBatch(list);
    }

    public List<City> queryByProvinceNo(String provinceNo) {
        return super.queryByColumnName("provinceNo", provinceNo);
    }

    public City queryByCityNo(String cityNo) {
        return super.queryForFirst("cityNo", cityNo);
    }
}
