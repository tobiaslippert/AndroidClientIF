package com.example.tobias.androidclientif.Entities;

import java.sql.Blob;

/**
 * Created by Tobias on 21.02.15.
 */
public class Attachment {

    //Var-declaration
    String id;
    String file_type;
    String taskId;
    String assignmentId;
    byte[] binaryObject;

    //Constructor
    public Attachment() {
    }

    //Getter and Setter

    public String getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(String assignmentId) {
        this.assignmentId = assignmentId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    public byte[] getBinaryObject() {
        return binaryObject;
    }

    public void setBinaryObject(byte[] binaryObject) {
        this.binaryObject = binaryObject;
    }
}
