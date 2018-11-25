package com.example.android.justjava;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * TODO: document your custom view class.
 */
public class OptimizedKeyboardView extends View {
    private List<OptimizedButton> optimizedButtonList = new ArrayList<OptimizedButton>();
    private float circleRadius = 25f;
    public static Bitmap optimizedKeyboardBitmap;

    public static int[] intArray;

    public OptimizedKeyboardView(Context context) {
        super(context);
        init(null, 0);
    }

    public OptimizedKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public OptimizedKeyboardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        InputStream is = getResources().openRawResource(R.raw.keyboard_data);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));

        try {
            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                String[] lineSplit = line.split("\t");
                this.optimizedButtonList.add(new OptimizedButton(Float.parseFloat(lineSplit[0]),
                        Float.parseFloat(lineSplit[1]),
                        lineSplit[2]));
            }
        }
        catch (IOException e){
            Log.e("OptimizedKeyboard", e.getMessage());
            }
        }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (optimizedKeyboardBitmap == null){
            optimizedKeyboardBitmap = Bitmap.createBitmap(getWidth(), getHeight(),
                    Bitmap.Config.ARGB_8888);
            intArray = new int[optimizedKeyboardBitmap.getWidth() * optimizedKeyboardBitmap.getHeight()];

            optimizedKeyboardBitmap.getPixels(intArray, 0, optimizedKeyboardBitmap.getWidth(), 0, 0, optimizedKeyboardBitmap.getWidth(),
                    optimizedKeyboardBitmap.getHeight());

            for (int ind = 0; ind < intArray.length; ind++){
                int row = ind / getWidth();
                int col = ind % getWidth();
                int minInd = 0;
                int minNorm = Integer.MAX_VALUE;
                for (int buttonInd = 0; buttonInd < optimizedButtonList.size(); buttonInd ++){
                    int colDiff = Math.round(optimizedButtonList.get(buttonInd).getCentreX()) - col;
                    int rowDiff = Math.round(optimizedButtonList.get(buttonInd).getCentreY()) - row;

                    if (colDiff*colDiff + rowDiff * rowDiff < minNorm){
                        minInd = buttonInd;
                        minNorm = colDiff*colDiff + rowDiff * rowDiff;
                    }
                }
                intArray[ind] = optimizedButtonList.get(minInd).getButtonColor();
            }
            optimizedKeyboardBitmap.setPixels(intArray, 0, optimizedKeyboardBitmap.getWidth(), 0, 0, optimizedKeyboardBitmap.getWidth(),
                    optimizedKeyboardBitmap.getHeight());
        }

        canvas.drawBitmap(optimizedKeyboardBitmap, 0, 0, null);

        for(OptimizedButton button: optimizedButtonList) {
            canvas.drawText(button.getCharacter(),button.getCentreX(),button.getCentreY(), OptimizedButton.getCharacterPaint());
        }

        //OpenList voronoiSites = generateVoronoi(canvas);

//        for (int ind = 0; ind < voronoiSites.size; ind ++){
//            PolygonSimple polygon = voronoiSites.get(ind).getPolygon();
//            Paint polygonPaint = new Paint();
//            polygonPaint.setColor(optimizedButtonList.get(ind).getButtonColor());
//
//            Path polygonPath = new Path();
//            double[] xList = polygon.getXPoints();
//            double[] yList = polygon.getYPoints();
//
//            polygonPath.moveTo((float)xList[0], (float)yList[0]);
//
//            for (int pointInd = 1; pointInd < xList.length; pointInd++){
//                polygonPath.lineTo((float)xList[pointInd], (float)yList[pointInd]);
//            }
//
//            polygonPath.close();
//
//            canvas.drawPath(polygonPath, polygonPaint);
//        }
    }

    public void updateKeyboard(float x, float y){
        OptimizedButton closestButton = findClosest(x,y);
        closestButton.updateMean(x,y);
        invalidate();
    }

    private OptimizedButton findClosest(float x, float y){
        return optimizedButtonList.stream().min(Comparator.comparing((OptimizedButton button) -> {
            float distX = button.getCentreX() - x;
            float distY = button.getCentreY() - y;
            return distX *distX + distY*distY;
        } )).get();
    }

//    private OpenList generateVoronoi(Canvas canvas){
//        PowerDiagram diagram = new PowerDiagram();
//
//        OpenList sites = new OpenList();
//
//        PolygonSimple rootPolygon = new PolygonSimple();
//        int width = canvas.getWidth();
//        int height = canvas.getHeight();
//        rootPolygon.add(0, 0);
//        rootPolygon.add(width, 0);
//        rootPolygon.add(width, height);
//        rootPolygon.add(0, height);
//
//        optimizedButtonList.stream().forEach((OptimizedButton button) -> {
//            sites.add(new Site(button.getCentreX(),button.getCentreY()));
//        });
//
//        diagram.setSites(sites);
//        diagram.setClipPoly(rootPolygon);
//
//        diagram.computeDiagram();
//
//        return sites;
//    }

//    /**
//     * Gets the example string attribute value.
//     *
//     * @return The example string attribute value.
//     */
//    public String getExampleString() {
//        return mExampleString;
//    }
//
//    /**
//     * Sets the view's example string attribute value. In the example view, this string
//     * is the text to draw.
//     *
//     * @param exampleString The example string attribute value to use.
//     */
//    public void setExampleString(String exampleString) {
//        mExampleString = exampleString;
//        invalidateTextPaintAndMeasurements();
//    }
//
//    /**
//     * Gets the example color attribute value.
//     *
//     * @return The example color attribute value.
//     */
//    public int getExampleColor() {
//        return mExampleColor;
//    }
//
//    /**
//     * Sets the view's example color attribute value. In the example view, this color
//     * is the font color.
//     *
//     * @param exampleColor The example color attribute value to use.
//     */
//    public void setExampleColor(int exampleColor) {
//        mExampleColor = exampleColor;
//        invalidateTextPaintAndMeasurements();
//    }
//
//    /**
//     * Gets the example dimension attribute value.
//     *
//     * @return The example dimension attribute value.
//     */
//    public float getExampleDimension() {
//        return mExampleDimension;
//    }
//
//    /**
//     * Sets the view's example dimension attribute value. In the example view, this dimension
//     * is the font size.
//     *
//     * @param exampleDimension The example dimension attribute value to use.
//     */
//    public void setExampleDimension(float exampleDimension) {
//        mExampleDimension = exampleDimension;
//        invalidateTextPaintAndMeasurements();
//    }
//
//    /**
//     * Gets the example drawable attribute value.
//     *
//     * @return The example drawable attribute value.
//     */
//    public Drawable getExampleDrawable() {
//        return mExampleDrawable;
//    }
//
//    /**
//     * Sets the view's example drawable attribute value. In the example view, this drawable is
//     * drawn above the text.
//     *
//     * @param exampleDrawable The example drawable attribute value to use.
//     */
//    public void setExampleDrawable(Drawable exampleDrawable) {
//        mExampleDrawable = exampleDrawable;
//    }
}
