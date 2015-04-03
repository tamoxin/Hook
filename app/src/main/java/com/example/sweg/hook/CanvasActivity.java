package com.example.sweg.hook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class CanvasActivity extends ActionBarActivity {

    public static final String TAG = CanvasActivity.class.getSimpleName();
    public static final int takePhotoRequest = 0;
    public static final int pickPhotoRequest = 1;
    public static final int mediaTypeImage = 3;

    protected Uri mediaUri;

    protected DialogInterface.OnClickListener mDialogListener =
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch(which){
                        case 0: //Take a picture
                            //This intent calls the camera app to take a photo
                            Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            mediaUri = getOutputMediaFileUri(mediaTypeImage);
                            if(mediaUri == null){
                                //Display an error
                                Toast.makeText(CanvasActivity.this, R.string.error_external_store, Toast.LENGTH_LONG).show();
                            }
                            else{

                                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mediaUri);
                                //The starActivityForResult starts the camera app and wait
                                //for the camera to give back a result that is the TakePhotoRequest int
                                startActivityForResult(takePhotoIntent, takePhotoRequest);
                            }
                            break;
                        case 1: //Choose a picture
                            Intent choosePhotoIntent = new Intent(Intent.ACTION_GET_CONTENT);
                            choosePhotoIntent.setType("image/*");
                            startActivityForResult(choosePhotoIntent, pickPhotoRequest);
                            break;
                    }
                }

                private Uri getOutputMediaFileUri(int mediaType) {
                    // To be safe, you should check that the SDCard is mounted
                    // using Environment.getExternalStorageState() before doing this.
                    if(isExternalStorageAvailable()) {
                        //Get URI
                        //1. Get the external storage directory
                        String appName = CanvasActivity.this.getString(R.string.app_name);
                        File mediaStorageDir =
                                new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), appName);
                        //2. Create our subdirectory
                        //Checks if the directory exists
                        if (!mediaStorageDir.exists()) {
                            //This if returns false if there was an error creating the directory
                            if (!mediaStorageDir.mkdirs()) {
                                Log.e(TAG, "Failed to create the directory");
                                return null;
                            }
                        }
                        //3. Create a file name
                        //4. Create the file
                        File mediaFile;
                        Date now = new Date();
                        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(now);

                        String path = mediaStorageDir.getPath() + File.separator;
                        if (mediaType == mediaTypeImage)
                            mediaFile = new File(path + "IMG_" + timestamp + ".jpg");
                        else
                            return null;

                        Log.d(TAG, "File: " + Uri.fromFile(mediaFile));
                        //5. Return the file's URI
                        return Uri.fromFile(mediaFile);
                    }
                    else
                        return null;
                }

                private boolean isExternalStorageAvailable(){
                    String state = Environment.getExternalStorageState();

                    if (state.equals(Environment.MEDIA_MOUNTED))
                        return true;
                    else
                        return false;
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas);

        //Intent intent = new Intent(this, IpAddressActivity.class);
        //This two methods control the stack of navigation obligating the user to
        // setup the UDP connection before selecting an image to send.
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //startActivity(intent);
    }

    @Override
    //This method puts the pictures taken in the android gallery
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            // add it to the Gallery

            if (requestCode == pickPhotoRequest) {
                if (data == null)
                    Toast.makeText(this, getString(R.string.general_error), Toast.LENGTH_LONG).show();
                else
                    mediaUri = data.getData();
            }
            else {
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(mediaUri);
                sendBroadcast(mediaScanIntent);
            }
        }
        else if (resultCode != RESULT_CANCELED) {
            Toast.makeText(this, R.string.general_error, Toast.LENGTH_LONG).show();
        }
    }

    protected DialogInterface.OnClickListener settingsDialogListener =
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch(which){
                        case 0:
                            Intent intent = new Intent(CanvasActivity.this, IpAddressActivity.class);
                            startActivity(intent);
                    }
                }
            };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_canvas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        AlertDialog.Builder builder;
        AlertDialog dialog;

        switch(id){
            case R.id.action_camera:
                builder = new AlertDialog.Builder(this);
                builder.setItems(R.array.camera_choices, mDialogListener);
                dialog = builder.create();
                dialog.show();
            case R.id.action_share:

            case R.id.action_settings:
                builder = new AlertDialog.Builder(this);
                builder.setItems(R.array.settings_choices, settingsDialogListener);
                dialog = builder.create();
                dialog.show();
        }

        return super.onOptionsItemSelected(item);
    }
}
