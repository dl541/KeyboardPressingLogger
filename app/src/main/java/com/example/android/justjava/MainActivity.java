package com.example.android.justjava;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editText;
    private String filename = "Testing.txt";
    private File dir;
    private File file;
    private int STORAGE_PERMISSION_CODE = 1;
    private int INTERNET_PERMISSION_CODE = 2;
    public static final String TAG="Connection";
    public static final int TIMEOUT=10;
    Intent i=null;
    TextView tv=null;
    private String connectionStatus=null;
    private Handler mHandler=null;
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    ServerSocket server=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getWritingPermission();
        getInternetPermission();

        View logButton = findViewById(R.id.logButton);
        logButton.setOnClickListener(this);
        i = new Intent(this,MainActivity.class);
        mHandler = new Handler();

        dir = new File("/sdcard/kivy/ButtonPressingLogger2");
        dir.mkdirs();

        editText = (EditText) this.findViewById(R.id.editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    long time = System.currentTimeMillis();
                    Timestamp t = new Timestamp(time);
                    String newChar = s.toString().substring(s.length() - 1);
                    String log = String.format("%s\t%s", newChar, df.format(t));
                    //System.out.println(log);
                    new Globals().execute(log);
                    //writeToFile(log);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logButton:
                tv = (TextView) findViewById(R.id.connectionMessage);
//initialize server socket in a new separate thread
                new Thread(initializeConnection).start();
                String msg="Attempting to connect…";
                Toast.makeText(MainActivity.this, msg, msg.length()).show();
                break;
        }
    }

    private Runnable initializeConnection = new Thread() {
        public void run() {
            System.out.println("Debug 1");
            Socket client=null;
// initialize server socket
            try{
                server = new ServerSocket(38300);
                server.setSoTimeout(TIMEOUT*1000);
            System.out.println("Debug 2");
//attempt to ccept a connection
                client = server.accept();
                System.out.println("Debug 4");
                Globals.socketIn=new Scanner(client.getInputStream());
                Globals.socketOut = new PrintWriter(client.getOutputStream(), true);
            } catch (SocketTimeoutException e) {
// print out TIMEOUT
                connectionStatus="Connection has timed out! Please try again";
                mHandler.post(showConnectionStatus);
            } catch (IOException e) {
                System.out.println("Debug 3");
                Log.e(TAG, ""+e);
            } finally {
                Globals.socketOut.println("This is a message from the server");
//close the server socket
//                try {
//                    if (server!=null)
//                        System.out.println("Socket is closed");
//                        server.close();
//                } catch (IOException ec) {
//                    Log.e(TAG, "Cannot close server socket"+ec);
//                }
            }

            if (client!=null) {
                Globals.connected=true;
// print out success
                connectionStatus="Connection was succesful!";
                mHandler.post(showConnectionStatus);

                startActivity(i);
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

    private void getWritingPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        STORAGE_PERMISSION_CODE);
            }
        } else {
        }
    }

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


    private void initialiseFile() throws IOException {
        System.out.println("Create file");
        file = new File(dir, filename);
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        System.out.println("Path: " + dir.getPath());
    }

    private void writeToFile(String text) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(file, true));
            out.write(text + "\n");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void startLogging(View view) {
        EditText editText = (EditText) findViewById(R.id.editText);
        editText.setText("");
        long time = System.currentTimeMillis();
        Timestamp t = new Timestamp(time);
        DateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        filename = df.format(t) + ".txt";
        displayMessage("Logging in " + filename);
        try {
            initialiseFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void displayMessage(String message) {
        TextView priceTextView = (TextView) findViewById(R.id.message);
        priceTextView.setText(message);
    }


}
