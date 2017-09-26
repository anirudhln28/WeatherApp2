package com.example.weatherapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

public class WeatherUtil {
    static public class WeatherJSONParser{
        static ArrayList<ArrayList<Weather>> parseWeather(String input){
            ArrayList<ArrayList<Weather>> forecastList = new ArrayList<>();
            JSONObject root = null;
            String date = "";
            int index = 0;

            try {
                root = new JSONObject(input);
                JSONArray JSONWeatherArray = root.getJSONArray("list");
                forecastList.add(new ArrayList<Weather>());
                for(int i=0; i<JSONWeatherArray.length();i++){
                    JSONObject JSONWeatherObject = JSONWeatherArray.getJSONObject(i);
                    Weather weatherObject = new Weather();

                    String dateTime = JSONWeatherObject.getString("dt_txt");
                    StringTokenizer st = new StringTokenizer(dateTime);
                    weatherObject.setDate(formatDate(st.nextToken()));
                    weatherObject.setTime(formatTime(st.nextToken()));
                    weatherObject.setTemperature(JSONWeatherObject.getJSONObject("main").getString("temp"));    //+ (char) 0x00B0

                    weatherObject.setPressure(JSONWeatherObject.getJSONObject("main").getString("pressure")+" hPa");
                    weatherObject.setHumidity(JSONWeatherObject.getJSONObject("main").getString("humidity")+" %");
                    weatherObject.setWindspeed(JSONWeatherObject.getJSONObject("wind").getString("speed")+" mps");
                    weatherObject.setWindDirection(JSONWeatherObject.getJSONObject("wind").getString("deg"));
                    JSONArray cond = JSONWeatherObject.getJSONArray("weather");
                    JSONObject condition = cond.getJSONObject(0);
                    weatherObject.setClimateType(condition.getString("description"));
                    String icon = condition.getString("icon");
                    weatherObject.setIconurl("http://openweathermap.org/img/w/"+icon+".png");

                    if(date.equals("")){
                        date = weatherObject.getDate();
                    }

                    if(date.compareTo(weatherObject.getDate()) == 0)
                        forecastList.get(index).add(weatherObject);
                    else if(date.compareTo(weatherObject.getDate()) < 0){
                        forecastList.add(new ArrayList<Weather>());
                        index++;
                        date = weatherObject.getDate();
                        forecastList.get(index).add(weatherObject);
                    }
                }
                return forecastList;
            } catch (JSONException e) {
                return forecastList;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        private static String formatTime(String s) throws ParseException {
            SimpleDateFormat sdfSource = new SimpleDateFormat("HH:mm:ss");
            Date date = sdfSource.parse(s);
            SimpleDateFormat sdfDestination = new SimpleDateFormat("hh:mm aaa");
            return sdfDestination.format(date);
        }

        private static String formatDate(String s) throws ParseException {
            SimpleDateFormat sdfSource = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = sdfSource.parse(s);
            SimpleDateFormat sdfDestination = new SimpleDateFormat("MMM d, yyyy");
            return sdfDestination.format(date1);
        }
    }
}