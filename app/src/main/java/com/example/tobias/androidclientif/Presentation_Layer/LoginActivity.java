package com.example.tobias.androidclientif.Presentation_Layer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.tobias.androidclientif.R;

/**
 * Created by Tobias on 14.02.15.
 */
public class LoginActivity extends Activity{
    Button Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        Login = (Button) findViewById(R.id.login);
        Login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent openMenu = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(openMenu);
            }
        });
    }


    //VCS works

}
