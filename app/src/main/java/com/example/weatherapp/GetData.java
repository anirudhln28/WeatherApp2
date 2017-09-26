package com.example.weatherapp;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class GetData extends AsyncTask<String, Void, ArrayList<ArrayList<Weather>>>
{

    Idata activity;

    public GetData(Idata activity)
     {
        this.activity = activity;
    }

    @Override
    protected ArrayList<ArrayList<Weather>> doInBackground(String... strings) {
        BufferedReader reader = null;
        ArrayList<ArrayList<Weather>> weatherList = new ArrayList<>();
        StringBuilder sb = null;
        try {
            publishProgress();
            URL url = new URL(strings[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int statusCode = connection.getResponseCode();
            if(statusCode == HttpURLConnection.HTTP_OK){
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null){
                    sb.append(line + "\n");
                }
                weatherList = WeatherUtil.WeatherJSONParser.parseWeather(sb.toString());
                return weatherList;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return weatherList;
    }

    @Override
    protected void onPostExecute(ArrayList<ArrayList<Weather>> weatherList) {
        super.onPostExecute(weatherList);
        activity.setList(weatherList);

    }
    public interface Idata{
        void setList(ArrayList<ArrayList<Weather>> list);
    }
}
