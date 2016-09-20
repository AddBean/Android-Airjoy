package com.android.airjoy.core.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.addbean.autils.utils.ALog;
import com.android.airjoy.core.service.core.QueueLooper;
import com.android.airjoy.core.service.core.TaskQueue;
import com.android.airjoy.core.service.tasks.UDPTaskSender;

/**
 * Created by AddBean on 2016/3/27.
 */
public class MainService extends Service {
    private final IBinder mBinder = new mBinder();
    private Context mContext;
    private int mScheduleTime = 10000;//定时时间；
    private int mLooperNumber = 3;//异步队列处理线程数；
    private static boolean mThreadRunning = true;

    public class mBinder extends Binder {
        public MainService getService() {
            return MainService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        this.mContext = this;
        QueueLooper.startMuliLooper(1);
        mThread.start();
    }


    public Thread mThread = new Thread() {
        @Override
        public void run() {
            super.run();
            while (mThreadRunning) {
                try {
                    Thread.sleep(mScheduleTime);
                    ALog.e("Airjoy Service Schedule Task");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

}
