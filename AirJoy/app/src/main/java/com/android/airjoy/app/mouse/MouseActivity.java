package com.android.airjoy.app.mouse;

import com.android.airjoy.R;
import com.android.airjoy.core.BaseActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
/**
 * Created by AddBean on 2016/3/20.
 */
public class MouseActivity extends BaseActivity {
    public LinearLayout mHelpText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.app_mouse);
        initTital();
        this.mHelpText = (LinearLayout) findViewById(R.id.mouse_msg);
        startTextAnim(mHelpText);
    }

    private void initTital() {
        final ImageView backImageView = (ImageView) findViewById(R.id.app_back_icon);
        backImageView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void startTextAnim(View view) {
        AlphaAnimation animation = new AlphaAnimation(0.1f, 1.0f);
        animation.setDuration(3000);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setRepeatCount(-1);
        animation.startNow();
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(animation);
        view.startAnimation(animationSet);
        animationSet.start();
    }
}
