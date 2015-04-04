package com.example.tobias.androidclientif.Presentation_Layer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tobias.androidclientif.Application_Layer.ApplicationHelper;
import com.example.tobias.androidclientif.Entities.Assignment;
import com.example.tobias.androidclientif.R;


public class CustomAdapter_Assignment extends BaseAdapter implements Filterable {

    List<Assignment> assignmentList;
    List<Assignment> assList;
    Context context;

    //Constructor; provides the needed context and list of assignments given by the calling method
    public CustomAdapter_Assignment(Context activityContext, List<Assignment> assignments) {
        super();
        this.context = activityContext;
        this.assignmentList = assignments;
        this.assList = assignments;
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
        ImageView AssignmentImage = (ImageView) convertView.findViewById(R.id.Assignment_imageView);
        Assignment assignment = assignmentList.get(position);


        if(assignment.getState()!=2) {
            AssignmentImage.setImageResource(R.drawable.checklist_not_finished);
        }
        else{
            AssignmentImage.setImageResource(R.drawable.checklist_finished);
        }
        Date dueDate = new Date(assignment.getDueDate());

        Name.setText(assignment.getAssignmentName());
        DueDate.setText(dueDate.toString());

        return convertView;
    }

    //Gives the item per position (needed for ClickListener)
    public Assignment getClickedAssignment(int position) {
        return assignmentList.get(position);
    }


    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                assignmentList = (List<Assignment>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                ArrayList<Assignment> FilteredArrayNames = new ArrayList<Assignment>();

                // perform your search here using the searchConstraint String.
                List<Assignment> asslist = assList;
                constraint = constraint.toString().toLowerCase();
                for (int i = 0; i < asslist.size(); i++) {
                    Assignment ass = asslist.get(i);
                    if (ass.getAssignmentName().toLowerCase().contains(constraint.toString()))  {
                        FilteredArrayNames.add(ass);
                    }
                }

                results.count = FilteredArrayNames.size();
                results.values = FilteredArrayNames;
                //Log.e("VALUES", results.values.toString());

                return results;
            }
        };

        return filter;
    }

}