package com.example.locatetask;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TaskDBAdapter {

    private TaskDBHelper helper;
    private  Context mContext;
    private  static final SimpleDateFormat   dateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("en","IN"));
    private  static final SimpleDateFormat    timeFormat = new SimpleDateFormat("HH:mm",new Locale("en","IN"));
    private SQLiteDatabase mDatabase;

     TaskDBAdapter(Context  context)
    {
        helper = new TaskDBHelper(context);
        mDatabase = helper.getWritableDatabase();

    }

     long insertTask(String taskName, String taskDate,String taskTime,String location)

    {

//        SQLiteDatabase database = helper.getWritableDatabase();
        try {
             Date date = dateFormat.parse(taskDate);

            Date  time = timeFormat.parse(taskTime);

//            Log.d("DEBUG1", "insertTask: " + date + "String taskDate: " + taskDate);

            ContentValues contentValues = new ContentValues();
            contentValues.put(TaskEntryHelper.TaskEntry.COLUMN_TASK,taskName);
            contentValues.put(TaskEntryHelper.TaskEntry.COLUMN_DATE,date.getTime());
            contentValues.put(TaskEntryHelper.TaskEntry.COLUMN_TIME,time.getTime());
            contentValues.put(TaskEntryHelper.TaskEntry.COLUMN_LOCATION,location);
            return(mDatabase.insert(TaskEntryHelper.TaskEntry.TABLE_NAME,null,contentValues));
        }catch (Exception e)
        {
            CustomMessages.showToast(mContext,"ERROR in date time conversion.");
            e.printStackTrace();
        }

   return -1;

    }
     Cursor getAllTasks()
    {
    //    SQLiteDatabase database = helper.getWritableDatabase();

        return mDatabase.query(
                TaskEntryHelper.TaskEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                TaskEntryHelper.TaskEntry.COLUMN_DATE + " ASC"
        );
    }

     Cursor getRowById(long id)
    {
        return mDatabase.rawQuery("SELECT * FROM "+ TaskEntryHelper.TaskEntry.TABLE_NAME +" WHERE _id = " + id,null);


    }

     void removeTask(long id)
    {
        mDatabase.delete(TaskEntryHelper.TaskEntry.TABLE_NAME,
                TaskEntryHelper.TaskEntry._ID + "=" + id,
                        null);

    }
    public  long getLastTaskID()
    {
        Cursor cursor =  getAllTasks();
        if(cursor.moveToLast())
        return cursor.getLong(cursor.getColumnIndex(TaskEntryHelper.TaskEntry._ID));
        else
            return 0;
    }

      boolean isEmpty()
    {
        return  !getAllTasks().moveToFirst();
    }


    static String  getTaskName(Cursor cursor)
    {

        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex(TaskEntryHelper.TaskEntry.COLUMN_TASK));

    }
    static String  getTaskDate(Cursor cursor)
    {

        cursor.moveToFirst();
           Long rawDate = cursor.getLong(cursor.getColumnIndex(TaskEntryHelper.TaskEntry.COLUMN_DATE));
           return CustomDatePicker.getDate(rawDate);

    }
    static String  getTaskTime(Cursor cursor)
    {

        cursor.moveToFirst();
        Long rawTime=  cursor.getLong(cursor.getColumnIndex(TaskEntryHelper.TaskEntry.COLUMN_TIME));
        return CustomTimePicker.getTime(rawTime);

    }
    static String  getTaskLocation(Cursor cursor)
    {

        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex(TaskEntryHelper.TaskEntry.COLUMN_LOCATION));

    }

}
