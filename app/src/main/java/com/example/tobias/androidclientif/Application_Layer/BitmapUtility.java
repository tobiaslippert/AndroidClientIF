package com.example.tobias.androidclientif.Application_Layer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

/**
 * Created by Tobias on 07.03.15.
 */
public class BitmapUtility {

    public byte[] getBytes(Bitmap bitmap) {

        int bytes = bitmap.getByteCount();

        ByteBuffer buffer = ByteBuffer.allocate(bytes);
        bitmap.copyPixelsToBuffer(buffer);
        byte[] array = buffer.array();
        return array;
    }

    // convert from byte array to bitmap
    // this is needed when data from database is retrieved
    public Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
