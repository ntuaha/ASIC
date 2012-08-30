package com.nrl.utility;

import com.nrl.sinicainformationcenter.R;

public enum WeatherConstant {
	SUN(R.drawable.sun),CLOUD(R.drawable.clouds),RAIN(R.drawable.rain),MOON(R.drawable.night),UNKNOW(R.drawable.unknown);
	final private int drawable;
	private WeatherConstant(int d){
		this.drawable = d;
	}
	public int getDrawable(){
		return drawable;
	}
}
