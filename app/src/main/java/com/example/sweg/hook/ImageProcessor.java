package com.example.sweg.hook;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.media.Image;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class ImageProcessor {

    protected String fileName;
    protected Uri uri;
    protected String path;
    protected byte[] byteArray;

    public ImageProcessor(String fileName, Uri uri){
        this.fileName = fileName;
        this.uri = uri;
        this.path = uri.getPath();
    }

    public void imageToByteArray(){
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        this.byteArray = stream.toByteArray();
    }

    public byte[] getByteArray(){
        return this.byteArray;
    }

}
