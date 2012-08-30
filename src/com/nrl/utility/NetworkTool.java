package com.nrl.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkTool {

    public static boolean HaveNetworkConnection(Context context) 
    { 
        boolean HaveConnectedWifi = false; 
        boolean HaveConnectedMobile = false; 
     
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); 
        NetworkInfo[] netInfo = cm.getAllNetworkInfo(); 
        for (NetworkInfo ni : netInfo) 
        { 
            if (ni.getTypeName().equalsIgnoreCase("WIFI")) 
                if (ni.isConnected()) 
                    HaveConnectedWifi = true; 
            if (ni.getTypeName().equalsIgnoreCase("MOBILE")) 
                if (ni.isConnected()) 
                    HaveConnectedMobile = true; 
        } 
        return HaveConnectedWifi || HaveConnectedMobile; 
    } 
}
