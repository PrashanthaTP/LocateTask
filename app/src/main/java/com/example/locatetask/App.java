package com.example.locatetask;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {

    public static final String CHANNEL_1_ID = "channel_1";
    public static final String CHANNEL_2_ID = "channel_2";
    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel_1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Task Message channel ",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel_1.setDescription("Task related Notifications with high priority");

            NotificationChannel channel_2 = new NotificationChannel(
                    CHANNEL_2_ID,
                    "Task Alarm channel ",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel_2.setDescription("Task related Notifications with low priority");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel_1);
            manager.createNotificationChannel(channel_2);
        }
    }
}
