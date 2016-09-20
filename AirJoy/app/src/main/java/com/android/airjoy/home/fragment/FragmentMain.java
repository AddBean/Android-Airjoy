package com.android.airjoy.home.fragment;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.addbean.autils.tools.ToolsUtils;
import com.addbean.autils.utils.ALog;
import com.addbean.autils.utils.AnimUtils;
import com.addbean.autils.utils.PreferencesTools;
import com.addbean.aviews.views.dynamic_fragment.ADynamicBaseFragment;
import com.addbean.aviews.views.dynamic_fragment.ADynamicBaseSubFragment;
import com.addbean.aviews.views.dynamic_fragment.TabTitleView;
import com.android.airjoy.R;
import com.android.airjoy.app.help.AppHelp;
import com.android.airjoy.constant.Config;
import com.android.airjoy.constant.ConfigModel;
import com.android.airjoy.home.HostScaner;
import com.android.airjoy.home.fragment.custom.FragmentCustom;
import com.android.airjoy.home.fragment.home.FragmentHome;
import com.android.airjoy.others.umeng.UmengConfig;
import com.android.airjoy.widget.ScaningView;
import com.android.airjoy.widget.WaveView;
import com.android.airjoy.widget.anim.ScaningAnim;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by AddBean on 2016/3/20.
 */
public class FragmentMain extends ADynamicBaseFragment {
    private HorizontalScrollView mScrollView;
    private ViewPager mViewPager;
    private WaveView mWaveView;
    private RelativeLayout mMsgLayout;
    private List<ADynamicBaseSubFragment> mFragments = new ArrayList<ADynamicBaseSubFragment>();
    private FrameLayout mScaningView;
    private FrameLayout mTitleContent;
    private ImageView mImageHelp;
    private TextView mMsgText;
    private List<String> mSelectedIps = new ArrayList<String>();
    public static FragmentMain _fragmentMain;
    public int DP = 1;

    public static FragmentMain getInstance() {
        if (_fragmentMain == null)
            _fragmentMain = new FragmentMain();
        return _fragmentMain;
    }


    @Override
    public void initView() {
        DP = ToolsUtils.dpConvertToPx(getContext(), 1);
        mScaningView = (FrameLayout) getCurrentView().findViewById(R.id.scaning_view);
        mTitleContent = (FrameLayout) getCurrentView().findViewById(R.id.title_content);
        mMsgLayout = (RelativeLayout) getCurrentView().findViewById(R.id.msg_layout);
        mImageHelp = (ImageView) getCurrentView().findViewById(R.id.image_help);
        mMsgText = (TextView) getCurrentView().findViewById(R.id.text_msg);
        mMsgLayout.setAlpha(0);
        super.initView();
        setIndex(0);
        mScaningView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScaningAnim();
                mScaningView.setEnabled(false);
            }
        });
        mImageHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimUtils.ScaleAnim(v);
                MobclickAgent.onEvent(getActivity(), UmengConfig.EVENT_HELP);
                startActivity(new Intent(getActivity(), AppHelp.class));
            }
        });
        scanNet();
    }

    private void startOverAnim() {
        if (mScaningView == null) return;
        ScaningAnim anim = new ScaningAnim(mScaningView, mMsgLayout, 60 * DP, 10 * DP, 10 * DP);
        anim.setDuration(500);
        mScaningView.startAnimation(anim);
        mScaningView.setEnabled(true);
    }

    private void startScaningAnim() {
        if (mScaningView == null) return;
        ScaningAnim anim = new ScaningAnim(mScaningView, mMsgLayout, 150 * DP, mTitleContent.getMeasuredWidth() / 2 - 75 * DP, (mTitleContent.getMeasuredHeight()) / 2 - 75 * DP);
        anim.setDuration(500);
        mScaningView.startAnimation(anim);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                scanNet();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void setIndex(int index) {
        getViewPager().setCurrentItem(index);
    }

    @Override
    public List<ADynamicBaseSubFragment> getFragmentList() {
        mFragments.clear();
        mFragments.add(FragmentHome.getInstance("常用功能"));
        mFragments.add(FragmentCustom.getInstance("自定义场景"));
        return mFragments;
    }

    @Override
    public TabTitleView getTabTitleView(Object tag) {
        return new TabView(getActivity(), tag);
    }

    @Override
    protected HorizontalScrollView getTabView() {
        mScrollView = (HorizontalScrollView) getCurrentView().findViewById(R.id.main_scroll_view);
        return mScrollView;
    }

    @Override
    protected ViewPager getViewPageView() {
        mViewPager = (ViewPager) getCurrentView().findViewById(R.id.view_pager);
        return mViewPager;
    }

    @Override
    public void onPageSelected(int position) {
        super.onPageSelected(position);
    }

    @Override
    public int getFragmentView() {
        return R.layout.fragment_main;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            if (bundle != null) {
                switch (msg.what) {
                    case 5://开始连接
                        break;
                    case 3://连接成功:
                        sendEventToUmeng(bundle.getString("msg"));
                        mSelectedIps.add("Airjoy " + bundle.getString("msg"));
                        break;
                    case 0://扫描失败:
                    case 4: //扫描完成；
                        startOverAnim();
                        for (String ip : mSelectedIps) {
                            String tag = ip.substring(0, 6);
                            if (tag.equals("Airjoy")) {
                                String realIp = ip.substring(7, ip.length());
                                Config.Ip = realIp;
                                mMsgText.setText("连接成功\n" + Config.Ip);
                                return;
                            }
                        }
                        mMsgText.setText("连接失败\n" + "点击圆圈重试");
                        MobclickAgent.onEvent(getActivity(), UmengConfig.CONNECT_FALSE);
                        break;
                }
            }
        }
    };

    @Override
    public boolean onDefaultIndexDraw(Canvas canvas, int x1, int y1, int x2, int y2) {
        Paint paint = new Paint();
        paint.setStrokeWidth(ToolsUtils.dpConvertToPx(getActivity(), 2));
        paint.setAntiAlias(true);
        paint.setAlpha(100);
        paint.setColor(getResources().getColor(R.color.color_blue));
        int padding = ToolsUtils.dpConvertToPx(getActivity(), 2);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawLine(x1 + padding, y1, x2 - padding, y2, paint);
        return true;
    }

    private void sendEventToUmeng(String ip) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("IP", ip);
        MobclickAgent.onEvent(getActivity(), UmengConfig.CONNECT_SUCCESS, map);
    }

    private void scanNet() {
        new HostScaner(getContext()).scanNet(getContext(), new HostScaner.ResultListener() {
            @Override
            public void onFailed() {
                Bundle bundle = new Bundle();
                bundle.putString("msg", "扫描失败");
                Message message = new Message();
                message.setData(bundle);
                message.what = 0;
                mHandler.sendMessage(message);
            }

            @Override
            public void onConnectPre(String ipInf) {
                Bundle bundle = new Bundle();
                bundle.putString("msg", ipInf);
                Message message = new Message();
                message.setData(bundle);
                message.what = 5;
                mHandler.sendMessage(message);
            }

            @Override
            public void onResultSuccess(String ipInf) {
                Bundle bundle = new Bundle();
                bundle.putString("msg", ipInf);
                Message message = new Message();
                message.setData(bundle);
                message.what = 3;
                mHandler.sendMessage(message);
                Config.Ip = ipInf;
                ConfigModel mConfigModel = new ConfigModel();
                mConfigModel.setmIp(ipInf);
                mConfigModel.setmPort("37016");
                PreferencesTools.saveObj(getContext(), Config.IP_CONFIG, mConfigModel, false);
            }

            @Override
            public void onScanOver() {
                Bundle bundle = new Bundle();
                bundle.putString("msg", "扫描完成");
                Message message = new Message();
                message.setData(bundle);
                message.what = 4;
                mHandler.sendMessage(message);
            }

        });
    }
}
