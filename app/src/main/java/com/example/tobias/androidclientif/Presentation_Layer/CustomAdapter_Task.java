package com.example.tobias.androidclientif.Presentation_Layer;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tobias.androidclientif.Entities.Assignment;
import com.example.tobias.androidclientif.Entities.Task;
import com.example.tobias.androidclientif.Persistence_Layer.MySQLiteHelper;
import com.example.tobias.androidclientif.R;


public class CustomAdapter_Task extends BaseAdapter {

    List<Task> taskList;
    Context context;
    //CheckBox CB;
    RadioButton Ok;
    RadioButton NotOk;
    RadioGroup RG;
    private MySQLiteHelper datasource;


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
        //CB = (CheckBox) convertView.findViewById(R.id.checkBox);
        Ok = (RadioButton) convertView.findViewById(R.id.radioOk);
        NotOk = (RadioButton) convertView.findViewById(R.id.radioNotOk);
        RG = (RadioGroup) convertView.findViewById(R.id.radioGroup);


        final Task task = taskList.get(position);
        Name.setText(task.getTaskName());
        Desc.setText(task.getDescription());

        datasource = new MySQLiteHelper((AssTasksActivity) context);
        Assignment TempAssignment = datasource.getAssignmentById(task.getAssignmentId());

        //Setting up the radioButtons (OK/NotOK)
        if(task.getState() == 1) {
            RG.check(R.id.radioOk);
        } else if(task.getState() == 2) {
            RG.check(R.id.radioNotOk);
        }

        //Enabling or disabling editing of taskstat according to assignment stat
        if(TempAssignment.getState()==2){
            Ok.setEnabled(false);
            NotOk.setEnabled(false);
        }
        else{
            Ok.setEnabled(true);
            NotOk.setEnabled(true);
        }



        Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datasource = new MySQLiteHelper((AssTasksActivity) context);
                final Task Ta = task;
                //Ta.setState(1);
                if(!Ta.getErrorDescription().isEmpty()) {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);
                    dlgAlert.setMessage("This task has an error linked to it.\nDo you want to delete it?");
                    dlgAlert.setTitle("Warning...");
                    dlgAlert.setPositiveButton("No", null);
                    dlgAlert.setNegativeButton("Yes", null);
                    dlgAlert.setCancelable(false);
                    dlgAlert.setPositiveButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Ta.setState(1);
                                    datasource.updateTask(Ta);
                                }
                            });
                    dlgAlert.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Ta.setErrorDescription("");
                            Ta.setState(1);
                            datasource.updateTask(Ta);
                            datasource.deleteAttachment(Ta.getId());
                            Toast.makeText(context, "Error was deleted",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                    dlgAlert.create().show();

                }
                else{
                    Ta.setState(1);
                    datasource.updateTask(Ta);
                }

            }
        });

        NotOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datasource = new MySQLiteHelper((AssTasksActivity) context);
                final Task Ta = task;
                Ta.setState(2);
                datasource.updateTask(Ta);
            }
        });


        return convertView;
    }

    //Gives the item per position (needed for ClickListener)
    public Task getClickedTask(int position) {
        return taskList.get(position);
    }

}