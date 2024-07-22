package com.example.taskmanager.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;


@Entity(tableName = "Task_Table")
public class TaskEntity {
    @PrimaryKey(autoGenerate = true)

    @ColumnInfo(name = "Taskid")
    private int taskid;

    @ColumnInfo(name = "Title")
    private String title;

    @ColumnInfo(name = "Description")
    private String description;

    @ColumnInfo(name = "due_date")
    private Date dueDate;

    @ColumnInfo(name = "priority")
    private int priority;

    @ColumnInfo(name = "reminder_time")
    private String reminderTime;

    public TaskEntity() {
    }

    public TaskEntity(int taskid, String title, String description, Date dueDate, int priority, String reminderTime) {
        this.taskid = taskid;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.reminderTime = reminderTime;
    }

    public int getTaskid() {
        return taskid;
    }

    public void setTaskid(int taskid) {
        this.taskid = taskid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(String reminderTime) {
        this.reminderTime = reminderTime;
    }
}
