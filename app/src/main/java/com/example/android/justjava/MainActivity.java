package com.example.android.justjava;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {
    int quantity = 2;
    private EditText editText;
    private String filename = "Testing.txt";
    private File dir;
    private File file;
    private int STORAGE_PERMISSION_CODE = 1;
    private int INTERNET_PERMISSION_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWritingPermission();
        getInternetPermission();
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
                    System.out.println("current text:" + s);
                    long time = System.currentTimeMillis();
                    Timestamp t = new Timestamp(time);
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                    String newChar = s.toString().substring(s.length() - 1);
                    String log = String.format("%s\t%s", newChar, df.format(t));
                    System.out.println(log);
                    writeToFile(log);
                }
            }
        });
    }


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

   /*
    }
*/