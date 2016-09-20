package com.android.airjoy.home.fragment.custom.config;

/**
 * Created by AddBean on 2016/3/24.
 */
public class ModelItemText {
    private String mText;
    private int mGravity=1;//默认居中
    private int mTextColor;
    private int mBgColor;
    private int mTextSize=0;

    public String getmText() {
        return mText;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }

    public int getmGravity() {
        return mGravity;
    }

    public void setmGravity(int mGravity) {
        this.mGravity = mGravity;
    }

    public int getmTextColor() {
        return mTextColor;
    }

    public void setmTextColor(int mTextColor) {
        this.mTextColor = mTextColor;
    }

    public int getmBgColor() {
        return mBgColor;
    }

    public void setmBgColor(int mBgColor) {
        this.mBgColor = mBgColor;
    }

    public int getmTextSize() {
        return mTextSize;
    }

    public void setmTextSize(int mTextSize) {
        this.mTextSize = mTextSize;
    }
}
