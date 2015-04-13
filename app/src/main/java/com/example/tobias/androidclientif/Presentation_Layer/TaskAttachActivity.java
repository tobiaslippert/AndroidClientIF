package com.example.tobias.androidclientif.Presentation_Layer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import com.example.tobias.androidclientif.Entities.Assignment;
import com.example.tobias.androidclientif.Entities.Attachment;
import com.example.tobias.androidclientif.Entities.Task;
import com.example.tobias.androidclientif.Persistence_Layer.MySQLiteHelper;
import com.example.tobias.androidclientif.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


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
    static final int REQUEST_TAKE_PHOTO = 1;
    private MySQLiteHelper datasource;
    Bitmap imageBitmap;
    BitmapUtility bitmapUtility;
    HttpCustomClient client;
    TextView Problem_Desc;
    Task task;
    int Clicked = 0;
    int NoPic=0;
    String mCurrentPhotoPath;


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
        Save = (Button)findViewById(R.id.save);
        IMG = (ImageView)findViewById(R.id.imageView_Pic);

        Assignment assignment = datasource.getAssignmentById(assignmentId);
        if(assignment.getState()==2) {
            Problem_Desc.setEnabled(false);
            Save.setVisibility(View.GONE);
        }

        task = datasource.getTaskByTaskId(taskId);
        if(task.getErrorDescription()!=null){
            Problem_Desc.setText(task.getErrorDescription());
        }

        //if(datasource.getAttachmentPhotoByTaskId(taskId).length!=0){
        try{
            byte[] B = datasource.getAttachmentPhotoByTaskId(taskId);
            Bitmap bitmap = BitmapFactory.decodeByteArray(B, 0, B.length);
            IMG.setImageBitmap(bitmap);

        } catch (Exception e) {
        e.printStackTrace();
            NoPic=1;
            //Toast.makeText(getApplicationContext(), "problem encountred no pic "+NoPic,
                    //Toast.LENGTH_LONG).show();
    }

        //Butt = (Button)findViewById(R.id.button_Pic);
        if(assignment.getState()!=2) {
            IMG.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dispatchTakePictureIntent();

                }
            });
        }


        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Updating the task in the database

                task.setErrorDescription(Problem_Desc.getText().toString());
                task.setState(1);
                datasource.updateTask(task);



                //Creating a new assignment and store it to the database
                if (Clicked == 1) {

                    //The imagebitmap is transferred to byte[] before storing it to the database
                    //byte[] array = bitmapUtility.getBytes(imageBitmap);
                    IMG.buildDrawingCache();
                    Bitmap bmap = IMG.getDrawingCache();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
                    byte[] array = bos.toByteArray();
                    System.out.println(array);
                    Attachment attachment = new Attachment();
                    attachment.setAssignmentId(assignmentId);
                    attachment.setFile_type("Photo");
                    attachment.setBinaryObject(array);
                    attachment.setTaskId(taskId);
                    if(NoPic==1) {
                        datasource.createAttachment(attachment);
                        Toast.makeText(getApplicationContext(), "Attachment created ",
                                Toast.LENGTH_LONG).show();
                    }
                    else{
                        datasource.deleteAttachment(taskId);
                        datasource.createAttachment(attachment);
                        //Attachment exatt = datasource.getAttachmentsByTaskId(taskId);
                        //exatt.setBinaryObject(array);
                        //datasource.updateAttachment(exatt);
                        Toast.makeText(getApplicationContext(), "Attachment updated ",
                                Toast.LENGTH_LONG).show();
                    }

                }

            }
        });

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(getApplicationContext(), "File not created",
                        Toast.LENGTH_LONG).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            //Bundle extras = data.getExtras();
            //imageBitmap = (Bitmap) extras.get("data");
            //System.out.println(imageBitmap.toString());
            //IMG.setImageBitmap(imageBitmap);
            setPic();
            //Toast.makeText(getApplicationContext(), "Image Path: "+mCurrentPhotoPath,
                    //Toast.LENGTH_LONG).show();
            Clicked=1;
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }



    private void setPic() {
        // Get the dimensions of the View
        int targetW = IMG.getWidth();
        int targetH = IMG.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        IMG.setImageBitmap(bitmap);
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

}
