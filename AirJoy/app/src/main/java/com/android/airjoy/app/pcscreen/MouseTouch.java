package com.android.airjoy.app.pcscreen;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.android.airjoy.core.service.core.TaskQueue;
import com.android.airjoy.core.service.tasks.UDPTaskSender;

public class MouseTouch extends View {
    private static final float RADIUS = 40;
    private Paint backgroundPaint;
    private Context _context;
    float xDown = 0;
    float yDown = 0;
    long currentMS = 0;
    int TimeCount = 0;
    Boolean ClickFlag = true;
    private int mode = 0;
    public static boolean mPadModelEnable = false;

    public MouseTouch(Context context, AttributeSet attrs) {
        super(context, attrs);
        this._context = context;
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.BLUE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int pointerCount = event.getPointerCount();
        int action = (event.getAction() & MotionEvent.ACTION_MASK) % 5;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                xDown = event.getX();
                yDown = event.getY();
                if (mPadModelEnable)
                    setMouseLocation(xDown, yDown);
                currentMS = System.currentTimeMillis();
                ClickFlag = true;
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX() - xDown;
                float moveY = event.getY() - yDown;
                if (pointerCount == 1) {
                    TimeCount++;
                    if (TimeCount > 0) {
                        long moveTime = System.currentTimeMillis() - currentMS;
                        String s = "M:M:" + String.valueOf((int) moveX) + "+"
                                + String.valueOf((int) moveY);
                        TaskQueue.add(new UDPTaskSender("MouseTouch", s));
                        TimeCount = 0;
                    }
                    if (moveX == 0 && moveX == 0) {
                        ClickFlag = true;
                    } else {
                        ClickFlag = false;
                    }
                } else if (pointerCount == 2) {
                    ClickFlag = false;
                    String s = "M:W:" + String.valueOf((int) moveY / 2);
                    TaskQueue.add(new UDPTaskSender("MouseTouch", s));
                }
                break;
            case MotionEvent.ACTION_UP:
                if (pointerCount == 1) {
                    if (ClickFlag) {
                        TaskQueue.add(new UDPTaskSender("MouseTouch", "M:L:DOWN"));
                        TaskQueue.add(new UDPTaskSender("MouseTouch", "M:L:UP"));
                        ClickFlag = true;
                    }
                } else if (pointerCount == 3) {
                    ClickFlag = false;
                    TaskQueue.add(new UDPTaskSender("MouseTouch", "M:R:DOWN"));
                    TaskQueue.add(new UDPTaskSender("MouseTouch", "M:R:UP"));
                }
                break;
        }
        return true;
    }

    /**
     * 定位鼠标
     *
     * @param x
     * @param y
     */
    private void setMouseLocation(float x, float y) {
        String s = "M:P:" + String.valueOf(x / getMeasuredWidth()) + "+"
                + String.valueOf(String.valueOf(y / getMeasuredHeight()));
        TaskQueue.add(new UDPTaskSender("MouseTouch", s));
    }
}
