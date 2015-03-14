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


import com.example.tobias.androidclientif.Application_Layer.BitmapUtility;
import com.example.tobias.androidclientif.Application_Layer.HttpCustomClient;
import com.example.tobias.androidclientif.Entities.Attachment;
import com.example.tobias.androidclientif.Persistence_Layer.MySQLiteHelper;
import com.example.tobias.androidclientif.R;




/**
 * Created by MElke_000 on 2/28/2015.
 */
public class TaskAttachActivity extends Activity {

    Button Butt, Save;
    ImageView IMG;
    int REQUEST_IMAGE_CAPTURE =1;
    private MySQLiteHelper datasource;
    Bitmap imageBitmap;
    BitmapUtility bitmapUtility;
    HttpCustomClient client;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_attach_activity);

        datasource = new MySQLiteHelper(getApplicationContext());
        bitmapUtility = new BitmapUtility();
        client = new HttpCustomClient();
        Butt = (Button)findViewById(R.id.button_Pic);
        IMG = (ImageView)findViewById(R.id.imageView_Pic);
        Butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dispatchTakePictureIntent();

            }
        });

        Save = (Button)findViewById(R.id.save);
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Attachment attachment = new Attachment();
                //The imagebitmap is transferred to byte[] before storing it to the database
                byte[] array = bitmapUtility.getBytes(imageBitmap);
                attachment.setBinaryObject(array);
                System.out.println(imageBitmap);
                System.out.println(attachment.getBinaryObject());
                attachment.setTaskId("54fe1bbbe4b0d176d07102ef");
                datasource.createAttachment(attachment);
                Attachment attachment1 = new Attachment();
                attachment1.setTaskId("54fe1bbbe4b0d176d07102ef");
                attachment1.setAssignmentId("54fe1bbbe4b0d176d07102f0");
                attachment1.setBinaryObject(array);
                //attachment1.setBinaryObject(datasource.getAttachmentPhotoByTaskId(attachment1.getTaskId()));
                client.postAttachmentToHerokuServer(attachment1.getAssignmentId(),attachment1.getTaskId(), attachment1.getBinaryObject());
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
