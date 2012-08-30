package com.nrl.about;



import com.nrl.sinicainformationcenter.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;


public class About extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm); 
        int vWidth = dm.widthPixels;  
        int margin = 0;
        int largeWidth = vWidth-margin*2;
        int largeHeight = largeWidth*130/300;
        LinearLayout.LayoutParams large = new LinearLayout.LayoutParams(largeWidth,largeHeight);
        large.setMargins(margin, 0, margin, margin);
        //ImageView IntroductionButton = (ImageView)findViewById(R.id.aboutIntroduction);
        //IntroductionButton.setLayoutParams(large);
    }
}