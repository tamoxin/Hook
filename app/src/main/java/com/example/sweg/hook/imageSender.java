package com.example.sweg.hook;

import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ImageSender implements Runnable{
    String ip;
    int port;
    byte[] bytes;

    public ImageSender(String ip, int port, byte[] bytes)
    {
        this.ip = ip;
        this.port = port;
        this.bytes = bytes;
        Log.d("ip_Sender", this.ip);
        Log.d("port_Sender", String.valueOf(this.port));
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

                        /* Create UDP-packet with
                         * data & destination(url+port) */
            DatagramPacket packetDirection = new DatagramPacket(bytes, bytes.length, serverAddress, port);
            Log.d("UDP", "C: Sending: '" + new String(bytes) + "'");

                        /* Send out the packet */
            socket.send(packetDirection);
            Log.d("UDP", "C: Sent.");
            Log.d("UDP", "C: Done.");
        } catch (Exception e) {
            Log.e("UDP", "C: Error", e);
        }
    }
}
