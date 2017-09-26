package com.example.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class SavedCitiesAdapter extends RecyclerView.Adapter<SavedCitiesAdapter.CityHolder>{

    private List<City> cities;
    static DatabaseDataManager dm;
    changedList listener;
    Context context;
    String unit;

    public SavedCitiesAdapter(List<City> cities, String unit, Context context) {
        this.cities =  cities;
        this.unit = unit;
        this.context = context;
    }

    @Override
    public SavedCitiesAdapter.CityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.savedcities_row_item, parent, false);
        return new CityHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(CityHolder holder, int position) {
        City itemCity = cities.get(position);
        holder.bindCity(itemCity, unit);
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    class CityHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        TextView city, temp, date;
        ImageButton fav;
        View container;
        City mCity;

        public CityHolder(View itemView) {
            super(itemView);
            dm = new DatabaseDataManager(itemView.getContext());
            listener = (changedList) context;
            container = (View) itemView.findViewById(R.id.container);
            city = (TextView) itemView.findViewById(R.id.savedCityCountry);
            temp = (TextView) itemView.findViewById(R.id.savedCityTemp);
            date = (TextView) itemView.findViewById(R.id.savedCityDate);
            fav = (ImageButton) itemView.findViewById(R.id.favoriteButton);
            fav.setClickable(true);
            fav.setOnClickListener(this);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void bindCity(City c, String units) {
            mCity = c;
            String temp_unit=units;
            Double temp1=0.0;

            city.setText(c.getCityName()+", "+c.getCountry());
            if (temp_unit.equals("c") ){
                temp1 = Double.valueOf(Math.round(((Double.parseDouble(c.getTemperature())-273.5)*100))/100);
                temp.setText(temp1+"\u2103");

            }else if (temp_unit.equals("f")){
                temp1=Double.valueOf(Math.round(((Double.parseDouble(c.getTemperature())*9/5-459.67)*100))/100);;
                temp.setText(temp1+"\u2109");

            }
            date.setText("Updated on: "+c.getDate());
            if(c.getFavorite() == 0)
                fav.setImageResource(R.drawable.star_gray);
            else if(c.getFavorite() == 1)
                fav.setImageResource(R.drawable.star_gold);
        }

        @Override
        public void onClick(View v) {
            Log.d("VIEW ID"," - "+v.getId());
            if(v.getId() == R.id.favoriteButton){
                if(mCity.getFavorite() == 0) {
                    mCity.setFavorite(1);
                    fav.setImageResource(R.drawable.star_gold);
                } else if(mCity.getFavorite() == 1){
                    mCity.setFavorite(0);
                    fav.setImageResource(R.drawable.star_gray);
                }
                dm.updateCity(mCity);
            } else{
                Intent intent = new Intent(itemView.getContext(),CityWeatherActivity.class);
                intent.putExtra(MainActivity.CITY_KEY, mCity.getCityName());
                intent.putExtra(MainActivity.COUNTRY_KEY, mCity.getCountry());
                v.getContext().startActivity(intent);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            Log.d("demo","delected city");
            dm.deleteCity(mCity);
            listener.refresh();
            return true;
        }
    }

    public interface changedList{
        public void refresh();
    }
}
