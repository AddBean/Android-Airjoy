package com.android.airjoy.core.service.core;

/**
 * Created by Administrator on 2015/12/24 0024.
 */
public abstract class TaskBase {
    public String mTaskName;
    public Object mData;

    public TaskBase(String taskName, Object data) {
        setmTaskName(taskName);
        setmData(data);
    }

    public Object getmData() {
        return mData;
    }

    public void setmData(Object mData) {
        this.mData = mData;
    }

    public String getmTaskName() {
        return mTaskName;
    }

    public void setmTaskName(String mTaskName) {
        this.mTaskName = mTaskName;
    }

    public abstract void RunTask();/* 此处执行对应任务*/
}