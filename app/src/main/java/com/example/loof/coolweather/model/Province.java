package com.example.loof.coolweather.model;

/**
 * Created by loof on 2016/3/12.
 */
public class Province {
    private int id;
    private String provinceName;
    private String provinceCode;

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {

        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
