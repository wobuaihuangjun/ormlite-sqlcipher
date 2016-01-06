package com.example.huangzj.myapplication.way2;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 售后网点市信息数据表
 * <p/>
 * Created by lhd on 2015/10/9.
 */
@DatabaseTable(tableName = "city")
public class City {

    @DatabaseField(generatedId = true)
    private Integer id;

    @DatabaseField
    private String cityNo;

    @DatabaseField
    private String cityName;

    @DatabaseField
    private String provinceNo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCityNo() {
        return cityNo;
    }

    public void setCityNo(String cityNo) {
        this.cityNo = cityNo;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getProvinceNo() {
        return provinceNo;
    }

    public void setProvinceNo(String provinceNo) {
        this.provinceNo = provinceNo;
    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", cityNo='" + cityNo + '\'' +
                ", cityName='" + cityName + '\'' +
                ", provinceNo='" + provinceNo + '\'' +
                '}';
    }
}
