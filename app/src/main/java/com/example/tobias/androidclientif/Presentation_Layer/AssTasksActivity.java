package com.example.tobias.androidclientif.Presentation_Layer;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tobias.androidclientif.Entities.Assignment;
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
    private List<Task> taskList = new ArrayList<>();
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
        taskList = datasource.getTasksByAssignmentId(assignmentId);
        listenAdapter = new CustomAdapter_Task(this, datasource.getTasksByAssignmentId(assignmentId));
        listViewAssTasks.setAdapter(listenAdapter);
        registerForContextMenu(listViewAssTasks);

        /*listViewAssTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task clickedTask = listenAdapter.getClickedTask(position);
                Intent openTaskAttach = new Intent(getApplicationContext(), TaskAttachActivity.class);
                openTaskAttach.putExtra("TaskName", clickedTask.getTaskName());
                openTaskAttach.putExtra("TaskId", clickedTask.getId());
                openTaskAttach.putExtra("AssignmentId", assignmentId);
                startActivity(openTaskAttach);
            }
        });*/
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
            menu.setHeaderTitle("Task Menu");
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.task_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case R.id.Task_Attachment:
                Task clickedTask = listenAdapter.getClickedTask(info.position);
                Intent openTaskAttach = new Intent(getApplicationContext(), TaskAttachActivity.class);
                openTaskAttach.putExtra("TaskName", clickedTask.getTaskName());
                openTaskAttach.putExtra("TaskId", clickedTask.getId());
                openTaskAttach.putExtra("AssignmentId", assignmentId);
                startActivityForResult(openTaskAttach, 1);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    /*@Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int pos = listViewAssTasks.getPositionForView(buttonView);

        if (pos != ListView.INVALID_POSITION) {
            Task t = taskList.get(pos);
            if(t.getState()==1) {
                t.setState(0);
                datasource.updateTask(t);
                Toast.makeText(getApplicationContext(), "state 0 set",
                        Toast.LENGTH_LONG).show();
            }
            else{
                t.setState(1);
                datasource.updateTask(t);
                Toast.makeText(getApplicationContext(), "state 1 set",
                        Toast.LENGTH_LONG).show();
            }

            Toast T;

        }

        }*/

    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }


    }
