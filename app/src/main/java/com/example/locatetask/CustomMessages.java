package com.example.locatetask;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class CustomMessages
{
     static  void showToast(Context context, String message)
    {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

      static  void showSnackBar(Activity activity, String message)
    {
        View view =activity.getWindow().getDecorView().getRootView();
        Snackbar.make(view,message, BaseTransientBottomBar.LENGTH_LONG).show();
    }
      static  void showSnackBar( View view, String message)
    {

        Snackbar.make(view,message, BaseTransientBottomBar.LENGTH_LONG).show();
    }
      static  void showSnackBarShortTime( View view, String message)
    {

        Snackbar.make(view,message, BaseTransientBottomBar.LENGTH_SHORT).show();
    }

     static  void showNetworkInfoSnackBar(Context context,View view)
    {
        if(CustomNetworkAssistant.isConnectedToNetwork(context) )
        {
            showSnackBarShortTime(view,"You are online...");
            return;
        };
       final Snackbar snackbar  =  Snackbar.make(view,"Please check your Network Connection",BaseTransientBottomBar.LENGTH_INDEFINITE);
               snackbar.setAction(R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
         //      snackbar.setActionTextColor(ContextCompat.getColor(context,R.color.colorPrimaryBlue));
            snackbar.setActionTextColor(Color.WHITE);
               snackbar.show();
    }

    static  void showOfflineMessage(Context context,View view)
    {
        if(!CustomNetworkAssistant.isConnectedToNetwork(context)){
            final Snackbar snackbar  =  Snackbar.make(view,"Please check your Network Connection",BaseTransientBottomBar.LENGTH_INDEFINITE);
            snackbar.setAction(R.string.ok, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
            snackbar.setActionTextColor(Color.WHITE);
            snackbar.show();
        }
    }



}
