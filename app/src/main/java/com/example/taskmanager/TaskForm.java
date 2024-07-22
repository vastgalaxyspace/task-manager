package com.example.taskmanager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.taskmanager.model.TaskEntity;
import com.example.taskmanager.model.TaskRoomDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;

public class TaskForm extends AppCompatActivity {
    private Button timepick, datepick, save;
    private TextView showtime;
    private EditText title, description, date;
    private TaskRoomDatabase roomDatabase;
    private TaskEntity task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_form);

        timepick = findViewById(R.id.idBtnPickTime);
        datepick = findViewById(R.id.idBtnPickDate);
        showtime = findViewById(R.id.idTVSelectedTime);
        title = findViewById(R.id.task_title);
        description = findViewById(R.id.task_description);
        date = findViewById(R.id.task_date);
        save = findViewById(R.id.save_task);

        roomDatabase = TaskRoomDatabase.getDatabase(this);

        if (timepick == null) {
            Log.e("TaskForm", "timepick Button not found");
        } else {
            Log.d("TaskForm", "timepick Button found");
        }

        if (showtime == null) {
            Log.e("TaskForm", "showtime TextView not found");
        } else {
            Log.d("TaskForm", "showtime TextView found");
        }

        datepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(TaskForm.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                date.setText(String.format("%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth));
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        timepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(TaskForm.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                showtime.setText(String.format("%02d:%02d", i, i1));
                            }
                        }, hour, minute, true);
                timePickerDialog.show();
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTask();
            }
        });
    }

    private void saveTask() {
        String tasktitle = title.getText().toString();
        String taskdescription = description.getText().toString();
        String taskdate = date.getText().toString();
        String timetask = showtime.getText().toString();
        String dateTimeString = taskdate + " " + timetask;

        Log.d("TaskForm", "Date: " + taskdate);
        Log.d("TaskForm", "Time: " + timetask);
        Log.d("TaskForm", "DateTime: " + dateTimeString);

        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
        Date dueDate = null;
        try {
            dueDate = dateTimeFormat.parse(dateTimeString);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("TaskForm", "Failed to parse date: " + dateTimeString, e);
        }

        TaskEntity task = new TaskEntity();
        task.setTitle(tasktitle);
        task.setDescription(taskdescription);
        task.setDueDate(dueDate);
        task.setReminderTime(timetask);

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                roomDatabase.taskDao().inserttask(task);
                runOnUiThread(() -> Toast.makeText(TaskForm.this, "Task saved successfully", Toast.LENGTH_SHORT).show());
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(TaskForm.this, "Failed to save task", Toast.LENGTH_SHORT).show());
            }
        });
    }
}
