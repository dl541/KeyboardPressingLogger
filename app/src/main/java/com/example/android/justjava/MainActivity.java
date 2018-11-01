package com.example.android.justjava;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editText;
    private String filename = "Testing.txt";
    private File file;
    private int STORAGE_PERMISSION_CODE = 1;
    private int INTERNET_PERMISSION_CODE = 2;
    public static final String TAG = "Connection";
    public static final int TIMEOUT = 10;
    Intent i = null;
    TextView tv = null;
    private String connectionStatus = null;
    private Handler mHandler = null;
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    ServerSocket server = null;
    private Keyboard mKeyboard;
    private KeyboardView mKeyboardView;
    private KeyboardView.OnKeyboardActionListener mOnKeyboardActionListener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void onKey(int primaryCode, int[] keyCodes) {

            Log.i("Keyboard", "You just pressed " + primaryCode);
            editText.append(Character.toString((char) primaryCode));
        }

        @Override
        public void onPress(int arg0) {

        }

        @Override
        public void onRelease(int primaryCode) {
        }

        @Override
        public void onText(CharSequence text) {
        }

        @Override
        public void swipeDown() {
        }

        @Override
        public void swipeLeft() {
        }

        @Override
        public void swipeRight() {
        }

        @Override
        public void swipeUp() {
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getWritingPermission();
        getInternetPermission();

        View logButton = findViewById(R.id.logButton);
        logButton.setOnClickListener(this);
        i = new Intent(this, MainActivity.class);
        mHandler = new Handler();

        createKeyboard();
        editText = (EditText) this.findViewById(R.id.editText);
    }

    private void createKeyboard() {
        // Create the Keyboard
        mKeyboard = new Keyboard(this, R.xml.keyboard);

        // Lookup the KeyboardView
        mKeyboardView = (KeyboardView) findViewById(R.id.keyboardview);
        // Attach the keyboard to the view
        mKeyboardView.setKeyboard(mKeyboard);

        // Do not show the preview balloons
        //mKeyboardView.setPreviewEnabled(false);

        // Install the key handler
        mKeyboardView.setOnKeyboardActionListener(mOnKeyboardActionListener);
        mKeyboardView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP) {
                    String log = String.format("%s\t%s\t%s", event.getAction(), event.getX(), event.getY());
                    Log.i("Keyboard", String.format("Action %s is detected", event.getAction()));
                    new Globals().execute(log);
                }

                return false;
            }
        });
    }

    public void openKeyboard(View v) {
        mKeyboardView.setVisibility(View.VISIBLE);
        mKeyboardView.setEnabled(true);
        if (v != null)
            ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logButton:
                tv = (TextView) findViewById(R.id.connectionMessage);
//initialize server socket in a new separate thread
                new Thread(initializeConnection).start();
                String msg = "Attempting to connect…";
                Toast.makeText(MainActivity.this, msg, msg.length()).show();
                break;
        }
    }

    private Runnable initializeConnection = new Thread() {
        public void run() {
            System.out.println("Debug 1");
            Socket client = null;
// initialize server socket
            try {
                server = new ServerSocket(38300);
                server.setSoTimeout(TIMEOUT * 1000);
                System.out.println("Debug 2");
//attempt to ccept a connection
                client = server.accept();
                System.out.println("Debug 4");
                Globals.socketIn = new Scanner(client.getInputStream());
                Globals.socketOut = new PrintWriter(client.getOutputStream(), true);
            } catch (SocketTimeoutException e) {
// print out TIMEOUT
                connectionStatus = "Connection has timed out! Please try again";
                mHandler.post(showConnectionStatus);
            } catch (IOException e) {
                System.out.println("Debug 3");
                Log.e(TAG, "" + e);
            } finally {
                Globals.socketOut.println("This is a message from the server");
            }

            if (client != null) {
                Globals.connected = true;
// print out success
                connectionStatus = "Connection was succesful!";
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


    private void displayMessage(String message) {
        TextView priceTextView = (TextView) findViewById(R.id.message);
        priceTextView.setText(message);
    }

    public void stopLogging(View view) {
        //close the server socket
        String msg = "Connection closed";
        Toast.makeText(MainActivity.this, msg, msg.length()).show();
        new Globals().execute("quit");

    }
}
