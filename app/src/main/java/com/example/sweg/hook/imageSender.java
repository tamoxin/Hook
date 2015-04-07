package com.example.sweg.hook;

import android.os.AsyncTask;
import android.util.Log;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ImageSender {

    String ip;
    int port;
    byte[] fileBytes;
    byte[] fileName;
    String bytesLength;

    public ImageSender(String ip, String port, byte[] bytes, String fileName) {
        this.ip = ip;
        this.port = Integer.parseInt(port);
        this.fileBytes = bytes;
        this.fileName = fileName.getBytes();
        this.bytesLength = "" + fileBytes.length;
    }

    public void run() {
        BackgroundProcess run = new BackgroundProcess();
        run.execute(this);
    }

    private class BackgroundProcess extends AsyncTask<ImageSender, Void, Void>{

        @Override
        protected Void doInBackground(ImageSender... params) {
            try {
                // Retrieve the ServerName
                InetAddress serverAddress = InetAddress.getByName(ip);

                Log.d("UDP", "C: Connecting...");
                        /* Create new UDP-Socket */
                DatagramSocket socket = new DatagramSocket();
                DatagramPacket packetDirection;

                boolean done = false;
                int i = 0;
                int bytes = 0;
                while(!done){
                    switch(i){
                        case 0:
                            //Sends the length of the byte array of the image
                            packetDirection = new DatagramPacket(bytesLength.getBytes(),
                                    bytesLength.getBytes().length, serverAddress, port);
                            i++;
                            break;
                        case 1:
                            //Sends the name of the image file
                            packetDirection = new DatagramPacket(fileName,
                                    fileName.length, serverAddress, port);
                            i++;
                            break;
                        default:
                            //Sends the image
                            byte[] buffer = new byte[65535];
                            int c;
                            for(c = bytes; c <= 65535; c++){
                                buffer[c] = fileBytes[c];
                            }
                            bytes = c;
                            packetDirection = new DatagramPacket(buffer,
                                    buffer.length, serverAddress, port);
                            if(bytes == 65535)
                                done = true;
                            break;
                    }
                    /* Send out the packet */
                    socket.send(packetDirection);
                    Log.d("UDP", "C: Sending");
                }
            } catch (Exception e) {
                Log.e("UDP", "C: Error", e);
            }
            return null;
        }

        protected void onPostExecute(Void z){
            Log.d("UDP", "C: Sent.");
            Log.d("UDP", "C: Done.");
        }
    }
}