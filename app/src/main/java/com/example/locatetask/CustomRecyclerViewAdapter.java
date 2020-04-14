package com.example.locatetask;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewAdapter.TaskViewHolder>
{

 //   static int taskOver =0;

    private  Context mContext;
    private  Cursor mCursor;
    private SimpleDateFormat dateFormat;
    private  SimpleDateFormat timeFormat;

     CustomRecyclerViewAdapter(@NonNull Context context , Cursor cursor)
    {
        mContext = context;
        mCursor = cursor;
        timeFormat = new SimpleDateFormat("HH:mm",new Locale("en","IN"));
        dateFormat = new SimpleDateFormat("dd/MM/yyyy",new Locale("en","IN"));

    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.task_item_grid,parent,false);
        return ( new TaskViewHolder(view));
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        if(!mCursor.moveToPosition(position))
        {
            return;
        }



        String taskName = mCursor.getString(mCursor.getColumnIndex(TaskEntryHelper.TaskEntry.COLUMN_TASK));
        Long rawDate = mCursor.getLong(mCursor.getColumnIndex(TaskEntryHelper.TaskEntry.COLUMN_DATE));
        Long rawTime = mCursor.getLong(mCursor.getColumnIndex(TaskEntryHelper.TaskEntry.COLUMN_TIME));
        String taskLocation = mCursor.getString(mCursor.getColumnIndex(TaskEntryHelper.TaskEntry.COLUMN_LOCATION));
        long id = mCursor.getLong(mCursor.getColumnIndex(TaskEntryHelper.TaskEntry._ID));

//        holder.taskName.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_in_left));
//        holder.taskLocation.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_in_right));
//        if(taskOver == id){
//            holder.taskTime.setTextColor(mContext.getResources().getColor(R.color.colorPinkishRed1));
//        }
        holder.taskCard.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_in_left));
        holder.taskName.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_in_right));
        holder.taskName.setText(taskName);
        holder.taskDate.setText(CustomDatePicker.getDate(rawDate));
        holder.taskTime.setText(CustomTimePicker.getTime(rawTime));
        holder.taskLocation.setText(taskLocation);
        holder.itemView.setTag(id);
    }



    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor)
    {
        if(mCursor!=null)
        {
            mCursor.close();
        }
        mCursor = newCursor;
        if(newCursor!=null)
        {
            notifyDataSetChanged();
        }
    }
     void updateCursor(Cursor newCursor)
    {
        if(mCursor!=null)
        {
            mCursor.close();
        }
        mCursor = newCursor;

    }

     class TaskViewHolder extends  RecyclerView.ViewHolder
    {
         TextView taskName, taskDate, taskTime,taskLocation;
         CardView taskCard;
         TaskViewHolder(@NonNull View itemView)
        {
            super(itemView);
            taskName = itemView.findViewById(R.id.taskNameTextView);
            taskDate = itemView.findViewById(R.id.taskDateTextView);
            taskTime = itemView.findViewById(R.id.taskTimeTextView);
            taskLocation = itemView.findViewById(R.id.taskLocationTextView);
            taskCard = itemView.findViewById(R.id.cardId);
        }
    }

/*
    private  String getDate(long rawDate)
    {
//       Log.d("DEBUG1", "getDate: " + dateFormat.format(new Date(rawDate)));
        return dateFormat.format(new Date(rawDate));
    }
    private String getTime(Long rawTime) {
//        Log.d("DEBUG1", "getTime: " + dateFormat.format(new Date(rawTime)));
        return  timeFormat.format((new Date(rawTime)));
    }*/
}
