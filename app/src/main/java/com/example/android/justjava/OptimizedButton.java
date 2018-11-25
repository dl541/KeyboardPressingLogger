package com.example.android.justjava;

import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OptimizedButton {
    private float centreX;
    private float centreY;

    public static Paint getCharacterPaint() {
        return characterPaint;
    }

    private static Paint characterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public int getButtonColor() {
        return buttonColor;
    }

    private int buttonColor;

    public float getCentreX() {
        return centreX;
    }

    public float getCentreY() {
        return centreY;
    }

    public String getCharacter() {
        return character;
    }

    private String character;
    private List<Float> xWindow = new ArrayList<Float>();
    private List<Float> yWindow = new ArrayList<Float>();

    public OptimizedButton(float x, float y, String character){
        this.centreX = x;
        this.centreY = y;
        this.character = character;
        this.xWindow.add(x);
        this.yWindow.add(y);
        characterPaint.setColor(Color.BLACK);
        characterPaint.setTextSize(40);

        Random rand = new Random();
        this.buttonColor = Color.rgb(rand.nextInt(),rand.nextInt(),rand.nextInt());

    }


    public void updateMean(float x, float y){
        centreX = (x + centreX * xWindow.size())/(xWindow.size()+1);
        centreY = (y + centreY * yWindow.size())/(yWindow.size()+1);
        xWindow.add(x);
        yWindow.add(y);
        Log.i("OptimizedKeyboard", String.format("%s is updated to %s %s", this.character, this.centreX, this.centreY));
    }
}
