package com.example.tobias.androidclientif.Presentation_Layer;

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

    private List<String> taskList = new ArrayList<>();
    private MySQLiteHelper datasource;
    ListView listViewAssTasks;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assignment_tasks);
        datasource = new MySQLiteHelper(getApplicationContext());
        listViewAssTasks = (ListView) findViewById(R.id.lvAssTasks);

        CustomAdapter_Task listenAdapter = new CustomAdapter_Task(this, datasource.getAllTasks());
        listViewAssTasks.setAdapter(listenAdapter);

        listViewAssTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent openTaskAttach = new Intent(getApplicationContext(), TaskAttachActivity.class);
                startActivity(openTaskAttach);
            }
        });
    }
}
