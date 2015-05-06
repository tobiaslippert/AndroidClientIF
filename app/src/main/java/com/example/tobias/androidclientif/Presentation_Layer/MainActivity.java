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





    @Override
    protected void onPause() {
        datasource.closeDB();
        super.onPause();
    }

    }



