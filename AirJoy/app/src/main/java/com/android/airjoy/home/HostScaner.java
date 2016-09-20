package com.android.airjoy.home;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;

import com.addbean.autils.utils.ALog;
import com.addbean.autils.utils.PreferencesTools;
import com.android.airjoy.constant.Config;
import com.android.airjoy.constant.ConfigModel;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by AddBean on 2016/3/27.
 */
public class HostScaner {
    private String ShakeCmd = "SHAKE|";
    private int SERVERPORT = Config.Port;
    private String locAddress;// 存储本机ip，例：本地ip ：192.168.1.
    private Runtime run = Runtime.getRuntime();// 获取当前运行环境，来执行ping，相当于windows的cmd
    private Process proc = null;
    private String ping = "ping -c 1 -w 0.7 ";// 其中 -c 1为发送的次数，-w 表示发送后等待响应的时间
    private Context mContext;// 上下文
    private Integer mCount = 0;
    private ExecutorService mCachedThreadPool;
    private Handler handler = new Handler();
    private ResultListener mResultListener;

    public HostScaner(Context mContext) {
        this.mContext = mContext;
        this.mCount = 0;
    }

    /**
     * 扫描网络；
     *
     * @param resultListener
     */
    public void scanNet(Context context, final ResultListener resultListener) {
        this.mResultListener = resultListener;
        ConfigModel model = PreferencesTools.getObj(context, Config.IP_CONFIG, ConfigModel.class, false);
        if (model != null) {
            Config.Ip = model.getmIp();
        }else{
            Config.Ip=null;
        }
        if (!TextUtils.isEmpty(Config.Ip)) {
            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    String result = "";
                    try {
                        result = sendShakeMessage(Config.Ip, resultListener);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    return result;
                }

                @Override
                protected void onPostExecute(String result) {
                    super.onPostExecute(result);
                    if (!TextUtils.isEmpty(result) && result.contains("SUCCESS")) {
                        resultListener.onResultSuccess(Config.Ip );
                        resultListener.onScanOver();
                    } else {
                        scan(resultListener);
                    }
                }
            }.execute();
        } else {
            scan(resultListener);
        }

    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mCachedThreadPool != null && mCachedThreadPool.isTerminated()) {
                handler.removeCallbacks(runnable);
                mResultListener.onScanOver();
            } else {
                handler.postDelayed(this, 200);
            }
        }
    };


    private void startScanThreadPool(final ResultListener resultListener){
        Config.Ip=null;
        handler.postDelayed(runnable, 100);//每100ms监测扫描是否完毕；
        locAddress = getLocAddrIndex();// 获取本地ip前缀
        if (TextUtils.isEmpty(locAddress)) {
            resultListener.onFailed();
            return;
        }
        mCachedThreadPool = Executors.newCachedThreadPool();
        for (int i = 0; i < 256; i++)// 创建256个线程分别去ping
        {
            final int mLastIp = i;
            mCachedThreadPool.execute(new Runnable() {
                public void run() {
                    String pingCMD = HostScaner.this.ping + locAddress + mLastIp;
                    ALog.e("正在连接:" + pingCMD);
                    String current_ip = locAddress + mLastIp;
                    try {
                        sendShakeMessage(current_ip,resultListener);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } finally {
                        if (proc != null)
                            proc.destroy();
                    }
                }
            });
        }
        mCachedThreadPool.shutdown();//已添加的任务只执行一次即销毁，后续添加任务不予执行；
    }
    /**
     * 开启扫描线程池
     *
     * @param resultListener
     */
    private void scan(final ResultListener resultListener) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                startScanThreadPool(resultListener);
                return null;
            }
        }.execute();
    }

    /**
     * 发送握手消息；
     *
     * @param ip
     * @param resultListener
     * @return
     */
    private String sendShakeMessage(String ip, ResultListener resultListener) throws UnsupportedEncodingException {
        String msg = java.net.URLEncoder.encode(ShakeCmd + getWifiIP() + "-" + getLocDeviceName() + "|", "utf-8");
        String res = null;
        Socket socket = null;
        byte[] buffer = new byte[7];
        resultListener.onConnectPre(ip);
        try {
            socket = new Socket();
            SocketAddress address=new InetSocketAddress(ip, SERVERPORT);
            socket.connect(address,1000);//0.1秒钟超时检查；
            PrintWriter os = new PrintWriter(socket.getOutputStream());// 向服务器发送消息
            os.println(msg);
            os.flush();// 刷新输出流，使Server马上收到该字符串
            DataInputStream input = new DataInputStream(socket.getInputStream()); // 从服务器获取返回消息
            input.read(buffer);
            res = new String(buffer);
            ALog.d("server 返回信息：" + res);
            if (res != null) { // 如果验证通过
                if (res.contains("SUCCESS")) {
                    ALog.d("服务器IP：" + ip);
                    Config.Ip = ip;
                    resultListener.onResultSuccess(ip);
                }
            }
        } catch (Exception unknownHost) {
            ALog.d("连接未知主机出错！");
        } finally// 关闭socket;
        {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        return res;
    }

    private String getWifiIP() {
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            return null;
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String ip = intToIp(ipAddress);
        return ip;
    }

    private String intToIp(int ipAddress) {
//        return ((i >> 24) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + (i & 0xFF);
        return ((ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff)
                + "." + (ipAddress >> 16 & 0xff) + "." + (ipAddress >> 24 & 0xff));
    }


    /* 获取IP前缀*/
    private String getLocAddrIndex() {
        String str = getWifiIP();
        if (!TextUtils.isEmpty(str)) {
            return str.substring(0, str.lastIndexOf(".") + 1);
        }
        return null;
    }

    /* 获取本机设备名称*/
    private String getLocDeviceName() {
        return android.os.Build.MODEL;
    }

    /* 回调接口*/
    public interface ResultListener {
        public void onFailed();

        public void onConnectPre(String ipInf);

        public void onResultSuccess(String ipInf);

        public void onScanOver();
    }
}
