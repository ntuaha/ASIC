package com.nrl.sinicainformationcenter;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;




import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import android.support.v4.view.ViewPager;

import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements RetrieveDataTask.PostTask{

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
	InfoFragment otherFragment;
	long[] time;
	double[] temperature;
	double[] humidity;
	private static final int NUM_ITEMS = 3;



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());



		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.setOffscreenPageLimit(NUM_ITEMS-1);
		temperatureFragment = new InfoFragment();				
		humidityFragment = new InfoFragment();
		otherFragment = new InfoFragment();
		new RetrieveDataTask(MainActivity.this).execute();

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			Log.i("otherFragment","GG");
			switch(i){
			case 0:
				Log.i("temperatureFragment","HI");
				return temperatureFragment;				
			case 1:
				Log.i("humidityFragment","HI");
				return humidityFragment;
			default:
				Log.i("otherFragment","HI");
				return otherFragment;
			}
			//Bundle args = new Bundle();
			//args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, i + 1);
			//fragment.setArguments(args);


		}

		@Override
		public int getCount() {
			return NUM_ITEMS;
		}


		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0: return getString(R.string.title_section1).toUpperCase();
			case 1: return getString(R.string.title_section2).toUpperCase();
			case 2: return getString(R.string.title_section3).toUpperCase();
			}
			return null;
		}

	}


	@Override
	public void run(JSONObject data) {
		try{
			if(data==null){
				Log.i("MainActivity","null");
				return;
			}
			JSONArray temperatures = data.getJSONArray("temperature");
			JSONArray humidities = data.getJSONArray("humidity");
			JSONArray times = data.getJSONArray("time");
			int size = temperatures.length();

			temperature = new double[size];
			humidity = new double[size];
			time = new long[size];
			for(int i=0;i<time.length;i++){
				temperature[i] = temperatures.getDouble(i);
				humidity[i] = humidities.getDouble(i); 
				//time[i] = t - 60*(time.length-i)*1000;
				time[i] = times.getLong(i)*1000;
			}
			reDraw();

		} catch (JSONException e) {
			Toast.makeText(MainActivity.this, "JSON error", Toast.LENGTH_SHORT).show();
			finish();
			e.printStackTrace();
		}

	}
	private void reDraw(){
		if(time == null){
			Log.i("noredraw","no");
			return;
		}
		double Tmin = 100;
		double Tmax = 0;
		double Hmin = 100;
		double Hmax = 0;

		for(int i=0;i<time.length;i++){
			temperatureFragment.add(time[i], temperature[i]);
			Tmin = (Tmin>temperature[i])?temperature[i]:Tmin;
			Tmax = (temperature[i]>Tmax)?temperature[i]:Tmax;

			humidityFragment.add(time[i], humidity[i]);
			Hmin = (Hmin>humidity[i])?humidity[i]:Hmin;
			Hmax = (humidity[i]>Hmax)?humidity[i]:Hmax;
		}

		temperatureFragment.reDraw(Tmin-0.5,Tmax+0.5,"temperature");
		humidityFragment.reDraw(Hmin-1,Hmax+1,"humidity");

	}



}
