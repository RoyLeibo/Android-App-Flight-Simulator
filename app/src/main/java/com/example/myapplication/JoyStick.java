package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class JoyStick extends AppCompatActivity {

    RelativeLayout layout_joystick;
    ImageView image_joystick, image_border;
    double x;
    double y;
    JoyStickClass js;
    Socket socket;
    OutputStream stream;
    Thread connectionThread;

    @SuppressLint("ClickableViewAccessibility")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joy_stick);
        // Get intent
        Intent intent = getIntent();
        String msg = intent.getStringExtra("message");
        // extract message from intent and split it
        int split = msg.indexOf('$');
        final String ip = msg.substring(0,split);
        final int port = Integer.parseInt(msg.substring(split + 1));
        // create a connection to server thread
        this.connectionThread = new Thread(new Runnable() {
            // override the run function in runnable
            public void run() {
                try {
                    // connect to the server
                    socket = new Socket(ip, port);
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                    Log.e("TCP", "C: Error", e);
                }
            }
        });
        this.connectionThread.start();
    }

    // function that is called when the joystick is moving
    public void onJoystickMoved(final float x, final float y) {
        // create a new thread to send data to the server
        Thread th = new Thread(new Runnable() {
            public void run() {
                try {
                    // gets the output stream
                    stream = socket.getOutputStream();
                    // create the commands to the server
                    String str = "set /controls/flight/aileron " + x + "\r\n";
                    String str1 = "set /controls/flight/elevator " + y + "\r\n";
                    // send each command to simulator
                    stream.write(str.getBytes());
                    stream.flush();
                    stream.write(str1.getBytes());
                    stream.flush();
                    Thread.currentThread().interrupt();
                } catch (IOException e) {
                }
            }
        });
        th.start();
    }

    // when the user exit the app
    public void onDestroy() {
        super.onDestroy();
        try {
            // close socket
            this.socket.close();
        } catch (IOException e){
            Log.e("TCP", "C: Error closing the socket", e);
        }
    }
}