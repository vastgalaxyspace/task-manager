package com.example.taskmanager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.model.TaskEntity;
import com.example.taskmanager.model.TaskRoomDatabase;
import com.example.taskmanager.model.TaskViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class Taskadaptar extends RecyclerView.Adapter<Taskadaptar.TaskViewholder> {

    private List<TaskEntity> tasklist;
    private TaskRoomDatabase roomDatabase;
    private Context context;
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    public Taskadaptar(List<TaskEntity> tasklist,Context context){
        this.tasklist=tasklist;
        this.roomDatabase = TaskRoomDatabase.getDatabase(context);
        this.context=context;
    }
    @NonNull
    @Override
    public Taskadaptar.TaskViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.taskview_layout,parent,false);

        return new TaskViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Taskadaptar.TaskViewholder holder, int position) {
        TaskEntity task=tasklist.get(position);

        holder.tvTaskTitle.setText(task.getTitle());
        holder.tvTaskDescription.setText(task.getDescription());

        if (task.getDueDate() != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            String dueDateString = dateFormat.format(task.getDueDate());
            holder.tvTaskDate.setText(dueDateString);
        } else {
            holder.tvTaskDate.setText(""); // Or some default text
        }
        holder.tvTaskTime.setText((task.getReminderTime()));

        holder.deleteButton.setOnClickListener(view -> {
            deletetask(task,position);
        });



    }
    private void setAlaram(TaskEntity task){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(task.getDueDate());

        String[]  timeparts=task.getReminderTime().split(":");
        int hour=Integer.parseInt(timeparts[0]);
        int minute=Integer.parseInt(timeparts[1]);

        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,minute);
        calendar.set(Calendar.SECOND,0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlramReceiver.class);
        intent.putExtra("taskId", task.getTaskid());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, task.getTaskid(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    private void deletetask(TaskEntity task, int position) {
        new Thread(()->{
            if(position >= 0 && position < tasklist.size()){roomDatabase.taskDao().deletetask(task);
            mainHandler.post(() -> {
                tasklist.remove(position);
                notifyItemRemoved(position);
            });}

        }).start();
    }

    @Override
    public int getItemCount() {
        return tasklist == null ? 0 : tasklist.size();
    }

    public static class TaskViewholder  extends RecyclerView.ViewHolder{
        TextView tvTaskTitle, tvTaskDescription, tvTaskDate, tvTaskTime;
        ImageButton deleteButton;


        public TaskViewholder(@NonNull View itemView) {
            super(itemView);
            tvTaskTitle = itemView.findViewById(R.id.idTVCourseName);
            tvTaskDescription = itemView.findViewById(R.id.idTVCourseDuration);
            tvTaskDate = itemView.findViewById(R.id.iddate);
            tvTaskTime = itemView.findViewById(R.id.time);
            deleteButton = itemView.findViewById(R.id.imageButton);
        }
    }
    public void setTasks(List<TaskEntity> taskList) {
        this.tasklist = taskList;
        notifyDataSetChanged();
    }
    public void attachItemTouchHelperToRecyclerView(RecyclerView recyclerView) {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                TaskEntity task = tasklist.get(position);
                deletetask(task, position);
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

}
