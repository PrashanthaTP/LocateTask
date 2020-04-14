package com.example.locatetask;


import android.app.Service;
import android.content.Intent;

import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import androidx.core.app.NotificationCompat;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryEventListener;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;

public class CustomLocationService extends Service {

    private Location currentLocation;
    private LocationManager locationManager;
    private TaskDBAdapter dbAdapter;
   private FusedLocationProviderClient fusedLocationProviderClient;
    private GeoFire geoFire;


    @Override
    public void onCreate() {
        super.onCreate();
      fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        dbAdapter = new TaskDBAdapter(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        geoFire = CustomFireBaseHelper.getGeoFireReference(this);


    }

    private void fetchCurrentLocation() {


        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(onSuccessListener);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


      //  startForeground(1,  new CustomNotificationHelper(this).getNotification(1));
        if(new TaskDBAdapter(this).isEmpty()){
            return  START_STICKY;
        }

        try {

         fetchCurrentLocation();

            Toast.makeText(getApplicationContext(), "Location services on ", Toast.LENGTH_LONG).show();


            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    AppConstantsHelper.UPDATE_INTERVAL,
                    AppConstantsHelper.DISPLACEMENT, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    AppConstantsHelper.UPDATE_INTERVAL,
                    AppConstantsHelper.DISPLACEMENT, locationListener);
            currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(currentLocation!=null)
            {
                geoFire.queryAtLocation(new GeoLocation(currentLocation.getLatitude(), currentLocation.getLongitude()),1.0f).addGeoQueryEventListener(geoQueryEventListener);

            }

        } catch (SecurityException e) {
            e.printStackTrace();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {

        Toast.makeText(getApplicationContext(), "Location Service stopped", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            currentLocation = location;

                Toast.makeText(getApplicationContext(),"Querying around the current location",Toast.LENGTH_SHORT).show();
                geoFire.queryAtLocation(new GeoLocation(currentLocation.getLatitude(), currentLocation.getLongitude()), 1.0f).addGeoQueryEventListener(geoQueryEventListener);

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private GeoQueryEventListener geoQueryEventListener = new GeoQueryEventListener() {
        @Override
        public void onKeyEntered(String key, GeoLocation location) {

            showNotificationInLocation(Long.parseLong(key));

        }

        @Override
        public void onKeyExited(String key) {

        }

        @Override
        public void onKeyMoved(String key, GeoLocation location) {

        }

        @Override
        public void onGeoQueryReady() {

        }

        @Override
        public void onGeoQueryError(DatabaseError error) {

        }
    };

    private void showNotificationInLocation(long taskID) {
        Cursor cursor = dbAdapter.getRowById(taskID);
        if (!cursor.moveToFirst()) {
            return;
        }
        String taskName = TaskDBAdapter.getTaskName(cursor);
        String taskDate = TaskDBAdapter.getTaskDate(cursor);
        String taskTime = TaskDBAdapter.getTaskTime(cursor);
        String taskLocation = TaskDBAdapter.getTaskLocation(cursor);

        CustomNotificationHelper notificationHelper = new CustomNotificationHelper(getApplicationContext());
        notificationHelper.showNotification(
                (int) taskID,
                "LocateTask",
                "See if you have a task to do here...",
                "" + taskName,
                "Task is scheduled on " + taskDate + " at " + taskTime,
                "Location " + taskLocation,
                "More info",
                NotificationCompat.CATEGORY_MESSAGE,
                false);


    }
    private OnSuccessListener<Location> onSuccessListener = new OnSuccessListener<Location>() {
        @Override
        public void onSuccess(Location location) {
            if(location !=null )
            {
//                Toast.makeText(getApplicationContext(), "current location"+ location.getLatitude(), Toast.LENGTH_LONG).show();
            currentLocation = location;
            }
        }
    };

}
