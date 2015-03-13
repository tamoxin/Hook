package com.example.sweg.hook;

import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class ipAddressActivity extends ActionBarActivity {

    TextView cellphoneIP;
    EditText serverIP, serverPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip_address);
        WifiManager wifiMgr = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        String ipAddress = Formatter.formatIpAddress(ip);
        cellphoneIP = (TextView) findViewById(R.id.textView_cellphone_ip);
        serverIP = (EditText) findViewById(R.id.editText_server_ip);
        cellphoneIP.setText(""+ipAddress);
        serverPort = (EditText) findViewById(R.id.port_editText);
    }

    public void onClick(View view)
    {
        String serverIPString, serverPortString;
        serverIPString = serverIP.getText().toString();
        serverPortString = serverPort.getText().toString();

        if (serverIPString.matches("") || serverIPString.length()<7)
        {
            Toast.makeText(this, this.getString(R.string.ip_toast_error), Toast.LENGTH_SHORT).show();
            return;
        }else if(serverPortString.matches(""))
        {
            Toast.makeText(this, this.getString(R.string.port_toast_error), Toast.LENGTH_SHORT).show();
            return;
        }

        Intent shareContent = new Intent(this, BrowseImageActivity.class);
        shareContent.putExtra("ipServer", serverIPString);
        shareContent.putExtra("ipPort", serverPortString);
        startActivity(shareContent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ip_address, menu);
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
