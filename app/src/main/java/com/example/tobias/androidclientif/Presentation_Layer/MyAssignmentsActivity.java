package com.example.tobias.androidclientif.Presentation_Layer;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.content.Intent;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.example.tobias.androidclientif.Entities.Assignment;
import com.example.tobias.androidclientif.Persistence_Layer.MySQLiteHelper;
import com.example.tobias.androidclientif.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Tobias on 14.02.15.
 */
public class MyAssignmentsActivity extends Activity implements SearchView.OnQueryTextListener{

    //VAR-declaration
    ListView listViewMyAss;
    private MySQLiteHelper datasource;
    private List<Assignment> listWithAllStoredAssignments;
    private List<String> listOutput;
    CustomAdapter_Assignment listenAdapter;
    String UserId;
    SearchView searchView;
    MenuItem searchMenuItem;
    int checksort = 0;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.myassignments_activity);

        this.UserId = getIntent().getExtras().getString("UserId");
        datasource = new MySQLiteHelper(getApplicationContext());
        listViewMyAss = (ListView) findViewById(R.id.lvMyAss);
        //load all assignments from the database to the list listWithAllStoredAssigmments
        listWithAllStoredAssignments = datasource.getAssignmentsByUserId(UserId);

        listenAdapter = new CustomAdapter_Assignment(this, listWithAllStoredAssignments);
        listViewMyAss.setAdapter(listenAdapter);
        listViewMyAss.setTextFilterEnabled(true);
        registerForContextMenu(listViewMyAss);

        listViewMyAss.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Assignment clickedAssignment = listenAdapter.getClickedAssignment(position);
                Intent openAssTask = new Intent(getApplicationContext(), AssTasksActivity.class);
                openAssTask.putExtra("AssignmentName", clickedAssignment.getAssignmentName());
                openAssTask.putExtra("AssignmentId", clickedAssignment.getId());
                startActivity(openAssTask);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_search_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextChange(String newText)
    {
        // this is your adapter that will be filtered

        listenAdapter.getFilter().filter(newText);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Assignment Menu");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.assignment_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case R.id.Assignment_Details:
                Assignment clickedAssignment = listenAdapter.getClickedAssignment(info.position);
                Intent openAssDetails = new Intent(getApplicationContext(), AssignmentDetailsActivity.class);
                openAssDetails.putExtra("AssignmentId", clickedAssignment.getId());
                startActivity(openAssDetails);
                return true;

            case R.id.Assignment_notification:
                Assignment clickedAssignment2 = listenAdapter.getClickedAssignment(info.position);
                Notification noti = new Notification.Builder(getApplicationContext())
                        .setContentTitle("Reminder")
                        .setContentText("Close and sync: "+clickedAssignment2.getAssignmentName())
                        .setSmallIcon(R.drawable.if_gold_logo)
                        .build();
                scheduleNotification(noti, 10000, clickedAssignment2.getDueDate().intValue());


                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    private void scheduleNotification(Notification notification, int delay, int requestCode) {

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, requestCode);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.sort:

                if(checksort==0){

                    Collections.sort(listWithAllStoredAssignments, new CustomComparator());

                    listenAdapter.notifyDataSetChanged();
                    item.setTitle("Unsort");
                    checksort=1;
                }
                else{

                    finish();
                    startActivity(getIntent());
                }

                return true;
            case R.id.help:
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(MyAssignmentsActivity.this);

                dlgAlert.setMessage("Instructions:\n\n-  Press and hold any assignment to access the contextual menu where you can view details or set alerts\n\n-  Alert will be canceled if assignment is closed\n\n-  You can search and sort assignments in this activity");
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

    public class CustomComparator implements Comparator<Assignment> {
        @Override
        public int compare(Assignment a1, Assignment a2) {
            return a1.getDueDate().compareTo(a2.getDueDate());
        }
    }
    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}