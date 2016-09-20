package com.android.airjoy.app.ppt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.airjoy.R;
import com.android.airjoy.core.BaseActivity;

/**
 * ppt����ҳ��
 * 
 * @author �ֶ�
 * 
 */
public class AppPpt extends BaseActivity
{
	public LinearLayout _helpText;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // �����ޱ���
        setContentView(R.layout.app_ppt);
        initTital();
    }
    /* ��ʼ������*/
    private void initTital(){
        final ImageView backImageView=(ImageView)findViewById(R.id.app_back_icon);
        backImageView.setOnClickListener(new OnClickListener() {
            public void onClick(View v)
            {
                backImageView.setAlpha(50);
               finish();
            }
        });
    }
}
