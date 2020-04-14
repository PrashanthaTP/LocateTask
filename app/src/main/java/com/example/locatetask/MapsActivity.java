package com.example.locatetask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker mLastMarker;
    private boolean hasUpdatedDatabase = false;
    private LocationManager locationManager;
    private GeoFire geoFire;


    // String taskID ;//To use taskTIme as taskID to store in FireBase through geoFire


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

////=============To use taskTIme as taskID to store in FireBase through geoFire =======================
//         taskID = getIntent().getStringExtra(AppConstantsHelper.FIREBASE_TASKID);

        CustomMessages.showOfflineMessage(this, findViewById(android.R.id.content));
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        MaterialButton selectLocationButton = findViewById(R.id.selectLocationButton);
        selectLocationButton.setOnClickListener(listener);


        geoFire = CustomFireBaseHelper.getGeoFireReference(this);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermissions();
        }

    }

    private void requestLocationPermissions() {
        {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, PackageManager.PERMISSION_GRANTED);

        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, PackageManager.PERMISSION_GRANTED);
        }
        Location location = null;
try{

     location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
}
catch (Exception  e)
        {
            e.printStackTrace();
        }
    if(location==null){
        LatLng  latLng = new LatLng(13.42, 75.25);
        mLastMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Current Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }
    else {
        mLastMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude())).title("Current Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()), 15));
    }


        // Add a marker in Sydney and move the camera




        mMap.setOnMapClickListener(onMapClickListener);
//        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {
//                if(mLastMarker!=null) mLastMarker.remove();
//                CustomMessages.showSnackBar(findViewById(android.R.id.content),latLng.toString());
//                mLastMarker = mMap.addMarker(new MarkerOptions().draggable(true).position(latLng));
//            }
//        });
    }


    //========================What to do upon clicking SelectLocation Button==========================
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            try {
                String selectedLocation = getLocationName(mLastMarker);


                CustomMessages.showToast(MapsActivity.this, selectedLocation);
                if (selectedLocation.equals("Check your Network connection ")) {
                    return;
                }
                //===================save location in shared preferences==================
                saveLocation(selectedLocation);
                saveLocation(mLastMarker.getPosition());

                //geoFire.setLocation( String.valueOf(taskID) ,  new GeoLocation(mLastMarker.getPosition().latitude,mLastMarker.getPosition().longitude),completionListener);


            } catch (Exception e) {
                CustomMessages.showToast(MapsActivity.this, "Please select a location first!!! " + e.toString());
                e.printStackTrace();
                return;

            }
            finish();
//            mMap.setOnMapClickListener(null);
//           if(hasUpdatedDatabase){
//               finish();
//           }
//           else {
//               CustomMessages.showSnackBar(v,"Please check your Network Connection and Retry");
//           }
        }
    };

    private GoogleMap.OnMapClickListener onMapClickListener = new GoogleMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng) {
            if (mLastMarker != null) {

                //remove old marker;
                mLastMarker.remove();
                mLastMarker = mMap.addMarker(new MarkerOptions().draggable(true).position(latLng));
                mLastMarker.setTitle(getLocationLocality(mLastMarker));


            }
        }
    };

    //=================Get Name of selected location ===========================

    private String getLocationName(Marker marker) {

        List<Address> addresses = null;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(marker.getPosition().latitude,
                    marker.getPosition().longitude, 1);
        } catch (Exception e) {
            //CustomMessages.showToast(this,"Error occurred .Check your Network connection" + e.toString());
            return "Check your Network connection ";
        }
        Address address = addresses.get(0);
        String addressString = "";
        addressString += address.getAddressLine(0);
//        addressString += address.getAddressLine(1) + ", ";
////        addressString +=address.getAddressLine(2);


        return addressString;
//     return addresses.get(0).getLocality();

    }

    private String getLocationLocality(Marker marker) {

        List<Address> addresses = null;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(marker.getPosition().latitude,
                    marker.getPosition().longitude, 1);
        } catch (Exception e) {
            //CustomMessages.showToast(this,"Error occurred .Check your Network connection" + e.toString());
            return "Check your Network connection ";
        }


        return addresses.get(0).getLocality();

    }

    private void saveLocation(String selectedLocation) {
        SharedPreferences sharedPreferences = getSharedPreferences(AppConstantsHelper.LOCATION_SHARED_PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AppConstantsHelper.SELECTED_LOCATION, selectedLocation);
        editor.apply();
    }

    private void saveLocation(LatLng latLng) {
        SharedPreferences sharedPreferences = getSharedPreferences(AppConstantsHelper.LOCATION_SHARED_PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AppConstantsHelper.SELECTED_LOCATION_LATITUDE, Double.toString(latLng.latitude));
        editor.putString(AppConstantsHelper.SELECTED_LOCATION_LONGITUDE, Double.toString(latLng.longitude));
        editor.apply();
    }

    private GeoFire.CompletionListener completionListener = new GeoFire.CompletionListener() {
        @Override
        public void onComplete(String key, DatabaseError error) {
            if (error == null) {
                hasUpdatedDatabase = true;
                Toast.makeText(MapsActivity.this, "added", Toast.LENGTH_LONG).show();
            }
        }
    };


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PackageManager.PERMISSION_GRANTED:
                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//                    CustomMessages.showToast(this,"Location Permission is Required");
                    finish();
                }
                break;
        }
    }
}
