package com.example.weatherapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class CitiesDAO {
    private SQLiteDatabase db;


    public CitiesDAO(SQLiteDatabase db)
    {
        this.db = db;
    }

    public long save(City city){
        ContentValues values = new ContentValues();
        values.put(CitiesTable.COLUMN_CITYNAME, city.getCityName());
        values.put(CitiesTable.COLUMN_COUNTRY, city.getCountry());
        values.put(CitiesTable.COLUMN_DATE, city.getDate());
        values.put(CitiesTable.COLUMN_TEMPERATURE, city.getTemperature());
        values.put(CitiesTable.COLUMN_FAVOURITE, city.getFavorite());
        return db.insert(CitiesTable.TABLENAME, null, values);
    }

    public boolean update(City city){
        ContentValues values = new ContentValues();
        values.put(CitiesTable.COLUMN_CITYNAME, city.getCityName());
        values.put(CitiesTable.COLUMN_COUNTRY, city.getCountry());
        values.put(CitiesTable.COLUMN_DATE, city.getDate());
        values.put(CitiesTable.COLUMN_TEMPERATURE, city.getTemperature());
        values.put(CitiesTable.COLUMN_FAVOURITE, city.getFavorite());
        return db.update(CitiesTable.TABLENAME, values, CitiesTable.COLUMN_ID + "= ?", new String[]{city.get_id()+""}) > 0;
    }

    public boolean delete(City city){
        return db.delete(CitiesTable.TABLENAME, CitiesTable.COLUMN_ID + "=?", new String[]{city.get_id()+""}) > 0;
    }

    public City get(long id){
        City city = null;
        Cursor c = db.query(true, CitiesTable.TABLENAME, new String[] {
                        CitiesTable.COLUMN_ID, CitiesTable.COLUMN_CITYNAME, CitiesTable.COLUMN_COUNTRY,CitiesTable.COLUMN_DATE,
                        CitiesTable.COLUMN_TEMPERATURE, CitiesTable.COLUMN_FAVOURITE },
                CitiesTable.COLUMN_ID + "= ?" ,
                new String[] {id + ""}, null, null, null, null, null);

        if (c != null && c.moveToFirst()){
            city = buidCityFromCursor(c);
            if (!c.isClosed()){
                c.close();
            }
        }
        return city;
    }

    public List<City> getAll(){
        List<City> cities = new ArrayList<City>();
        Cursor c = db.query(CitiesTable.TABLENAME, new String[] {
                        CitiesTable.COLUMN_ID, CitiesTable.COLUMN_CITYNAME, CitiesTable.COLUMN_COUNTRY,CitiesTable.COLUMN_DATE,
                        CitiesTable.COLUMN_TEMPERATURE, CitiesTable.COLUMN_FAVOURITE},
                null, null, null, null, null);

        if (c != null && c.moveToFirst()){
            do {
                City city = buidCityFromCursor(c);
                if(city != null){
                    cities.add(city);
                }
            } while (c.moveToNext());

            if (!c.isClosed()){
                c.close();
            }
        }
        return cities;
    }

    public City buidCityFromCursor(Cursor c){
        City city = null;
        if(c != null){
            city = new City();
            city.set_id(c.getLong(0));
            city.setCityName(c.getString(1));
            city.setCountry(c.getString(2));
            city.setDate(c.getString(3));
            city.setTemperature(c.getString(4));
            city.setFavorite(c.getInt(5));
        }
        return city;
    }
}
