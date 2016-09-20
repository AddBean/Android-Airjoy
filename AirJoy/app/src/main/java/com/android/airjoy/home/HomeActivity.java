package com.android.airjoy.home;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.addbean.autils.utils.ALog;
import com.addbean.autils.utils.ResultActivityAdaptor;
import com.addbean.autils.utils.ResultActivityListener;
import com.android.airjoy.R;
import com.android.airjoy.constant.Config;
import com.android.airjoy.core.BaseActivity;
import com.android.airjoy.core.BaseFragmentActivity;
import com.android.airjoy.core.service.MainService;
import com.android.airjoy.core.service.core.QueueLooper;
import com.android.airjoy.core.service.core.TaskQueue;
import com.android.airjoy.core.service.tasks.UDPTaskSender;
import com.android.airjoy.home.fragment.FragmentMain;
import com.umeng.update.UmengUpdateAgent;

import net.youmi.android.AdManager;
import net.youmi.android.spot.SpotManager;

import java.util.List;

/**
 * 主页面；
 *
 * @author 贾豆
 */
public class HomeActivity extends BaseFragmentActivity {
    public static String RESULT_MESSAGE;
    private FragmentMain mFragment;
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
        setContentView(R.layout.home_activity);
        initView();
        initAd();
        intUpdate();
        startMainService();
    }

    private void intUpdate() {
        UmengUpdateAgent.setUpdateOnlyWifi(false);
//      UmengUpdateAgent.setUpdateCheckConfig(false)
        UmengUpdateAgent.update(this);
    }

    private void initAd() {
        AdManager.getInstance(this).init(Config.YOUMI_APPID, Config.YOUMI_SCRORT, true);
        SpotManager.getInstance(this).setSpotOrientation(SpotManager.ORIENTATION_PORTRAIT);
    }


    /**
     * 启动mainServer；
     */
    private void startMainService() {
        try {
            if (isServiceRunning(getApplicationContext(), "com.android.airjoy.core.service.MainService")) {
            } else {
                Intent intent = new Intent(getApplicationContext(), MainService.class);
                startService(intent);
            }
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    /**
     * @param mContext
     * @param className
     * @return
     */
    public static boolean isServiceRunning(Context mContext, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(30);
        if (!(serviceList.size() > 0)) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    private void initView() {
        mFragment = FragmentMain.getInstance();
        FragmentManager mManger = getSupportFragmentManager();
        FragmentTransaction mTrans = mManger.beginTransaction();
        mTrans.add(R.id.fragment_content, mFragment).commit();
    }
}
