package com.example.tobias.androidclientif.Presentation_Layer;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tobias.androidclientif.Entities.Task;
import com.example.tobias.androidclientif.R;


public class CustomAdapter_Task extends BaseAdapter {

    List<Task> taskList;
    Context context;

    //Constructor; provides the needed context and list of assignments given by the calling method
    public CustomAdapter_Task(Context activityContext, List<Task> tasks) {
        super();
        this.context = activityContext;
        this.taskList = tasks;
    }

    @Override
    public int getCount() {
        return taskList.size();
    }

    @Override
    public Object getItem(int position) {
        return taskList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_row_task, parent, false);
        }
        TextView Name = (TextView) convertView.findViewById(R.id.taskName);
        TextView Desc = (TextView) convertView.findViewById(R.id.taskDescription);

        Task task = taskList.get(position);

        Name.setText(task.getTaskName());
        Desc.setText(task.getDescription());

        return convertView;
    }

    //Gives the item per position (needed for ClickListener)
    public Task getClickedAssignment(int position) {
        return taskList.get(position);
    }

}