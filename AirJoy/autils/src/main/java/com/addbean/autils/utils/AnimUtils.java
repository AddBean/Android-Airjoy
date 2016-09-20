package com.addbean.autils.utils;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;

public class AnimUtils {
	/**
	 * 点击缩放动画；
	 * 
	 * @param view
	 * @return
	 */
	public static boolean ScaleAnim(View view) {
		ScaleAnimation animation = new ScaleAnimation(0.8f, 1.0f, 0.8f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setDuration(200);
		animation.startNow();
		AnimationSet animationSet = new AnimationSet(true);
		animationSet.addAnimation(animation);
		view.startAnimation(animationSet);
		animationSet.start();
		return false;
	}

	/**
	 * 渐变消失动画
	 * 
	 * @param view
	 * @return
	 */
	public static boolean FadeOutAnim(View view) {
		AlphaAnimation animation = new AlphaAnimation(1.0f,0.1f);
		animation.setDuration(100);
		animation.startNow();
		AnimationSet animationSet = new AnimationSet(true);
		animationSet.addAnimation(animation);
		view.startAnimation(animationSet);
		animationSet.start();
		return false;
	}
	/**
	 * 永远旋转动画
	 *
	 * @param v
	 * @return
	 */
	public static void RotateForeverAnim(View v) {
		RotateAnimation animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setDuration(500);//设置动画持续时间
		animation.setRepeatCount(-1);
		animation.setInterpolator(new LinearInterpolator());
		v.setAnimation(animation);
		animation.startNow();
	}
}
