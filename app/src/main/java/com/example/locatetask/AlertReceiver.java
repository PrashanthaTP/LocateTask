package com.example.locatetask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.database.Cursor;
import android.util.Log;

import androidx.core.app.NotificationCompat;


public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        //SharedPreferences sharedPreferences = context.getSharedPreferences(AppConstantsHelper.TASK_DETAILS_SHARED_PREFERENCE,Context.MODE_PRIVATE);


            /*
                public void  showNotification(
                                    int ID,
                                   String contentTitle,
                                   String contentText,
                                   String bigContentTitle,
                                   String bigText1,
                                   String bigText2,
                                   String summaryText,
                                   String notificationCategory)
             */
//            notificationHelper.showNotification(
//                                                                sharedPreferences.getInt("taskID",1),
//                                                                 context.getString(R.string.app_name),
//                                                                 "You have a task to do!!!",
//                                                                 sharedPreferences.getString("taskName","Task"),
//                                                                 "This task is scheduled on " +
//                                                                            sharedPreferences.getString("taskDate", "Unknown ") + " at "+
//                                                                            sharedPreferences.getString("taskTime"," ,,, ") ,
//                                                                "Location : "+  sharedPreferences.getString("taskLocation","Unknown"),
//                                                                 "More info"
//                                                                 ,NotificationCompat.CATEGORY_ALARM);


        int id = intent.getIntExtra("taskID", 1);

        Cursor cursor = new TaskDBAdapter(context).getRowById(id);

        if (cursor == null) {
//         Log.d("ALARM", "onReceive: ");
            return;
        }
        cursor.moveToFirst();
        String taskName = cursor.getString(cursor.getColumnIndex(TaskEntryHelper.TaskEntry.COLUMN_TASK));
        Long rawDate = cursor.getLong(cursor.getColumnIndex(TaskEntryHelper.TaskEntry.COLUMN_DATE));
        Long rawTime = cursor.getLong(cursor.getColumnIndex(TaskEntryHelper.TaskEntry.COLUMN_TIME));

        CustomNotificationHelper notificationHelper = new CustomNotificationHelper(context);
        notificationHelper.showNotification(id,
                "LocateTask",
                "You have a task to do!!!",
                taskName,
                "This task is scheduled on " + CustomDatePicker.getDate(rawDate) +
                        " at " + CustomTimePicker.getTime(rawTime),
                cursor.getString(cursor.getColumnIndex(TaskEntryHelper.TaskEntry.COLUMN_LOCATION)) + " \n This Task will be deleted",
                "More Info",
                NotificationCompat.CATEGORY_ALARM,
                true);
        cursor.close();


    }
}
