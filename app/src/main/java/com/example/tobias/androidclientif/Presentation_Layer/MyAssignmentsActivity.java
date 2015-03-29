package com.example.tobias.androidclientif.Presentation_Layer;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
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

        /*listViewMyAss.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id)
            {
                Assignment clickedAssignment = listenAdapter.getClickedAssignment(pos);
                Toast.makeText(getApplicationContext(), "long clicked task is "+clickedAssignment.getAssignmentName(),
                        Toast.LENGTH_LONG).show();
                return true;
            }
        });*/
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
            default:
                return super.onContextItemSelected(item);
        }
    }
}
