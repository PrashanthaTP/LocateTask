package com.example.locatetask;

import android.app.DatePickerDialog;
import android.media.audiofx.Visualizer;
import android.view.View;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CustomDatePicker implements View.OnClickListener, View.OnFocusChangeListener, DatePickerDialog.OnDateSetListener {

    private EditText editText;
    private Calendar calendar;
   private   static final  SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy",new Locale("en","IN"));

     CustomDatePicker(EditText editText)
    {
        this.editText = editText;
        this.editText.setOnClickListener(this);
        this.editText.setOnFocusChangeListener(this);
        calendar = Calendar.getInstance();
//        dateFormat = new SimpleDateFormat("dd/MM/yyyy",new Locale("en","IN"));

    }
    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.YEAR,year);

        this.editText.setText(dateFormat.format(calendar.getTime()));
    }

    @Override
    public void onClick(View v) {
            showDatePicker(v);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus) showDatePicker(v);
    }

    private void showDatePicker(View view) {
       int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
       int month = calendar.get(Calendar.MONTH);
       int year = calendar.get(Calendar.YEAR);

//       new DatePickerDialog(view.getContext(),this,year,month,dayOfMonth).show();

        DatePickerDialog dialog = new DatePickerDialog(view.getContext(),this,year,month,dayOfMonth);
        dialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
        dialog.show();
    }

      static   String getDate(long rawDate)
    {
        return dateFormat.format(new Date(rawDate));
    }
}
