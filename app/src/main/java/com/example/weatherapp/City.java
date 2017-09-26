package com.example.weatherapp;

import java.io.Serializable;

public class City implements Serializable {
    private long _id;
    private String cityName, country;
    private String temperature;
    private int favorite;
    private String date;
    public City() {
    }

    public City(String cityName, String country, String temperature, int favorite) {
        this.cityName = cityName;
        this.country = country;
        this.temperature = temperature;
        this.favorite = favorite;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    @Override
    public String toString() {
        return "City{" +
                "cityName='" + cityName + '\'' +
                ", country='" + country + '\'' +
                ", temperature='" + temperature + '\'' +
                ", favorite=" + favorite +
                '}';
    }
}
