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
import com.example.tobias.androidclientif.Entities.Attachment;
import com.example.tobias.androidclientif.Persistence_Layer.MySQLiteHelper;
import com.example.tobias.androidclientif.R;




/**
 * Created by MElke_000 on 2/28/2015.
 */
public class TaskAttachActivity extends Activity {

    private String taskId;
    private String taskName;
    Button Butt, Save;
    ImageView IMG;
    int REQUEST_IMAGE_CAPTURE =1;
    private MySQLiteHelper datasource;
    Bitmap imageBitmap;
    BitmapUtility bitmapUtility;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_attach_activity);

        this.taskId = getIntent().getExtras().getString("TaskId");
        this.taskName = getIntent().getExtras().getString("TaskName");

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
                attachment.setBinaryObject(bitmapUtility.getBytes(imageBitmap));
                datasource.createAttachment(attachment);
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
            IMG.setImageBitmap(imageBitmap);
        }
    }


}
