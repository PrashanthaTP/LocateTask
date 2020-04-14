package com.example.locatetask;

import android.provider.BaseColumns;

public class TaskEntryHelper
{


    private TaskEntryHelper(){}
    public static  final  class  TaskEntry implements BaseColumns
    {

        public  static final String TABLE_NAME = "taskTable";
        public static final String COLUMN_TASK= "taskName";
        public static final String COLUMN_DATE = "taskDate";
        public static final String COLUMN_TIME = "taskTime";
        public static final String COLUMN_LOCATION = "taskLocation";
        public static final String COLUMN_TIMESTAMP = "timeStamp";
    }
}
