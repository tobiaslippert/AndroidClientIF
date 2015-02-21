package com.example.tobias.androidclientif.Presentation_Layer;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.tobias.androidclientif.Entities.Assignment;
import com.example.tobias.androidclientif.Persistence_Layer.MySQLiteHelper;
import com.example.tobias.androidclientif.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tobias on 14.02.15.
 */
public class MyAssignmentsActivity extends Activity {

    //VAR-declaration
    ListView listViewMyAss;
    private MySQLiteHelper datasource;
    private List<Assignment> listWithAllStoredAssignments;
    private List<String> listOutput;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.myassignments_activity);
        datasource = new MySQLiteHelper(getApplicationContext());
        listViewMyAss = (ListView) findViewById(R.id.lvMyAss);
        //load all assignments from the database to the list listWithAllStoredAssigmments
        listWithAllStoredAssignments = datasource.getAllAssignments();
        listOutput = new ArrayList<>();

            for (int i = 0; i < listWithAllStoredAssignments.size(); i++){
                Assignment assignment = listWithAllStoredAssignments.get(i);
                String assignmentName = assignment.getAssignmentName();
                listOutput.add(assignmentName);
                System.out.println(assignmentName);
            }

        ListAdapter listenAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listOutput);
        listViewMyAss.setAdapter(listenAdapter);

    }
}
