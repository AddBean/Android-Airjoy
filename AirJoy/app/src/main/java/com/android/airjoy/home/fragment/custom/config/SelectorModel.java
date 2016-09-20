package com.android.airjoy.home.fragment.custom.config;

import com.android.airjoy.home.fragment.custom.keypad.layouts.ItemCircle;
import com.android.airjoy.home.fragment.custom.keypad.layouts.ItemCorner;
import com.android.airjoy.home.fragment.custom.keypad.layouts.ItemImage;
import com.android.airjoy.home.fragment.custom.keypad.layouts.ItemScreen;
import com.android.airjoy.home.fragment.custom.keypad.layouts.ItemText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AddBean on 2016/3/23.
 */
public class SelectorModel {
    private String mCode;
    private String mName;

    public SelectorModel(String mName, String mCode) {
        this.mName = mName;
        this.mCode = mCode;
    }

    public String getmCode() {
        return mCode;
    }

    public void setmCode(String mCode) {
        this.mCode = mCode;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public static ArrayList<SelectorModel> getKeyList() {
        ArrayList<SelectorModel> keyList = new ArrayList<SelectorModel>();
        SelectorModel keyCode;
        keyCode = new SelectorModel("无命令", "-1");
        keyList.add(keyCode);
        keyCode = new SelectorModel("触摸板(单击左键，三指右键)", "0");
        keyList.add(keyCode);
        keyCode = new SelectorModel("左方向", "37");
        keyList.add(keyCode);
        keyCode = new SelectorModel("上方向", "38");
        keyList.add(keyCode);
        keyCode = new SelectorModel("右方向", "39");
        keyList.add(keyCode);
        keyCode = new SelectorModel("下方向", "40");
        keyList.add(keyCode);
        keyCode = new SelectorModel("回车键", "13");
        keyList.add(keyCode);
        keyCode = new SelectorModel("空格键", "32");
        keyList.add(keyCode);
        keyCode = new SelectorModel("Backspace", "8");
        keyList.add(keyCode);
        keyCode = new SelectorModel("Tab", "9");
        keyList.add(keyCode);
        keyCode = new SelectorModel("Clear", "12");
        keyList.add(keyCode);
        keyCode = new SelectorModel("Shift", "16");
        keyList.add(keyCode);
        keyCode = new SelectorModel("Ctrl", "17");
        keyList.add(keyCode);
        keyCode = new SelectorModel("Alt", "18");
        keyList.add(keyCode);
        keyCode = new SelectorModel("Pause", "19");
        keyList.add(keyCode);
        keyCode = new SelectorModel("CapsLock", "20");
        keyList.add(keyCode);
        keyCode = new SelectorModel("Esc", "27");
        keyList.add(keyCode);
        keyCode = new SelectorModel("PageUp", "33");
        keyList.add(keyCode);
        keyCode = new SelectorModel("PageDown", "34");
        keyList.add(keyCode);
        keyCode = new SelectorModel("End", "35");
        keyList.add(keyCode);
        keyCode = new SelectorModel("Home", "36");
        keyList.add(keyCode);
        keyCode = new SelectorModel("Select", "41");
        keyList.add(keyCode);
        keyCode = new SelectorModel("Execute", "43");
        keyList.add(keyCode);
        keyCode = new SelectorModel("PrintScreen", "44");
        keyList.add(keyCode);
        keyCode = new SelectorModel("Windows", "91");
        keyList.add(keyCode);
        keyCode = new SelectorModel("Ins", "45");
        keyList.add(keyCode);
        keyCode = new SelectorModel("Del", "46");
        keyList.add(keyCode);
        keyCode = new SelectorModel("Help", "47");
        keyList.add(keyCode);
        keyCode = new SelectorModel("０", "48");
        keyList.add(keyCode);
        keyCode = new SelectorModel("１", "49");
        keyList.add(keyCode);
        keyCode = new SelectorModel("２", "50");
        keyList.add(keyCode);
        keyCode = new SelectorModel("３", "51");
        keyList.add(keyCode);
        keyCode = new SelectorModel("４", "52");
        keyList.add(keyCode);
        keyCode = new SelectorModel("５", "53");
        keyList.add(keyCode);
        keyCode = new SelectorModel("６", "54");
        keyList.add(keyCode);
        keyCode = new SelectorModel("７", "55");
        keyList.add(keyCode);
        keyCode = new SelectorModel("８", "56");
        keyList.add(keyCode);
        keyCode = new SelectorModel("９", "57");
        keyList.add(keyCode);
        keyCode = new SelectorModel("Ａ", "65");
        keyList.add(keyCode);
        keyCode = new SelectorModel("Ｂ", "66");
        keyList.add(keyCode);
        keyCode = new SelectorModel("Ｃ", "67");
        keyList.add(keyCode);
        keyCode = new SelectorModel("Ｄ", "68");
        keyList.add(keyCode);
        keyCode = new SelectorModel("Ｅ", "69");
        keyList.add(keyCode);
        keyCode = new SelectorModel("Ｆ", "70");
        keyList.add(keyCode);
        keyCode = new SelectorModel("Ｇ", "71");
        keyList.add(keyCode);
        keyCode = new SelectorModel("Ｈ", "72");
        keyList.add(keyCode);
        keyCode = new SelectorModel("Ｉ", "73");
        keyList.add(keyCode);
        keyCode = new SelectorModel("Ｊ", "74");
        keyList.add(keyCode);
        keyCode = new SelectorModel("Ｋ", "75");
        keyList.add(keyCode);
        keyCode = new SelectorModel("Ｌ", "76");
        keyList.add(keyCode);
        keyCode = new SelectorModel("Ｍ", "77");
        keyList.add(keyCode);
        keyCode = new SelectorModel("Ｎ", "78");
        keyList.add(keyCode);
        keyCode = new SelectorModel("Ｏ", "79");
        keyList.add(keyCode);
        keyCode = new SelectorModel("Ｐ", "80");
        keyList.add(keyCode);
        keyCode = new SelectorModel("Ｑ", "81");
        keyList.add(keyCode);
        keyCode = new SelectorModel("Ｒ", "82");
        keyList.add(keyCode);
        keyCode = new SelectorModel("Ｓ", "83");
        keyList.add(keyCode);
        keyCode = new SelectorModel("Ｔ", "84");
        keyList.add(keyCode);
        keyCode = new SelectorModel("Ｕ", "85");
        keyList.add(keyCode);
        keyCode = new SelectorModel("Ｖ", "86");
        keyList.add(keyCode);
        keyCode = new SelectorModel("Ｗ", "87");
        keyList.add(keyCode);
        keyCode = new SelectorModel("Ｘ", "88");
        keyList.add(keyCode);
        keyCode = new SelectorModel("Ｙ", "89");
        keyList.add(keyCode);
        keyCode = new SelectorModel("Ｚ", "90");
        keyList.add(keyCode);
        keyCode = new SelectorModel("数字０", "96");
        keyList.add(keyCode);
        keyCode = new SelectorModel("数字键１", "97");
        keyList.add(keyCode);
        keyCode = new SelectorModel("数字键２", "98");
        keyList.add(keyCode);
        keyCode = new SelectorModel("数字键３", "99");
        keyList.add(keyCode);
        keyCode = new SelectorModel("数字键４", "100");
        keyList.add(keyCode);
        keyCode = new SelectorModel("数字键５", "101");
        keyList.add(keyCode);
        keyCode = new SelectorModel("数字键６", "102");
        keyList.add(keyCode);
        keyCode = new SelectorModel("数字键７", "103");
        keyList.add(keyCode);
        keyCode = new SelectorModel("数字键８", "104");
        keyList.add(keyCode);
        keyCode = new SelectorModel("数字键９", "105");
        keyList.add(keyCode);
        keyCode = new SelectorModel("＊", "106");
        keyList.add(keyCode);
        keyCode = new SelectorModel("＋", "107");
        keyList.add(keyCode);
        keyCode = new SelectorModel("－", "109");
        keyList.add(keyCode);
        keyCode = new SelectorModel("．", "110");
        keyList.add(keyCode);
        keyCode = new SelectorModel("／", "111");
        keyList.add(keyCode);
        keyCode = new SelectorModel("F1", "112");
        keyList.add(keyCode);
        keyCode = new SelectorModel("F2", "113");
        keyList.add(keyCode);
        keyCode = new SelectorModel("F3", "114");
        keyList.add(keyCode);
        keyCode = new SelectorModel("F4", "115");
        keyList.add(keyCode);
        keyCode = new SelectorModel("F5", "116");
        keyList.add(keyCode);
        keyCode = new SelectorModel("F6", "117");
        keyList.add(keyCode);
        keyCode = new SelectorModel("F7", "118");
        keyList.add(keyCode);
        keyCode = new SelectorModel("F8", "119");
        keyList.add(keyCode);
        keyCode = new SelectorModel("F9", "120");
        keyList.add(keyCode);
        keyCode = new SelectorModel("F10", "121");
        keyList.add(keyCode);
        keyCode = new SelectorModel("F11", "122");
        keyList.add(keyCode);
        keyCode = new SelectorModel("F12", "123");
        keyList.add(keyCode);
        keyCode = new SelectorModel("F13", "124");
        keyList.add(keyCode);
        keyCode = new SelectorModel("F14", "125");
        keyList.add(keyCode);
        keyCode = new SelectorModel("F15", "126");
        keyList.add(keyCode);
        keyCode = new SelectorModel("F16", "127");
        keyList.add(keyCode);
        keyCode = new SelectorModel("F17", "128");
        keyList.add(keyCode);
        keyCode = new SelectorModel("F18", "129");
        keyList.add(keyCode);
        keyCode = new SelectorModel("F19", "130");
        keyList.add(keyCode);
        keyCode = new SelectorModel("F20", "131");
        keyList.add(keyCode);
        keyCode = new SelectorModel("F21", "132");
        keyList.add(keyCode);
        keyCode = new SelectorModel("F22", "133");
        keyList.add(keyCode);
        keyCode = new SelectorModel("F23", "134");
        keyList.add(keyCode);
        keyCode = new SelectorModel("F24", "135");
        keyList.add(keyCode);
        keyCode = new SelectorModel("Fn(可能无效)", "255");
        keyList.add(keyCode);
        return keyList;
    }

    public static List<SelectorModel> getTypeList() {
        ArrayList<SelectorModel> keyList = new ArrayList<SelectorModel>();
        keyList.add(new SelectorModel(ItemCircle.getNick(), ItemCircle.getType()));
        keyList.add(new SelectorModel(ItemCorner.getNick(), ItemCorner.getType()));
        keyList.add(new SelectorModel(ItemImage.getNick(), ItemImage.getType()));
        keyList.add(new SelectorModel(ItemText.getNick(), ItemText.getType()));
        keyList.add(new SelectorModel(ItemScreen.getNick(), ItemScreen.getType()));
        return keyList;
    }

    public static List<SelectorModel> getAnimList() {
        ArrayList<SelectorModel> keyList = new ArrayList<SelectorModel>();
        keyList.add(new SelectorModel("无效果", "none"));
        keyList.add(new SelectorModel("缩放", "scale"));
        keyList.add(new SelectorModel("淡变", "fade"));
        return keyList;
    }
}
