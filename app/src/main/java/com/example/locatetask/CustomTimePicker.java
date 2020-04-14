package com.example.locatetask;

import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CustomTimePicker implements View.OnFocusChangeListener, TimePickerDialog.OnTimeSetListener ,View.OnClickListener{

    private EditText editText;
    private Calendar calendar;
    private static final SimpleDateFormat   timeFormat = new SimpleDateFormat("HH:mm",new Locale("en","IN"));
    private  Context mContext;

    public CustomTimePicker(EditText editText,Context context)
    {
        mContext = context;
        this.editText  = editText;
        this.editText.setOnFocusChangeListener(this);
        this.editText.setOnClickListener(this);
        this.calendar = Calendar.getInstance();
//        timeFormat = new SimpleDateFormat("HH:mm",new Locale("en","IN"));
    }

    @Override
    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
            calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
            calendar.set(Calendar.MINUTE,minute);

//            if(calendar.getTimeInMillis()>=Calendar.getInstance().getTimeInMillis()) {
//                CustomMessages.showToast(mContext, "Invalid Time. Time should be in future");
//                return;
//            }
            this.editText.setText(timeFormat.format(calendar.getTime()));
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        if(hasFocus)
        {
            showTimePicker(v);
        }
    }

    @Override
    public void onClick(View v) {
        showTimePicker(v);
    }

    private void showTimePicker(View view) {

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);

        new TimePickerDialog(view.getContext(),this,hour,minutes,true).show();
//        TimePickerDialog dialog = new TimePickerDialog(view.getContext(),this,hour,minutes,true);


    }


     static  String getTime(Long rawTime) {
        return  timeFormat.format((new Date(rawTime)));
    }
}
