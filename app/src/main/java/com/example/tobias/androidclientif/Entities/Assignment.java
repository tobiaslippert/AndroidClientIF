package com.example.tobias.androidclientif.Entities;

/**
 * Created by Tobias on 27.01.15.
 */
public class Assignment {

    //Var Declaration
    String assignmentName;
    String id;
    String description;
    Integer startDate;
    Integer dueDate;
    Integer userId;
    Integer inspectionObjectId;

    //Constructor
    public Assignment(){

    }

    //Setter
    public void setAssignmentName(String assignmentName1)
    {
        assignmentName=assignmentName1;
    }

    public void setId(String id1){
        id=id1;
    }

    public void setDescription(String description1){
        description=description1;
    }

    public void setStartDate(Integer startDate) {
        this.startDate = startDate;
    }

    public void setDueDate(Integer dueDate) {
        this.dueDate = dueDate;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setInspectionObjectId(Integer inspectionObjectId) {
        this.inspectionObjectId = inspectionObjectId;
    }

    //Getter
    public String getAssignmentName() {
        return assignmentName;
    }

    public String getId(){
        return id;
    }

    public String getDescription(){
        return description;
    }



    public Integer getStartDate() {
        return startDate;
    }

    public Integer getDueDate() {
        return dueDate;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getInspectionObjectId() {
        return inspectionObjectId;
    }

    @Override
    public String toString() {
        return description;
    }
}
