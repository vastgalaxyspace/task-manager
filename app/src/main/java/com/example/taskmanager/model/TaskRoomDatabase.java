package com.example.taskmanager.model;  // Define the package for this class

// Import Room annotations and Android architecture components
import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;
import android.content.Context;
import android.os.AsyncTask;

import com.example.taskmanager.model.TaskDao;
import com.example.taskmanager.model.TaskEntity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Annotate the class with @Database to define the database configuration
@Database(entities = {TaskEntity.class}, version = 1)
@TypeConverters({DateConverter.class})
// Specify the entities and version of the database
public abstract class TaskRoomDatabase extends RoomDatabase {

    // Abstract method to get the DAO (Data Access Object)
    public abstract TaskDao taskDao();

    // Singleton instance of the database
    private static volatile TaskRoomDatabase INSTANCE;
    static final ExecutorService databaseWriterExecutor = Executors.newFixedThreadPool(4);

    // Method to get the instance of the database
    public static TaskRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TaskRoomDatabase.class) {  // Synchronize to prevent multiple threads from creating multiple instances
                if (INSTANCE == null) {
                    // Create the database instance
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    TaskRoomDatabase.class, "task_database")
                            .addCallback(sRoomDatabaseCallback)
                            // Build the database with the name "task_database"
                            .fallbackToDestructiveMigration()  // Handle migration by destroying and recreating the database
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // Callback for database creation and initialization
    private static final RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);

                    // Execute a task after the database is created
                    databaseWriterExecutor.execute(() -> {
                        TaskDao taskDao = INSTANCE.taskDao();  // Get the DAO
                        // You can perform initialization operations here, like pre-populating the database
                        // Example: taskDao.insert(new TaskEntity(...));
                        taskDao.getAllTasks();
                    });
                }
            };
}
