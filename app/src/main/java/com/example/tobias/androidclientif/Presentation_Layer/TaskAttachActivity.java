package com.example.tobias.androidclientif.Presentation_Layer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


import com.example.tobias.androidclientif.Application_Layer.BitmapUtility;
import com.example.tobias.androidclientif.Application_Layer.HttpCustomClient;
import com.example.tobias.androidclientif.Entities.Attachment;
import com.example.tobias.androidclientif.Entities.Task;
import com.example.tobias.androidclientif.Persistence_Layer.MySQLiteHelper;
import com.example.tobias.androidclientif.R;




/**
 * Created by MElke_000 on 2/28/2015.
 */
public class TaskAttachActivity extends Activity {

    private String taskId;
    private String taskName;
    private String assignmentId;
    Button Butt, Save;
    ImageView IMG;
    int REQUEST_IMAGE_CAPTURE =1;
    private MySQLiteHelper datasource;
    Bitmap imageBitmap;
    BitmapUtility bitmapUtility;
    HttpCustomClient client;
    TextView Problem_Desc;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_attach_activity);

        datasource = new MySQLiteHelper(getApplicationContext());
        bitmapUtility = new BitmapUtility();
        client = new HttpCustomClient();
        this.taskId = getIntent().getExtras().getString("TaskId");
        this.taskName = getIntent().getExtras().getString("TaskName");
        this.assignmentId = getIntent().getExtras().getString("AssignmentId");
        Problem_Desc = (TextView) findViewById(R.id.problem_desc);


        //Butt = (Button)findViewById(R.id.button_Pic);
        IMG = (ImageView)findViewById(R.id.imageView_Pic);
        IMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dispatchTakePictureIntent();

            }
        });

        Save = (Button)findViewById(R.id.save);
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Updating the task in the database
                Task task = new Task();
                task = datasource.getTaskByTaskId(taskId);
                task.setErrorDescription(Problem_Desc.getText().toString());
                datasource.updateTask(task);

                //Creating a new assignment and store it to the database
                if (IMG !=null) {
                    Attachment attachment = new Attachment();
                    //The imagebitmap is transferred to byte[] before storing it to the database
                    byte[] array = bitmapUtility.getBytes(imageBitmap);
                    attachment.setBinaryObject(array);
                    attachment.setTaskId(taskId);
                    datasource.createAttachment(attachment);
                    task.setState(1);
                }
                else{

                    System.out.println("No attachment created and saved, because no picture has been taken");
                }
            }
        });

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            System.out.println(imageBitmap.toString());
            IMG.setImageBitmap(imageBitmap);
        }
    }


}
