package com.android.airjoy.widget.anim;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by AddBean on 2016/2/16.
 */
public class ScaleAnim extends Animation {
    private View mView;
    private int mStartHeigh;
    private int mEndHeigh;

    public ScaleAnim(View view, int startHeigh, int endHeigh) {
        this.mView = view;
        this.mStartHeigh = startHeigh;
        this.mEndHeigh = endHeigh;
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    //其中interpolatedTime 为当前动画帧对应的相对时间,值总在0-1之间
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        int height= (int) (mStartHeigh+interpolatedTime*(mEndHeigh-mStartHeigh));
        FrameLayout.LayoutParams lp= new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,height);
        lp.gravity= Gravity.BOTTOM;
        this.mView.setLayoutParams(lp);
        this.mView.requestLayout();
    }
}
