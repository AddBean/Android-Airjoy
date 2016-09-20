package com.android.airjoy.core.service.core;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/24 0024.
 */
public class TaskQueue {
    public static List<TaskBase> queue = new LinkedList<TaskBase>();
    /*	队列添加任务*/
    public static void add(TaskBase t) {
        synchronized (TaskQueue.queue) {
            TaskQueue.queue.add(t); // 添加任务
            TaskQueue.queue.notifyAll();// 激活该队列对应的执行线程，全部Run起来
        }
    }
}