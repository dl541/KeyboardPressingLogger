package com.example.android.justjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class OptimizedKeyboardActivity extends AppCompatActivity {

    private OptimizedKeyboardView optimizedKeyboardBaseView;
    private static int logIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.optimized_keyboard);
        optimizedKeyboardBaseView = (OptimizedKeyboardView) findViewById(R.id.optimizedKeyboardBase);
        optimizedKeyboardBaseView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getActionMasked();
                if (action == MotionEvent.ACTION_DOWN ||
                        action == MotionEvent.ACTION_UP ||
                        action == MotionEvent.ACTION_POINTER_DOWN ||
                        action == MotionEvent.ACTION_POINTER_UP) {

                    int pointerIndex = event.getActionIndex();
                    float actionX = event.getX(pointerIndex);
                    float actionY = event.getY(pointerIndex);

                    String log = String.format("%s\t1\t%s\t%s\t%s",logIndex,action, actionX, actionY);
                    Log.i("Keyboard", String.format("Action %s is detected",action));
                    new Globals().execute(log);
                    logIndex += 1;
                }
                return true;
            }
        });
    }
}
