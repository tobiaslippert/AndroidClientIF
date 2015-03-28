package com.example.tobias.androidclientif.Presentation_Layer;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.tobias.androidclientif.Entities.Assignment;
import com.example.tobias.androidclientif.Entities.InspectionObject;
import com.example.tobias.androidclientif.Persistence_Layer.MySQLiteHelper;
import com.example.tobias.androidclientif.R;

import org.w3c.dom.Text;

/**
 * Created by MElke_000 on 3/28/2015.
 */
public class AssignmentDetailsActivity extends Activity {
    String assignmentId;
    TextView AssignmentName;
    TextView AssignmentDescription;
    TextView ObjectName;
    TextView ObjectDescription;
    TextView ObjectCustomer;
    TextView ObjectLocation;
    Assignment assignment;
    InspectionObject object;
    MySQLiteHelper datasource;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assignment_details_activity);
        this.assignmentId = getIntent().getExtras().getString("AssignmentId");
        //Setting up the TextViews
        AssignmentName = (TextView) findViewById(R.id.Ass_Details_name);
        AssignmentDescription = (TextView) findViewById(R.id.Ass_Details_desc);
        ObjectName = (TextView) findViewById(R.id.Ass_Details_object_name);
        ObjectDescription = (TextView) findViewById(R.id.Ass_Details_object_desc);
        ObjectCustomer = (TextView) findViewById(R.id.Ass_Details_object_customer);
        ObjectLocation = (TextView) findViewById(R.id.Ass_Details_object_location);

        datasource = new MySQLiteHelper(getApplicationContext());

        //Getting the assignment information and setting up the TextViews
        assignment = datasource.getAssignmentById(assignmentId);
        AssignmentName.setText(assignment.getAssignmentName());
        AssignmentDescription.setText(assignment.getDescription());

        //Getting the object information and setting up the TextViews
        object = datasource.getInspectionObjectById(assignment.getInspectionObjectId());
        ObjectName.setText(object.getObjectName());
        ObjectDescription.setText(object.getDescription());
        ObjectCustomer.setText(object.getCustomerName());
        ObjectLocation.setText(object.getLocation());


    }
}
