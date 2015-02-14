package com.example.tobias.androidclientif.Presentation_Layer;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.tobias.androidclientif.Persistence_Layer.MySQLiteHelper;
import com.example.tobias.androidclientif.R;

import java.util.List;

/**
 * Created by Tobias on 14.02.15.
 */
public class MyAssignmentsActivity extends Activity {

    //VAR-declaration
    ListView listViewMyAss;
    private MySQLiteHelper datasource;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.myassignments_activity);
        datasource = new MySQLiteHelper(getApplicationContext());
        listViewMyAss = (ListView) findViewById(R.id.lvMyAss);
        ListAdapter listenAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, datasource.getAllAssignments());
        listViewMyAss.setAdapter(listenAdapter);

    }
}
