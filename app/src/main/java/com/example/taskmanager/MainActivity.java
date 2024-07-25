package com.example.taskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.model.TaskEntity;
import com.example.taskmanager.model.TaskRoomDatabase;
import com.example.taskmanager.model.TaskViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton floatingActionButton;
    private RecyclerView taskview;
    private TaskViewModel viewModel;
    private Taskadaptar adapter;
    private TaskRoomDatabase roomDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        floatingActionButton = findViewById(R.id.add_task);
        taskview = findViewById(R.id.recyclerView);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TaskForm.class);
                startActivity(intent);
                finish();
            }
        });

        adapter = new Taskadaptar(null, this);
        taskview.setLayoutManager(new LinearLayoutManager(this));
        taskview.setAdapter(adapter);
        roomDatabase = TaskRoomDatabase.getDatabase(this);
        viewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        viewModel.getalltask().observe(this, new Observer<List<TaskEntity>>() {
            @Override
            public void onChanged(@Nullable final List<TaskEntity> tasks) {
                // Update the cached copy of the tasks in the adapter.
                adapter.setTasks(tasks);
            }
        });



        adapter.attachItemTouchHelperToRecyclerView(taskview);


//
    }
}