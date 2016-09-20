package com.android.airjoy.widget;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.addbean.autils.tools.ToolsUtils;

public class SpringProgressView extends View {

    private float maxCount;
    private float currentCount;
    private Paint mPaint;
    private int mSeekWidth, mTextWidthDP=50, mWidth, mHeight;
    private Bitmap bitMap;
    private int DP = 1;

    public SpringProgressView(Context context, AttributeSet attrs,
                              int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public SpringProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SpringProgressView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        /*bitMap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.scrubber_control_pressed_holo);*/
        DP = ToolsUtils.dpConvertToPx(context, 1);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        int round = mHeight / 2;
        mPaint.setColor(Color.GRAY);

        RectF rectBg = new RectF(0, 0, mWidth, mHeight);
        canvas.drawRoundRect(rectBg, round, round, mPaint);
        mPaint.setColor(Color.WHITE);
        RectF rectBlackBg = new RectF(2, 2, mWidth - 2, mHeight - 2);
        canvas.drawRoundRect(rectBlackBg, round, round, mPaint);

        float section = currentCount / maxCount;
        RectF rectProgressBg = new RectF(3, 3,mTextWidthDP*DP+ (mSeekWidth - 3) * section, mHeight - 3);
        mPaint.setColor(Color.GRAY);
        canvas.drawRoundRect(rectProgressBg, round, round, mPaint);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(1*DP);
        canvas.drawLine(mTextWidthDP*DP-mHeight/2,3*DP,mTextWidthDP*DP-mHeight/2,mHeight-3*DP,mPaint);
        String value=String.valueOf((int)currentCount);
        int textSize=11;
        mPaint.setTextSize(textSize*DP);
        mPaint.setAntiAlias(true);
        canvas.drawText(value,mTextWidthDP*DP/2-ToolsUtils.getTextWidth(value,textSize)*DP/2-mHeight/4,
                getHeight()/2+textSize*DP/3,mPaint);
    }

    private int dipToPx(int dip) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
    }

    public void setMaxCount(float maxCount) {
        this.maxCount = maxCount;
    }

    public void setCurrentCount(float currentCount) {
        this.currentCount = currentCount > maxCount ? maxCount : currentCount;
        invalidate();
    }

    public float getMaxCount() {
        return maxCount;
    }

    public float getCurrentCount() {
        return currentCount;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == MeasureSpec.EXACTLY || widthSpecMode == MeasureSpec.AT_MOST) {
            mWidth = widthSpecSize;
        } else {
            mWidth = 0;
        }
        if (heightSpecMode == MeasureSpec.AT_MOST || heightSpecMode == MeasureSpec.UNSPECIFIED) {
            mHeight = dipToPx(15);
        } else {
            mHeight = heightSpecSize;
        }
        mSeekWidth = mWidth - mTextWidthDP * DP;
        setMeasuredDimension(mWidth, mHeight);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        Log.i("DEMO", "x:" + x + ",y:" + y);
        getParent().requestDisallowInterceptTouchEvent(true);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                moved(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                moved(x, y);
                break;
            case MotionEvent.ACTION_UP:
                moved(x, y);
                break;
        }
        return true;
    }

    private void moved(float x, float y) {
        if (x > mWidth||x<mTextWidthDP*DP) {
            return;
        }
        currentCount = maxCount * ((x-mTextWidthDP*DP) / mSeekWidth);
        invalidate();
    }


}
