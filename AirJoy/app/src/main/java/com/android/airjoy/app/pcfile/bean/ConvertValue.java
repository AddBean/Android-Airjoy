package com.android.airjoy.app.pcfile.bean;

import java.text.DecimalFormat;
/**
 * 文件大小值转换类；
 *
 * @author 贾豆
 *
 */
public class ConvertValue {
    public static String FormetFileSize(long fileS)
    {
        String fileSizeString = "";
        DecimalFormat df = new DecimalFormat("#.00");
        if (fileS < 1024)
        {
            fileSizeString =df.format((double)fileS) + "B";
        }
        else if (fileS < 1048576)
        {
            fileSizeString = df.format((double)fileS / 1024) + "K";
        }
        else if (fileS < 1073741824)
        {
            fileSizeString = df.format((double)fileS / 1048576) + "M";
        }
        else
        {
            fileSizeString = df.format((double)fileS / 1073741824) + "G";
        }
        return fileSizeString.toString();
    }
}
