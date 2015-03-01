package com.example.tobias.androidclientif.Application_Layer;

import com.example.tobias.androidclientif.Entities.Assignment;
import com.example.tobias.androidclientif.Entities.InspectionObject;
import com.example.tobias.androidclientif.Entities.Task;
import com.example.tobias.androidclientif.Entities.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Tobias on 19.01.15.
 */
public class ParseJSON {

    //Method: Parse User to JSON String
    public String userToJson(User user){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", user.getUserId());
            jsonObject.put("userName", user.getUserName());
            jsonObject.put("emailAddress", user.getEmail());
            jsonObject.put("role", user.getRole());
            jsonObject.put("firstName", user.getFirstName());
            jsonObject.put("lastName", user.getLastName());
            jsonObject.put("phoneNumber", user.getPhoneNumber());
            jsonObject.put("mobileNumber", user.getMobileNumber());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    //Method: Parse Task to JSON String
    public String taskToJson (Task task){
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("id", task.getId());
            jsonObject.put("taskName", task.getTaskName());
            jsonObject.put("description", task.getDescription());
            jsonObject.put("state", task.getState());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    //Method: Parse Assignment to JSON String
    public String assignmentToJson(Assignment assignment, Task task, User user, InspectionObject inspectionObject){
        JSONObject jsonObjectAssignment = new JSONObject();

        try {
            jsonObjectAssignment.put("id", assignment.getId());
            jsonObjectAssignment.put("assignmentName", assignment.getAssignmentName());
            jsonObjectAssignment.put("description", assignment.getDescription());
            jsonObjectAssignment.put("isTemplate", assignment.getIsTemplate());
            jsonObjectAssignment.put("state", assignment.getState());
            jsonObjectAssignment.put("tasks", null);
            jsonObjectAssignment.put("startDate", assignment.getStartDate());
            jsonObjectAssignment.put("endDate", assignment.getDueDate());
            //jsonObjectAssignment.put("attachmentIds", null);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObjectAssignment.toString();
    }
}
