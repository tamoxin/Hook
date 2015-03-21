package com.example.sweg.hook;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;


public class BrowseImageActivity extends ActionBarActivity {

    //Variables to communicate thorough classes
    public Bundle ipRecover;
    public String ipServer;
    public int portServer;
    //Variables for sending data
    private ImageSender sender;
    private String degrees,accelerometer;
    private String[] dataPackage;
    private Thread senderThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_picture);

        ipRecover = getIntent().getExtras();
        ipServer = ipRecover.getString("ipServer");
        portServer = Integer.parseInt(ipRecover.getString("ipPort"));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_browse_picture, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
