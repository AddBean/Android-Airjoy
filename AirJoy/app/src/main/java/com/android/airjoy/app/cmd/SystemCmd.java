package com.android.airjoy.app.cmd;

import java.util.ArrayList;
import java.util.HashMap;

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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import com.android.airjoy.R;
import com.android.airjoy.constant.CMD;
import com.android.airjoy.core.BaseActivity;
import com.android.airjoy.core.service.core.TaskQueue;
import com.android.airjoy.core.service.tasks.UDPTaskSender;
import com.android.airjoy.home.HomeActivity;
import com.android.airjoy.widget.AppleStyleDialog;

/**
 * 系统指令Activity；
 *
 * @author 贾豆
 *
 */
public class SystemCmd extends BaseActivity
{
    public static String CmdString="";
    public Context _context;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置无标题
        setContentView(R.layout.app_system);
        this._context=this;
        initTital();
        initGird();
    }

    /* 初始化标题 */
    private void initTital()
    {
        final ImageView backImageView = (ImageView) findViewById(R.id.app_back_icon);
        backImageView.setOnClickListener(new OnClickListener() {
            public void onClick(View v)
            {
                backImageView.setAlpha(50);
                finish();
            }
        });
    }

    private void initGird()
    {
        GridView gridview = (GridView) findViewById(R.id.AppSystemGridView);
        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map0 = new HashMap<String, Object>();
        map0.put("Sys_itemImage", R.drawable.icon_windows);
        map0.put("Sys_itemText", "关机");
        lstImageItem.add(map0);
        HashMap<String, Object> map1 = new HashMap<String, Object>();
        map1.put("Sys_itemImage", R.drawable.icon_windows);
        map1.put("Sys_itemText", "重启");
        lstImageItem.add(map1);
        HashMap<String, Object> map2 = new HashMap<String, Object>();
        map2.put("Sys_itemImage", R.drawable.icon_windows);
        map2.put("Sys_itemText", "休眠");
        lstImageItem.add(map2);
        HashMap<String, Object> map3 = new HashMap<String, Object>();
        map3.put("Sys_itemImage", R.drawable.icon_windows);
        map3.put("Sys_itemText", "定时关机");
        lstImageItem.add(map3);
        HashMap<String, Object> map4 = new HashMap<String, Object>();
        map4.put("Sys_itemImage", R.drawable.icon_windows);
        map4.put("Sys_itemText", "注销");
        lstImageItem.add(map4);
        HashMap<String, Object> map5 = new HashMap<String, Object>();
        map5.put("Sys_itemImage", R.drawable.icon_windows);
        map5.put("Sys_itemText", "锁定电脑");
        lstImageItem.add(map5);
        HashMap<String, Object> map6 = new HashMap<String, Object>();
        map6.put("Sys_itemImage", R.drawable.icon_windows);
        map6.put("Sys_itemText", "退出电脑助手");
        lstImageItem.add(map6);
        HashMap<String, Object> map7 = new HashMap<String, Object>();
        map7.put("Sys_itemImage", R.drawable.icon_windows);
        map7.put("Sys_itemText", "重启资源管理");
        lstImageItem.add(map7);
        HashMap<String, Object> map8 = new HashMap<String, Object>();
        map8.put("Sys_itemImage", R.drawable.icon_windows);
        map8.put("Sys_itemText", "待机");
        lstImageItem.add(map8);
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
    class ItemClickListener implements OnItemClickListener
    {
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long rowid)
        {
            switch (position)
            {
                case 0://关机;
                    CmdString=CMD.CmdToComputer.Shutdown;

                    ShowOkBox("关机");
                    break;
                case 1://重启;
                    CmdString=CMD.CmdToComputer.Restart;
                    ShowOkBox("重启");
                    break;
                case 2://休眠;
                    CmdString=CMD.CmdToComputer.Sleep;
                    ShowOkBox("休眠");
                    break;
                case 3://定时关机;
                    ShowSelectBox();
                    break;
                case 4://注销;
                    CmdString=CMD.CmdToComputer.LogOff;
                    ShowOkBox("注销");
                    break;
                case 5://锁定电脑;
                    CmdString=CMD.CmdToComputer.LockComputer;
                    ShowOkBox("锁定电脑");
                    break;
                case 6://退出AirJoy;
                    CmdString=CMD.CmdToComputer.KillAirjoy;;
                    ShowOkBox("退出AirJoy");
                    break;
                case 7://重启资源管理器；
                    CmdString=CMD.CmdToComputer.RestartExplorer;
                    ShowOkBox("重启资源管理器");
                    break;
                case 8://待机;
                    CmdString=CMD.CmdToComputer.SuspendState;
                    ShowOkBox("待机");
                    break;
            }
        }
    }
    /* 选择定时关机时间*/
    private void ShowSelectBox()
    {
        CmdString="C:shutdown -s -t "+"600"+" -c 'shutdown' -f:End";//默认十分钟后关机；
        AlertDialog.Builder builder = new AlertDialog.Builder(_context);
        builder.setTitle("选择关机时间").setSingleChoiceItems(
                new String[] { "10分钟后", "30分钟后", "1小时后", "2小时后", "3小时后","4小时后","5小时后","6小时后" }, 0,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        String string="";
                        switch(which){
                            case 0://10分钟；
                                string=CMD.CmdToComputer.ShutdownBytime.replace("time", "600");
                                CmdString=string;
                                break;
                            case 1://30分钟；
                                string=CMD.CmdToComputer.ShutdownBytime.replace("time", "1800");
                                CmdString=string;
                                break;
                            case 2://1小时；
                                string=CMD.CmdToComputer.ShutdownBytime.replace("time", "3600");
                                CmdString=string;
                                break;
                            case 3://2小时；
                                string=CMD.CmdToComputer.ShutdownBytime.replace("time", "7200");
                                CmdString=string;
                                break;
                            case 4://3小时；
                                string=CMD.CmdToComputer.ShutdownBytime.replace("time", "10800");
                                CmdString=string;
                                break;
                            case 5://4小时；
                                string=CMD.CmdToComputer.ShutdownBytime.replace("time", "14400");
                                CmdString=string;
                                break;
                            case 6://5小时
                                string=CMD.CmdToComputer.ShutdownBytime.replace("time", "18000");
                                CmdString=string;
                                break;
                            case 7://6小时
                                string=CMD.CmdToComputer.ShutdownBytime.replace("time", "100");
                                CmdString=string;
                                break;
                        }

                    }
                });
        builder.setPositiveButton("选择", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                ShowOkBox("定时关机");
            }
        });
        builder.show();

    }

    /* 选择确认对话框*/
    public void ShowOkBox(String tital){
        final AppleStyleDialog dialog=new AppleStyleDialog(this,"提示","确定执行"+tital+"操作吗？");
        dialog.setOnBtnClickListener(new AppleStyleDialog.IOnBtnClickListener() {
            @Override
            public void onSubmit() {
                TaskQueue.add(new UDPTaskSender("Makefun",CmdString));
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

