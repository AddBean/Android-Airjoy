package com.android.airjoy.home.welcome;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.airjoy.R;
import com.android.airjoy.app.anim.Rotate3dHelper;
import com.android.airjoy.home.HomeActivity;

/**
 * ��ӭҳ�棻
 * 
 * @author �ֶ�
 * 
 */
public class WelcomePage extends Activity 
{
    public ImageView jumpToHome;
    public Context _context;
    /* ������� */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);                 // �����ޱ���
        setContentView(R.layout.welcome_page);       
        this._context=this;
        Intent intent=new Intent();
        intent.setClass(WelcomePage.this,HomeActivity.class );
        startActivity(intent);
        RelativeLayout fristIn=(RelativeLayout)findViewById(R.id.fristIn);
        final Rotate3dHelper fristInHelper=new Rotate3dHelper(fristIn);
        fristIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent();
                intent.setClass(WelcomePage.this,HomeActivity.class );
                startActivity(intent);
            }
        });
        jumpToHome=(ImageView)findViewById(R.id.jumpToHome);
        jumpToHome.setOnClickListener(new OnClickListener() {//�����ת��
            public void onClick(View v)
            {
                animClick(v);
                Intent intent=new Intent();
                intent.setClass(WelcomePage.this,HomeActivity.class );
                startActivity(intent);
            }
        });
    }
    /* ������� */
    public boolean animClick(View view)
    {
        Vibrator vibrator = (Vibrator) _context.getSystemService("vibrator");
        vibrator.vibrate(200);
        /** �������Ŷ��� */
        ScaleAnimation animation = new ScaleAnimation(
                                                      0.1f,
                                                      1.0f,
                                                      0.1f,
                                                      1.0f,
                                                      Animation.RELATIVE_TO_SELF,
                                                      0.5f,
                                                      Animation.RELATIVE_TO_SELF,
                                                      0.5f);
        animation.setDuration(300);// ���ö�������ʱ��
        animation.startNow();
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(animation);
        view.startAnimation(animationSet);
        animationSet.start();
        return false;
    }
}
