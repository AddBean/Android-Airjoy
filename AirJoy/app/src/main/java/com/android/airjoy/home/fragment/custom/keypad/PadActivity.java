package com.android.airjoy.home.fragment.custom.keypad;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.view.WindowManager;

import com.addbean.autils.utils.ResultActivityAdaptor;
import com.addbean.autils.utils.ResultActivityListener;
import com.android.airjoy.R;
import com.android.airjoy.home.fragment.custom.config.ModelModule;

public class PadActivity extends FragmentActivity {
    private FragmentPadEdit mFragment;
    private ModelModule mModel;
    public ResultActivityAdaptor mResultActivityAdaptor = new ResultActivityAdaptor(this);

    public void startActivityWithCallback(Intent intent, ResultActivityListener listener) {
        mResultActivityAdaptor.startActivityForResult(intent, listener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mResultActivityAdaptor.onResult(requestCode, resultCode, data);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        setContentView(R.layout.activity_custom_key_pad);
        mModel = (ModelModule) getIntent().getSerializableExtra("data");
        initView();
    }

    private void setOrientation(ModelModule mModel) {
        if (!mModel.ismIsVertical())
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setOrientation(mModel);
    }

    private void initView() {
        mFragment =  FragmentPadEdit.getInstance(mModel);
        FragmentManager mManger = getSupportFragmentManager();
        FragmentTransaction mTrans = mManger.beginTransaction();
        mTrans.add(R.id.fragment_content, mFragment).commit();
    }
}
