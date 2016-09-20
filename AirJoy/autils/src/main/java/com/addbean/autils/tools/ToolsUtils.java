package com.addbean.autils.tools;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by AddBean on 2016/3/3.
 */
public class ToolsUtils {
    /**
     * 关闭软键盘
     */
    public static void closeKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * dp转px；
     *
     * @param dp
     * @return
     */
    public static int dpConvertToPx(Context context, int dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        int px = (int) (dp * scale + 0.5f);
        return px;
    }

    /**
     * 监测网络是否可行；
     *
     * @param act
     * @return
     */
    public static boolean detectNetwork(Activity act) {
        ConnectivityManager connectivity = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }


    /**
     * 获取真实物理路径
     * @param context
     * @param uri
     * @return
     */
    public static String getRealFilePath( final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 获取字符长度
     * @param data
     * @param textSize
     * @return
     */
    public static int getTextWidth(String data, int textSize) {
        int size = 0;
        String reg = "[^\u4e00-\u9fa5]";
        String reg1 = "[\u4e00-\u9fa5]";
        String strZh = data.replaceAll(reg, "");//把英文剔除；
        String strEn = data.replaceAll(reg1, "");//把汉字剔除；
        size = strZh.length() * textSize + strEn.length() * textSize / 2;
        return size;
    }
}
