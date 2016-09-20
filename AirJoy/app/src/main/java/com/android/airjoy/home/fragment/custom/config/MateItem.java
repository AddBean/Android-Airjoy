package com.android.airjoy.home.fragment.custom.config;

import java.io.Serializable;
import java.util.List;

/**
 * Created by AddBean on 2016/3/20.
 */
public class MateItem implements Serializable{
    private String mName;
    private List<ModelItem> mButtons;
    private boolean mIsVertical = true;
    private String mBackgroundSrc;
    private int mBackgroundColor=0;

    public boolean ismIsVertical() {
        return mIsVertical;
    }

    public void setmIsVertical(boolean mIsVertical) {
        this.mIsVertical = mIsVertical;
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

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public List<ModelItem> getmButtons() {
        return mButtons;
    }

    public void setmButtons(List<ModelItem> mButtons) {
        this.mButtons = mButtons;
    }

}
