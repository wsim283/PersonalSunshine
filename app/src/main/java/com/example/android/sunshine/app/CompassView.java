package com.example.android.sunshine.app;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Welly Mulyadi on 19/09/2016.
 */

public class CompassView extends View {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private int selectedSize;
    private final int DEFAULT_PADDING = 5;
    private Paint compassOuterBorderPaint;
    private Paint compassInnerBorderPaint;
    private Paint compassDirLabelPaint;
    private Paint compassArrowPaint;

   private double degrees = 0;
    public CompassView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.CompassView,
                0,
                0);

        try{
            if(!a.hasValue(R.styleable.CompassView_size)) {
                throw new RuntimeException("compasstool:size is required");
            }
            int[] compassSizeValues = context.getResources().getIntArray(R.array.compass_size_values);
            selectedSize = compassSizeValues[a.getInteger(R.styleable.CompassView_size,0)];
            init();
        }finally {
            a.recycle();
        }


    }

    private void init(){
        compassOuterBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        compassInnerBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        compassDirLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        compassArrowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        compassOuterBorderPaint.setStyle(Paint.Style.STROKE);
        compassOuterBorderPaint.setColor(getResources().getColor(R.color.sunshine_dark_blue));
        compassOuterBorderPaint.setStrokeWidth(DEFAULT_PADDING);

        compassInnerBorderPaint.setStyle(Paint.Style.STROKE);
        compassInnerBorderPaint.setColor(getResources().getColor(R.color.sunshine_light_blue));
        compassInnerBorderPaint.setStrokeWidth(DEFAULT_PADDING);

        compassDirLabelPaint.setStyle(Paint.Style.FILL);
        compassDirLabelPaint.setColor(getResources().getColor(R.color.sunshine_detail_grey_colour));
        compassDirLabelPaint.setTextSize(40);

        compassArrowPaint.setStyle(Paint.Style.STROKE);
        compassArrowPaint.setColor(getResources().getColor(R.color.sunshine_red));
        compassArrowPaint.setStrokeWidth(DEFAULT_PADDING);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int paddingWidth = getPaddingLeft() + getPaddingRight();
        int paddingHeight = getPaddingBottom() + getPaddingTop();

        if(paddingHeight == 0) paddingHeight = DEFAULT_PADDING;
        if(paddingWidth == 0) paddingWidth = DEFAULT_PADDING;

       setMeasuredDimension(selectedSize+paddingWidth,selectedSize+paddingHeight);
    }

    public void setCompassDirection(String wind){



        if(wind.contains("SE")){
            degrees = 45;
        }else if(wind.contains("SW")){
            degrees = 315;
        }
        else if(wind.contains("S")){
            degrees = 0;
        }else if(wind.contains("NE")){
            degrees = 135;
        }else if(wind.contains("NW")){
            degrees = 225;
        }else if(wind.contains("N")){
            degrees = 180;
        }
        else if(wind.contains("E")){
            degrees = 90;
        }else if(wind.contains("W")){
            degrees = 270;
        }

        invalidate();
        requestLayout();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float cx = getWidth()/2;
        float cy = getHeight()/2;
        float outer_rad = selectedSize/2;

        float inner_rad = (selectedSize-(DEFAULT_PADDING*4))/2;


        canvas.drawCircle(cx, cy, outer_rad, compassOuterBorderPaint);
        canvas.drawCircle(cx, cy, inner_rad, compassInnerBorderPaint);
        canvas.drawText("N",cx-(DEFAULT_PADDING*2), getTop() + getPaddingTop() + (DEFAULT_PADDING*5), compassDirLabelPaint);
        canvas.drawText("S",cx-(DEFAULT_PADDING*2), getBottom() - getPaddingBottom() - (DEFAULT_PADDING*2), compassDirLabelPaint);
        canvas.drawText("E",getWidth() - (DEFAULT_PADDING*5), cy+(DEFAULT_PADDING*2), compassDirLabelPaint);
        canvas.drawText("W",DEFAULT_PADDING*2, cy+(DEFAULT_PADDING*2), compassDirLabelPaint);

        double radian = degrees * Math.PI/180;

        float endX = (float)(cx + outer_rad * Math.sin(radian));
        float endY = (float)(cy + outer_rad * Math.cos(radian));
        canvas.drawLine(cx,cy,endX,endY,compassArrowPaint);
    }
}
