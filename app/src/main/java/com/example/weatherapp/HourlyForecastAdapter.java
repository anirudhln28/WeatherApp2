package com.example.weatherapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HourlyForecastAdapter extends RecyclerView.Adapter<HourlyForecastAdapter.HourlyHolder>{

    private ArrayList<Weather> hourlyData;
    String munit;
    public HourlyForecastAdapter(ArrayList<Weather> hourlyData, String unit) {
        this.hourlyData = hourlyData;
        munit= unit;
    }

    @Override
    public HourlyForecastAdapter.HourlyHolder onCreateViewHolder(ViewGroup parent, int viewType)
     {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.city_hourly_details_item, parent, false);
        return new HourlyForecastAdapter.HourlyHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(HourlyForecastAdapter.HourlyHolder holder, int position) {
        Weather forecast = hourlyData.get(position);
        holder.bindData(forecast,munit);
    }

    @Override
    public int getItemCount() {
        return hourlyData.size();
    }

    public static class HourlyHolder extends RecyclerView.ViewHolder {

        TextView time, temp, humid, pressure, wind, cond;
        ImageView icon;
        View itemView;

        public HourlyHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;
            time = (TextView) itemView.findViewById(R.id.HourlyForecastTime);
            temp = (TextView) itemView.findViewById(R.id.HourlyForecastTemp);
            cond = (TextView) itemView.findViewById(R.id.HourlyForecastCond);
            pressure = (TextView) itemView.findViewById(R.id.HourlyForecastPressure);
            humid = (TextView) itemView.findViewById(R.id.HourlyForecastHumidity);
            wind = (TextView) itemView.findViewById(R.id.HourlyForecastWind);
            icon = (ImageView) itemView.findViewById(R.id.HourlyForecastIcon);
            //itemView.setOnClickListener(this);
        }

//        @Override
//        public void onClick(View v) {
//        }

        public void bindData(Weather weather, String unit){
            Double temp1;
            time.setText(weather.getTime());
            Picasso.with(itemView.getContext()).load(weather.getIconurl()).into(icon);
            if (unit.equals("c")){
                temp1 = Double.valueOf(Math.round(((Double.parseDouble(weather.getTemperature())-273.5)*100))/100);
                temp.setText("Temperature: "+temp1+"\u2103");
            }else if (unit.equals("f")){
                temp1 = Double.valueOf(Math.round(((Double.parseDouble(weather.getTemperature())*9/5-459.67)*100))/100);
                temp.setText("Temperature: "+temp1+"\u2109");
            }
            cond.setText("Condition: " + weather.getClimateType());
            pressure.setText("Pressure: " + weather.getPressure());
            humid.setText("Humidity: " + weather.getHumidity());
            wind.setText("Wind: " + weather.getWindspeed()+", "+weather.getWindDirection());
        }
    }
}
