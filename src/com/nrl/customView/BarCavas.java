package com.nrl.customView;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Shader;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

public class BarCavas extends View{
	static public enum Style{
		BIG,SMALL;
	}
	static public enum Mode{
		TEXT,NUMBER;
	}
	double num;
	double prev_num;
	String title;
	Paint paintWord;
	Style style;
	Mode mode;
	int titleWord = 18;
	int infoWordBig = 100;
	int infoWordSmall = 30;
	int infoWords = 25;
	
	
	int gapBig = 25;
	int gapSmall = 15;
	int halfWidth;
	int gapBetweenInfoAndTitle;
	int inforWordSize;
	String context;


	Shader mShader;
	Shader mShader2;
	Paint paintBar;
	Paint paintBar2;
	Handler handler;
	
	int color = Color.BLACK;
	
	
	public BarCavas(Context context, AttributeSet attrs) {

		super(context,attrs);
		handler = this.getHandler();
		this.num = 100;
		this.prev_num = 100;
		this.title = "N/A";
		paintWord = new Paint();
		paintWord.setAntiAlias(true);
		rescale();
		invalidate();
	}
	public BarCavas(Context context) {

		super(context);
		handler = this.getHandler();
		this.num = -1;
		this.prev_num = 0;
		this.title = "N/A";
		paintWord = new Paint();
		paintWord.setAntiAlias(true);
		rescale();
		invalidate();
	}
	public void setStyle(Style style){
		this.style = style;
	}
	public void setTitle(String title){
		this.title = title;
	}
	public void setMode(Mode mode){
		this.mode = mode;
	}
	private void rescale(){
		final float scale = getResources().getDisplayMetrics().density;
		// Convert the dps to pixels, based on density scale
		titleWord = (int) (titleWord * scale + 0.5f);
		infoWordBig = (int) (infoWordBig * scale + 0.5f);
		infoWordSmall = (int) (infoWordSmall * scale + 0.5f);
		infoWords = (int) (infoWords * scale + 0.5f);
		gapBig = (int) (gapBig * scale + 0.5f);
		gapSmall = (int) (gapSmall * scale + 0.5f);
		//Log.d("BarCavas","LEFT1:"+LEFT1+"LEFT2:"+LEFT2+"LEFT3:"+LEFT3);
	}
	int count = 0;
	public void reDraw(final double num,int color){
		this.num = num;
		this.color = color;
		invalidate();
	}
	
	public void reDraw(String title,final double num,String context){
		this.prev_num = this.num;
		this.title = title;
		this.context = context;
		BarCavas.this.num = num;
		invalidate();
	}
	
	private void drawInformationBig(Canvas canvas){

		//For title
		//paintWord.setColor(Color.WHITE);
		paintWord.setARGB(200, 150, 150, 150);
		paintWord.setTextSize(titleWord);
		paintWord.setTextAlign(Align.CENTER);
		canvas.drawText(title,halfWidth,getHeight()-20, paintWord);
		canvas.save(Canvas.ALL_SAVE_FLAG);

		//For content
		paintWord.setTextSize(inforWordSize);

		if(num==100)
		{
			paintWord.setColor(Color.WHITE);
			paintWord.setAlpha(200);
			canvas.drawText("STOP",halfWidth,getHeight()-titleWord-20-gapBetweenInfoAndTitle, paintWord);
		}else if(num>50){
			paintWord.setColor(Color.GREEN);
			paintWord.setAlpha(200);
			canvas.drawText(num+"",halfWidth,getHeight()-titleWord-20-gapBetweenInfoAndTitle, paintWord);
		}else if(num>20){
			paintWord.setColor(Color.YELLOW);
			paintWord.setAlpha(200);
			canvas.drawText(num+"",halfWidth,getHeight()-titleWord-20-gapBetweenInfoAndTitle, paintWord);
		}else{
			paintWord.setColor(Color.RED);

			canvas.drawText(num+"",halfWidth,getHeight()-titleWord-20-gapBetweenInfoAndTitle, paintWord);
		}
		canvas.save(Canvas.ALL_SAVE_FLAG);
	}
	
	
	private void drawInformationSmall(Canvas canvas){

		//For title
		paintWord.setARGB(200, 150, 150, 150);
		paintWord.setTextSize(titleWord);
		paintWord.setTextAlign(Align.CENTER);
		canvas.drawText(title,halfWidth,getHeight()-20, paintWord);
		canvas.save(Canvas.ALL_SAVE_FLAG);

		//For content
		paintWord.setTextSize(inforWordSize);
		paintWord.setAlpha(200);
		paintWord.setColor(color);
		
		canvas.drawText(num+"",halfWidth,getHeight()-titleWord-20-gapBetweenInfoAndTitle, paintWord);
		
			
		canvas.save(Canvas.ALL_SAVE_FLAG);

	}
	private void drawInformationContext(Canvas canvas){

		//For title
		
		//paintWord.setARGB(200, 150, 150, 150);
		paintWord.setTextSize(titleWord);
		paintWord.setColor(Color.parseColor("#33b5e5"));
		paintWord.setTextAlign(Align.CENTER);
		canvas.drawText(title,halfWidth,getHeight()-20, paintWord);
		canvas.save(Canvas.ALL_SAVE_FLAG);

		//For content
		paintWord.setTextSize(infoWords);
		paintWord.setARGB(200, 0, 0, 0);
		paintWord.setAlpha(200);
		paintWord.setColor(color);
		if(mode==Mode.TEXT)
			canvas.drawText(context,halfWidth,getHeight()-titleWord-20-gapBetweenInfoAndTitle, paintWord);
		else if(mode==Mode.NUMBER){
			num = Math.floor((num*10))/10;
			canvas.drawText(num+"",halfWidth,getHeight()-titleWord-20-gapBetweenInfoAndTitle, paintWord);
		}
		canvas.save(Canvas.ALL_SAVE_FLAG);

	}


	protected void onDraw(Canvas canvas){
		halfWidth = getWidth()/2;
		switch(style){
		case BIG:
			inforWordSize = infoWordBig;
			gapBetweenInfoAndTitle = gapBig;
			drawInformationBig(canvas);
			break;
		case SMALL:
			inforWordSize = infoWordSmall;
			gapBetweenInfoAndTitle = gapSmall;
			drawInformationContext(canvas);
			break;
		default:
			inforWordSize = infoWordSmall;
			gapBetweenInfoAndTitle = gapSmall;
			drawInformationSmall(canvas);
		}

	}



}
