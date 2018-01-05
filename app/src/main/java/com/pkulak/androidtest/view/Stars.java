package com.pkulak.androidtest.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.pkulak.androidtest.R;

// This view lends itself very well to ASCII art:
//    stars.setRating(3) -> |***  |
//    stars.setRating(1) -> |*    |
//    stars.setRating(5) -> |*****|
public class Stars extends View {
    private float rating;
    private final Paint yellowPaint = new Paint();
    private final Paint grayPaint = new Paint();
    private final Paint bitmapPaint = new Paint();
    private final Bitmap mask;

    public Stars(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray starAttrs = context.obtainStyledAttributes(attrs, R.styleable.Stars);
        this.rating = starAttrs.getFloat(R.styleable.Stars_rating, 0);
        starAttrs.recycle();

        this.yellowPaint.setColor(Color.argb(255, 254, 182, 26));
        this.grayPaint.setColor(Color.argb(255, 215, 215, 215));

        mask = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.stars);
    }

    @Override
    @SuppressLint("DrawAllocation")
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        float division = (rating / 5f) * width;

        // draw the yellow from the left, then the finish with gray
        canvas.drawRect(0, 0, division, height, yellowPaint);
        canvas.drawRect(division, 0, width, height, grayPaint);

        // and mask the star shapes on top
        canvas.drawBitmap(
                mask,
                new Rect(0, 0, mask.getWidth(), mask.getHeight()),
                new Rect(0 ,0, getWidth(), getHeight()),
                bitmapPaint
        );
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(
                MeasureSpec.getSize(widthMeasureSpec),
                MeasureSpec.getSize(heightMeasureSpec));
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
        invalidate();
    }
}
