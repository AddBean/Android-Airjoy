package com.android.airjoy.home.fragment.custom.keypad.core;

import android.content.Context;

import com.android.airjoy.home.fragment.custom.config.ModelItem;
import com.android.airjoy.home.fragment.custom.keypad.layouts.ItemCircle;
import com.android.airjoy.home.fragment.custom.keypad.layouts.ItemCorner;
import com.android.airjoy.home.fragment.custom.keypad.layouts.ItemImage;
import com.android.airjoy.home.fragment.custom.keypad.layouts.ItemScreen;
import com.android.airjoy.home.fragment.custom.keypad.layouts.ItemText;

/**
 * Created by AddBean on 2016/3/22.
 */
public class ItemFactory {
    public static ItemLayoutBase getItemByMate(Context context, ModelItem mate) {
        if (mate.getmType().equals(ItemCircle.getType())) {
            return ItemCircle.getInstance(context, mate);
        }
        if (mate.getmType().equals(ItemCorner.getType())) {
            return ItemCorner.getInstance(context, mate);
        }
        if (mate.getmType().equals(ItemImage.getType())) {
            return ItemImage.getInstance(context, mate);
        }
        if (mate.getmType().equals(ItemText.getType())) {
            return ItemText.getInstance(context, mate);
        }
        if (mate.getmType().equals(ItemScreen.getType())) {
            return ItemScreen.getInstance(context, mate);
        }
        return null;
    }
}
