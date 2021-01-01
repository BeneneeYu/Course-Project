package com.photoSharing.entity;

/**
 * @program: Project
 * @description: 区域
 * @author: Shen Zhengyu
 * @create: 2020-07-13 15:26
 **/
public class geocountries_regions {
    private String ISO;
    private String fipsCountry_RegionCode;
    private String ISO3;
    private String ISONumeric;
    private String Country_RegionName;
    private String Capital;
    private String Area;
    private String Continent;
    private int GeoNameID;
    private int Population;
    private String TopLevelDomain;
    private String CurrencyCode;
    private String CurrencyName;
    private String PhoneCountry_RegionCode;
    private String Languages;
    private String PostalCodeFormat;
    private String PostalCodeRegex;
    private String Neighbours;
    private String Country_RegionDescription;

    public String getISO() {
        return ISO;
    }

    public void setISO(String ISO) {
        this.ISO = ISO;
    }

    public String getFipsCountry_RegionCode() {
        return fipsCountry_RegionCode;
    }

    public void setFipsCountry_RegionCode(String fipsCountry_RegionCode) {
        this.fipsCountry_RegionCode = fipsCountry_RegionCode;
    }

    public String getISO3() {
        return ISO3;
    }

    public void setISO3(String ISO3) {
        this.ISO3 = ISO3;
    }

    public String getISONumeric() {
        return ISONumeric;
    }

    public void setISONumeric(String ISONumeric) {
        this.ISONumeric = ISONumeric;
    }

    public String getCountry_RegionName() {
        return Country_RegionName;
    }

    public void setCountry_RegionName(String country_RegionName) {
        Country_RegionName = country_RegionName;
    }

    public String getCapital() {
        return Capital;
    }

    public void setCapital(String capital) {
        Capital = capital;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public String getContinent() {
        return Continent;
    }

    public void setContinent(String continent) {
        Continent = continent;
    }

    public int getGeoNameID() {
        return GeoNameID;
    }

    public void setGeoNameID(int geoNameID) {
        GeoNameID = geoNameID;
    }

    public int getPopulation() {
        return Population;
    }

    public void setPopulation(int population) {
        Population = population;
    }

    public String getTopLevelDomain() {
        return TopLevelDomain;
    }

    public void setTopLevelDomain(String topLevelDomain) {
        TopLevelDomain = topLevelDomain;
    }

    public String getCurrencyCode() {
        return CurrencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        CurrencyCode = currencyCode;
    }

    public String getCurrencyName() {
        return CurrencyName;
    }

    public void setCurrencyName(String currencyName) {
        CurrencyName = currencyName;
    }

    public String getPhoneCountry_RegionCode() {
        return PhoneCountry_RegionCode;
    }

    public void setPhoneCountry_RegionCode(String phoneCountry_RegionCode) {
        PhoneCountry_RegionCode = phoneCountry_RegionCode;
    }

    public String getLanguages() {
        return Languages;
    }

    public void setLanguages(String languages) {
        Languages = languages;
    }

    public String getPostalCodeFormat() {
        return PostalCodeFormat;
    }

    public void setPostalCodeFormat(String postalCodeFormat) {
        PostalCodeFormat = postalCodeFormat;
    }

    public String getPostalCodeRegex() {
        return PostalCodeRegex;
    }

    public void setPostalCodeRegex(String postalCodeRegex) {
        PostalCodeRegex = postalCodeRegex;
    }

    public String getNeighbours() {
        return Neighbours;
    }

    public void setNeighbours(String neighbours) {
        Neighbours = neighbours;
    }

    public String getCountry_RegionDescription() {
        return Country_RegionDescription;
    }

    public void setCountry_RegionDescription(String country_RegionDescription) {
        Country_RegionDescription = country_RegionDescription;
    }

    @Override
    public String toString() {
        return "geocountries_regions{" +
                "ISO='" + ISO + '\'' +
                ", fipsCountry_RegionCode='" + fipsCountry_RegionCode + '\'' +
                ", ISO3='" + ISO3 + '\'' +
                ", ISONumeric='" + ISONumeric + '\'' +
                ", Country_RegionName='" + Country_RegionName + '\'' +
                ", Capital='" + Capital + '\'' +
                ", Area='" + Area + '\'' +
                ", Continent='" + Continent + '\'' +
                ", GeoNameID=" + GeoNameID +
                ", Population=" + Population +
                ", TopLevelDomain='" + TopLevelDomain + '\'' +
                ", CurrencyCode='" + CurrencyCode + '\'' +
                ", CurrencyName='" + CurrencyName + '\'' +
                ", PhoneCountry_RegionCode='" + PhoneCountry_RegionCode + '\'' +
                ", Languages='" + Languages + '\'' +
                ", PostalCodeFormat='" + PostalCodeFormat + '\'' +
                ", PostalCodeRegex='" + PostalCodeRegex + '\'' +
                ", Neighbours='" + Neighbours + '\'' +
                ", Country_RegionDescription='" + Country_RegionDescription + '\'' +
                '}';
    }
}
