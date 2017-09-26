package com.example.weatherapp;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class CitiesTable {
    static final String TABLENAME = "cities";
    static final String COLUMN_ID = "_id";
    static final String COLUMN_CITYNAME = "cityName";
    static final String COLUMN_COUNTRY = "country";
    static final String COLUMN_DATE = "date";
    static final String COLUMN_TEMPERATURE = "temperature";
    static final String COLUMN_FAVOURITE = "favorite";

    static public void onCreate(SQLiteDatabase db){
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE " + TABLENAME + " (");
        sb.append(COLUMN_ID + " integer primary key autoincrement, ");
        sb.append(COLUMN_CITYNAME + " text not null, ");
        sb.append(COLUMN_COUNTRY + " text not null, ");
        sb.append(COLUMN_DATE + " text not null, ");
        sb.append(COLUMN_TEMPERATURE + " real not null, ");
        sb.append(COLUMN_FAVOURITE + " integer not null);");

        try{
            db.execSQL(sb.toString());
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    static public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLENAME);
        CitiesTable.onCreate(db);
    }
}
