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
	String title;
	double YMax;
	double YMin;
	private GraphicalView mChartView;
	private TimeSeries time_series;

	// chart container
	private LinearLayout layout;


	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {  
		View view = inflater.inflate(R.layout.fragment_info, container, false); 
		return initial(view,inflater.getContext());  
	}  



	@Override
	public void setArguments(Bundle args) {
		YMax = args.getDouble("YMax");
		YMin = args.getDouble("YMin");
		title = args.getString("title");

		super.setArguments(args);
	}



	@Override
	public void onCreate(Bundle savedInstanceState) {
		title="test";
		super.onCreate(savedInstanceState);

	}

	public View initial(View view,final Context context){
		//comfortHistoryPlot = (XYPlot) view.findViewById(R.id.plot_area);
		//initialComfortHistoryPlot();
		//return view;
		return initialChart(view,context);

	}
	private View initialChart(View view,Context context){
		layout = (LinearLayout) view.findViewById(R.id.chart);
		mDataset = new XYMultipleSeriesDataset();
		mRenderer = new XYMultipleSeriesRenderer();
		mRenderer.setAxisTitleTextSize(30);
		mRenderer.setYLabelsAlign(Align.RIGHT);
		mRenderer.setYAxisMax(YMax);
		mRenderer.setYAxisMin(YMin);
		mRenderer.setChartTitleTextSize(20);
		mRenderer.setLabelsTextSize(30);
		mRenderer.setLegendTextSize(30);
		mRenderer.setMargins(new int[]{50,50,50,50});
		mRenderer.setLegendHeight(60);
		mRenderer.setPointSize(6f);

		XYSeriesRenderer r = new XYSeriesRenderer();
		r.setColor(Color.GREEN);
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
		return view;
	}

	public void add(double time,double value){
		if(time_series!=null){
			time_series.add(time, value);			
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
