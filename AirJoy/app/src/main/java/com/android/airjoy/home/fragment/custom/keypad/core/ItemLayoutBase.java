package com.android.airjoy.home.fragment.custom.keypad.core;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.addbean.autils.tools.ToolsUtils;
import com.addbean.autils.utils.ALog;
import com.addbean.autils.utils.ResultActivityListener;
import com.android.airjoy.core.service.core.TaskQueue;
import com.android.airjoy.core.service.tasks.UDPTaskSender;
import com.android.airjoy.home.fragment.custom.config.ModelItem;
import com.android.airjoy.home.fragment.custom.keypad.PadActivity;
import com.android.airjoy.home.fragment.custom.keypad.anims.AnimFactory;
import com.android.airjoy.home.fragment.custom.keypad.menu.MenuItemViewBase;

/**
 * Created by AddBean on 2016/3/21.
 */
public abstract class ItemLayoutBase extends LinearLayout {
    private int mWidth = 0;
    private int mHeigh = 0;
    private int DP;
    private ModelItem mModelItem;
    private Context mContext;
    private float mDownX = 0;
    private float mDownY = 0;
    private int mTimeCount = 0;
    private Boolean mClickFlag = true;

    public ItemLayoutBase(Context context, ModelItem mate) {
        super(context);
        this.mContext = context;
        DP = ToolsUtils.dpConvertToPx(context, 1);
        this.mWidth = mate.getmWidth() * DP;
        this.mHeigh = mate.getmHeight() * DP;
        this.mModelItem = mate;
        initView(mWidth, mHeigh);
    }


    protected abstract void initView(int mWidth, int mHeigh);

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(mHeigh, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeigh);
        ALog.e("mWidth:" + mWidth + " mHeigh:" + mHeigh);
    }

    public  void setPosition(Point position){};

    public  void setMatrix(float[] scalex){};

    public void setSize(int width, int height) {
        if (width > 0)
            setmWidth(width);
        if (height > 0)
            setmHeigh(height);
    }

    public void startActivity(Intent intent, ResultActivityListener listener) {
        ((PadActivity) this.mContext).startActivityWithCallback(intent, listener);
    }

    public ModelItem getmModelItem() {
        return mModelItem;
    }

    protected void setmWidth(int mWidth) {
        this.mWidth = mWidth;
    }

    protected void setmHeigh(int mHeigh) {
        this.mHeigh = mHeigh;
    }

    protected int getmWidth() {
        return mWidth;
    }

    protected int getmHeigh() {
        return mHeigh;
    }


    public abstract MenuItemViewBase getMenuView();

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mModelItem.getmCmd().equals("-1")) {
            return super.onTouchEvent(event);
        } else if (mModelItem.getmCmd().equals("0")) {
            return handleMouseEvent(event);
        } else {
            return handleKeyEvent(event);
        }
    }

    private boolean handleMouseEvent(MotionEvent event) {
        int pointerCount = event.getPointerCount();
        int action = (event.getAction() & MotionEvent.ACTION_MASK) % 5;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                mClickFlag = true;
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX() - mDownX;
                float moveY = event.getY() - mDownY;
                if (pointerCount == 1) {
                    mTimeCount++;
                    if (mTimeCount > 0) {
                        String s = "M:M:" + String.valueOf((int) moveX) + "+"
                                + String.valueOf((int) moveY);
                        TaskQueue.add(new UDPTaskSender("MouseTouch", s));
                        mTimeCount = 0;
                    }
                    if (moveX == 0 && moveX == 0) {
                        mClickFlag = true;
                    } else {
                        mClickFlag = false;
                    }
                } else if (pointerCount == 2) {
                    mClickFlag = false;
                    String s = "M:W:" + String.valueOf((int) moveY / 2);
                    TaskQueue.add(new UDPTaskSender("MouseTouch", s));
                }
                break;
            case MotionEvent.ACTION_UP:
                if (pointerCount == 1) {
                    if (mClickFlag) {
                        TaskQueue.add(new UDPTaskSender("MouseTouch", "M:L:DOWN"));
                        TaskQueue.add(new UDPTaskSender("MouseTouch", "M:L:UP"));
                        mClickFlag = true;
                    }
                } else if (pointerCount == 3) {
                    mClickFlag = false;
                    TaskQueue.add(new UDPTaskSender("MouseTouch", "M:R:DOWN"));
                    TaskQueue.add(new UDPTaskSender("MouseTouch", "M:R:UP"));
                }
                break;
        }
        return true;
    }

    public void onMoved(){}
    public void onAllScaled(){}
    public void onXScaled(){}
    public void onYScaled(){}
    public void onDeleted(){}
    public void onRotated(){}
    public void onModelChanged(boolean isEdit){};
    private boolean handleKeyEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                doAnim();
                if (mModelItem != null)
                    TaskQueue.add(new UDPTaskSender("ItemLayoutBase", "K:" + mModelItem.getmCmd() + ":DOWN"));
                return true;
            case MotionEvent.ACTION_UP:
                if (mModelItem != null)
                    TaskQueue.add(new UDPTaskSender("ItemLayoutBase", "K:" + mModelItem.getmCmd() + ":UP"));
                break;
        }
        return true;
    }

    public void doAnim() {
        AnimFactory.startAnim(mModelItem.getmAnim(), this);
        if (mModelItem.ismIsVibrate()) {
            Vibrator vibrator = (Vibrator) getContext().getSystemService("vibrator");
            vibrator.vibrate(100);
        }
    }

    public void onStop() {

    }
}
