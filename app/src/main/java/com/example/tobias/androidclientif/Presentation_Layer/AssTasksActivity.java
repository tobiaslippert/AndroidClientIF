package com.example.tobias.androidclientif.Presentation_Layer;

import android.app.Activity;
import android.os.Bundle;
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

    private List<String> taskList = new ArrayList<>();
    private MySQLiteHelper datasource;
    ListView listViewAssTasks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assignment_tasks);
        datasource = new MySQLiteHelper(getApplicationContext());
        listViewAssTasks = (ListView) findViewById(R.id.lvAssTasks);

        for (int i = 0; i < datasource.getAllTasks().size(); i++){
            taskList.add(datasource.getAllTasks().get(i).getTaskName());
        }
        ListAdapter listenAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, taskList);
        listViewAssTasks.setAdapter(listenAdapter);
    }
}
