package com.android.airjoy.app.phone;

import java.io.DataOutputStream;
import java.util.List;

import com.addbean.autils.utils.AnimUtils;
import com.android.airjoy.R;
import com.android.airjoy.app.anim.Rotate3dHelper;
import com.android.airjoy.app.help.AppHelp;
import com.android.airjoy.core.BaseActivity;
import com.android.airjoy.core.service.core.TaskQueue;
import com.android.airjoy.core.service.tasks.TCPTaskCameraShakeSender;
import com.android.airjoy.widget.AirjoyToast;
import com.android.airjoy.widget.AppleStyleDialog;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by AddBean on 2016/3/28.
 */
public class AppPhone extends BaseActivity {
    ImageView startImageView;
    ImageView helpImageView;
    static Context _context;
    String apkRoot = "chmod 777  " + "/dev/graphics/fb0";
    boolean isStart = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.app_phone);
        TaskQueue.add(new TCPTaskCameraShakeSender("TCPTaskCameraShakeSender", true));
        this._context = this;
        final AppleStyleDialog dialog = new AppleStyleDialog(this, "提示", "该功能需要ROOT权限", "可能只适用于少数机型", "请确认ROOT权限已开");
        dialog.setOnBtnClickListener(new AppleStyleDialog.IOnBtnClickListener() {
            @Override
            public void onSubmit() {
                dialog.dismiss();
            }

            @Override
            public void onCancel() {
                finish();
            }
        });
        dialog.show();
    }


    @Override
    protected void onStart() {
        super.onStart();
        initTital();
        helpImageView = (ImageView) findViewById(R.id.helpImage);
        final Rotate3dHelper helpAnim = new Rotate3dHelper(helpImageView);
        helpImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimUtils.ScaleAnim(view);
                Intent intent = new Intent();
                intent.setClass(AppPhone.this, AppHelp.class);
                startActivity(intent);
            }
        });
        startImageView = (ImageView) findViewById(R.id.startSever);
        startImageView.setBackgroundResource(R.drawable.app_phone_off);
        if (isServiceRunning(_context, "com.android.airjoy.app.phone.SnapService")) {
            setImageView(true);
        } else {
            setImageView(false);
        }
        startImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                AnimUtils.ScaleAnim(arg0);
                if (RootCommand(apkRoot)) {
                    try {
                        if (isServiceRunning(_context,
                                "com.android.airjoy.app.phone.SnapService")) {
                            setImageView(false);
                            Intent intent = new Intent(AppPhone.this,
                                    SnapService.class);
                            stopService(intent);
                            final AppleStyleDialog dialog = new AppleStyleDialog(_context, "提示", "已关闭后台服务！");
                            dialog.setOnBtnClickListener(new AppleStyleDialog.IOnBtnClickListener() {
                                @Override
                                public void onSubmit() {
                                    dialog.dismiss();
                                }

                                @Override
                                public void onCancel() {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                            isStart = true;
                        } else {
                            setImageView(true);
                            Intent intent = new Intent(AppPhone.this,
                                    SnapService.class);
                            startService(intent);
                            final AppleStyleDialog dialog = new AppleStyleDialog(_context, "提示", "已开启后台服务!");
                            dialog.setOnBtnClickListener(new AppleStyleDialog.IOnBtnClickListener() {
                                @Override
                                public void onSubmit() {
                                    dialog.dismiss();
                                }

                                @Override
                                public void onCancel() {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                            isStart = false;
                        }
                    } catch (Exception error) {
                        final AppleStyleDialog dialog = new AppleStyleDialog(_context, "提示", "操作失败！", "请确认ROOT权限已被允许");
                        dialog.setOnBtnClickListener(new AppleStyleDialog.IOnBtnClickListener() {
                            @Override
                            public void onSubmit() {
                                dialog.dismiss();
                            }

                            @Override
                            public void onCancel() {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                } else {
                    final AppleStyleDialog dialog = new AppleStyleDialog(_context, "提示", "ROOT权限开启失败！", "请确认ROOT权限已被允许");
                    dialog.setOnBtnClickListener(new AppleStyleDialog.IOnBtnClickListener() {
                        @Override
                        public void onSubmit() {
                            finish();
                        }

                        @Override
                        public void onCancel() {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }

            }
        });

    }

    private void initTital() {
        final ImageView backImageView = (ImageView) findViewById(R.id.app_back_icon);
        backImageView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                backImageView.setAlpha(50);
                finish();
            }
        });
    }

    public void setImageView(boolean flag) {
        Rotate3dHelper _switchAnim = new Rotate3dHelper(startImageView);
        if (flag) {
            _switchAnim.Rotate3dByX(200, 0, 90, 100, true,
                    new Rotate3dHelper.OnAnimOverListener() {
                        @Override
                        public void AnimOver() {
                            startImageView.setBackgroundResource(R.drawable.app_phone_on);
                        }
                    });

        } else {
            _switchAnim.Rotate3dByX(200, 0, 90, 100, true,
                    new Rotate3dHelper.OnAnimOverListener() {
                        @Override
                        public void AnimOver() {
                            startImageView.setBackgroundResource(R.drawable.app_phone_off);
                        }
                    });
        }
    }

    public static boolean RootCommand(String command) {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
        return true;
    }

    public static boolean isServiceRunning(Context mContext, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(30);
        if (!(serviceList.size() > 0)) {
            return false;
        }

        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }
}
