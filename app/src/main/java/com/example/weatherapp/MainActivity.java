/*
Anirudh ln
ID: 800964326


 */

package com.example.weatherapp;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SavedCitiesAdapter.changedList{

    static final String CITY_KEY ="CITY" ;
    static final String COUNTRY_KEY ="COUNTRY" ;
    DatabaseDataManager dm;
    EditText city,country;
    String c,countryvalue;
    List<City> savedCitiesList;
    TextView msg;
    RecyclerView savedCitiesRecyclerView;
    LinearLayoutManager linearLayoutManager;
    SavedCitiesAdapter adapter;
    ImageView favourite;
    static  String temp_unit="c";

    SharedPreferences prefs;
    PrefChangeListener mListner = null;

    @Override
    protected void onResume() {
        super.onResume();
        savedCitiesList =  dm.getAllCities();
        Sort(savedCitiesList);
        ApplySettings();
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Weather App");

        dm = new DatabaseDataManager(this);
        city = (EditText) findViewById(R.id.city_text);
        country = (EditText) findViewById(R.id.country_text);
        favourite = (ImageView) findViewById(R.id.favoriteButton);
        msg = (TextView) findViewById(R.id.message);
        savedCitiesRecyclerView = (RecyclerView) findViewById(R.id.savedCities);
        linearLayoutManager = new LinearLayoutManager(this);
        savedCitiesRecyclerView.setLayoutManager(linearLayoutManager);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        mListner = new PrefChangeListener();
        prefs.registerOnSharedPreferenceChangeListener(mListner);

        savedCitiesList =  dm.getAllCities();
        Sort (savedCitiesList);
        ApplySettings();

        findViewById(R.id.buttonSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,CityWeatherActivity.class);
                if(city.getText().toString().equals(""))
                    city.setError("Enter a city (i.e. Charlotte)");
                else if(country.getText().toString().equals(""))
                    country.setError("Enter a country (i.e. US)");
                else{
                    c= String.valueOf(city.getText());
                    intent.putExtra(CITY_KEY,c);
                    countryvalue= String.valueOf(country.getText());
                    intent.putExtra(COUNTRY_KEY,countryvalue);

                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.settingsMain){
            Intent i = new Intent(MainActivity.this, MyPreferenceActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void finish() {
        finishAffinity();
    }

    @Override
    protected void onDestroy() {
        dm.close();
        super.onDestroy();
    }

    @Override
    public void refresh() {
        Toast.makeText(this,"City Deleted",Toast.LENGTH_SHORT).show();
        onResume();
    }

    private class PrefChangeListener implements SharedPreferences.OnSharedPreferenceChangeListener{
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            ApplySettings();
        }
    }

    public void ApplySettings(){
        String temp_unit_change = prefs.getString("temp_unit","c");
        if (!temp_unit.equals(temp_unit_change)){
            Toast.makeText(MainActivity.this,"Temperature unit change to "+temp_unit_change,Toast.LENGTH_SHORT).show();
        }
        temp_unit=temp_unit_change;
        adapter = new SavedCitiesAdapter(savedCitiesList, temp_unit, MainActivity.this);
        if (savedCitiesList.size() > 0){
            msg.setText("Saved Cities");
            savedCitiesRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        } else{
            msg.setText("There are no cities to display. Search the city from the search box and save.");
            savedCitiesRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }
    }

    public  void Sort (List<City> list){
        int index = 0;

        for(int i=0;i<list.size();i++){
            if(list.get(i).getFavorite()==1){
                City temp =list.get(i);
                list.remove(i);
                list.add(index,temp);
                index++;
            }
        }
    }
}
