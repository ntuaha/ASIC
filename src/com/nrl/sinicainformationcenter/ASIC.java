package com.nrl.sinicainformationcenter;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nrl.about.About;
import com.nrl.utility.Constant;
import com.nrl.utility.DateUtility;
import com.nrl.utility.NetworkTool;
import com.nrl.utility.WeatherConstant;




import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import android.support.v4.view.ViewPager;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ASIC extends FragmentActivity implements RetrieveDataTask.UITask{

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the
	 * sections. We use a {@link android.support.v4.app.FragmentPagerAdapter} derivative, which will
	 * keep every loaded fragment in memory. If this becomes too memory intensive, it may be best
	 * to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;


	ViewPager mViewPager;
	InfoFragment temperatureFragment;
	InfoFragment humidityFragment;
	InfoFragment rainfallFragment;
	InfoFragment illuminationFragment;
	InfoFragment wind_velocityFragment;
	InfoFragment wind_directionFragment;
	InfoFragment otherFragment;
	WeatherFragment weatherFragment;
	
	TextView processView;
	
	long[] time_th;
	long[] time_l;
	long[] time_w;
	double[] temperature;
	double[] humidity;
	double[] rainfall;
	double[] illumination;
	double[] wind_velocity;
	double[] wind_direction;
	
	long last_update_time =0;
	
	Handler UIhandler;
	Runnable runnable = new Runnable(){
		public void run() {
			refresh();						
		}	
	};
	
	@Override
	protected void onResume() {
		refresh();
		super.onResume();
	}
	private void refresh(){
		if((System.currentTimeMillis()-last_update_time)>Constant.REFRESH_PERIODIC)
			new RetrieveDataTask(ASIC.this).execute();
		else
			Toast.makeText(ASIC.this,getResources().getString(R.string.refresh_periodi_short), Toast.LENGTH_SHORT).show();
	}
	private static final int NUM_ITEMS = 7;



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		processView = (TextView) findViewById(R.id.process);

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.setOffscreenPageLimit(NUM_ITEMS-1);
		temperatureFragment = new InfoFragment();				
		humidityFragment = new InfoFragment();
		rainfallFragment = new InfoFragment();
		illuminationFragment  =new InfoFragment();
		wind_velocityFragment = new InfoFragment();
		wind_directionFragment = new InfoFragment();
		otherFragment = new InfoFragment();
		weatherFragment = new WeatherFragment();
		
		UIhandler = new Handler();
	}

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item)
	{
		Intent intent;
		switch(item.getItemId()){
		case R.id.menu_about:
			intent = new Intent(ASIC.this,About.class);
			startActivity(intent);
		break;
		case R.id.menu_refresh:
			refresh();
		}
		return false;
	}

	public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			Log.d("ASIC","getItem run");
			switch(i){
			case 0: return weatherFragment;
			case 1:	return temperatureFragment;				
			case 2: return humidityFragment;
			case 3: return rainfallFragment;
			case 4: return illuminationFragment;
			case 5: return wind_velocityFragment;
			case 6: return wind_directionFragment;
			default: return otherFragment;
			}

		}

		@Override
		public int getCount() {
			return NUM_ITEMS;
		}


		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0: return getString(R.string.current_weather).toUpperCase();
			case 1: return getString(R.string.title_section1).toUpperCase();
			case 2: return getString(R.string.title_section2).toUpperCase();
			case 3: return getString(R.string.title_section3).toUpperCase();
			case 4: return getString(R.string.title_section4).toUpperCase();
			case 5: return getString(R.string.title_section5).toUpperCase();
			case 6: return getString(R.string.title_section6).toUpperCase();

			}
			return null;
		}

	}


	@Override
	protected void onDestroy() {
		UIhandler.removeCallbacks(runnable);
		super.onDestroy();
	}
	
	
	public void pre_run() {
		if(processView!=null){
			processView.setVisibility(View.VISIBLE);
			processView.setText(getResources().getString(R.string.loading));
		}
	}
	
	private void showProcessInfo(String string){
		if(processView!=null){
			processView.setVisibility(View.VISIBLE);
			processView.setText(string);
		}else{
			Log.d("ASIC","processView is null");
		}
	}
	private void hideProcessInfo(){
		if(processView!=null){
			processView.setVisibility(View.INVISIBLE);
			
		}else{
			Log.d("ASIC","processView is null");
		}
	}
	
	
	@Override
	public void post_run(JSONObject data) {
		try{
			if(data==null){
				if(NetworkTool.HaveNetworkConnection(ASIC.this))
				{
					Log.i("MainActivity","null");
					showProcessInfo(getResources().getString(R.string.wait_refresh));
					UIhandler.postDelayed(runnable,Constant.AUTO_REFRESH_PERIODIC);
					return;
				}else{					
					showProcessInfo(getResources().getString(R.string.disconnect));
					return;
				}
			}
			hideProcessInfo();
			last_update_time = System.currentTimeMillis();
			
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
			}

			JSONArray illuminations = data.getJSONArray("light");
			JSONArray times_l = data.getJSONArray("time_l");
			size = times_l.length();
			illumination = new double[size];
			time_l = new long[size];
			for(int i=0;i<time_l.length;i++){
				illumination[i] = illuminations.getDouble(i)/1000;
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

			reDraw();

		} catch (JSONException e) {
			Toast.makeText(ASIC.this, "JSON error", Toast.LENGTH_SHORT).show();
			finish();
			e.printStackTrace();
		}

	}
	private void reDraw(){
		if(time_th == null){
			Log.i("noredraw","no");
			return;
		}

		WeatherConstant current_weather= WeatherConstant.UNKNOW;
		int lastest_temperature = parserDataToFragment(temperatureFragment,temperature,time_th,0.5,"Temperature");
		int lastest_humidity = parserDataToFragment(humidityFragment,humidity,time_th,1,"Humidity");
		parserDataToFragment(rainfallFragment,rainfall,time_w,1,"Rainfall");
		double lastest_rainfall = 0.0;
		
		long t = System.currentTimeMillis()-Constant.TOTAL_RAINFALL_PERIODIC;
		for(int i=rainfall.length-1;time_w[i]>t;i--){
			lastest_rainfall = rainfall[i]+lastest_rainfall;
			Log.i("ASIC",lastest_rainfall+"");
		}
		
		parserDataToFragment(wind_velocityFragment,wind_velocity,time_w,1,"Wind Vecocity");
		parserDataToFragment(wind_directionFragment,wind_direction,time_w,90,"Wind Direction");
		int lastest_light = parserDataToFragment(illuminationFragment,illumination,time_l,0.5,"Light");
		current_weather = judgeWeather(lastest_light,lastest_rainfall);
		weatherFragment.update(current_weather,DateUtility.convertUNIXtoDate(System.currentTimeMillis(),true), lastest_temperature, lastest_humidity, lastest_rainfall);


	}
	private int parserDataToFragment(InfoFragment fragment,final double[] data,final long[] time,final double data_margin,final String title){
		double Dmin = 100;
		double Dmax = 0;
		int lastest_data = 0;
		fragment.clean();
		for(int i=0;i<time.length;i++){
			fragment.add(time[i], data[i]);
			Dmin = (Dmin>data[i])?data[i]:Dmin;
			Dmax = (data[i]>Dmax)?data[i]:Dmax;
			lastest_data = (int)data[i];
		}
		fragment.reDraw(Dmin-data_margin,Dmax+data_margin,title);
		return lastest_data;
	}
	// Judge the weather in Sinica
	private WeatherConstant judgeWeather(double current_light,double current_rainfall){
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
