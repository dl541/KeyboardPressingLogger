package com.example.android.justjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class OptimizedKeyboardActivity extends AppCompatActivity {

    private OptimizedKeyboardView optimizedKeyboardBaseView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.optimized_keyboard);
        optimizedKeyboardBaseView = (OptimizedKeyboardView) findViewById(R.id.optimizedKeyboardBase);
        optimizedKeyboardBaseView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN ||
                        event.getAction() == MotionEvent.ACTION_UP ||
                        event.getAction() == MotionEvent.ACTION_POINTER_DOWN ||
                        event.getAction() == MotionEvent.ACTION_POINTER_UP) {

                    //First flag: 0 represents coordinate message
                    //Second flag: 0 represents pressed 1 represents released
                    String log = String.format("%s\t%s\t%s", event.getAction(), event.getX(), event.getY());
                    Log.i("OptimizedKeyboard", log);

                }

                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    optimizedKeyboardBaseView.updateMean(event.getX(),event.getY());
                }


                return true;
            }
        });
    }
}
