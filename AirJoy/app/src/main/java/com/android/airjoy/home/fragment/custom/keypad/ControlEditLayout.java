package com.android.airjoy.home.fragment.custom.keypad;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.addbean.autils.tools.ToolsUtils;

/**
 * Created by AddBean on 2016/3/26.
 */
public class ControlEditLayout extends RelativeLayout {
    private int DP = 1;
    private boolean mEditEnable = false;
    private int mAnimFlag=0;
    public ControlEditLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        DP = ToolsUtils.dpConvertToPx(getContext(), 1);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (mEditEnable)
            drawLines(canvas);
        super.dispatchDraw(canvas);
        if (mEditEnable) invalidate();
    }

    private void drawLines(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAlpha(100);
        paint.setStrokeWidth(DP);
        mAnimFlag++;
        if(mAnimFlag>8){
            mAnimFlag=0;
        }
        //绘制动态虚线
        PathEffect effects = new DashPathEffect(new float[]{2 * DP, 2 * DP, 2 * DP, 2 * DP}, (mAnimFlag>=4)?0:2 * DP);
        paint.setPathEffect(effects);
        paint.setStrokeWidth(2 * DP);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);

        RectF rect = new RectF();
        int pad = 6 * DP;
        rect.set(pad, pad, getMeasuredWidth() - pad, getMeasuredHeight() - pad);
        canvas.drawRect(rect, paint);
    }

    public boolean ismEditEnable() {
        return mEditEnable;
    }

    public void setmEditEnable(boolean mEditEnable) {
        this.mEditEnable = mEditEnable;
        invalidate();
    }
}
