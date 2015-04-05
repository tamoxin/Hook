package com.example.sweg.hook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class CanvasActivity extends ActionBarActivity {

    static final String TAG = CanvasActivity.class.getSimpleName();
    static final int takePhotoRequest = 0;
    static final int pickPhotoRequest = 1;
    static final int mediaTypeImage = 3;
    Uri mediaUri = null;
    String ip;
    String port;
    ImageView selectedImage;
    SharedPreferences savedValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas);
        selectedImage = (ImageView) findViewById(R.id.imageSelected);
        savedValues = getSharedPreferences("portAndAddress", 0);
        ip = savedValues.getString("ip", null);
        port = savedValues.getString("port", null);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    @Override
    // This method puts the pictures taken in the android gallery
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

            // This method flush the ImageView cache.
            selectedImage.setImageURI(null);

            // This is the equivalent of selectImage.setImageURI(mediaUri);
            // but if done that way, bitmap too large to be uploaded into a texture
            // error appears.
            Picasso.with(getApplicationContext()).
                    load(mediaUri).fit().centerInside().into(selectedImage);
        }
        else if (resultCode != RESULT_CANCELED) {
            Toast.makeText(this, R.string.general_error, Toast.LENGTH_LONG).show();
        }
    }

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
                break;

            case R.id.action_share:
                PopupMenu popupShare = new PopupMenu(CanvasActivity.this,
                        findViewById(R.id.action_share));
                popupShare.inflate(R.menu.menu_canvas_share_popup);
                popupShare.show();
                break;

            case R.id.action_settings:
                PopupMenu popupSettings = new PopupMenu(CanvasActivity.this,
                        findViewById(R.id.action_overflow));
                popupSettings.inflate(R.menu.menu_canvas_settings_popup);
                popupSettings.show();
                break;

            case R.id.action_overflow:
                PopupMenu popupOverflow = new PopupMenu(CanvasActivity.this,
                        findViewById(R.id.action_overflow));
                popupOverflow.inflate(R.menu.menu_canvas_overflow_popup);
                popupOverflow.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    protected DialogInterface.OnClickListener mDialogListener =
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch(which){
                        case 0: //Take a picture
                            //This intent calls the camera app to take a photo
                            Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            mediaUri = getOutputMediaFileUri(mediaTypeImage);
                            if (mediaUri == null) {
                                //Display an error
                                Toast.makeText(CanvasActivity.this,
                                        R.string.error_external_store,
                                        Toast.LENGTH_LONG).show();
                            } else {
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
                        //5. Return the file's URI
                        return Uri.fromFile(mediaFile);
                    }
                    else
                        return null;
                }

                private boolean isExternalStorageAvailable(){
                    String state = Environment.getExternalStorageState();
                    return state.equals(Environment.MEDIA_MOUNTED);
                }
            };

    public void settingsOnClick(MenuItem item) {
        Intent intent = new Intent(CanvasActivity.this, IpAddressActivity.class);
        startActivity(intent);
    }

    public void overflowOnClick(MenuItem item) {
        Toast.makeText(getApplicationContext(), R.string.menu_about_message_label, Toast.LENGTH_SHORT).show();
    }

    public void shareViaUdpOnClick(MenuItem item) throws FileNotFoundException {

        //Get information of the receiver
        Intent serverData = getIntent();
        ip = serverData.getStringExtra("ipServer");
        port = serverData.getStringExtra("ipPort");

        if(mediaUri == null){
            Toast.makeText(getApplicationContext(), getString(R.string.uri_error_null), Toast.LENGTH_SHORT).show();
        }else{

            if(port == null || ip == null){
                Intent intent = new Intent(this, IpAddressActivity.class);
                startActivity(intent);
            }else{
                byte[] fileBytes = FileHelper.getByteArrayFromFile(this, mediaUri);
                if(fileBytes == null) {
                    Log.d("Error in fileBytes", "lol");
                    return;
                }else {
                    fileBytes = FileHelper.reduceImageForUpload(fileBytes);
                }
                String fileName = FileHelper.getFileName(this, mediaUri);

                // Send the image
                ImageSender sender = new ImageSender(ip, port, fileBytes, fileName);
                sender.run();

                Toast.makeText(getApplicationContext(),
                        getString(R.string.sending_picture_label), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void shareViaBluetooth(MenuItem item) {
        Toast.makeText(getApplicationContext(),
                getString(R.string.sending_picture_label), Toast.LENGTH_LONG).show();
    }

}