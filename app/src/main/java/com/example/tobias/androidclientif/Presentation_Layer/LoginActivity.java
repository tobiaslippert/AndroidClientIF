package com.example.tobias.androidclientif.Presentation_Layer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.tobias.androidclientif.Application_Layer.HttpCustomClient;
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
    private HttpCustomClient clientInstance;
    Button Login;
    String user;
    EditText editTextUserName;
    EditText editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        Login = (Button) findViewById(R.id.login);
        editTextUserName = (EditText) findViewById(R.id.editText1);
        editPassword = (EditText) findViewById(R.id.editText2);
        datasource = new MySQLiteHelper(getApplicationContext());
        clientInstance = new HttpCustomClient();
        //download the JSON String with all users from the server
        //store the JSON String into the variable user


        //User Login Handling
        Login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String username = editTextUserName.getText().toString();

                String password = editPassword.getText().toString();
                boolean status = clientInstance.postToHerokuServer("login", username, password);

                if (status == true) {
                    user = clientInstance.readHerokuServer("users");

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

                            datasource.createUser(user);
                            System.out.println(user.getUserName());
                            Intent openMenu = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(openMenu);
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(LoginActivity.this);


                    dlgAlert.setMessage("wrong username or password");
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

            }
        });
    }





    //Compares a String with the entries of a List<String> in order to check, whether the String appears in the list
    //Returns true if the String appears in the list
    //Returns false if the string is not in the list
    public boolean isValidUserName(String fieldEntry, List<User> databaseEntry) {

        for (int i = 0; i < databaseEntry.size(); i++) {
            String userName = databaseEntry.get(i).getUserName();
            if (userName.equals(fieldEntry)) {
                return true;
            } else {

            }
        }
        return false;
    }
 }

