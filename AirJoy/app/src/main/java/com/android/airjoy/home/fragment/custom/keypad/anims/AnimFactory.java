package com.android.airjoy.home.fragment.custom.keypad.anims;

import android.text.TextUtils;
import android.view.View;

import com.addbean.autils.utils.AnimUtils;

/**
 * Created by AddBean on 2016/3/25.
 */
public class AnimFactory {
    public static void startAnim(String type, View view) {
        if (TextUtils.isEmpty(type)) {
            return;
        }
        if (type.equals("none")) {
            return;
        }
        if (type.equals("scale")) {
            AnimUtils.ScaleAnim(view);
        }
        if (type.equals("fade")) {
            AnimUtils.FadeOutAnim(view);
        }
    }
}
