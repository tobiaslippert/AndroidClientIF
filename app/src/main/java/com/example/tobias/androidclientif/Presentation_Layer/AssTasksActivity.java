package com.example.tobias.androidclientif.Presentation_Layer;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.tobias.androidclientif.Entities.Task;
import com.example.tobias.androidclientif.Persistence_Layer.MySQLiteHelper;
import com.example.tobias.androidclientif.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MElke_000 on 2/15/2015.
 */
public class AssTasksActivity extends Activity{
    private String assignmentId;
    private String assignmentName;
    private List<String> taskList = new ArrayList<>();
    private MySQLiteHelper datasource;
    ListView listViewAssTasks;
    CustomAdapter_Task listenAdapter;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assignment_tasks);
        datasource = new MySQLiteHelper(getApplicationContext());
        listViewAssTasks = (ListView) findViewById(R.id.lvAssTasks);
        // Set Variables
        this.assignmentId = getIntent().getExtras().getString("AssignmentId");
        this.assignmentName = getIntent().getExtras().getString("AssignmentName");

        // Adjust Action Bar title
        //ActionBar actionBar = getActionBar();
        //actionBar.setTitle(getString(R.string.title_activity_task_list) + ": " + assignmentName);


        listenAdapter = new CustomAdapter_Task(this, datasource.getTasksByAssignmentId(assignmentId));
        listViewAssTasks.setAdapter(listenAdapter);

        listViewAssTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task clickedTask = listenAdapter.getClickedTask(position);
                Intent openTaskAttach = new Intent(getApplicationContext(), TaskAttachActivity.class);
                openTaskAttach.putExtra("TaskName", clickedTask.getTaskName());
                openTaskAttach.putExtra("TaskId", clickedTask.getId());
                startActivity(openTaskAttach);
            }
        });
    }
}
