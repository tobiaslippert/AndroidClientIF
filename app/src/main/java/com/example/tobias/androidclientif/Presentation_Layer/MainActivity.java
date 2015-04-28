package com.example.tobias.androidclientif.Presentation_Layer;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.example.tobias.androidclientif.Application_Layer.ApplicationHelper;
import com.example.tobias.androidclientif.Application_Layer.HttpCustomClient;
import com.example.tobias.androidclientif.Application_Layer.InternetConnectionDetector;
import com.example.tobias.androidclientif.Application_Layer.SynchronizationHelper;
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

import java.util.List;


public class MainActivity extends Activity {

    //VAR-Declaration
    private MySQLiteHelper datasource;
    private HttpCustomClient restInstance;
    private ParseJSON parser;
    private String username;
    Button Download;
    Button MyAssignments;
    Button Logout;
    User user;
    SynchronizationHelper synchronizationHelper;
    ProgressDialog pd;
    private InternetConnectionDetector icd;


    @Override

    //Main Program
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.username = getIntent().getExtras().getString("UserName");
        System.out.println(username);

        datasource = new MySQLiteHelper(getApplicationContext());
        restInstance = new HttpCustomClient();
        parser = new ParseJSON();
        Download = (Button) findViewById(R.id.bDown);
        MyAssignments = (Button) findViewById(R.id.bMyAss);
        Logout = (Button) findViewById(R.id.bLog);
        user = datasource.getUserByUserName(username);
        pd = new ProgressDialog(this);
        pd.setTitle("In progress");
        pd.setMessage("Sync...");
        //icd = new InternetConnectionDetector(getApplicationContext());





        //waits until Button bMyAss is pressed
        MyAssignments.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent openMyAssignments = new Intent(getApplicationContext(), MyAssignmentsActivity.class);
                openMyAssignments.putExtra("UserId", user.getUserId());
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
                icd = new InternetConnectionDetector(getApplicationContext());
                boolean isOnline = icd.isConnectedToInternet();
                    /*//Download all inspectionObjects from ther server
                    String inputInspectionObjects = restInstance.readHerokuServer("inspectionobject");

                    try {
                        JSONArray jsonArray0 = new JSONArray(inputInspectionObjects);

                        for (int y = 0; y < jsonArray0.length(); y++) {
                            InspectionObject insOb = new InspectionObject();
                            JSONObject jsonObject0 = jsonArray0.getJSONObject(y);

                            insOb.setId(jsonObject0.get("id").toString());
                            insOb.setObjectName(jsonObject0.get("objectName").toString());
                            insOb.setDescription(jsonObject0.get("description").toString());
                            insOb.setCustomerName(jsonObject0.get("customerName").toString());
                            insOb.setLocation(jsonObject0.get("location").toString());

                            //store all inspectionObjects into the database
                            datasource.createInspectionObject(insOb);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //Download user assignments from the server based on their ids

                    String inputAssignment = restInstance.readHerokuServer("assignment?user_id=" + user.getUserId());
                    System.out.println(inputAssignment);
                    try {
                        JSONArray jArray = new JSONArray(inputAssignment);

                        for (int i = 0; i < jArray.length(); i++) {
                            Assignment ass = new Assignment();
                            JSONObject jObject = jArray.getJSONObject(i);

                            if (jObject.get("isTemplate").toString().equals("true")) {
                                System.out.println("Template!");
                                continue;
                            } else {
                                System.out.println("No Template!");
                                //get and set the values for the table assignments
                                ass.setDescription(jObject.get("description").toString());
                                ass.setAssignmentName(jObject.get("assignmentName").toString());
                                ass.setId(jObject.get("id").toString());
                                ass.setStartDate(jObject.getLong("startDate"));
                                ass.setDueDate(jObject.getLong("endDate"));
                                ass.setIsTemplate(jObject.get("isTemplate").toString());
                                ass.setState(jObject.getInt("state"));

                                //Download all tasks assigned to an assignment from the server
                                //jArrayTask gets the SubJSONObject "tasks"
                                JSONArray jArrayTask = new JSONArray(jObject.get("tasks").toString());

                                for (int j = 0; j < jArrayTask.length(); j++) {
                                    Task task = new Task();
                                    JSONObject jObjectTask = jArrayTask.getJSONObject(j);
                                    task.setId(jObjectTask.get("id").toString());
                                    task.setDescription(jObjectTask.get("description").toString());
                                    if (jObjectTask.isNull("state")) {
                                        task.setState(0);
                                    } else {
                                        task.setState(jObjectTask.getInt("state"));
                                    }
                                    task.setTaskName(jObjectTask.get("taskName").toString());
                                    task.setAssignmentId(ass.getId());

                                    //Store all assigned tasks into the database
                                    datasource.createTask(task);
                                }

                                JSONObject jObjectInspectionObject = new JSONObject(jObject.get("inspectionObject").toString());
                                ass.setInspectionObjectId(jObjectInspectionObject.get("id").toString());

                                JSONObject jObjectUser = new JSONObject(jObject.get("user").toString());
                                ass.setUserId(jObjectUser.get("id").toString());
                                //Store all assignments into the database
                                datasource.createAssignment(ass);

                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            */
                if (isOnline==false) {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(MainActivity.this);


                    dlgAlert.setMessage("No internet connecion");
                    dlgAlert.setTitle("Error Message...");
                    dlgAlert.setPositiveButton("OK", null);
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();

                    dlgAlert.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                } else
                {
                /*    pd.show();
                new Thread() {
                    public void run() {

                        try {
                            sleep(1000);
*/
                            synchronizationHelper = new SynchronizationHelper();
                            synchronizationHelper.SynchronizeAssignments(getApplicationContext(), user.getUserId(), MainActivity.this);
/*
                        } catch (Exception e) {
                            Log.e("tag", e.getMessage());
                        }
                        // dismiss the progressdialog
                        pd.dismiss();
                    }
                }.start();
        */
            }
            }}
            );
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



