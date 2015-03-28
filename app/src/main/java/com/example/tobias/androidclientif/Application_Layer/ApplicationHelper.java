package com.example.tobias.androidclientif.Application_Layer;

/**
 * Created by Tobias on 28.03.15.
 */


import android.app.Application;
import android.content.res.Configuration;

import com.example.tobias.androidclientif.Entities.Assignment;
import com.example.tobias.androidclientif.Entities.Task;
import com.example.tobias.androidclientif.Entities.User;
import com.example.tobias.androidclientif.Persistence_Layer.MySQLiteHelper;

/**
 * This class stores application-wide used values
 *
 */
public class ApplicationHelper extends Application {

    private User user;
    private Assignment assignment;
    private Task task;

    private MySQLiteHelper datasource;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    // Getter and Setter

    public User getUser() {
        return user;
    }

    public void setUser(String userId) {
        datasource = new MySQLiteHelper(getApplicationContext());
        User user = datasource.getUserByUserId(userId);
        this.user = user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(String assignmentId) {
        datasource = new MySQLiteHelper(getApplicationContext());
        Assignment assignment = datasource.getAssignmentById(assignmentId);
        this.assignment = assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(String taskId) {
        datasource = new MySQLiteHelper(getApplicationContext());
        Task task = datasource.getTaskByTaskId(taskId);
        this.task = task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

}
