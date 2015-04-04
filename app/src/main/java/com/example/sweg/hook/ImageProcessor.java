package com.example.sweg.hook;

import android.content.Context;
import android.net.Uri;

public class ImageProcessor {

    protected String fileName;
    protected Uri uri;

    public ImageProcessor(String fileName, Uri uri){
        this.fileName = fileName;
        this.uri = uri;
    }
}
