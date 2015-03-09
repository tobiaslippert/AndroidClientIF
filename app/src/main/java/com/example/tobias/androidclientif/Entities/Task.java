package com.example.tobias.androidclientif.Entities;

/**
 * Created by Tobias on 27.01.15.
 */
public class Task {

    //Var Declaration
    String id;
    String taskName;
    String description;
    Integer state;
    String assignmentId;
    String errorDescription;

    //Constructor
    public Task(){
    }


    //Setter

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public void setAssignmentId(String assignmentId) {
        this.assignmentId = assignmentId;
    }

    public void setId(String id1){
        id=id1;
    }

    public void setTaskName(String taskName1){
        taskName=taskName1;
    }

    public void setDescription(String description1){
        description=description1;
    }

    public void setState(Integer state1){
        state=state1;
    }

    //Getter

    public String getErrorDescription() {
        return errorDescription;
    }

    public String getAssignmentId() {
        return assignmentId;
    }

    public String getId() {
        return id;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getDescription() {
        return description;
    }

    public Integer getState() {
        return state;
    }
}
