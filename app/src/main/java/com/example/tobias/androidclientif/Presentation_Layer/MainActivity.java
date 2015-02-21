package com.example.tobias.androidclientif.Presentation_Layer;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.example.tobias.androidclientif.Application_Layer.RESTServices;
import com.example.tobias.androidclientif.Entities.Assignment;
import com.example.tobias.androidclientif.Entities.InspectionObject;
import com.example.tobias.androidclientif.Entities.Task;
import com.example.tobias.androidclientif.Application_Layer.ParseJSON;

import com.example.tobias.androidclientif.Entities.User;
import com.example.tobias.androidclientif.R;
import com.example.tobias.androidclientif.Persistence_Layer.MySQLiteHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;




public class MainActivity extends Activity {

    //VAR-Declaration
    private MySQLiteHelper datasource;
    private RESTServices restInstance;
    private ParseJSON parser;
    Button Download;
    Button MyAssignments;
    Button Logout;


    @Override

    //Main Program
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        datasource = new MySQLiteHelper(getApplicationContext());
        restInstance = new RESTServices();
        parser = new ParseJSON();
        Download = (Button) findViewById(R.id.bDown);
        MyAssignments = (Button) findViewById(R.id.bMyAss);
        Logout = (Button) findViewById(R.id.bLog);



        //waits until Button bMyAss is pressed
        MyAssignments.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent openMyAssignments = new Intent(getApplicationContext(), MyAssignmentsActivity.class);
                startActivity(openMyAssignments);
            }
        });

        //wait until button blog is pressed
        Logout.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent openLogin = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(openLogin);

            }
        });

        //waits until Button bDown is pressed
        Download.setOnClickListener(new View.OnClickListener() {

            //Declaration what happens when buttons are pressed
            public void onClick(View view) {
                @SuppressWarnings("unchecked")

                //Download all inspectionObjects from ther server
               String inputInspectionObjects = restInstance.readHerokuServer("inspectionobjects");

                try {
                    JSONArray jsonArray0 = new JSONArray(inputInspectionObjects);

                    for (int y = 0; y < jsonArray0.length(); y++ ){
                        InspectionObject insOb = new InspectionObject();
                        JSONObject jsonObject0 = jsonArray0.getJSONObject(y);

                        insOb.setId(jsonObject0.get("id").toString());
                        insOb.setObjectName(jsonObject0.get("objectName").toString());
                        insOb.setDescription(jsonObject0.get("description").toString());
                        insOb.setCustomerName(jsonObject0.get("customerName").toString());
                        insOb.setLocation(jsonObject0.get("location").toString());

                        String objectId;
                        String objectName;
                        String objectDescription;
                        String customerName;
                        String location;

                        objectId = insOb.getId();
                        objectName = insOb.getObjectName();
                        objectDescription = insOb.getDescription();
                        customerName = insOb.getCustomerName();
                        location = insOb.getLocation();

                        //store all inspectionObjects into the database
                        datasource.createInspectionObject(objectId, objectName, objectDescription,location,customerName);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Download all asignments from the server
                String inputAssignment = restInstance.readHerokuServer("assignment");

                try {
                    JSONArray jArray = new JSONArray(inputAssignment);

                    for (int i = 0; i < jArray.length(); i++) {
                        Assignment ass = new Assignment();
                        JSONObject jObject = jArray.getJSONObject(i);

                        //get and set the values for the table assignments
                        ass.setDescription(jObject.get("description").toString());
                        ass.setAssignmentName(jObject.get("assignmentName").toString());
                        ass.setId(jObject.get("id").toString());
                        ass.setStartDate(jObject.getInt("startDate"));
                        ass.setDueDate(jObject.getInt("endDate"));
                        ass.setInspectionObjectId(jObject.get("isTemplate").toString());


                        String assignmentName;
                        String description;
                        String id;
                        Integer startDate;
                        Integer endDate;
                        String isTemplate;


                        assignmentName = ass.getAssignmentName();
                        description = ass.getDescription();
                        id = ass.getId();
                        startDate = ass.getStartDate();
                        endDate = ass.getDueDate();
                        isTemplate = ass.getIsTemplate();

                        //Download all tasks assigned to an assignment from the server
                        //jArrayTask gets the SubJSONObject "tasks"
                        JSONArray jArrayTask = new JSONArray(jObject.get("tasks").toString());

                        for (int j = 0; j < jArrayTask.length(); j++) {
                            Task task = new Task();
                            JSONObject jObjectTask = jArrayTask.getJSONObject(j);
                            task.setId(jObjectTask.get("id").toString());
                            task.setDescription(jObjectTask.get("description").toString());
                            task.setState(jObjectTask.getInt("state"));
                            task.setTaskName(jObjectTask.get("taskName").toString());

                            String taskName;
                            String taskId;
                            String taskDescription;
                            Integer taskState;

                            taskName = task.getTaskName();
                            taskId = task.getId();
                            taskDescription = task.getDescription();
                            taskState = task.getState();

                            //Store all assigned tasks into the database
                            datasource.createTask(taskId, taskName, taskDescription, taskState, id);
                        }

                        JSONObject jObjectInspectionObject = new JSONObject(jObject.get("inspectionObject").toString());
                        InspectionObject inspectionObject = new InspectionObject();
                        inspectionObject.setId(jObjectInspectionObject.get("id").toString());
                        String objectId;
                        objectId = inspectionObject.getId();

                        JSONObject jObjectUser = new JSONObject(jObject.get("user").toString());
                        User user = new User();
                        user.setUserId(jObjectUser.get("id").toString());
                        String userId;
                        userId = user.getUserId();

                        //Store all assignments into the database
                        datasource.createAssignment(id, assignmentName, description, startDate, endDate, objectId,userId, isTemplate);

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }




        });
    }



    /*@Override
    protected void onResume() {
        datasource.open();
        super.onResume();
    }*/

    @Override
    protected void onPause() {
        datasource.closeDB();
        super.onPause();
    }

    }



