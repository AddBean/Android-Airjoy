package com.android.airjoy.app.anim;

import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
/**
 * 3d动画辅助类
 *
 * @author 贾豆
 *
 */
public class Rotate3dHelper
{
    Layout homeLayout;
    private Rotate3dX _rotate3dByX = null;
    private Rotate3dY _rotate3dByY = null;
    private View view;
    private int start, end;
    private float depthZ;
    private int R3dTime;
    private OnAnimOverListener _onAnimOverListener;
    public Rotate3dHelper(LinearLayout Layou)
    {
        this.view = Layou;
    }

    public Rotate3dHelper(FrameLayout Layou)
    {
        this.view = Layou;
    }

    public Rotate3dHelper(RelativeLayout Layou)
    {
        this.view = Layou;
    }

    public Rotate3dHelper(View view)
    {
        this.view = view;
    }

    /* 绕X轴旋转3d效果（时间，开始角度，结束角度，深度，是否返回） */
    public boolean Rotate3dByX(int time, int start, int end, float depthZ,boolean isOneTime,OnAnimOverListener onAnimOverListener)
    {
        this.start = start;
        this.end = end;
        this.depthZ=depthZ;
        this._onAnimOverListener=onAnimOverListener;
        final float centerX = view.getWidth() / 2.0f;
        final float centerY = view.getHeight() / 2.0f;
        _rotate3dByX = new Rotate3dX(start, end, centerX, centerY, depthZ, true);
        _rotate3dByX.reset();
        _rotate3dByX.setRepeatCount(0);// 设置重复次数
        _rotate3dByX.setDuration(time);// 此时间应和对应动画时间一致；
        _rotate3dByX.setFillAfter(true);
        _rotate3dByX.setInterpolator(new AccelerateInterpolator());
        R3dTime = time;
        /** 开始动画 */
        // 设置监听
        if (isOneTime)
        {
            _rotate3dByX.setAnimationListener(new AnimXListener());
        }
        view.startAnimation(_rotate3dByX);
        return false;
    }

    /* 绕Y轴旋转3d效果（时间，开始角度，结束角度，深度，是否返回）*/
    public boolean Rotate3dByY(int time, int start, int end, float depthZ,boolean isOneTime,OnAnimOverListener onAnimOverListener)
    {
        this.start = start;
        this.end = end;
        this.depthZ=depthZ;
        this._onAnimOverListener=onAnimOverListener;
        final float centerX = view.getWidth() / 2.0f;
        final float centerY = view.getHeight() / 2.0f;
        _rotate3dByY = new Rotate3dY(start, end, centerX, centerY, depthZ, true);
        _rotate3dByY.reset();
        _rotate3dByY.setRepeatCount(0);// 设置重复次数
        _rotate3dByY.setDuration(time);// 此时间应和对应动画时间一致；
        _rotate3dByY.setFillAfter(true);
        _rotate3dByY.setInterpolator(new AccelerateInterpolator());
        R3dTime = time;
        /** 开始动画 */
        // 设置监听
        if (isOneTime)
        {
            _rotate3dByY.setAnimationListener(new AnimYListener());
        }
        view.startAnimation(_rotate3dByY);
        return false;
    }

    /* 设置X轴动画监听 */
    private final class AnimXListener implements Animation.AnimationListener
    {
        // 动画开始；
        public void onAnimationStart(Animation animation)
        {
        }

        // 动画结束；
        public void onAnimationEnd(Animation animation)
        {
            _onAnimOverListener.AnimOver();
            view.post(new SwapViewsX());
        }

        // 动画重复；
        public void onAnimationRepeat(Animation animation)
        {
        }

        /* 切换视图 */
        private final class SwapViewsX implements Runnable
        {
            public void run()
            {
                final float centerX = view.getWidth() / 2.0f;
                final float centerY = view.getHeight() / 2.0f;
                Rotate3dX Rotate3dX = null;
                view.requestFocus();
                Rotate3dX = new Rotate3dX(end, start, centerX, centerY,
                        depthZ, false);
                Rotate3dX.setDuration(R3dTime);
                Rotate3dX.setFillAfter(true);
                Rotate3dX.setInterpolator(new DecelerateInterpolator());
                // 开始动画
                view.startAnimation(Rotate3dX);
            }
        }
    }

    /* 设置Y轴动画监听 */
    private final class AnimYListener implements Animation.AnimationListener
    {
        // 动画开始；
        public void onAnimationStart(Animation animation)
        {
        }

        // 动画结束；
        public void onAnimationEnd(Animation animation)
        {
            _onAnimOverListener.AnimOver();
            view.post(new SwapViewsY());
        }

        // 动画重复；
        public void onAnimationRepeat(Animation animation)
        {
        }

        /* 切换视图 */
        private final class SwapViewsY implements Runnable
        {
            public void run()
            {
                final float centerX = view.getWidth() / 2.0f;
                final float centerY = view.getHeight() / 2.0f;
                Rotate3dY Rotate3dY = null;
                view.requestFocus();
                Rotate3dY = new Rotate3dY(end, start, centerX, centerY,
                        depthZ, false);
                Rotate3dY.setDuration(R3dTime);
                Rotate3dY.setFillAfter(true);
                Rotate3dY.setInterpolator(new DecelerateInterpolator());
                // 开始动画
                view.startAnimation(Rotate3dY);
            }
        }
    }

    /* 3d动画回调接口*/
    public interface OnAnimOverListener {
        void AnimOver();
    }
}
