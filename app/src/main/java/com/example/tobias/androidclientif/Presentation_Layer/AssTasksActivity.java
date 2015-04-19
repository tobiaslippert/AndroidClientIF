package com.example.tobias.androidclientif.Presentation_Layer;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
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
    Assignment assignment;
    private Menu menu;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assignment_tasks);
        datasource = new MySQLiteHelper(getApplicationContext());
        listViewAssTasks = (ListView) findViewById(R.id.lvAssTasks);
        // Set Variables
        this.assignmentId = getIntent().getExtras().getString("AssignmentId");
        this.assignmentName = getIntent().getExtras().getString("AssignmentName");

        assignment = datasource.getAssignmentById(assignmentId);


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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.close:
                if(assignment.getState()==2) {
                    assignment.setState(1);
                    datasource.updateAssignment(assignment);
                    Toast.makeText(getApplicationContext(), assignment.getState().toString(),
                            Toast.LENGTH_LONG).show();
                    setOptionTitle();
                    listenAdapter.notifyDataSetChanged();
                }
                else{
                    /*List<Task> templist;
                    templist = datasource.getTasksByAssignmentId(assignmentId);
                    int complete=1;
                    for(int i =0;i<templist.size();i++){
                        if(templist.get(i).getState()!=1){
                        complete=0;
                        }
                    }*/
                    //if(complete==1) {
                        assignment.setState(2);
                        datasource.updateAssignment(assignment);
                        //Toast.makeText(getApplicationContext(), assignment.getState().toString(),
                                //Toast.LENGTH_LONG).show();
                        setOptionTitle();
                        listenAdapter.notifyDataSetChanged();

                        //canceling notification when the assignment is closed
                        Notification noti = new Notification.Builder(getApplicationContext())
                                .setContentTitle("Reminder")
                                .setContentText("you have set a reminder for the following assignment:"+assignment.getAssignmentName())
                                .setSmallIcon(R.drawable.if_gold_logo)
                                .build();
                        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
                        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, assignment.getDueDate().intValue());
                        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, noti);
                        PendingIntent pendingIntent;
                        PendingIntent.getBroadcast(this, assignment.getDueDate().intValue(), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT).cancel();


                    //}
                    /*else{
                        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                        alertDialog.setTitle("Alert");
                        alertDialog.setMessage("Some tasks are not checked. Please check all tasks before closing the assignment.");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }*/
                }
                return true;
            case R.id.help_task:
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(AssTasksActivity.this);

                dlgAlert.setMessage("Instructions:\n\n-  Press and hold any task to access the contextual menu where you can access the attachment activity\n\n-  Press the menu button to close/edit the assignment\n" +
                        "\n" +
                        "-  Note: you cannot close an incomplete assignment or edit a closed one");
                dlgAlert.setTitle("Help");
                dlgAlert.setPositiveButton("OK", null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();

                dlgAlert.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.task_action_bar_menu, menu);
        setOptionTitle();
        return true;
    }

    private void setOptionTitle()
    {
        MenuItem item = menu.findItem(R.id.close);
        if(assignment.getState()==2){
            item.setTitle("Edit assignment");
        }
        else{
            item.setTitle("Close assignment");
        }
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
