package com.example.locatetask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class CustomNotificationHelper {

    private Context mContext;

    private    NotificationManagerCompat  notificationManagerCompat ;


     CustomNotificationHelper(Context context )
    {
        mContext = context;

        notificationManagerCompat = NotificationManagerCompat.from(context);
    }

     void  showNotification(int ID,
                                   String contentTitle,
                                   String contentText,
                                   String bigContentTitle,
                                   String bigText1,
                                   String bigText2,
                                  String summaryText,
                                   String notificationCategory,
                                    Boolean hasToDeleteTask)
    {
        Intent intent = new Intent(mContext, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);

        Bitmap imageIcon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.tasks);
//        String bigText = "This task was scheduled at \" " + mTaskName+ "\" today.";
        Notification notification = new NotificationCompat.Builder(mContext, App.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_assignment_turned)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setLargeIcon(imageIcon)
                .setStyle(new NotificationCompat.InboxStyle()
                        .addLine(bigText1)
                        .addLine(bigText2)
                        .setBigContentTitle(bigContentTitle)
                        .setSummaryText(summaryText))

                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setColor(Color.BLUE)
                .setOnlyAlertOnce(true)
                .setDefaults(NotificationCompat.DEFAULT_SOUND)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(notificationCategory)
                .build();
        notificationManagerCompat.notify(ID, notification);

        if(hasToDeleteTask){
            new TaskDBAdapter(mContext).removeTask(ID);
            CustomFireBaseHelper.getGeoFireReference(mContext).removeLocation(Integer.toString(ID));
        }
    }

    Notification getNotification(int notificationID)
    {
        Intent intent = new Intent(mContext, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);
        Bitmap imageIcon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.tasks);
        Notification notification = new NotificationCompat.Builder(mContext, App.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_assignment_turned)
                .setContentTitle("LocateTask")
                .setContentText("Location Service is Running...")
                .setLargeIcon(imageIcon)

                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setColor(Color.BLUE)
                .setOnlyAlertOnce(true)
                .setDefaults(NotificationCompat.DEFAULT_SOUND)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_PROGRESS)
                .build();
        notificationManagerCompat.notify(notificationID, notification);
        return  notification;
    }
}
