package com.android.airjoy.home.fragment.custom.config;

import com.addbean.autils.tools.MD5Utils;

import java.io.Serializable;

/**
 * Created by AddBean on 2016/3/20.
 */
public class ModelModule implements Serializable {

    private int mResId;
    private String mName;
    private String mConfigId;
    private EICON_TYPE mType;
    private boolean mIsVertical = true;
    private String mBackgroundSrc;
    private int mBackgroundColor=0;

    public ModelModule(int mResId, String mName, EICON_TYPE type) {
        this.mResId = mResId;
        this.mName = mName;
        this.mType = type;
        this.mConfigId =MD5Utils.String2md5(this.mName);
    }

    public String getmBackgroundSrc() {
        return mBackgroundSrc;
    }

    public void setmBackgroundSrc(String mBackgroundSrc) {
        this.mBackgroundSrc = mBackgroundSrc;
    }

    public int getmBackgroundColor() {
        return mBackgroundColor;
    }

    public void setmBackgroundColor(int mBackgroundColor) {
        this.mBackgroundColor = mBackgroundColor;
    }

    public boolean ismIsVertical() {
        return mIsVertical;
    }

    public void setmIsVertical(boolean mIsVertical) {
        this.mIsVertical = mIsVertical;
    }

    public EICON_TYPE getmType() {
        return mType;
    }

    public void setmType(EICON_TYPE mType) {
        this.mType = mType;
    }

    public String getmConfigId() {
        return mConfigId;
    }

    public void setmConfigId(String mConfigId) {
        this.mConfigId = mConfigId;
    }

    public int getmResId() {
        return mResId;
    }

    public void setmResId(int mResId) {
        this.mResId = mResId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public static enum EICON_TYPE {
        APP(0), CUSTOM(1), ADD(2);
        private int mIndex = 0;

        EICON_TYPE(int index) {
            this.mIndex = index;
        }

        public int getmIndex() {
            return mIndex;
        }

        public void setmIndex(int mIndex) {
            this.mIndex = mIndex;
        }
    }
}
