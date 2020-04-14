package com.example.locatetask;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.locatetask.R;
import com.google.android.material.button.MaterialButton;

public class TaskSettingsFragment extends Fragment {

    private MaterialButton locationServiceButton;

    public static TaskSettingsFragment newInstance() {
        return new TaskSettingsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings, container, false);
        locationServiceButton = view.findViewById(R.id.locationServiceButton);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(AppConstantsHelper.APP_SPECIFIC_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        if(sharedPreferences.getBoolean(AppConstantsHelper.LOCATION_SERVICES_SETTINGS,Boolean.FALSE)){
            locationServiceButton.setText("Turn OFF");
        }
        else
        {
            locationServiceButton.setText("Turn ON");
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        locationServiceButton.setOnClickListener(locationServiceButtonListener);
    }

    private View.OnClickListener locationServiceButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences(AppConstantsHelper.APP_SPECIFIC_SHARED_PREFERENCE, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Boolean isLocationServicesOn = sharedPreferences.getBoolean(AppConstantsHelper.LOCATION_SERVICES_SETTINGS, Boolean.FALSE);

            if (isLocationServicesOn) {
//                CustomMessages.showSnackBarShortTime(v,"Stopping");
                getContext().stopService(new Intent(getContext(), CustomLocationService.class));
                locationServiceButton.setText(getContext().getResources().getString(R.string.turn_on));
                editor.putBoolean(AppConstantsHelper.LOCATION_SERVICES_SETTINGS,Boolean.FALSE);
            }
            else
                {
                    if (!new TaskDBAdapter(getContext()).isEmpty()) {

                        ////                            CustomMessages.showSnackBarShortTime(v,"Starting");
                      getContext().startService(new Intent(getContext(), CustomLocationService.class));
                        locationServiceButton.setText(getContext().getResources().getString(R.string.turn_off));
                        editor.putBoolean(AppConstantsHelper.LOCATION_SERVICES_SETTINGS,Boolean.TRUE);
                    }
                    else {
                        CustomMessages.showToast(getContext(),"Please add tasks first");
                    }
                }
            editor.apply();
            }

    };


}

//   if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
////                            CustomMessages.showSnackBarShortTime(v,"Starting");
//           getContext().startForegroundService((new Intent(getContext(), CustomLocationService.class)));
//        }
//        else{
////                            CustomMessages.showSnackBarShortTime(v,"Starting");
//        getContext().startService(new Intent(getContext(), CustomLocationService.class));
//        }

