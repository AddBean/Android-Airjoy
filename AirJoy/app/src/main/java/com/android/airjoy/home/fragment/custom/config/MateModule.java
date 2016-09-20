package com.android.airjoy.home.fragment.custom.config;

import com.addbean.aviews.utils.multiadapter.ListItemEx;
import com.android.airjoy.constant.Config;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AddBean on 2016/3/20.
 */
public class MateModule {
    private List<ModelModule> mConfigs = new ArrayList<ModelModule>();
    private String mName;
    public MateModule(List<ListItemEx> mConfigs) {
        for (ListItemEx item : mConfigs) {
            this.mConfigs.add((ModelModule) item.getmData());
        }
        this.mName = Config.ITEM_CONFIG_NAME;
    }

    public List<ModelModule> getmConfigs() {
        return mConfigs;
    }

    public void setmConfigs(List<ModelModule> mConfigs) {
        this.mConfigs = mConfigs;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }
}
