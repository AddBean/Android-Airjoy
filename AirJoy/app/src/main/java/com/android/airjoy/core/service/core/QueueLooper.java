package com.android.airjoy.core.service.core;

import com.addbean.autils.utils.ALog;

/**
 * Created by AddBean on 2015/12/24 0024.
 */
public class QueueLooper extends Thread {
    @Override
    public void run() {
        super.run();
        while (true) {
            synchronized (TaskQueue.queue) {
                while (TaskQueue.queue.isEmpty()) { //
                    try {
                        TaskQueue.queue.wait(); // 队列为空时，使线程处于等待状态
                    } catch (InterruptedException e) {
                        ALog.e("InterruptedException:"+e.getMessage());
                    }
                    ALog.e(this.getName() + " wait...");
                }
                TaskBase t = TaskQueue.queue.remove(0); // 得到第一个
                t.RunTask(); // 执行该任务
//                ALog.e("Consuming Task :" + t.getmTaskName());
            }
        }
    }


    //开启多个线程执行队列中的任务，那就是先到先得，先处理；
    public static void startMuliLooper(int threadNumber) {
        for (int i = 0; i < threadNumber; i++) {
            QueueLooper th = new QueueLooper(); //开始执行时，队列为空，处于等待状态
            th.setName("AsyThread_" + i);
            th.start();
            ALog.e("Start Thread_" + i);
        }
    }
}
