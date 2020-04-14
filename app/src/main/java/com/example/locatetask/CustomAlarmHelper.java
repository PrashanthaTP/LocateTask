package com.example.locatetask;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CustomAlarmHelper {

    private CustomTaskAttributesHelper mTaskAttrbutes ;

    private Context mContext;
    private Calendar mCalendar;

    private static  AlarmManager mAlarmManager;
    private static   Intent mIntent ;

    private static final SimpleDateFormat  mDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm", new Locale("en","IN"));
//    private  static  final SimpleDateFormat mTimeFormat = new SimpleDateFormat("HH:mm",new Locale("en","IN"));

     CustomAlarmHelper(Context context, CustomTaskAttributesHelper taskAttributesHelper) {
        mContext = context;
        mAlarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        mIntent = new Intent(mContext, AlertReceiver.class);

        mTaskAttrbutes= taskAttributesHelper;

        try {
            mCalendar = Calendar.getInstance();
            mCalendar.setTime(mDateFormat.parse(taskAttributesHelper.getTASK_DATE() +" "+ taskAttributesHelper.getTASK_TIME()));
        }
        catch (Exception  e)
        {
            CustomMessages.showToast(mContext,"ERROR occurred in scheduling Alarm");
            e.printStackTrace();
        }
    }




     void setAlarm()
    {
        mIntent.putExtra("taskID",mTaskAttrbutes.getTASK_ID());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext,mTaskAttrbutes.getTASK_ID(),mIntent,0);
        mAlarmManager.setExact(AlarmManager.RTC_WAKEUP,mCalendar.getTimeInMillis(),pendingIntent);
    }

      static  void cancelAlarm(Context context,long ID)
    {
        try {
            Intent intent = new Intent(context, AlertReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) ID, intent, 0);
            mAlarmManager.cancel(pendingIntent);
        }catch (Exception  e)
        {
           // CustomMessages.showToast(context,"Task with ID "+ ID+ " has no alarm set ");
            e.printStackTrace();
        }
    }

/*    //////////////////////////////////UNUSED////////////////////////////////////////////////////////////////////////////////
    public  void storeAlarmInfo()
    {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(AppConstantsHelper.TASK_DETAILS_SHARED_PREFERENCE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(String.valueOf(mTaskAttrbutes.getTASK_ID()),mTaskAttrbutes.getTASK_ID());
//        editor.putInt("taskID",mTaskAttrbutes.getTASK_ID());
//        editor.putString("taskName",mTaskAttrbutes.getTASK_NAME());
//        editor.putString("taskDate",mTaskAttrbutes.getTASK_DATE());
//        editor.putString("taskTime",mTaskAttrbutes.getTASK_TIME());
//        editor.putString("taskLocation",mTaskAttrbutes.getTASK_LOCATION());
        editor.apply();
    }*/


}
