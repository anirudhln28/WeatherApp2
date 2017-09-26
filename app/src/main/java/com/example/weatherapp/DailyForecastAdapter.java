package com.example.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.util.ArrayList;

public class DailyForecastAdapter extends RecyclerView.Adapter<DailyForecastAdapter.DailyHolder> {

    static ArrayList<ArrayList<Weather>> fiveDayForecast;
    String temp_unit;

    public DailyForecastAdapter(ArrayList<ArrayList<Weather>> fiveDayForecast, String unit) {

        this.fiveDayForecast = fiveDayForecast;
        temp_unit= unit;
    }

    @Override
    public DailyForecastAdapter.DailyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.city_day_column_item, parent, false);
        return new DailyForecastAdapter.DailyHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(DailyForecastAdapter.DailyHolder holder, int position) {
        ArrayList<Weather> dayForecast = fiveDayForecast.get(position);
        holder.bindData(dayForecast,temp_unit);
    }

    @Override
    public int getItemCount() {
        return fiveDayForecast.size();
    }

    public static class DailyHolder extends RecyclerView.ViewHolder {

        TextView date, temp;
        ImageView icon;
        View itemView;
        ArrayList<Weather> al;
        final static String LIST = "LIST";
        final static String LIST_LIST = "LIST_LIST";

        public DailyHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            temp = (TextView) itemView.findViewById(R.id.DailyForecastTemp);
            date = (TextView) itemView.findViewById(R.id.DailyForecastDate);
            icon = (ImageView) itemView.findViewById(R.id.DailyForecastIcon);

        }


        public void bindData(ArrayList<Weather> list, String units){
            al = list;
            Double temp1;
            if (units.equals("c")){
                temp1 = Double.valueOf(Math.round((((avgTemp(list)-273.5)*100))/100));
                temp.setText(temp1+"\u2103");
            }else if (units.equals("f")){
                temp1 = Double.valueOf(Math.round((((avgTemp(list)*9/5-459.67)*100))/100));
                temp.setText(temp1+"\u2109");
            }
            date.setText(list.get(0).getDate());
            int median = getMedian(list.size());
            Picasso.with(itemView.getContext()).load(list.get(median).getIconurl()).into(icon);
        }

        private int getMedian(int size) {
            return size/2;
        }

        private double avgTemp(ArrayList<Weather> list) {
            double sum = 0;
            for(int i = 0; i < list.size(); i++){
                sum = sum + Double.parseDouble(list.get(i).getTemperature());
            }

            BigDecimal a = new BigDecimal(sum/list.size());
            BigDecimal roundOff = a.setScale(2, BigDecimal.ROUND_HALF_EVEN);
            return Double.parseDouble(roundOff.toString());
        }
    }
}
