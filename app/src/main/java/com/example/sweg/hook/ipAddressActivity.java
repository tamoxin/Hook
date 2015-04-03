package com.example.sweg.hook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.format.Formatter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class IpAddressActivity extends ActionBarActivity {

    TextView cellphoneIP;
    EditText serverIp, serverPort;
    String serverIpString, serverPortString;
    SharedPreferences savedValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip_address);

        savedValues = getSharedPreferences("portAndAddress", 0);
        WifiManager wifiMgr = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        String ipAddress = Formatter.formatIpAddress(ip);
        cellphoneIP = (TextView) findViewById(R.id.textView_cellphone_ip);
        serverIp = (EditText) findViewById(R.id.editText_server_ip);
        serverPort = (EditText) findViewById(R.id.port_editText);

        serverIp.setText(savedValues.getString("ip", ""));
        serverPort.setText(savedValues.getString("port", ""));
        cellphoneIP.setText(""+ipAddress);
    }

    public void onClick(View view) {
        //If the user insert and invalid IP or Port
        if (serverIp.getText().toString().matches("") || serverIp.getText().toString().length()<7)
        {
            Toast.makeText(this, this.getString(R.string.ip_toast_error), Toast.LENGTH_SHORT).show();
            return;
        }else if(serverPort.getText().toString().matches(""))
        {
            Toast.makeText(this, this.getString(R.string.port_toast_error), Toast.LENGTH_SHORT).show();
            return;
        }

        serverIpString = serverIp.getText().toString();
        serverPortString = serverPort.getText().toString();

        //This part saves the IP and Port in cache so the user does not have to write them
        //every time she wants to send an image
        SharedPreferences.Editor editor = savedValues.edit();
        editor.putString("ip", serverIpString);
        editor.putString("port", serverPortString);
        editor.apply();

        Intent shareContent = new Intent(this, CanvasActivity.class);
        shareContent.putExtra("ipServer", serverIpString);
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
