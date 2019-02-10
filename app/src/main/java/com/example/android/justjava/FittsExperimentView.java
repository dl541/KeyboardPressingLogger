package com.example.android.justjava;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
public class FittsExperimentView extends View {
    private Paint circlePaint;
    private Paint textPaint;

    public String getExperimentResult() {
        return experimentResult;
    }

    public void setExperimentResult(String experimentResult) {
        this.experimentResult = experimentResult;
    }

    private String experimentResult = "";
    public int getCentreX() {
        return centreX;
    }

    public void setCentreX(int centreX) {
        this.centreX = centreX;
    }

    public int getCentreY() {
        return centreY;
    }

    public void setCentreY(int centreY) {
        this.centreY = centreY;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    private int centreX, centreY;
    private Point handCentre;
    private int radius;
    public FittsExperimentView(Context context, AttributeSet attrs){
        super(context, attrs);
        circlePaint = new Paint();
        textPaint = new Paint();
        String hand = FittsExperimentActivity.hand;
        Log.i("FittsExperimentView", "Chosen hand: "+ hand);

        Point screenSize = FittsExperimentActivity.screenSize;

        radius = FittsExperimentActivity.W * 2;
        if (hand.equals("L")){
            handCentre = new Point(screenSize.x/4, screenSize.y / 2 );
            centreX = handCentre.x - FittsExperimentActivity.D/2;
            centreY = handCentre.y;
        }
        else{
            handCentre = new Point(3 * screenSize.x/4, screenSize.y / 2 );
            centreX = handCentre.x - FittsExperimentActivity.D/2;
            centreY = handCentre.y;
        }
        Log.i("FittsExperimentView", "Hand Centre: " + handCentre.x + " " + handCentre.y);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        circlePaint.setStyle(Style.FILL);
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.RED);
        canvas.drawCircle(centreX, centreY, radius, circlePaint);

        Point screenSize = FittsExperimentActivity.screenSize;
        textPaint.setStyle(Style.FILL);
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.RED);
        textPaint.setTextSize(40);
        canvas.drawText(experimentResult,screenSize.x/2, screenSize.y*0.8f, textPaint);
    }

    public void updateCirclePosition(){
        centreX = 2 * handCentre.x - centreX;
        invalidate();
    }
}
