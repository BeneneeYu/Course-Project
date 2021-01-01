package com.photoSharing.entity;

/**
 * @program: Project
 * @description: 大洲
 * @author: Shen Zhengyu
 * @create: 2020-07-13 15:21
 **/
public class geocontinents {
    private String ContinentCode;
    private String ContinentName;
    private int GeoNameId;

    public String getContinentCode() {
        return ContinentCode;
    }

    public void setContinentCode(String continentCode) {
        ContinentCode = continentCode;
    }

    public String getContinentName() {
        return ContinentName;
    }

    public void setContinentName(String continentName) {
        ContinentName = continentName;
    }

    public int getGeoNameId() {
        return GeoNameId;
    }

    public void setGeoNameId(int geoNameId) {
        GeoNameId = geoNameId;
    }

    @Override
    public String toString() {
        return "geocontinents{" +
                "ContinentCode='" + ContinentCode + '\'' +
                ", ContinentName='" + ContinentName + '\'' +
                ", GeoNameId=" + GeoNameId +
                '}';
    }
}
