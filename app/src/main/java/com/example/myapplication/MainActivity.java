package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    Socket socket;
    DataOutputStream stream;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button connectButton = findViewById(R.id.connect);
        connectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                EditText ip = findViewById(R.id.ipAddress2);
                EditText port = findViewById(R.id.ipAddress);
                try {
                    socket = new Socket(ip.getText().toString(), Integer.parseInt(port.getText().toString()));
                    stream = new DataOutputStream(socket.getOutputStream());
                }
                catch (IOException e){}
            }
        });
        //joysticMoveFunc(){
            // the value is missing
        try {
            String aileronCommand = "set /controls/flight/aileron " + "\r\n";
            String elevatorCommand = "set /controls/flight/elevator " + "\r\n";
            stream.writeUTF(aileronCommand);
            stream.writeUTF(elevatorCommand);
        }
        catch (IOException e){}
        //}
    }
}
