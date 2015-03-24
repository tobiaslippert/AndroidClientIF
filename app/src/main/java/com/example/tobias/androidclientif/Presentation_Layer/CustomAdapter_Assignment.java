package com.example.tobias.androidclientif.Presentation_Layer;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tobias.androidclientif.Entities.Assignment;
import com.example.tobias.androidclientif.R;


public class CustomAdapter_Assignment extends BaseAdapter {

    List<Assignment> assignmentList;
    Context context;

    //Constructor; provides the needed context and list of assignments given by the calling method
    public CustomAdapter_Assignment(Context activityContext, List<Assignment> assignments) {
        super();
        this.context = activityContext;
        this.assignmentList = assignments;
    }

    @Override
    public int getCount() {
        return assignmentList.size();
    }

    @Override
    public Object getItem(int position) {
        return assignmentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_row_assignment, parent, false);
        }
        TextView Name = (TextView) convertView.findViewById(R.id.assignmentName);
        TextView DueDate = (TextView) convertView.findViewById(R.id.assignmentDueDate);

        Assignment assignment = assignmentList.get(position);

        Date dueDate = new Date(assignment.getDueDate());

        Name.setText(assignment.getAssignmentName());
        DueDate.setText(dueDate.toString());

        return convertView;
    }

    //Gives the item per position (needed for ClickListener)
    public Assignment getClickedAssignment(int position) {
        return assignmentList.get(position);
    }

}