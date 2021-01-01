package com.photoSharing.entity;

import java.util.Date;

/**
 * @program: Project
 * @description: 旅游图片的实体
 * @author: Shen Zhengyu
 * @create: 2020-07-13 14:02
 **/
public class travelimage {
    private int ImageID;
    private String Title;
    private String Description;
    private double Latitude;
    private double Longitude;
    private int CityCode;
    private String Country_RegionCodeISO;
    private int UID;
    private String PATH;
    private String Content;
    private int Heat;
    private Date DateUpdated;
    private String Author;

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public int getImageID() {
        return ImageID;
    }

    public void setImageID(int imageID) {
        ImageID = imageID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
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

    public int getCityCode() {
        return CityCode;
    }

    public void setCityCode(int cityCode) {
        CityCode = cityCode;
    }

    public String getCountry_RegionCodeISO() {
        return Country_RegionCodeISO;
    }

    public void setCountry_RegionCodeISO(String country_RegionCodeISO) {
        Country_RegionCodeISO = country_RegionCodeISO;
    }

    public int getUID() {
        return UID;
    }

    public void setUID(int UID) {
        this.UID = UID;
    }

    public String getPATH() {
        return PATH;
    }

    public void setPATH(String PATH) {
        this.PATH = PATH;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public int getHeat() {
        return Heat;
    }

    public void setHeat(int heat) {
        Heat = heat;
    }

    public Date getDateUpdated() {
        return DateUpdated;
    }

    public void setDateUpdated(Date dateUpdated) {
        DateUpdated = dateUpdated;
    }

    @Override
    public String toString() {
        return "travelimage{" +
                "ImageID=" + ImageID +
                ", Title='" + Title + '\'' +
                ", Description='" + Description + '\'' +
                ", Latitude=" + Latitude +
                ", Longitude=" + Longitude +
                ", CityCode=" + CityCode +
                ", Country_RegionCodeISO='" + Country_RegionCodeISO + '\'' +
                ", UID=" + UID +
                ", PATH='" + PATH + '\'' +
                ", Content='" + Content + '\'' +
                ", Heat=" + Heat +
                ", DateUpdated=" + DateUpdated +
                ", Author='" + Author + '\'' +
                '}';
    }
}
