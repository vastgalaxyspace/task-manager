package com.example.taskmanager.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    void inserttask(TaskEntity task);

    @Update
    void updatetask(TaskEntity task);

    @Delete
    void deletetask(TaskEntity task);

    @Query("SELECT* FROM Task_Table WHERE Taskid= :id")
    TaskEntity gettaskbyid(int id);

    @Query("SELECT * FROM Task_Table")
    LiveData<List<TaskEntity>> getAllTasks();

    @Query("DELETE FROM Task_Table")
    void deleteAllTask();



}
