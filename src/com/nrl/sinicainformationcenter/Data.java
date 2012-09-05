package com.nrl.sinicainformationcenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nrl.utility.Constant;



import android.content.Context;
import android.util.Log;


public class Data {
	public long[] time_th;
	public long[] time_l;
	public long[] time_w;
	public double[] temperature;
	public double[] humidity;
	public double[] rainfall;
	public double[] illumination;
	public double[] wind_velocity;
	public double[] wind_direction;

	public double lastest_temperature;
	public double lastest_rainfall;
	public double lastest_light;
	public double lastest_humidity;
	//public WeatherConstant current_weather;

	
	
	public Data(){
		time_th = null;
		time_l = null;
		time_w = null;
		temperature = null;
		humidity = null;
		rainfall = null;
		illumination = null;
		wind_velocity = null;
		wind_direction = null;
		lastest_temperature = 0;
		lastest_rainfall = 0;
		lastest_light = 0;
		lastest_humidity = 0;
	}
	public void parserData(JSONObject data,Context context){

		try{
			//Parser JSONData to native array
			JSONArray temperatures = data.getJSONArray("temperature");
			JSONArray humidities = data.getJSONArray("humidity");
			JSONArray times_th = data.getJSONArray("time_th");					
			int size = temperatures.length();
			temperature = new double[size];
			humidity = new double[size];
			time_th = new long[size];
			for(int i=0;i<time_th.length;i++){
				temperature[i] = temperatures.getDouble(i);
				humidity[i] = humidities.getDouble(i); 
				time_th[i] = times_th.getLong(i)*1000;
				lastest_temperature = temperature[i];
				lastest_humidity = humidity[i];
				
			}

			JSONArray illuminations = data.getJSONArray("light");
			JSONArray times_l = data.getJSONArray("time_l");
			size = times_l.length();
			illumination = new double[size];
			time_l = new long[size];
			for(int i=0;i<time_l.length;i++){
				illumination[i] = illuminations.getDouble(i)/1000;
				lastest_light = illumination[i];
				time_l[i] = times_l.getLong(i)*1000;
			}

			JSONArray rainfalls = data.getJSONArray("rain");
			JSONArray wind_directions = data.getJSONArray("wind");
			JSONArray wind_velocities = data.getJSONArray("anemometer");		
			JSONArray times_w = data.getJSONArray("time_w");
			size = wind_velocities.length();
			rainfall = new double[size];
			wind_velocity = new double[size];
			wind_direction = new double[size];
			time_w = new long[size];
			for(int i=0;i<time_w.length;i++){
				rainfall[i] = rainfalls.getDouble(i);
				wind_velocity[i] = wind_velocities.getDouble(i);
				String direciton = wind_directions.getString(i);
				if(direciton=="E"){
					wind_direction[i] = 90;
				}else if(direciton.equals("W")){
					wind_direction[i] = 270;
				}else if(direciton.equals("N")){
					wind_direction[i] = 0;
				}else if(direciton.equals("S")){
					wind_direction[i] = 180;
				}else if(direciton.equals("NW")){
					wind_direction[i] = 315;
				}else if(direciton.equals("NE")){
					wind_direction[i] = 45;
				}else if(direciton.equals("SE")){
					wind_direction[i] = 135;
				}else if(direciton.equals("SW")){
					wind_direction[i] = 225;
				}
				time_w[i] = times_w.getLong(i)*1000;
			}
			
			
			lastest_rainfall = 0.0;
			if(rainfall.length>0)
			{
				long t = System.currentTimeMillis()-Constant.TOTAL_RAINFALL_PERIODIC;
				for(int i=rainfall.length-1;time_w[i]>t;i--){
					lastest_rainfall = rainfall[i]+lastest_rainfall;
					Log.i("ASIC",lastest_rainfall+"");
				}
			}
			
		} catch (JSONException e) {
			//Toast.makeText(context, "JSON error", Toast.LENGTH_SHORT).show();
			//context.this...finish();
			Log.i("Data","JSON error");
			e.printStackTrace();
		}
	}
	
	
}
