package com.example.android.justjava;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class KeyBoardActivity extends AppCompatActivity{

    private static int logIndex = 0;
    private Keyboard mKeyboard;
    private KeyboardView mKeyboardView;
    private KeyboardView.OnKeyboardActionListener mOnKeyboardActionListener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
        }

        @Override
        public void onPress(int primaryCode) {
            Log.i("Keyboard", "You just pressed " + primaryCode);

            //First flag: 1 represents character message
            //Second flag: 0 represents pressed 1 represents released
            new Globals().execute(String.format("%s\t1\t0\t%s",logIndex,Character.toString ((char) primaryCode)));
            logIndex += 1;
        }

        @Override
        public void onRelease(int primaryCode) {
            Log.i("Keyboard", "You just released " + primaryCode);
            new Globals().execute(String.format("%s\t1\t1\t%s",logIndex, Character.toString ((char) primaryCode)));
            logIndex += 1;
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

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.keyboard_activity);
        createKeyboard();
        openKeyboard();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        Log.i("Keyboard", String.format("Size of screen %s %s", metrics.heightPixels, metrics.widthPixels));

    }

    private void createKeyboard() {
        // Create the Keyboard
        mKeyboard = new Keyboard(this, R.xml.keyboard);

        // Lookup the KeyboardView
        mKeyboardView = (KeyboardView) findViewById(R.id.keyboardview);
        // Attach the keyboard to the view
        mKeyboardView.setKeyboard(mKeyboard);

        // Do not show the preview balloons
        mKeyboardView.setPreviewEnabled(false);

        // Install the key handler
        mKeyboardView.setOnKeyboardActionListener(mOnKeyboardActionListener);
        mKeyboardView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN ||
                        event.getAction() == MotionEvent.ACTION_UP ||
                        event.getAction() == MotionEvent.ACTION_POINTER_DOWN ||
                        event.getAction() == MotionEvent.ACTION_POINTER_UP) {

                    //First flag: 0 represents coordinate message
                    //Second flag: 0 represents pressed 1 represents released
                    String log = String.format("%s\t0\t%s\t%s\t%s",logIndex,event.getAction(), event.getX(), event.getY());
                    Log.i("Keyboard", String.format("Action %s is detected", event.getAction()));
                    new Globals().execute(log);
                    logIndex += 1;
                }
                return false;
            }
        });
    }

    public void openKeyboard(){

        mKeyboardView.setVisibility(View.VISIBLE);
        mKeyboardView.setEnabled(true);
    }

}
