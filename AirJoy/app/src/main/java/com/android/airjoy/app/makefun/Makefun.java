package com.android.airjoy.app.makefun;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.android.airjoy.R;
import com.android.airjoy.constant.CMD;
import com.android.airjoy.core.BaseActivity;
import com.android.airjoy.core.service.core.TaskQueue;
import com.android.airjoy.core.service.tasks.UDPTaskSender;
import com.android.airjoy.widget.AppleStyleDialog;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * 恶搞页面
 *
 * @author 贾豆
 *
 */
public class Makefun extends BaseActivity
{
    public static String CMDString="";
    public Context _context;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置无标题
        setContentView(R.layout.app_makefun);
        this._context=this;
        initTital();
        initGird();
    }
    /* 初始化标题*/
    private void initTital(){
        final ImageView backImageView=(ImageView)findViewById(R.id.app_back_icon);
        backImageView.setOnClickListener(new OnClickListener() {
            public void onClick(View v)
            {
                backImageView.setAlpha(50);
                finish();
            }
        });
    }

    /* 初始化网格布局*/
    private void initGird()
    {
        GridView gridview = (GridView) findViewById(R.id.AppMakefunGridView);
        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map0 = new HashMap<String, Object>();
        map0.put("Sys_itemImage", R.drawable.icon_hacker);
        map0.put("Sys_itemText", "弹窗");
        lstImageItem.add(map0);
        HashMap<String, Object> map1 = new HashMap<String, Object>();
        map1.put("Sys_itemImage", R.drawable.icon_hacker);
        map1.put("Sys_itemText", "反复重启");
        lstImageItem.add(map1);
        HashMap<String, Object> map2 = new HashMap<String, Object>();
        map2.put("Sys_itemImage", R.drawable.icon_hacker);
        map2.put("Sys_itemText", "弹出文件夹");
        lstImageItem.add(map2);
        HashMap<String, Object> map3 = new HashMap<String, Object>();
        map3.put("Sys_itemImage", R.drawable.icon_hacker);
        map3.put("Sys_itemText", "无限弹窗");
        lstImageItem.add(map3);
        HashMap<String, Object> map4 = new HashMap<String, Object>();
        map4.put("Sys_itemImage", R.drawable.icon_hacker);
        map4.put("Sys_itemText", "关闭弹窗");
        lstImageItem.add(map4);
        SimpleAdapter saImageItems = new SimpleAdapter(
                this,
                lstImageItem,// 数据源
                R.layout.app_system_gird,// 显示布局
                new String[] {
                        "Sys_itemImage",
                        "Sys_itemText" },
                new int[] {
                        R.id.Sys_itemImage,
                        R.id.Sys_itemText });
        gridview.setAdapter(saImageItems);
        gridview.setOnItemClickListener(new ItemClickListener());
    }

    /* 点击事件处理*/
    class ItemClickListener implements OnItemClickListener
    {
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long rowid)
        {
            switch (position)
            {
                case 0://弹窗;
                    CreateMessageDialog dialog=new CreateMessageDialog(Makefun.this, new CreateMessageDialog.OnMenuClickListener() {
                        @Override
                        public void onConfirm(String title,String msg) {
                            CMDString= CMD.HackCmd.Popup;
                            CMDString=String.format(CMDString,msg,title);
                            TaskQueue.add(new UDPTaskSender("Makefun",CMDString));
                        }
                    });
                    dialog.show();
                    break;
                case 1://重启;
                    CMDString=CMD.HackCmd.RestartLoop;
                    ShowOkBox("重启五次，请慎重使用！");
                    break;
                case 2://弹出文件夹;
                    CMDString=CMD.HackCmd.PopupFolder;
                    ShowOkBox("弹出很多文件夹，有一定风险！");
                    break;
                case 3://无线弹窗;
                    CMDString=CMD.HackCmd.PopupCmdForever;
                    ShowOkBox("此命令会弹出无数个弹窗，直到电脑内存耗尽，有一定风险！");
                    break;
                case 4://关闭弹窗;
                    CMDString=CMD.HackCmd.PopupStop;
                    ShowOkBox("此命令将关闭非法弹窗！");
                    break;
            }
        }
    }
    /* 选择确认对话框*/
    public void ShowOkBox(String tital){
        final AppleStyleDialog dialog=new AppleStyleDialog(this,"提示",tital);
        dialog.setOnBtnClickListener(new AppleStyleDialog.IOnBtnClickListener() {
            @Override
            public void onSubmit() {
                TaskQueue.add(new UDPTaskSender("Makefun",CMDString));
                dialog.dismiss();
            }

            @Override
            public void onCancel() {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}
