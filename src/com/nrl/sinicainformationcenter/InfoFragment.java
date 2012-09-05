package com.nrl.sinicainformationcenter;




import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;



public class InfoFragment extends Fragment{



	private XYMultipleSeriesDataset mDataset;
	private XYMultipleSeriesRenderer mRenderer;
	String title = "";
	double YMax;
	double YMin;
	private GraphicalView mChartView;
	private TimeSeries time_series;

	// chart container
	private LinearLayout layout;

	
	double margin = 0;	
	double[] data = null;
	long[] time = null;

	
	

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {  
		View view = inflater.inflate(R.layout.fragment_info, container, false); 
		return initial(view,inflater.getContext());  
	}  



	@Override
	public void setArguments(Bundle args) {
		margin = args.getDouble("margin");		
		title = args.getString("title");
		data = args.getDoubleArray("data");
		time = args.getLongArray("time");
		super.setArguments(args);
	}

	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public View initial(View view,final Context context){
		return initialChart(view,context);

	}
	private View initialChart(View view,Context context){
		layout = (LinearLayout) view.findViewById(R.id.chart);
		mDataset = new XYMultipleSeriesDataset();
		mRenderer = new XYMultipleSeriesRenderer();
		mRenderer.setAxisTitleTextSize(30);
		mRenderer.setYLabelsAlign(Align.RIGHT);
		mRenderer.setYLabelsColor(0, Color.BLACK);
		mRenderer.setXLabelsColor(Color.BLACK);
		mRenderer.setYAxisMax(YMax);
		mRenderer.setYAxisMin(YMin);
		mRenderer.setChartTitleTextSize(20);
		mRenderer.setLabelsTextSize(30);
		mRenderer.setLegendTextSize(30);
		mRenderer.setAntialiasing(true);
		mRenderer.setAxesColor(getResources().getColor(R.color.main_color_blue));	
		mRenderer.setLabelsColor(Color.BLACK);
		mRenderer.setApplyBackgroundColor (true);
		mRenderer.setMargins(new int[]{100,100,100,100});
		mRenderer.setMarginsColor(Color.parseColor("#EEEEEE"));
		mRenderer.setLegendHeight(60);
		mRenderer.setPointSize(6f);

		XYSeriesRenderer r = new XYSeriesRenderer();
		r.setColor(getResources().getColor(R.color.main_color_blue));
		r.setPointStyle(PointStyle.CIRCLE);
		r.setFillPoints(true);
		
		mRenderer.addSeriesRenderer(r);
		mRenderer.setClickEnabled(true);
		mRenderer.setSelectableBuffer(20);
		mRenderer.setPanEnabled(true);
		

		time_series = new TimeSeries(title);
		mDataset.addSeries(time_series);
		mChartView = ChartFactory.getTimeChartView(context, mDataset, mRenderer,"H:mm");
		
		layout.addView(mChartView);	
		
		double Dmin = 100;
		double Dmax = 0;
		clean();
		if(time==null||time.length<=0)
			return view;
		for(int i=0;i<time.length;i++){
			add(time[i], data[i]);
			Dmin = (Dmin>data[i])?data[i]:Dmin;
			Dmax = (data[i]>Dmax)?data[i]:Dmax;
		}
		reDraw(Dmin-margin,Dmax+margin,title);	
		
		
		return view;
	}

	public void add(double time,double value){
		if(time_series!=null){
			time_series.add(time, value);			
		}
	}
	public void clean(){
		if(time_series!=null){
			time_series.clear();			
		}
	}
	public void reDraw(double YMin,double YMax, String title){
		if(mChartView!=null){
			mRenderer.setYAxisMax(YMax);
			mRenderer.setYAxisMin(YMin);
			time_series.setTitle(title);
			mChartView.repaint();	
		}

	}


}
