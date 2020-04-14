package com.example.locatetask;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class TaskDBHelper extends SQLiteOpenHelper
{
     private  static final String DATABASE_NAME = "taskList.db";
     private static final int  DATABASE_VERSION = 1;

    public TaskDBHelper(Context context) {
     super(context,DATABASE_NAME,null,DATABASE_VERSION );
    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {
        final String SQL_CREATE_TABLE = "CREATE TABLE " +   TaskEntryHelper.TaskEntry.TABLE_NAME + " (" +
                                                                                            TaskEntryHelper.TaskEntry._ID + "  INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                                                            TaskEntryHelper.TaskEntry.COLUMN_TASK + " TEXT NOT NULL, " +
                                                                                            TaskEntryHelper.TaskEntry.COLUMN_DATE + " INT, " +
                                                                                            TaskEntryHelper.TaskEntry.COLUMN_TIME + " INT, " +
                                                                                            TaskEntryHelper.TaskEntry.COLUMN_LOCATION + " TEXT NOT NULL, " +
                                                                                            TaskEntryHelper.TaskEntry.COLUMN_TIMESTAMP  + " TIMESTAMP DEFAULT CURRENT_TIME" +
                                                                                                ");" ;

        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
            db.execSQL("DROP TABLE IF EXISTS " + TaskEntryHelper.TaskEntry.TABLE_NAME);
            onCreate(db);
    }

}
