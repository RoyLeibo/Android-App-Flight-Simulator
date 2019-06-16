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
import java.net.InetAddress;
import java.net.Socket;

public class JoyStick extends AppCompatActivity {

    RelativeLayout layout_joystick;
    ImageView image_joystick, image_border;
    double x;
    double y;
    JoyStickClass js;
    Socket socket;
    DataOutputStream stream;
    Thread connectionThread;

    @SuppressLint("ClickableViewAccessibility")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joy_stick);
        // Get
        Intent intent = getIntent();
        String msg = intent.getStringExtra("message");
        int split = msg.indexOf('$');
        final String ip = msg.substring(0,split);
        final int port = Integer.parseInt(msg.substring(split + 1));
        this.connectionThread = new Thread(new Runnable() {
            public void run() {
                try {
                    InetAddress serverAddr = InetAddress.getByName(ip);
                    socket = new Socket(serverAddr, port);
                    stream = new DataOutputStream(socket.getOutputStream());
                } catch (Exception e) {
                    Log.e("TCP", "C: Error", e);
                }

                layout_joystick = (RelativeLayout)findViewById(R.id.layout_joystick);

                js = new JoyStickClass(getApplicationContext(), layout_joystick, R.drawable.image_button);
                js.setStickSize(150, 150);
                js.setLayoutSize(500, 500);
                js.setLayoutAlpha(150);
                js.setStickAlpha(100);
                js.setOffset(90);
                js.setMinimumDistance(50);
                layout_joystick.setOnTouchListener(new View.OnTouchListener() {
                    public boolean onTouch(View arg0, MotionEvent arg1) {
                        js.drawStick(arg1);
                        if (arg1.getAction() == MotionEvent.ACTION_DOWN
                                || arg1.getAction() == MotionEvent.ACTION_MOVE) {
                            x = js.getX();
                            y = js.getY();
                            int direction = js.get8Direction();
                            try {
                                stream.writeUTF("set /controls/flight/aileron " + x + "\r\n");
                                stream.writeUTF("set /controls/flight/elevator " + y + "\r\n");
                            }
                            catch (IOException e){}
                        }
                        return true;
                    }
                });
            }
        });
        this.connectionThread.start();
    }

    public void onDestroy() {
        super.onDestroy();
        try {
            this.socket.close();
        } catch (IOException e){
            Log.e("TCP", "C: Error closing the socket", e);
        }
        this.connectionThread.interrupt();
    }
}