package com.example.android.justjava;

import android.os.AsyncTask;

import java.io.PrintWriter;
import java.util.Scanner;

public class Globals extends AsyncTask<String,Void,Long> {
    public static boolean connected = false;
    public static Scanner socketIn;
    public static PrintWriter socketOut;

    @Override
    protected Long doInBackground(String... message) {
        socketOut.println(message[0]);
        return new Long(0);
    }

}
