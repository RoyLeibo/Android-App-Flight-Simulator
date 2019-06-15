package com.example.myapplication;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class RetrieveFeedTask extends AsyncTask<String, Void, Void> {

    private Exception exception;
    Socket socket;

    protected Void doInBackground(String msg) {
        int split = msg.indexOf('$');
        String ip = msg.substring(0,split);
        String port = msg.substring(split + 1);
        try {
            InetAddress serverAddr = InetAddress.getByName(ip);
            this.socket = new Socket(serverAddr, Integer.parseInt(port));
        } catch (Exception e) {
            Log.e("TCP", "C: Error", e);
        }
    }

}
