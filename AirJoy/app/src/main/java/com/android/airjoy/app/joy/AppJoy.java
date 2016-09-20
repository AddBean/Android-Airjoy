package com.android.airjoy.app.joy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.airjoy.R;
import com.android.airjoy.constant.CMD;
import com.android.airjoy.core.BaseActivity;
import com.android.airjoy.core.service.core.TaskQueue;
import com.android.airjoy.core.service.tasks.UDPTaskSender;
import com.android.airjoy.sensor.Gsensor;

/**
 * 游戏手柄页面
 *
 * @author 贾豆
 *
 */
public class AppJoy extends BaseActivity implements SensorEventListener
{
    private Context _context;
    ImageView XButton;// X按键；
    ImageView YButton;// Y按键；
    ImageView AButton;// A按键;
    ImageView BButton;// B按键；
    ImageView Button1;// 按键1；
    ImageView Button2;// 按键2;
    ImageView UpKey;// 上键；
    ImageView DownKey;// 下键；
    ImageView LeftKey;// 左键；
    ImageView RightKey;// 右键；
    ImageView LeftView;// 左显示;
    ImageView RightView;// 右显示；
    public static ImageView GravitySwitch;// 重力开关；
    Gsensor _gsensor;
    public static boolean Gswitch = true;
    String TAG = "Gsensor";
    public float _sensorDel = 2;// 设置重力感应灵敏度(范围0~10)；
    public static Sensor mSensor;
    public float[] _currentValue = new float[3];
    public static SensorManager mSensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 设置横屏
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置无标题
        setContentView(R.layout.app_joy);
        this._context = this;
        startSensor();
        initTital();
        initButtonListner();
    }

    /* 初始化标题 */
    private void initTital()
    {
        final ImageView backImageView = (ImageView) findViewById(R.id.app_back_icon);
        backImageView.setOnClickListener(new OnClickListener() {
            public void onClick(View v)
            {
                CloseSensor();
                backImageView.setAlpha(50);
                finish();
            }
        });
    }

    /* 初始化按键监听 */
    private void initButtonListner()
    {
        XButton = (ImageView) findViewById(R.id.XButton);// X按键；
        YButton = (ImageView) findViewById(R.id.YButton);// Y按键；
        AButton = (ImageView) findViewById(R.id.AButton);// A按键(ImageView)findViewById(R.Id.);
        BButton = (ImageView) findViewById(R.id.BButton);// B按键；
        Button1 = (ImageView) findViewById(R.id.Button1);// 按键1；
        Button2 = (ImageView) findViewById(R.id.Button2);// 按键2(ImageView)findViewById(R.Id.);
        UpKey = (ImageView) findViewById(R.id.UpKey);// 上键；
        DownKey = (ImageView) findViewById(R.id.DownKey);// 下键；
        LeftKey = (ImageView) findViewById(R.id.LeftKey);// 左键；
        RightKey = (ImageView) findViewById(R.id.RightKey);// 右键；
        LeftView = (ImageView) findViewById(R.id.LeftView);// 左显示(ImageView)findViewById(R.Id.);
        RightView = (ImageView) findViewById(R.id.RightView);// 右显示；
        GravitySwitch = (ImageView) findViewById(R.id.GravitySwitch);// 重力开关；
        // 设置监听；
        XButton.setOnTouchListener(keyTouchListener);// X按键；
        YButton.setOnTouchListener(keyTouchListener);// Y按键；
        AButton.setOnTouchListener(keyTouchListener);// A按键.setOnTouchListener(keyTouchListener);
        BButton.setOnTouchListener(keyTouchListener);// B按键；
        Button1.setOnTouchListener(keyTouchListener);// 按键1；
        Button2.setOnTouchListener(keyTouchListener);// 按键2.setOnTouchListener(keyTouchListener);
        UpKey.setOnTouchListener(keyTouchListener);// 上键；
        DownKey.setOnTouchListener(keyTouchListener);// 下键；
        LeftKey.setOnTouchListener(keyTouchListener);// 左键；
        RightKey.setOnTouchListener(keyTouchListener);// 右键；
        LeftView.setOnTouchListener(keyTouchListener);// 左显示.setOnTouchListener(keyTouchListener);
        RightView.setOnTouchListener(keyTouchListener);// 右显示；
        GravitySwitch.setOnClickListener(new OnClickListener() {
            @SuppressWarnings("deprecation")
            public void onClick(View v)
            {
                animClick(v);
                if (Gswitch)
                {// 关重力；
                    Gswitch = false;
                    CloseSensor();
                    GravitySwitch.setBackgroundDrawable(getResources().getDrawable(
                            R.drawable.app_joy_off));
                }
                else
                {// 开重力；
                    Gswitch = true;
                    startSensor();
                    GravitySwitch.setBackgroundDrawable(getResources().getDrawable(
                            R.drawable.app_joy_on));
                }
            }
        });
    }

    /* 点击事件处理 */
    public OnTouchListener keyTouchListener = new OnTouchListener() {
        public boolean onTouch(View view, MotionEvent event)
        {
            // TODO Auto-generated method stub
            animClick(view);
            switch (view.getId())
            {
                case R.id.XButton:// X按键；
                    sendCmd(CMD.JoyCmd.XButton,event.getAction());
                    break;
                case R.id.YButton:// Y按键；
                    sendCmd(CMD.JoyCmd.YButton,event.getAction());
                    break;
                case R.id.AButton:// A按键:
                    sendCmd(CMD.JoyCmd.AButton,event.getAction());
                    break;
                case R.id.BButton:// B按键；
                    sendCmd(CMD.JoyCmd.BButton,event.getAction());
                    break;
                case R.id.Button1:// 按键1；
                    sendCmd(CMD.JoyCmd.Button1,event.getAction());
                    break;
                case R.id.Button2:// 按键2:
                    sendCmd(CMD.JoyCmd.Button2,event.getAction());
                    break;
                case R.id.UpKey:// 上键；
                    sendCmd(CMD.JoyCmd.UpKey,event.getAction());
                    break;
                case R.id.DownKey:// 下键；
                    sendCmd(CMD.JoyCmd.DownKey,event.getAction());
                    break;
                case R.id.LeftKey:// 左键；
                    sendCmd(CMD.JoyCmd.LeftKey,event.getAction());
                    break;
                case R.id.RightKey:// 右键；
                    sendCmd(CMD.JoyCmd.RightKey,event.getAction());
                    break;
            }
            return true;
        }
    };

    /* 点击动画 */
    public boolean animClick(View view)
    {
        Vibrator vibrator = (Vibrator) _context.getSystemService("vibrator");
        vibrator.vibrate(100);
        /** 设置缩放动画 */
        ScaleAnimation animation = new ScaleAnimation(
                0.5f,
                1.0f,
                0.5f,
                1.0f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);
        animation.setDuration(100);// 设置动画持续时间
        animation.startNow();
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(animation);
        view.startAnimation(animationSet);
        animationSet.start();
        return false;
    }

    /* 启动此传感器监听; */
    public Boolean startSensor()
    {
        try
        {
            mSensorManager = (SensorManager) _context.getSystemService(SENSOR_SERVICE);
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);// TYPE_GRAVITY
            if (null == mSensorManager)
            {
                throw new Exception();
            }
            // 参数三，检测的精准度
            mSensorManager.registerListener(this, mSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);// SENSOR_DELAY_GAME
            return true;
        }
        catch (Exception error)
        {
            Toast.makeText(this._context, "您的手机不支持此传感器！", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /* 数值改变时 */
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }

    /* 关闭此传感器监听; */
    public void CloseSensor()
    {
        mSensorManager.unregisterListener(this, mSensor);
        mSensorManager.unregisterListener(this);
        Gswitch = false;
    }

    /* 传感器的回调函数 */
    public void onSensorChanged(SensorEvent event)
    {
        if (event.sensor == null)
        {
            return;
        }
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            this._currentValue[0] = event.values[0];
            this._currentValue[1] = event.values[1];
            this._currentValue[2] = event.values[2];
            if (Gswitch)
            {
                if (this._currentValue[1] > _sensorDel)
                {
                    sendCmd(CMD.JoyCmd.RightKey,MotionEvent.ACTION_DOWN);
                    animClick(RightView);
                }
                else if (this._currentValue[1] < -_sensorDel)
                {
                    sendCmd(CMD.JoyCmd.LeftKey,MotionEvent.ACTION_DOWN);
                    animClick(LeftView);
                }else
                {
                    sendCmd(CMD.JoyCmd.LeftKey,MotionEvent.ACTION_UP);
                    sendCmd(CMD.JoyCmd.RightKey,MotionEvent.ACTION_UP);
                }
            }

        }
    }

    /* 发送名命令 */
    public void sendCmd(String CmdStr, int action)
    {
        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
                TaskQueue.add(new UDPTaskSender("AppJoy","K:" + CmdStr + ":DOWN"));
                break;
            case MotionEvent.ACTION_MOVE:
                TaskQueue.add(new UDPTaskSender("AppJoy","K:" + CmdStr + ":DOWN"));
                break;
            case MotionEvent.ACTION_UP:
                TaskQueue.add(new UDPTaskSender("AppJoy","K:" + CmdStr + ":UP"));
                break;
        }

    }

    /* 返回键 */
    @Override
    public void onBackPressed()
    {
        CloseSensor();
        super.onBackPressed();
    }
}
