package com.example.taskmanager.model;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;



public class TaskRespositry {
    private TaskDao taskDao;
    private LiveData<List<TaskEntity>> liveData;

    public TaskRespositry(Application application) {
        TaskRoomDatabase database = TaskRoomDatabase.getDatabase(application);
        taskDao = database.taskDao();
        liveData = taskDao.getAllTasks();
    }

    public void inserttask(TaskEntity entity) {
        TaskRoomDatabase.databaseWriterExecutor.execute(() -> {
            taskDao.inserttask(entity);
        });
    }

    public void updatetask(TaskEntity entity) {
        TaskRoomDatabase.databaseWriterExecutor.execute(() -> {
            taskDao.updatetask(entity);
        });
    }

    public void deletetask(TaskEntity entity) {
        TaskRoomDatabase.databaseWriterExecutor.execute(() -> {
            taskDao.deletetask(entity);
        });
    }

    public LiveData<List<TaskEntity>> getallnots(){
        return liveData;
    }

    public void deletealltask() {
        TaskRoomDatabase.databaseWriterExecutor.execute(() -> {
            taskDao.deleteAllTask();
        });
    }


}
