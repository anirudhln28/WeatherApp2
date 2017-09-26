package com.example.weatherapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CityWeatherActivity extends AppCompatActivity implements GetData.Idata{
    TextView currentcity, HourlyDate;
    String url;
    ProgressDialog pd;
    String city,country;
    RecyclerView dayDataRecyclerView, hourlyDataRecyclerView;
    LinearLayoutManager linearLayoutManager1, linearLayoutManager2;
    DailyForecastAdapter dayAdapter;
    HourlyForecastAdapter hourAdapter;
    DatabaseDataManager dm;
    ArrayList<ArrayList<Weather>> temp_list;
    List<City> savedCities;
    String temp_unit=MainActivity.temp_unit;
    int pos=0;

    SharedPreferences prefs;
    PrefChangeListener mListner = null;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cityweather);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("City Weather");

        currentcity = (TextView) findViewById(R.id.currentCity);
        HourlyDate = (TextView) findViewById(R.id.HourlyForecastDate);
        dayDataRecyclerView = (RecyclerView) findViewById(R.id.FiveDayRecyclerView);
        linearLayoutManager1 = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        dayDataRecyclerView.setLayoutManager(linearLayoutManager1);

        hourlyDataRecyclerView = (RecyclerView) findViewById(R.id.ThreeHourRecyclerView);
        linearLayoutManager2 = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        hourlyDataRecyclerView.setLayoutManager(linearLayoutManager2);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        mListner = new PrefChangeListener();
        prefs.registerOnSharedPreferenceChangeListener(mListner);

        Intent intent = getIntent();

        if(intent == null) {
            return;
        }

        Bundle bundle = intent.getExtras();

        if(intent.getExtras().containsKey(MainActivity.CITY_KEY)){
            city = bundle.getString(MainActivity.CITY_KEY).replace(" ","_");
            country = bundle.getString(MainActivity.COUNTRY_KEY).toUpperCase();
        }

        url="http://api.openweathermap.org/data/2.5/forecast?q="+city+","+country+"&mode="+getText(R.string.mode)+"" +
                "&appid="+getText(R.string.api_key);

        city = checkCity(city.replace("_"," "));

        currentcity.setText("Daily Forecast for " + city + ", " + country);

        new GetData(this).execute(url);
        pd = new ProgressDialog(this);
        pd.setTitle("Loading Data");
        pd.setCancelable(false);
        pd.show();
    }

    @Override
    public void setList(final ArrayList<ArrayList<Weather>>list) {
        try{
            pd.dismiss();
            temp_list = list;
            ApplySettings();
            dayDataRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(CityWeatherActivity.this, hourlyDataRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    HourlyDate.setText(list.get(position).get(0).getDate());
                    pos = position;
                    ApplySettings_hour(pos);
                    /*hourAdapter = new HourlyForecastAdapter(list.get(position));
                    hourlyDataRecyclerView.setAdapter(hourAdapter);
                    hourAdapter.notifyDataSetChanged();
                    */
                }

                @Override
                public void onItemLongClick(View view, int position) {

                }
            }));

            HourlyDate.setText(list.get(0).get(0).getDate());
            ApplySettings_hour(0);
            /*hourAdapter = new HourlyForecastAdapter(list.get(0));
            hourlyDataRecyclerView.setAdapter(hourAdapter);
            hourAdapter.notifyDataSetChanged();*/
        } catch (Exception e){
            Toast.makeText(this,"City or County is invalid",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.city_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        dm = new DatabaseDataManager(CityWeatherActivity.this);
        City city1 = new City();
        if (id== R.id.saveCity) {
            savedCities=dm.getAllCities();
            boolean found = false;
            int index=0;
            for (int i=0; i<savedCities.size();i++){
                if (savedCities.get(0).getCityName().equals(city)){
                    found= true;
                    index=i;
                    break;
                }
            }
            if (found) {
                city1.set_id(savedCities.get(index).get_id());
                city1.setCityName(city);
                city1.setCountry(country);
                city1.setTemperature(temp_list.get(0).get(0).getTemperature());
                city1.setDate(new SimpleDateFormat("MM/dd/yyyy").format(new Date()));
                city1.setFavorite(0);
                dm.updateCity(city1);
                Toast.makeText(this,"City Updated",Toast.LENGTH_SHORT).show();

            } else{
                city1.setCityName(city);
                city1.setDate(new SimpleDateFormat("MM/dd/yyyy").format(new Date()));
                city1.setCountry(country);
                city1.setTemperature(temp_list.get(0).get(0).getTemperature());
                dm .saveCity(city1);
                Toast.makeText(this,"City Saved",Toast.LENGTH_SHORT).show();
            }

            return true;
        }else if(id == R.id.settingsCity){
            Intent i = new Intent(CityWeatherActivity.this, MyPreferenceActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String checkCity(String s) {
        String[] tokens = s.split("\\s");
        String toBeCapped = "";

        for(int i = 0; i < tokens.length; i++) {
            char capLetter = Character.toUpperCase(tokens[i].charAt(0));
            toBeCapped +=  " " + capLetter + tokens[i].substring(1);
        }
        toBeCapped = toBeCapped.trim();
        return toBeCapped;
    }

    private class PrefChangeListener implements SharedPreferences.OnSharedPreferenceChangeListener{
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            ApplySettings();
            ApplySettings_hour(pos);
        }
    }
    public void ApplySettings(){
        String temp_unit_change = prefs.getString("temp_unit","c");
        if (!temp_unit.equals(temp_unit_change)){
            Toast.makeText(CityWeatherActivity.this,"Temperature unit change to "+temp_unit_change,Toast.LENGTH_SHORT).show();
        }
        temp_unit=temp_unit_change;

        dayAdapter = new DailyForecastAdapter(temp_list,temp_unit);
        dayDataRecyclerView.setAdapter(dayAdapter);
        dayAdapter.notifyDataSetChanged();
    }
    public void ApplySettings_hour(int position){
        String temp_unit = prefs.getString("temp_unit","c");
        hourAdapter = new HourlyForecastAdapter(temp_list.get(position),temp_unit);
        hourlyDataRecyclerView.setAdapter(hourAdapter);
        hourAdapter.notifyDataSetChanged();
    }



}
