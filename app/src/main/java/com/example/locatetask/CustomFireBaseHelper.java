package com.example.locatetask;

import android.content.Context;

import com.firebase.geofire.GeoFire;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CustomFireBaseHelper {

    static GeoFire getGeoFireReference(Context context)
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(
                context.getSharedPreferences(AppConstantsHelper.APP_SPECIFIC_SHARED_PREFERENCE,context.MODE_PRIVATE).
                        getString(AppConstantsHelper.USERNAME,"newUser"));

//        DatabaseReference  databaseReference = FirebaseDatabase.getInstance().getReference("Aytala");

        return  new GeoFire(databaseReference);
    }

}
