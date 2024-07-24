package com.example.taskmanager.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {

    public TaskRespositry taskRespositry;
    private LiveData<List<TaskEntity>> task;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        taskRespositry=new TaskRespositry(application);
        task=taskRespositry.getalltask();
    }
    public void inserttask(TaskEntity entity){
        taskRespositry.inserttask(entity);
    }
    public void updatetask(TaskEntity entity){
        taskRespositry.updatetask(entity);
    }
    public void deletetask(TaskEntity entity){
        taskRespositry.deletetask(entity);
    }
    public void deleteall(TaskEntity entity){
        taskRespositry.deletealltask();
    }

    public LiveData<List<TaskEntity>> getalltask(){
        return task;
    }
}
