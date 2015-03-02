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



import com.example.tobias.androidclientif.R;




/**
 * Created by MElke_000 on 2/28/2015.
 */
public class TaskAttachActivity extends Activity {
    Spinner spinner;
    String[] Str = {"Picture","Audio"};
    Button Butt;
    ImageView IMG;
    int REQUEST_IMAGE_CAPTURE =1;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_attach_activity);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(TaskAttachActivity.this, android.R.layout.simple_spinner_item, Str);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        Butt = (Button)findViewById(R.id.button_Pic);
        IMG = (ImageView)findViewById(R.id.imageView_Pic);
        Butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dispatchTakePictureIntent();

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
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            IMG.setImageBitmap(imageBitmap);
        }
    }

}
