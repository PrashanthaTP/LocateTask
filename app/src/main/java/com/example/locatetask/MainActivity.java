package com.example.locatetask;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.Manifest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity {


//    private SQLiteDatabase mDatabase;
    private ToggleButton addTaskPageToggleButton;
    private BottomSheetBehavior bottomSheetBehavior;
    private FloatingActionButton addTaskFab;
//    private CoordinatorLayout root;
    private BottomNavigationView bottomNavigationView;
    private  ConstraintLayout bottomLayout;
    private EditText mTaskNameEditText, mTaskDateEditText, mTaskTimeEditText, mTaskLocationEditText;
//    private CustomRecyclerViewAdapter mAdapter;
    private FragmentManager fragmentManager;


    private  long taskID;


    @Override
    protected void onStart() {
        super.onStart();
        if (!new TaskDBAdapter(this).isEmpty()) {

            ////                            CustomMessages.showSnackBarShortTime(v,"Starting");
            SharedPreferences sharedPreferences =getSharedPreferences(AppConstantsHelper.APP_SPECIFIC_SHARED_PREFERENCE, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            if(sharedPreferences.getBoolean(AppConstantsHelper.LOCATION_SERVICES_SETTINGS,Boolean.TRUE))
            {
                startService(new Intent(this, CustomLocationService.class));
                editor.putBoolean(AppConstantsHelper.LOCATION_SERVICES_SETTINGS,Boolean.TRUE);
                editor.apply();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

//        TaskDBHelper dbHelper = new TaskDBHelper(this);
//        mDatabase = dbHelper.getWritableDatabase();


         bottomLayout = findViewById(R.id.bottomSheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomLayout);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        fragmentManager = getSupportFragmentManager();
        addFragment( TaskListFragment.newInstance(), "TaskListFragment");


        setBottomSheetCallbacks();
        setToggleButtonAction();


        addTaskFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTaskToDatabase();
            }
        });

        CustomMessages.showNetworkInfoSnackBar(this,findViewById(android.R.id.content));
    }

// =============== Configure actions on  Bottom Sheet State Changes==================
    private void setBottomSheetCallbacks() {
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    addTaskPageToggleButton.setChecked(true);
                } else
                    addTaskPageToggleButton.setChecked(false);
            }


            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }
//=================  action of ToggleButton in Bottom Sheet===========================
    private void setToggleButtonAction() {
        addTaskPageToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });
    }
//==================== Perform Assignment of views==========================
    private void initViews() {
        addTaskPageToggleButton = findViewById(R.id.bottomSheetToggleButton);
        addTaskFab = findViewById(R.id.addTaskFab);
//        root = findViewById(R.id.mainActivityRootId);
        bottomNavigationView = findViewById(R.id.bottomNav);

        mTaskNameEditText = findViewById(R.id.taskNameEditText);
        mTaskDateEditText = findViewById(R.id.taskDateEditText);
        new CustomDatePicker(mTaskDateEditText);
        mTaskTimeEditText = findViewById(R.id.taskTimeEditText);
        new CustomTimePicker(mTaskTimeEditText,this);
        mTaskLocationEditText = findViewById(R.id.taskLocationEditText);



        mTaskLocationEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//               if(mTaskNameEditText.getText().toString().trim().length()==0)
//               {
//                   CustomMessages.showToast(MainActivity.this,"First enter other details");
//               }
//               else{}
                   openMap();


            }
        });
        mTaskLocationEditText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mTaskLocationEditText.setText(getLocationFromSharedPreferences());
                return true;
            }
        });


    }

    private String getLocationFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(AppConstantsHelper.LOCATION_SHARED_PREFERENCE,MODE_PRIVATE);
        return sharedPreferences.getString(AppConstantsHelper.SELECTED_LOCATION,"No location selected");
    }

//==================== What to do when Floating Action Bar is pressed ==========================
    private void addTaskToDatabase() {

        String taskName = mTaskNameEditText.getText().toString().trim();
        String taskDate = mTaskDateEditText.getText().toString().trim();
        String taskTime = mTaskTimeEditText.getText().toString().trim();
        String taskLocation = mTaskLocationEditText.getText().toString().trim();


        if (taskName.length() == 0 || taskDate.length() == 0 || taskTime.length() == 0 || taskLocation.length() == 0) {


            CustomMessages.showSnackBar(findViewById(android.R.id.content), "Please fill all the fields!");
            return;

        }



        if(!CustomNetworkAssistant.isConnectedToNetwork(this))
        {
            CustomMessages.showOfflineMessage(this,findViewById(android.R.id.content));
            return ;
        }
        TaskDBAdapter dbAdapter = new TaskDBAdapter(this);

        long tempTaskID = dbAdapter.insertTask(taskName, taskDate, taskTime, taskLocation);
        if (tempTaskID == -1) {
            CustomMessages.showSnackBar(findViewById(android.R.id.content), "Cannot add the Task!.Please try again!!!");
            return;

        }
        taskID = tempTaskID;

        GeoFire geoFire = CustomFireBaseHelper.getGeoFireReference(this);
        SharedPreferences sharedPreferences = getSharedPreferences(AppConstantsHelper.LOCATION_SHARED_PREFERENCE,MODE_PRIVATE);
        String latitude = sharedPreferences.getString(AppConstantsHelper.SELECTED_LOCATION_LATITUDE,"0.0");
        String longitude = sharedPreferences.getString(AppConstantsHelper.SELECTED_LOCATION_LONGITUDE,"0.0");

        geoFire.setLocation(String.valueOf(taskID),new GeoLocation(Double.parseDouble(latitude),Double.parseDouble(longitude)));

        CustomMessages.showSnackBar(findViewById(android.R.id.content), " Successfully added");

        //==========================set alarm======================================

        CustomTaskAttributesHelper taskAttributesHelper = new CustomTaskAttributesHelper((int)taskID,taskName,taskDate,taskTime,taskLocation);
        CustomAlarmHelper alarmHelper = new CustomAlarmHelper(this,taskAttributesHelper);
        alarmHelper.setAlarm();
     //   alarmHelper.storeAlarmInfo();


        //=======================Notify the RecyclerView in TaskFragment about the inserted row=============================
        TaskListFragment fragment = (TaskListFragment)fragmentManager.findFragmentById(R.id.fragmentContainerLayout);
        try{
            fragment.notifyDataChange();
        }catch (NullPointerException e)
        {
            CustomMessages.showToast(this,"Task cannot be added.Please Try again");
        }




    }


//======================== Bottom Navigation Listener ==========================================


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    String tag = "";
                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            if(bottomLayout.getVisibility()==View.GONE)
                            {
                                bottomLayout.setVisibility(View.VISIBLE);
                            }
                            if(bottomSheetBehavior.getState()==BottomSheetBehavior.STATE_EXPANDED)
                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            selectedFragment =  TaskListFragment.newInstance();
                            tag = "TaskListFragment";
                            break;
                        case R.id.nav_settings:
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            bottomLayout.setVisibility(View.GONE);
                            selectedFragment =  TaskSettingsFragment.newInstance();
                            tag = "TaskSettingsFragment";
                            break;
                    }

                    addFragment(selectedFragment, tag);
                    return true;
                }
            };
//==============================Replace FragmentContainer Layout ===================================
    private void addFragment(Fragment fragment, String tag) {

        Fragment fragment1 = fragmentManager.findFragmentById(R.id.fragmentContainerLayout);
        if (fragment1 instanceof TaskListFragment && fragment instanceof TaskListFragment)
            return;
        if (fragment1 instanceof TaskSettingsFragment && fragment instanceof TaskSettingsFragment)
            return;

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slideup,0);
        transaction.replace(R.id.fragmentContainerLayout, fragment, tag);

        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();

    }
//======================== what to do if Back Key pressed===================================
    @Override
    public void onBackPressed() {

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerLayout);
        if (fragment instanceof TaskSettingsFragment) {
            // fragmentManager.popBackStack("TaskSettingsFragment",FragmentManager.POP_BACK_STACK_INCLUSIVE);
            bottomNavigationView.getMenu().getItem(0).setChecked(true);
            addFragment( TaskListFragment.newInstance(),"TaskListFragment");

        }
        else if(bottomSheetBehavior.getState()==BottomSheetBehavior.STATE_EXPANDED){
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        else{

            CustomExitDialog exitDialog = new CustomExitDialog(MainActivity.this);
          exitDialog.show(fragmentManager,"CustomExitDialog");
        }

    }
//========================= Method to execute when Location EditText is clicked ===========================
    private void openMap()
    {
//            startActivity(new Intent(this,MapsActivity.class).putExtra(AppConstantsHelper.FIREBASE_TASKID,taskName));
        requestLocationPermission();
        startActivity(new Intent(this,MapsActivity.class));
    }

    private void requestLocationPermission() {
        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },PackageManager.PERMISSION_GRANTED);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case  PackageManager.PERMISSION_GRANTED:
                if(grantResults.length==0 || grantResults[0]!= PackageManager.PERMISSION_GRANTED){
                    CustomMessages.showToast(this,"Location Permissions are required for Proper App Functioning");
                }
                break;
        }
    }
}


//            mTaskDateEditText.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    DatePickerDialogFragment dialogFragment = new DatePickerDialogFragment(new DatePickerDialog.OnDateSetListener() {
//                        @Override
//                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                            DateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/YYYY");
//
//                            Calendar calendar= GregorianCalendar.getInstance();
//                            calendar.set(year,month,dayOfMonth);
//                            String currentDate = simpleDateFormat.format(calendar.getTime());
//                            mTaskDateEditText.setText(currentDate);
//                        }
//                    },MainActivity.this);
//                    dialogFragment.show(getSupportFragmentManager(),"Date");
//                }
//
//            });

