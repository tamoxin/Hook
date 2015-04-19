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
    String fileName;
    String bytesLength;

    public ImageSender(String ip, String port, byte[] bytes, String fileName) {
        this.ip = ip;
        this.port = Integer.parseInt(port);
        this.fileBytes = bytes;
        this.fileName = fileName;
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

                int c = 0;
                for(int i = 0; i < 3; i++){
                    switch(i){
                        case 0:
                            //Sends the length of the byte array of the image
                            packetDirection = new DatagramPacket(bytesLength.getBytes(),
                                    bytesLength.getBytes().length, serverAddress, port);
                            /* Send out the packet */
                            socket.send(packetDirection);
                            Log.d("UDP", "C: Sending");
                            break;
                        case 1:
                            //Sends the name of the image file
                            packetDirection = new DatagramPacket(fileName.getBytes(),
                                    fileName.getBytes().length, serverAddress, port);
                            /* Send out the packet */
                            socket.send(packetDirection);
                            Log.d("UDP", "C: Sending");
                            break;
                        default:
                            //Sends the image
                            Log.e("Image size", "" + fileBytes.length);
                            byte[] buffer = new byte[1024];
                            for(int aux = 0; aux < fileBytes.length; aux ++){
                                buffer[c] = fileBytes[aux];
                                c++;
                                if(aux % 1023 == 0 || aux == (fileBytes.length - 1)){
                                    packetDirection = new DatagramPacket(buffer,
                                            buffer.length, serverAddress, port);
                                    c = 0;

                                    /* Send out the packet */
                                    socket.send(packetDirection);
                                    socket.close();
                                    socket = new DatagramSocket();
                                    Log.d("aux", "" + aux);
                                    Log.d("UDP", "C: Sending");

                                    for(int wait = 0; wait < 10000; wait ++){

                                    }
                                }
                            }
                    }
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