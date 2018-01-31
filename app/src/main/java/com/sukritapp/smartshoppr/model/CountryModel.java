package com.sukritapp.smartshoppr.model;

/**
 * Created by abc on 2/19/2017.
 */
public class CountryModel {
    private  String countryName;
    private  String countryUrl;
    private  String countrydefaultLanguage;

    public String getCountrydefaultLanguage() {
        return countrydefaultLanguage;
    }

    public CountryModel(String countryName, String countryUrl,String countrydefaultLanguage) {
        this.countryName = countryName;
        this.countryUrl = countryUrl;
        this.countrydefaultLanguage=countrydefaultLanguage;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getCountryUrl() {
        return countryUrl;
    }
}
