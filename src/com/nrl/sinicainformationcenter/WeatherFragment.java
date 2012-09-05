package com.nrl.sinicainformationcenter;









import com.nrl.customView.BarCavas;
import com.nrl.utility.WeatherConstant;

import android.content.Context;
import android.graphics.Color;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;



public class WeatherFragment extends Fragment{

	private double temperature = 0;
	private double humidity = 0;
	private double rainfall = 0;
	private double light = 0;
	private String update_time = "";



	@Override
	public void setArguments(Bundle args) {
		temperature = args.getDouble("temperature");
		humidity = args.getDouble("humidity");
		rainfall = args.getDouble("rainfall");
		update_time = args.getString("timestamp");
		light = args.getDouble("light");		
		super.setArguments(args);
	}

	// chart container
	private ImageView weatherView;
	private BarCavas updateTimeView;
	private BarCavas temperatureView;
	private BarCavas humidityView;
	private BarCavas rainfallView;

	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {  
		View view = inflater.inflate(R.layout.fragment_weather, container, false); 
		return initial(view,inflater.getContext());  
	}  

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	public View initial(View view,final Context context){
		return initialView(view,context);
	}
	private View initialView(View view,Context context){
		//layout = (LinearLayout) view.findViewById(R.id.chart);
		weatherView = (ImageView) view.findViewById(R.id.weather);
		updateTimeView = (BarCavas) view.findViewById(R.id.update_time);
		updateTimeView.setStyle(BarCavas.Style.SMALL);
		updateTimeView.setMode(BarCavas.Mode.TEXT);
		updateTimeView.setTitle(getResources().getString(R.string.update_time));
		updateTimeView.reDraw(getResources().getString(R.string.update_time),-1, "N/A");
		
		temperatureView = (BarCavas) view.findViewById(R.id.temperature);
		temperatureView.setStyle(BarCavas.Style.SMALL);
		temperatureView.setMode(BarCavas.Mode.NUMBER);
		temperatureView.setTitle(getResources().getString(R.string.title_section1));
		temperatureView.reDraw(getResources().getString(R.string.title_section1),-1,"N/A");
		
		humidityView = (BarCavas) view.findViewById(R.id.humidity);
		humidityView.setStyle(BarCavas.Style.SMALL);
		humidityView.setMode(BarCavas.Mode.NUMBER);
		humidityView.setTitle(getResources().getString(R.string.title_section2));
		humidityView.reDraw(getResources().getString(R.string.title_section2),-1, "N/A");
		
		rainfallView = (BarCavas) view.findViewById(R.id.rainfall);
		rainfallView.setStyle(BarCavas.Style.SMALL);
		rainfallView.setMode(BarCavas.Mode.NUMBER);
		rainfallView.setTitle(getResources().getString(R.string.total_rainfall));
		rainfallView.reDraw(getResources().getString(R.string.total_rainfall),-1,"N/A");
		
		
		update(judgeWeather(light,rainfall), update_time, (int)temperature, (int)humidity, rainfall);
		return view;
	}

	public void update(WeatherConstant weather,String update_time,int temperature,int humidity,double rainfall){
		if(isAdded())
		{
			weatherView.setImageResource(weather.getDrawable());
	
			updateTimeView.reDraw(getResources().getString(R.string.update_time),-1,update_time);
			if(temperature>33)
				temperatureView.reDraw(temperature,getResources().getColor(R.color.main_color_red));
			else if(temperature>28)
				temperatureView.reDraw(temperature,Color.argb(255, 255, 128, 0));
			else if(temperature>18)
				temperatureView.reDraw(temperature,getResources().getColor(R.color.main_color_green));
			else if(temperature>15)
				temperatureView.reDraw(temperature,getResources().getColor(R.color.main_color_deepblue));
			else
				temperatureView.reDraw(temperature,Color.argb(255, 255, 0, 255));
			
			if(humidity>70)
				humidityView.reDraw(humidity,getResources().getColor(R.color.main_color_deepblue));		
			else if (humidity>40)
				humidityView.reDraw(humidity,getResources().getColor(R.color.main_color_green));
			else
				humidityView.reDraw(humidity,getResources().getColor(R.color.main_color_red));
			rainfallView.reDraw(rainfall,Color.BLACK);
			Log.i("WeatherFragment","GO");
		}else{
			Log.d("WeatherFragment","No added");
		}
	}
	public static WeatherConstant judgeWeather(double current_light,double current_rainfall){
		if(current_rainfall==0)
		{
			if(current_light>=3.0){
				return WeatherConstant.SUN;
			}else if(current_light==0.0){
				return WeatherConstant.MOON;
			}else{
				return WeatherConstant.CLOUD;
			}
		}else{
			return WeatherConstant.RAIN;
		}
	}



}
