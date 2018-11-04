package com.example.android.justjava;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private int INTERNET_PERMISSION_CODE = 1;
    public static final String TAG = "Connection";
    public static final int TIMEOUT = 10;
    private String connectionStatus = null;
    private Handler mHandler = null;
    ServerSocket server = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getWritingPermission();
        getInternetPermission();

        View logButton = findViewById(R.id.logButton);
        logButton.setOnClickListener(this);
        mHandler = new Handler();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logButton:
//initialize server socket in a new separate thread
                new Thread(initializeConnection).start();
                String msg = "Attempting to connect…";
                Toast.makeText(MainActivity.this, msg, msg.length()).show();
                break;
        }
    }

    private Runnable initializeConnection = new Thread() {
        public void run() {
            Socket client = null;
// initialize server socket
            try {
                server = new ServerSocket(38300);
                server.setSoTimeout(TIMEOUT * 1000);
//attempt to ccept a connection
                client = server.accept();
                Globals.socketIn = new Scanner(client.getInputStream());
                Globals.socketOut = new PrintWriter(client.getOutputStream(), true);
            } catch (SocketTimeoutException e) {
// print out TIMEOUT
                connectionStatus = "Connection has timed out! Please try again";
                mHandler.post(showConnectionStatus);
            } catch (IOException e) {
                Log.e(TAG, "" + e);
            }
            if (client != null) {
                Globals.connected = true;
// print out success
                connectionStatus = "Connection was successful!";
                mHandler.post(showConnectionStatus);
                startActivity(new Intent(MainActivity.this, KeyBoardActivity.class));
            }
        }
    };

    /**
     * Pops up a “toast” to indicate the connection status
     */
    private Runnable showConnectionStatus = new Runnable() {
        public void run() {
            Toast.makeText(MainActivity.this, connectionStatus, Toast.LENGTH_SHORT).show();
        }
    };

    private void getInternetPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.INTERNET)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET},
                        INTERNET_PERMISSION_CODE);
            }
        } else {
        }
    }

    public void stopLogging(View v) throws IOException {
        new Globals().execute("quit");
        String msg = "Connection closed.";
        Toast.makeText(this, msg, msg.length()).show();
        try {
            server.close();
        }
        catch (IOException e){
            Log.e("MainActivity", "Cannot close socket");
        }
    }
}
