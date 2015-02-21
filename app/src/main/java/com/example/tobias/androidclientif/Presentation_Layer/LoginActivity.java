package com.example.tobias.androidclientif.Presentation_Layer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.tobias.androidclientif.Application_Layer.RESTServices;
import com.example.tobias.androidclientif.Entities.Assignment;
import com.example.tobias.androidclientif.Entities.Task;
import com.example.tobias.androidclientif.Entities.User;
import com.example.tobias.androidclientif.Persistence_Layer.MySQLiteHelper;
import com.example.tobias.androidclientif.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tobias on 14.02.15.
 */
public class LoginActivity extends Activity{

    //Var declaration
    private MySQLiteHelper datasource;
    private RESTServices restInstance;
    Button Login;
    String user;
    EditText editTextUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        Login = (Button) findViewById(R.id.login);
        editTextUserName = (EditText) findViewById(R.id.editText1);
        datasource = new MySQLiteHelper(getApplicationContext());
        restInstance = new RESTServices();

        //download the JSON String with all users from the server
        //store the JSON String into the variable user
        user = restInstance.readHerokuServer("users");

        try {
            JSONArray jArray = new JSONArray(user);

            for (int i = 0; i < jArray.length(); i++) {
                User user = new User();
                JSONObject jObject = jArray.getJSONObject(i);

                //get and set the values for the table assignments
                user.setUserId(jObject.get("id").toString());
                user.setUserName(jObject.get("userName").toString());
                user.setFirstName(jObject.get("firstName").toString());
                user.setLastName(jObject.get("lastName").toString());
                user.setRole(jObject.get("role").toString());
                user.setEmail(jObject.get("emailAddress").toString());
                user.setPhoneNumber(jObject.get("phoneNumber").toString());
                user.setMobileNumber(jObject.get("mobileNumber").toString());

                String userId;
                String userName;
                String firstName;
                String lastName;
                String role;
                String emailAddress;
                String phoneNumber;
                String mobileNumber;


                userId = user.getUserId();
                userName = user.getUserName();
                firstName = user.getFirstName();
                lastName = user.getLastName();
                role = user.getRole();
                emailAddress = user.getEmail();
                phoneNumber = user.getPhoneNumber();
                mobileNumber = user.getMobileNumber();

                datasource.createUser(userId, userName, firstName, lastName, role, emailAddress, phoneNumber, mobileNumber);
                System.out.println(user.getUserName());
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        //User Login Handling
        Login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String fieldInput;
                List<String> compareList = new ArrayList<String>();
                compareList = datasource.getAllUserNames();
                fieldInput = editTextUserName.getText().toString();
                boolean isValid;

                if (fieldInput != null) {
                    isValid = isValidUserName(fieldInput, compareList);


                    if (isValid == true) {                                                          //if isValid is true continue to the menu
                        Intent openMenu = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(openMenu);
                    } else {                                                                        //else raise an error message

                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(LoginActivity.this);


                        dlgAlert.setMessage("wrong username");
                        dlgAlert.setTitle("Error Message...");
                        dlgAlert.setPositiveButton("OK", null);
                        dlgAlert.setCancelable(true);
                        dlgAlert.create().show();

                        dlgAlert.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                    }
                } else {

                }
            }
        });
    }


    //Compares a String with the entries of a List<String> in order to check, whether the String appears in the list
    //Returns true if the String appears in the list
    //Returns false if the string is not in the list
    public boolean isValidUserName(String fieldEntry, List<String> databaseEntry) {

        for (int i = 0; i < databaseEntry.size(); i++) {
            if (databaseEntry.get(i).equals(fieldEntry)) {
                return true;
            } else {

            }
        }
        return false;
    }
 }

