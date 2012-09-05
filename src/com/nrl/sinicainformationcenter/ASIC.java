package com.nrl.sinicainformationcenter;



import org.json.JSONException;
import org.json.JSONObject;

import com.nrl.about.About;
import com.nrl.utility.Constant;
import com.nrl.utility.DateUtility;
import com.nrl.utility.NetworkTool;
import com.nrl.utility.TempData;
import com.nrl.utility.WeatherConstant;




import android.content.Intent;
import android.os.Bundle;
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

	long last_update_time =0;


	TempData tempData;
	Data data;

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
		//mViewPager.setOffscreenPageLimit(NUM_ITEMS-1);
		
		
		temperatureFragment = new InfoFragment();				
		humidityFragment = new InfoFragment();
		rainfallFragment = new InfoFragment();
		illuminationFragment  =new InfoFragment();
		wind_velocityFragment = new InfoFragment();
		wind_directionFragment = new InfoFragment();
		otherFragment = new InfoFragment();
		weatherFragment = new WeatherFragment();
		tempData = new TempData();
		tempData.loadData(getSharedPreferences(TempData.PREFS_NAME,0));
		data = new Data();
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

		FragmentManager fm;
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
			this.fm= fm;
		}

		@Override
		public Fragment getItem(int i) {
			Log.d("ASIC","getItem run "+i);
			switch(i){
			case 0: 
				weatherFragment = new WeatherFragment();
				setWeatherFragment();
				return weatherFragment;
			case 1:
				temperatureFragment = new InfoFragment();
				setTemperatureFragment();				
				return temperatureFragment;				
			case 2:
				humidityFragment = new InfoFragment();
				setHumidityFragment();
				return humidityFragment;
			case 3:
				rainfallFragment = new InfoFragment();
				setRainfallFragment();
				return rainfallFragment;
			case 4:
				illuminationFragment = new InfoFragment();
				setIlluminationFragment();
				return illuminationFragment;
			case 5:
				wind_velocityFragment = new InfoFragment();
				setWindVelocityFragment();
				return wind_velocityFragment;
			case 6:
				wind_directionFragment = new InfoFragment();
				setWindDirectionFragment();
				return wind_directionFragment;
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
					showProcessInfo(getResources().getString(R.string.wait_refresh));

				}else{					
					showProcessInfo(getResources().getString(R.string.disconnect));

				}
				tempData.loadData(getSharedPreferences(TempData.PREFS_NAME,0));
				if(tempData.data!=null)
					data = new JSONObject(tempData.data);
				else
					return;

			}else{
				tempData.data = data.toString();
				tempData.data_time = System.currentTimeMillis();
				tempData.saveData(getSharedPreferences(TempData.PREFS_NAME,0));
				last_update_time = System.currentTimeMillis();
				hideProcessInfo();
			}
			ASIC.this.data.parserData(data, ASIC.this);
			//mSectionsPagerAdapter.notifyDataSetChanged();
			reDraw();
		
		} catch (JSONException e) {
			Toast.makeText(ASIC.this, "JSON error", Toast.LENGTH_SHORT).show();
			finish();
			e.printStackTrace();
		}

	}
	private void setWeatherFragment(){
		if(data.time_th==null||data.time_th.length<=0)
		{
			Log.i("ASIC","Weather null");
			return;
		}
		Bundle bundle = new Bundle();
		
		bundle.putString("timestamp",DateUtility.convertUNIXtoDate(data.time_th[data.time_th.length-1],true));
		bundle.putDouble("temperature", data.lastest_temperature);
		bundle.putDouble("humidity", data.lastest_humidity);
		bundle.putDouble("rainfall", data.lastest_rainfall);
		bundle.putDouble("light", data.lastest_light);
		weatherFragment.setArguments(bundle);
	}
	private void setTemperatureFragment()
	{
		Bundle bundle = new Bundle();
		bundle.putDoubleArray("data", ASIC.this.data.temperature);
		bundle.putLongArray("time", ASIC.this.data.time_th);
		bundle.putDouble("margin", 0.5);
		bundle.putString("title","Temperature");
		temperatureFragment.setArguments(bundle);
	}

	private void setHumidityFragment(){
		Bundle bundle = new Bundle();
		bundle.putDoubleArray("data", ASIC.this.data.humidity);
		bundle.putLongArray("time", ASIC.this.data.time_th);
		bundle.putDouble("margin",1);
		bundle.putString("title","Humidity");
		humidityFragment.setArguments(bundle);
	}



	private void setRainfallFragment(){
		Bundle bundle = new Bundle();
		bundle.putDoubleArray("data", ASIC.this.data.rainfall);
		bundle.putLongArray("time", ASIC.this.data.time_w);
		bundle.putDouble("margin",1);
		bundle.putString("title","Rainfall");
		rainfallFragment.setArguments(bundle);
	}

	private void setWindVelocityFragment(){
		Bundle bundle = new Bundle();
		bundle.putDoubleArray("data", ASIC.this.data.wind_velocity);
		bundle.putLongArray("time", ASIC.this.data.time_w);
		bundle.putDouble("margin",1);
		bundle.putString("title","Wind Velocity");
		wind_velocityFragment.setArguments(bundle);
	}
	private void setWindDirectionFragment()
	{
		Bundle bundle = new Bundle();
		bundle.putDoubleArray("data", ASIC.this.data.wind_direction);
		bundle.putLongArray("time", ASIC.this.data.time_w);
		bundle.putDouble("margin",90);
		bundle.putString("title","Wind Direction");
		wind_directionFragment.setArguments(bundle);
	}
	private void setIlluminationFragment(){
		Bundle bundle = new Bundle();
		bundle.putDoubleArray("data", ASIC.this.data.illumination);
		bundle.putLongArray("time", ASIC.this.data.time_l);
		bundle.putDouble("margin",0.5);
		bundle.putString("title","Light");
		illuminationFragment.setArguments(bundle);

	}



	private void reDraw(){
		if(data.time_th == null){
			Log.i("noredraw","no");
			return;
		}

		WeatherConstant current_weather= WeatherConstant.UNKNOW;
		int lastest_temperature = parserDataToFragment(temperatureFragment,data.temperature,data.time_th,0.5,"Temperature");
		int lastest_humidity = parserDataToFragment(humidityFragment,data.humidity,data.time_th,1,"Humidity");
		parserDataToFragment(rainfallFragment,data.rainfall,data.time_w,1,"Rainfall");
		double lastest_rainfall = 0.0;

		long t = System.currentTimeMillis()-Constant.TOTAL_RAINFALL_PERIODIC;
		for(int i=data.rainfall.length-1;data.time_w[i]>t;i--){
			lastest_rainfall = data.rainfall[i]+lastest_rainfall;
			Log.i("ASIC",lastest_rainfall+"");
		}

		parserDataToFragment(wind_velocityFragment,data.wind_velocity,data.time_w,1,"Wind Vecocity");
		parserDataToFragment(wind_directionFragment,data.wind_direction,data.time_w,90,"Wind Direction");
		int lastest_light = parserDataToFragment(illuminationFragment,data.illumination,data.time_l,0.5,"Light");
		current_weather = WeatherFragment.judgeWeather(lastest_light,lastest_rainfall);
		weatherFragment.update(current_weather,DateUtility.convertUNIXtoDate(data.time_th[data.time_th.length-1],true), lastest_temperature, lastest_humidity, lastest_rainfall);


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
	


}
