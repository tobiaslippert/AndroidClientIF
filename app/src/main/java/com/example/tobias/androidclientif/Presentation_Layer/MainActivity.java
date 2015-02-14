package com.example.tobias.androidclientif.Presentation_Layer;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.example.tobias.androidclientif.Application_Layer.RESTServices;
import com.example.tobias.androidclientif.Entities.Assignment;
import com.example.tobias.androidclientif.Entities.Task;

import com.example.tobias.androidclientif.R;
import com.example.tobias.androidclientif.Persistence_Layer.MySQLiteHelper;

import org.json.JSONArray;
import org.json.JSONObject;




public class MainActivity extends Activity {

    //VAR-Declaration
    private MySQLiteHelper datasource;
    private RESTServices restInstance;
    Button Download;
    Button MyAssignments;


    @Override

    //Main Program
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        datasource = new MySQLiteHelper(getApplicationContext());
        restInstance = new RESTServices();
        Download = (Button) findViewById(R.id.bDown);
        MyAssignments = (Button) findViewById(R.id.bMyAss);



        //waits until Button bMyAss is pressed
        MyAssignments.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent openMyAssignments = new Intent(getApplicationContext(), MyAssignmentsActivity.class);
                startActivity(openMyAssignments);
                                          }
                                     });

        //waits until Button bDown is pressed
        Download.setOnClickListener(new View.OnClickListener() {

            //Declaration what happens when buttons are pressed
            public void onClick(View view) {
                @SuppressWarnings("unchecked")

                //Assignment assignment = null;


                String input = restInstance.readHerokuServer();

                try {
                    JSONArray jArray = new JSONArray(input);

                    for (int i = 0; i < jArray.length(); i++) {
                        Assignment ass = new Assignment();
                        JSONObject jObject = jArray.getJSONObject(i);

                        //get and set the values for the table assignments
                        ass.setDescription(jObject.get("description").toString());
                        ass.setAssignmentName(jObject.get("assignmentName").toString());
                        ass.setId(jObject.get("id").toString());
                        ass.setStartDate(jObject.getInt("startDate"));
                        ass.setDueDate(jObject.getInt("endDate"));

                        String assignmentName;
                        String description;
                        String id;
                        Integer startDate;
                        Integer endDate;


                        assignmentName = ass.getAssignmentName();
                        description = ass.getDescription();
                        id = ass.getId();
                        startDate = ass.getStartDate();
                        endDate = ass.getDueDate();

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

                            datasource.createTask(taskId, taskName, taskDescription, taskState, id);
                        }


                        datasource.createAssignment(id, assignmentName, description, startDate, endDate);

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



