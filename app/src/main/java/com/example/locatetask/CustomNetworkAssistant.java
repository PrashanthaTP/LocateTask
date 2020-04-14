package com.example.locatetask;

import android.content.Context;
import android.net.ConnectivityManager;

import android.net.NetworkInfo;


public class CustomNetworkAssistant {
     static  boolean isConnectedToNetwork(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if( networkInfo !=null  && networkInfo.isConnected())
        {
            if(networkInfo.getType() == ConnectivityManager.TYPE_MOBILE || networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
                return true;
            else return  false;

        }
        else
            return false;
    }

}
