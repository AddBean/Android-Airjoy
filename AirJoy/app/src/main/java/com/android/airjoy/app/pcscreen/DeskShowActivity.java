package com.android.airjoy.app.pcscreen;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.addbean.autils.utils.ALog;
import com.addbean.autils.utils.AnimUtils;
import com.android.airjoy.R;
import com.android.airjoy.app.phone.SnapService;
import com.android.airjoy.constant.Config;
import com.android.airjoy.core.BaseActivity;
import com.android.airjoy.widget.AirjoyToast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.io.DataInputStream;
import java.io.InputStream;

/**
 * 电脑屏幕显示页面
 *
 * @author 贾豆
 */
public class DeskShowActivity extends BaseActivity {
    private static ScreenSocket mScreenSocket = null;
    private static InputStream ins;
    private static DataInputStream dins;
    private Context mContext;
    private ScreenImagView img;// 定义各个组件
    private Bitmap bmm;// 定义一个Bitmap 用来存ImageView的每个图
    private byte[] data;// 放接收到数据的数组
    private byte[] dataLength = new byte[10];// 放接收到数据的数组
    private static Thread _synThread;
    private static boolean isRunning = true;
    public class ViewHolder  {

        @ViewInject( R.id.tips_title_text)
        private TextView tips_title_text;
        @ViewInject( R.id.loading_layout)
        private LinearLayout loading_layout;
        @ViewInject( R.id.iv_anim)
        private ImageView iv_anim;
        @ViewInject( R.id.PcView)
        private com.android.airjoy.app.pcscreen.ScreenImagView PcView;
        @ViewInject( R.id.touch_layout)
        private com.android.airjoy.app.pcscreen.MouseTouch touch_layout;
        @ViewInject( R.id.setting_text)
        private TextView setting_text;
        @ViewInject( R.id.control_layout)
        private LinearLayout control_layout;
        @ViewInject( R.id.tools_text)
        private TextView tools_text;
        @ViewInject( R.id.nomal_model)
        private TextView nomal_model;
        @ViewInject( R.id.loc_model)
        private TextView loc_model;

    }
    private ViewHolder mViewHolder=new ViewHolder();
    private Handler mUiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                if (!bmm.isRecycled() && bmm != null)
                    img.setImageBitmap(bmm);
                else
                    ALog.e("位图为空或已被回收");
            } catch (Exception e) {
            }
        }
    };

    /* 入口 */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 无title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); // 全屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 设置横屏
        setContentView(R.layout.app_screen_activity);
        ViewUtils.inject(mViewHolder,this);
        bindEvent();
        this.mContext = this;
        // 得到各个组件
        img = (ScreenImagView) findViewById(R.id.PcView);
        stopPhoneService();
        isRunning = true;
        _synThread = new mSynThread();
        _synThread.start();
    }

    private void bindEvent() {
        AnimUtils.RotateForeverAnim(mViewHolder.iv_anim);
        mViewHolder.tips_title_text.setText("提示：若长时间未连接上，请尝试退出应用后重新进入");
        if(mViewHolder.touch_layout.mPadModelEnable){
            mViewHolder.loc_model.setTextColor(getResources().getColor(R.color.color_blue));
            mViewHolder.nomal_model.setTextColor(getResources().getColor(R.color.white));
        }else{
            mViewHolder.loc_model.setTextColor(getResources().getColor(R.color.white));
            mViewHolder.nomal_model.setTextColor(getResources().getColor(R.color.color_blue));
        }
        mViewHolder.setting_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimUtils.ScaleAnim(v);
                mViewHolder.control_layout.setVisibility(View.VISIBLE);
                mViewHolder.setting_text.setVisibility(View.GONE);
                mViewHolder.tips_title_text.setVisibility(View.GONE);
            }
        });
        mViewHolder.control_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewHolder.control_layout.setVisibility(View.GONE);
                mViewHolder.setting_text.setVisibility(View.VISIBLE);
                mViewHolder.tips_title_text.setVisibility(View.VISIBLE);
            }
        });
        mViewHolder.loc_model.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewHolder.loc_model.setTextColor(getResources().getColor(R.color.color_blue));
                mViewHolder.nomal_model.setTextColor(getResources().getColor(R.color.white));
                mViewHolder.control_layout.setVisibility(View.GONE);
                mViewHolder.setting_text.setVisibility(View.VISIBLE);
                mViewHolder.touch_layout.mPadModelEnable=true;
                mViewHolder.tips_title_text.setVisibility(View.VISIBLE);
            }
        });
        mViewHolder.nomal_model.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewHolder.loc_model.setTextColor(getResources().getColor(R.color.white));
                mViewHolder.nomal_model.setTextColor(getResources().getColor(R.color.color_blue));
                mViewHolder.control_layout.setVisibility(View.GONE);
                mViewHolder.setting_text.setVisibility(View.VISIBLE);
                mViewHolder.touch_layout.mPadModelEnable=false;
                mViewHolder.tips_title_text.setVisibility(View.VISIBLE);
            }
        });
    }

    /* 停止录屏服务*/
    public void stopPhoneService() {
        try {
            Intent intent = new Intent(DeskShowActivity.this, SnapService.class);
            stopService(intent);
        } catch (Exception error) {
        }
    }

    /* 线程*/
    class mSynThread extends Thread {
        public mSynThread() {
            super();
            mScreenSocket = new ScreenSocket(Config.Ip, Config.Port);
        }

        public void run() {
            mScreenSocket.createConnection();
            mScreenSocket.sendFileInf();
            while (isRunning) {
                try {
                    if (mScreenSocket == null)
                        return;
                    dins = new DataInputStream(ins);
                    ins = mScreenSocket.getDownloadStream();
                    dins.readFully(dataLength);
                    int lengthA = ScreenSocket.getLengthFromByte(dataLength);//获取头部十个长度信息；
                    Log.d("DownloadThread", "大小：" + lengthA);
                    data = new byte[lengthA];
                    dins.readFully(data);
                    Bitmap bmpTemp = BitmapFactory.decodeByteArray(data, 0, data.length);
                    Message message = Message.obtain();
//                    if (bmm != null) {
//                        bmm.recycle();
//                        bmm=null;
//                    }
                    bmm = bmpTemp;
                    mUiHandler.sendMessage(message);
//                    Thread.sleep(500);
                } catch (Exception e) {
                    // e.printStackTrace();
                }
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            isRunning = false;//关闭循环发送消息；
            mScreenSocket.shutDownConnection();
            mSynThread.interrupted();
        } catch (Exception e) {
            e.printStackTrace();
            AirjoyToast.makeText(mContext, "停止线程出错！请重试！", AirjoyToast.LENGTH_LONG).show();
        }
    }

    /* 返回键*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }
}
