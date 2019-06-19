package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void connect(View view) {
        // gets the input from the ip and port EditTexts
        EditText editTextIp = (EditText) findViewById(R.id.ipAddress);
        EditText editTextPort = (EditText) findViewById(R.id.port);
        // create new intent for the next activity
        Intent intent = new Intent(this, JoyStick.class);
        String msg1 = editTextIp.getText().toString();
        String msg2 = editTextPort.getText().toString();
        String msg = msg1 + "$" + msg2;
        // put an extra message into the activity which contains the ip and the port
        intent.putExtra("message", msg);
        startActivity(intent);
    }
}