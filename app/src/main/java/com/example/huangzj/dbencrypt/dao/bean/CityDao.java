package com.example.huangzj.dbencrypt.dao.bean;

import android.content.Context;

import com.example.huangzj.dbencrypt.dao.ormlite.OrmLiteDao;

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
