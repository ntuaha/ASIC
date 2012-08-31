package com.nrl.utility;

import android.content.SharedPreferences;

public class TempData {
	static public final String PREFS_NAME = "ASICPreference";
	public long data_time = 0;
	public String data = null;
	public void loadData(SharedPreferences sp){
		data_time = sp.getLong("data_time", 0);
		data = sp.getString("data",null);
	}
	public void saveData(SharedPreferences sp){
		SharedPreferences.Editor editor = sp.edit();
		editor.putLong("data_time", data_time);
		editor.putString("data", data);
		editor.commit();
	}
}
