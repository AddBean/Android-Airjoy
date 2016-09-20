package com.android.airjoy.widget;

import com.android.airjoy.R;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AirjoyToast extends Toast {  
  
    public AirjoyToast(Context context) {  
        super(context);  
    }  
     
    public static Toast makeText(Context context, CharSequence text, int duration) {  
        Toast result = new Toast(context);  
          
        //��ȡLayoutInflater����  
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);   
        //��layout�ļ�����һ��View����  
        View layout = inflater.inflate(R.layout.airjoytoast, null);  
          
        //ʵ��ImageView��TextView����  
        TextView textView = (TextView) layout.findViewById(R.id.AirjoyToastText);    
        textView.setText(text);  
          
        result.setView(layout);  
        result.setGravity(Gravity.CENTER_VERTICAL, 0, 0);  
        result.setDuration(duration);  
          
        return result;  
    }  
  
}  