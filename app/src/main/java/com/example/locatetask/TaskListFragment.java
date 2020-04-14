package com.example.locatetask;


import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class TaskListFragment extends Fragment {

    private NotificationManagerCompat notificationManager;


    private RecyclerView recyclerView;
    private TaskDBAdapter dbAdapter;
    private CustomRecyclerViewAdapter adapter;

//    private TaskListCommunicator communicator;

    public static TaskListFragment newInstance() {
        return new TaskListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.bottom_sheet_list, container, false);
        if(new TaskDBAdapter(getContext()).isEmpty()){
            view.findViewById(R.id.placeholderTextView).setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        notificationManager = NotificationManagerCompat.from(getActivity());


        recyclerView = getActivity().findViewById(R.id.taskList);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
       // recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        dbAdapter = new TaskDBAdapter(getContext());
        adapter = new CustomRecyclerViewAdapter(getContext(), dbAdapter.getAllTasks());
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(getContext(),c,recyclerView,viewHolder,dX,dY,actionState,isCurrentlyActive)
                        .addBackgroundColor(ContextCompat.getColor(getContext(),R.color.swipe_background))
                        .addActionIcon(R.drawable.ic_delete)
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }


            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {


                if(!CustomNetworkAssistant.isConnectedToNetwork(getContext()))
                {
                    CustomMessages.showToast(getContext(),"Internet Connection required");
                    notifyDataChange();
                    return ;
                }

                CustomMessages.showSnackBar(getActivity().findViewById(android.R.id.content),"Item deleted...");
//                showNotification((TextView) viewHolder.itemView.findViewById(R.id.taskNameTextView),
//                        (TextView) viewHolder.itemView.findViewById(R.id.taskTimeTextView));


                dbAdapter.removeTask((long) viewHolder.itemView.getTag());
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                adapter.updateCursor(dbAdapter.getAllTasks());
                CustomAlarmHelper.cancelAlarm(getContext(),(long)viewHolder.itemView.getTag());

                CustomFireBaseHelper.getGeoFireReference(getContext()).removeLocation(Long.toString((long)viewHolder.itemView.getTag()));
                //notifyDataChange();
            }
        }).attachToRecyclerView(recyclerView);



        //   communicator.notifyDataChange(adapter);
    }

//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        if (context instanceof TaskListCommunicator)
//            communicator = (TaskListCommunicator) context;
//    }

     void notifyDataChange() {
        adapter.swapCursor(dbAdapter.getAllTasks());

    }

    private void showNotification(TextView taskName, TextView taskTime) {

        Intent intent = new Intent(getContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, 0);

        Bitmap imageIcon = BitmapFactory.decodeResource(getResources(), R.drawable.tasks);
        String bigText = "This task was scheduled at \" " + taskTime.getText() + "\" today.";
        Notification notification = new NotificationCompat.Builder(getContext(), App.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_assignment_turned)
                .setContentTitle("Alert!")
                .setContentText("Deleted the task \"" + taskName.getText())
                .setLargeIcon(imageIcon)
                .setStyle(new NotificationCompat.InboxStyle()
                        .addLine(taskName.getText() + " was deleted.")
                        .addLine(bigText)
                        .setBigContentTitle("Deleted a task!")
                        .setSummaryText("Summary "))

                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setColor(Color.RED)
                .setOnlyAlertOnce(true)
                .setTicker("Aytala")
                .setDefaults(NotificationCompat.DEFAULT_SOUND)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManager.notify(1, notification);
    }

//    public interface TaskListCommunicator {
//        public void respond();
//    }
}
