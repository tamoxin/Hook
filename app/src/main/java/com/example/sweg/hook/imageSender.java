package com.example.sweg.hook;

import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ImageSender implements Runnable{
    String ip, degrees, accelerometer, direction;
    int port;

    public ImageSender(String ip, int port)
    {
        this.ip = ip;
        this.port = port;
        Log.d("ip_Sender", this.ip);
        Log.d("port_Sender", String.valueOf(this.port));
    }

    public void setMessage()
    {
        direction = ip+"-----"+port;
    }

    public void setMessage(String[] dataPackage)
    {
        this.degrees = dataPackage[0];
        accelerometer = dataPackage[1];
        direction = degrees + accelerometer;
        Log.d("Direction:", degrees+"----------"+accelerometer);
    }

    @Override
    public void run()
    {
        try {
            // Retrieve the ServerName
            InetAddress serverAddress = InetAddress.getByName(ip);

            Log.d("UDP", "C: Connecting...");
                        /* Create new UDP-Socket */
            DatagramSocket socket = new DatagramSocket();

                        /* Prepare some data to be sent. */
            byte[] buffer = (direction).getBytes();

                        /* Create UDP-packet with
                         * data & destination(url+port) */
            DatagramPacket packetDirection = new DatagramPacket(buffer, buffer.length, serverAddress, port);
            Log.d("UDP", "C: Sending: '" + new String(buffer) + "'");

                        /* Send out the packet */
            socket.send(packetDirection);
            Log.d("UDP", "C: Sent.");
            Log.d("UDP", "C: Done.");
        } catch (Exception e) {
            Log.e("UDP", "C: Error", e);
        }
    }
}
