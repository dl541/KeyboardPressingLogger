package com.example.android.justjava;

import android.content.Intent;
import android.graphics.Point;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import java.sql.Timestamp;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Calendar;
import java.util.Date;

public class FittsExperimentActivity extends AppCompatActivity {
    public static String hand;
    private static int logIndex = 0;
    private FittsExperimentView fittsExperimentView;
    public static Point screenSize;
    public static int D, W;
    private LocalTime currentTime = null;
    private long totalMT = 0;
    private int taps = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        hand = intent.getStringExtra("Hand");

        Display display = getWindowManager().getDefaultDisplay();
        screenSize = new Point();
        display.getSize(screenSize);
        Log.i("FittsExperiment", "Screen size " + screenSize.x +" " + screenSize.y);


        D = intent.getIntExtra("FittsD", 200);
        W = intent.getIntExtra("FittsW", 50);
        Log.i("FittsExperiment", "Experiment Parameters D: " + D + " W: " + W );

        setContentView(R.layout.fitts_experiment);
        fittsExperimentView = (FittsExperimentView) findViewById(R.id.FittsExperimentBase);
        fittsExperimentView.setExperimentResult("Press to start");


        fittsExperimentView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getActionMasked();

                if (action == MotionEvent.ACTION_UP){
                    currentTime = LocalTime.now();
                    Log.i("FitsExperiment", "Action UP detected" + currentTime.toString());
                }
                if (action == MotionEvent.ACTION_DOWN){
                    if (currentTime != null) {
                        Log.i("FittsExperiment", "Action DOWN detected" + LocalTime.now().toString());
                        totalMT += currentTime.until(LocalTime.now(), ChronoUnit.MILLIS);
                        Log.i("FittsExperiment", "Total movement time: " + totalMT);
                    }
                    taps += 1;
                    double averageTime = taps == 0? 0:totalMT/taps;
                    String result = String.format("Current MT: %s\nTaps: %s\nAverage MT: %s", totalMT, taps, averageTime);
                    fittsExperimentView.setExperimentResult(result);
                    fittsExperimentView.updateCirclePosition();
                }

                if (action == MotionEvent.ACTION_DOWN ||
                        action == MotionEvent.ACTION_UP ||
                        action == MotionEvent.ACTION_POINTER_DOWN ||
                        action == MotionEvent.ACTION_POINTER_UP) {

                    int pointerIndex = event.getActionIndex();
                    float actionX = event.getX(pointerIndex);
                    float actionY = event.getY(pointerIndex);

                    String log = String.format("%s\t1\t%s\t%s\t%s",logIndex,action, actionX, actionY);
                    Log.i("FittsExperiment", String.format("Action %s is detected",action));
                    logIndex += 1;
                }
                return true;
            }
        });
    }


}
