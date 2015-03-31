package com.example.tobias.androidclientif.Presentation_Layer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.tobias.androidclientif.Application_Layer.BitmapUtility;
import com.example.tobias.androidclientif.Application_Layer.HttpCustomClient;
import com.example.tobias.androidclientif.Entities.Attachment;
import com.example.tobias.androidclientif.Entities.Task;
import com.example.tobias.androidclientif.Persistence_Layer.MySQLiteHelper;
import com.example.tobias.androidclientif.R;

import java.io.ByteArrayOutputStream;


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
    Task task;
    int Clicked = 0;


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
        IMG = (ImageView)findViewById(R.id.imageView_Pic);

        task = datasource.getTaskByTaskId(taskId);
        if(task.getErrorDescription()!=null){
            Problem_Desc.setText(task.getErrorDescription());
        }

        /*if(datasource.getAttachmentPhotoByTaskId(taskId)!=null){
            byte[] B = datasource.getAttachmentPhotoByTaskId(taskId);
            Bitmap bitmap = BitmapFactory.decodeByteArray(B, 0, B.length);
            IMG.setImageBitmap(bitmap);
            Toast.makeText(getApplicationContext(), datasource.getAttachmentPhotoByTaskId(taskId).toString(),
                    Toast.LENGTH_LONG).show();
        }*/

        //Butt = (Button)findViewById(R.id.button_Pic);

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

                task.setErrorDescription(Problem_Desc.getText().toString());
                task.setState(1);
                datasource.updateTask(task);
                Toast.makeText(getApplicationContext(), "Error desc updated",
                        Toast.LENGTH_LONG).show();


                //Creating a new assignment and store it to the database
                if (Clicked == 1) {
                    Attachment attachment = new Attachment();
                    //The imagebitmap is transferred to byte[] before storing it to the database
                    //byte[] array = bitmapUtility.getBytes(imageBitmap);
                    IMG.buildDrawingCache();
                    Bitmap bmap = IMG.getDrawingCache();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
                    byte[] array = bos.toByteArray();
                    System.out.println(array);
                    attachment.setBinaryObject(array);
                    attachment.setTaskId(taskId);
                    if(datasource.getAttachmentPhotoByTaskId(taskId)!=null){
                        datasource.updateAttachment(attachment);
                        Toast.makeText(getApplicationContext(), "Attachment Updated",
                                Toast.LENGTH_LONG).show();
                    }
                    else{
                        datasource.createAttachment(attachment);
                        Toast.makeText(getApplicationContext(), "Attachment created",
                                Toast.LENGTH_LONG).show();
                    }


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
            Clicked=1;
        }
    }


}
