package com.example.android.justjava;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class KeyBoardActivity extends AppCompatActivity{

    private Keyboard mKeyboard;
    private KeyboardView mKeyboardView;
    private KeyboardView.OnKeyboardActionListener mOnKeyboardActionListener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void onKey(int primaryCode, int[] keyCodes) {

            Log.i("Keyboard", "You just pressed " + primaryCode);
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

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.keyboard_activity);
        createKeyboard();
        openKeyboard();

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

    public void openKeyboard(){

        mKeyboardView.setVisibility(View.VISIBLE);
        mKeyboardView.setEnabled(true);
    }

}
