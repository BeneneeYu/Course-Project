package com.photoSharing.entity;

/**
 * @program: Project
 * @description: 全球城市
 * @author: Shen Zhengyu
 * @create: 2020-07-13 15:18
 **/
public class geocities {
    private int GeoNameID;
    private String AsciiName;
    private String Country_RegionCodeISO;
    private double Latitude;
    private double Longitude;
    private String FeatureCode;
    private int Admin1Code;
        private String Admin2Code;
    private int Population;
    private int Elevation;
    private String TimeZone;

    public geocities() {
    }

    public int getGeoNameID() {
        return GeoNameID;
    }

    public geocities(int geoNameID, String asciiName, String country_RegionCodeISO, double latitude, double longitude, String featureCode, int admin1Code, String admin2Code, int population, int elevation, String timeZone) {
        GeoNameID = geoNameID;
        AsciiName = asciiName;
        Country_RegionCodeISO = country_RegionCodeISO;
        Latitude = latitude;
        Longitude = longitude;
        FeatureCode = featureCode;
        Admin1Code = admin1Code;
        Admin2Code = admin2Code;
        Population = population;
        Elevation = elevation;
        TimeZone = timeZone;
    }

    public void setGeoNameID(int geoNameID) {
        GeoNameID = geoNameID;
    }

    public String getAsciiName() {
        return AsciiName;
    }

    public void setAsciiName(String asciiName) {
        AsciiName = asciiName;
    }

    public String getCountry_RegionCodeISO() {
        return Country_RegionCodeISO;
    }

    public void setCountry_RegionCodeISO(String country_RegionCodeISO) {
        Country_RegionCodeISO = country_RegionCodeISO;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public String getFeatureCode() {
        return FeatureCode;
    }

    public void setFeatureCode(String featureCode) {
        FeatureCode = featureCode;
    }

    public int getAdmin1Code() {
        return Admin1Code;
    }

    public void setAdmin1Code(int admin1Code) {
        Admin1Code = admin1Code;
    }

    public String getAdmin2Code() {
        return Admin2Code;
    }

    public void setAdmin2Code(String admin2Code) {
        Admin2Code = admin2Code;
    }

    public int getPopulation() {
        return Population;
    }

    public void setPopulation(int population) {
        Population = population;
    }

    public int getElevation() {
        return Elevation;
    }

    public void setElevation(int elevation) {
        Elevation = elevation;
    }

    public String getTimeZone() {
        return TimeZone;
    }

    public void setTimeZone(String timeZone) {
        TimeZone = timeZone;
    }

    @Override
    public String toString() {
        return "geocities{" +
                "GeoNameID=" + GeoNameID +
                ", AsciiName='" + AsciiName + '\'' +
                ", Country_RegionCodeISO='" + Country_RegionCodeISO + '\'' +
                ", Latitude=" + Latitude +
                ", Longitude=" + Longitude +
                ", FeatureCode='" + FeatureCode + '\'' +
                ", Admin1Code=" + Admin1Code +
                ", Admin2Code=" + Admin2Code +
                ", Population=" + Population +
                ", Elevation=" + Elevation +
                ", TimeZone=" + TimeZone +
                '}';
    }
}
