package com.android.airjoy.home.fragment.custom.keypad.layouts;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.addbean.autils.utils.ALog;
import com.android.airjoy.R;
import com.android.airjoy.app.pcscreen.ScreenImagView;
import com.android.airjoy.app.pcscreen.ScreenSocket;
import com.android.airjoy.constant.Config;
import com.android.airjoy.home.fragment.custom.config.ModelItem;
import com.android.airjoy.home.fragment.custom.keypad.core.ItemLayoutBase;
import com.android.airjoy.home.fragment.custom.keypad.menu.MenuItemViewBase;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by AddBean on 2016/3/28.
 */
public class ItemScreen extends ItemLayoutBase {
    private ImageView mImage;
    private static ScreenSocket mScreenSocket;
    private static InputStream ins;
    private static DataInputStream dins;
    private Context mContext;
    private ScreenImagView img;// 定义各个组件
    private Bitmap mBmp;// 定义一个Bitmap 用来存ImageView的每个图
    private byte[] data;// 放接收到数据的数组
    private byte[] dataLength = new byte[10];// 放接收到数据的数组
    private static Thread mSynThread;
    private static boolean isRunning = true;
    private View mView;
    private TextView mTextBtn, mTextTips;
    private Handler mSynHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (mImage != null && mBmp != null && !mBmp.isRecycled())
                        mImage.setImageBitmap(mBmp);
                    mImage.invalidate();
                    setRetryButton(false);
                    break;
                case 1:
                    setRetryButton(true);
                    break;
            }

        }
    };


    private void setRetryButton(boolean enable) {
        if (mTextBtn != null)
            mTextBtn.setVisibility(enable ? View.VISIBLE : View.GONE);
        if (mTextTips != null)
            mTextTips.setVisibility(enable ? View.VISIBLE : View.GONE);
        if (mTextBtn != null)
            mTextBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    synScreen();
                }
            });
    }

    public ItemScreen(Context context, ModelItem mate) {
        super(context, mate);

    }

    @Override
    protected void initView(int mWidth, int mHeigh) {
        mContext = getContext();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.item_screen_layout, null);
        mTextTips = (TextView) mView.findViewById(R.id.text_tips);
        mTextBtn = (TextView) mView.findViewById(R.id.text_btn);
        mImage = (ScreenImagView) mView.findViewById(R.id.image_view);
        this.addView(mView);
        closeSocketAndStopThread();
    }

    public void synScreen() {
        closeSocketAndStopThread();
        isRunning = true;
        mSynThread = null;
        if (mSynThread == null)
            mSynThread = new SynThread();
        try {
            mSynThread.start();
        } catch (Exception e) {
            e.printStackTrace();
            ALog.e("线程已在运行");
        }
    }

    /* 线程*/
    class SynThread extends Thread {
        public SynThread() {
            super();
            if (mScreenSocket == null)
                mScreenSocket = new ScreenSocket(Config.Ip, Config.Port);
        }

        public void run() {
            if (TextUtils.isEmpty(Config.Ip)) {
                return;
            }
            mScreenSocket.createConnection();
            mScreenSocket.sendFileInf();
            while (isRunning) {
                try {
                    if (mScreenSocket == null)
                        return;
                    ins = mScreenSocket.getDownloadStream();
                    dins = new DataInputStream(ins);
                    dins.readFully(dataLength);
                    int lengthA = ScreenSocket.getLengthFromByte(dataLength);//获取头部十个长度信息；
                    data = new byte[lengthA];
                    dins.readFully(data);
                    Bitmap temp = BitmapFactory.decodeByteArray(data, 0, data.length);
//                    if (mBmp != null) {
//                        mBmp.recycle();
//                        mBmp = null;
//                    }
                    mBmp = temp;
                    Message message = Message.obtain();
                    message.what = 0;
                    mSynHandler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    Message message = Message.obtain();
                    message.what = 1;
                    mSynHandler.sendMessage(message);
                }
            }
        }
    }

    @Override
    public void onModelChanged(boolean isEdit) {
        super.onModelChanged(isEdit);
        if (isEdit) {
            closeSocketAndStopThread();
        } else {
            synScreen();
        }
    }

    @Override
    public void onDeleted() {
        super.onDeleted();
        closeSocketAndStopThread();
    }

    private void closeSocketAndStopThread() {
        try {
            if (mSynThread != null) {
                mSynThread.interrupt();
                mScreenSocket.shutDownConnection();
            }
            isRunning = false;//关闭循环发送消息；
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mScreenSocket != null && mScreenSocket.getSocket() != null) {
                try {
                    mScreenSocket.getSocket().close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        closeSocketAndStopThread();
    }

    @Override
    public MenuItemViewBase getMenuView() {
        return null;
    }

    public static ItemLayoutBase getInstance(Context context, ModelItem mate) {
        return new ItemScreen(context, mate);
    }

    public static String getType() {
        return "ItemScreen";
    }

    public static String getNick() {
        return "监控屏幕";
    }
}