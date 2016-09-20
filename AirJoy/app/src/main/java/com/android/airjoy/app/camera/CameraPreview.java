package com.android.airjoy.app.camera;

import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.addbean.autils.utils.ALog;
import com.addbean.autils.utils.AnimUtils;
import com.addbean.aviews.utils.multiadapter.AdapterHelper;
import com.addbean.aviews.utils.multiadapter.ListItemEx;
import com.addbean.aviews.utils.multiadapter.MultiAdapter;
import com.android.airjoy.R;
import com.android.airjoy.app.phone.SnapService;
import com.android.airjoy.core.BaseActivity;
import com.android.airjoy.core.service.core.TaskQueue;
import com.android.airjoy.core.service.tasks.TCPTaskCameraSender;
import com.android.airjoy.core.service.tasks.TCPTaskCameraShakeSender;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 摄像头分享；
 *
 * @author 贾豆
 */

public class CameraPreview extends BaseActivity implements SurfaceHolder.Callback,
        Camera.PreviewCallback {
    private SurfaceView mSurfaceview = null; // SurfaceView对象：(视图组件)视频显示
    private SurfaceHolder mSurfaceHolder = null; // SurfaceHolder对象：(抽象接口)SurfaceView支持类
    private Camera mCamera = null; // Camera对象，相机预览
    private int VideoPreRate = 1;//视频刷新间隔
    private int VideoID = 0;//设置摄像头：0后置，1前置
    private int tempPreRate = 0;//当前视频序号
    private ImageModel mImageModel = new ImageModel();
    private int mDisplayOrientation = 90;
    private RelativeLayout mSurfaceContent;
    private ViewHolder mViewHolder = new ViewHolder();
    private MultiAdapter mAdapter;
    private List<ListItemEx> mData = new ArrayList<ListItemEx>();
    private Size mSize;
    private boolean mTimerEnable = true;

    public class ViewHolder {

        @ViewInject(R.id.tital_layout)
        private RelativeLayout tital_layout;
        @ViewInject(R.id.tital)
        private TextView tital;
        @ViewInject(R.id.app_back_icon)
        private ImageView app_back_icon;
        @ViewInject(R.id.SwitchCameraButton)
        private ImageView SwitchCameraButton;
        @ViewInject(R.id.surface_content)
        private RelativeLayout surface_content;
        @ViewInject(R.id.control_layout)
        private LinearLayout control_layout;
        @ViewInject(R.id.tools_text)
        private TextView tools_text;
        @ViewInject(R.id.tools_msg)
        private TextView tools_msg;
        @ViewInject(R.id.list_content)
        private LinearLayout list_content;
        @ViewInject(R.id.list)
        private ListView list;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setIsShowAd(false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camerapreview);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        TaskQueue.add(new TCPTaskCameraShakeSender("TCPTaskCameraShakeSender", true));
        ViewUtils.inject(mViewHolder, this);
        mSize = null;
        mSurfaceContent = (RelativeLayout) findViewById(R.id.surface_content);
        createSurface();
        initEvent();
        stopPhoneService();
        setupCamera();
        initListView();
        mFocusTimer.start();
    }

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mCamera != null) {
                mCamera.autoFocus(new Camera.AutoFocusCallback() {//自动对焦
                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {

                    }
                });
            }
        }
    };
    /**
     * 定时对焦；
     */
    public Thread mFocusTimer = new Thread() {
        @Override
        public void run() {
            while (mTimerEnable) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mHandler.sendEmptyMessage(0);
            }
            super.run();
        }
    };

    private void initEvent() {
        ImageView backImageView = (ImageView) findViewById(R.id.app_back_icon);
        ImageView switchCameraButton = (ImageView) findViewById(R.id.SwitchCameraButton);
        backImageView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                AnimUtils.ScaleAnim(v);
                finish();
            }
        });
        switchCameraButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimUtils.ScaleAnim(v);
                VideoID = VideoID == 0 ? 1 : 0;//切换
                mSize = null;
                createSurface();
                setupCamera();
            }
        });
        mViewHolder.control_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int visibility = mViewHolder.list_content.getVisibility();
                mViewHolder.list_content.setVisibility(visibility == View.GONE ? View.VISIBLE : View.GONE);
                if (mViewHolder.list_content.getVisibility() == View.VISIBLE) {
                    Toast.makeText(getApplicationContext(), "温馨提示：分辨率越低越流畅", Toast.LENGTH_LONG).show();
                }
            }
        });
        mViewHolder.list_content.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewHolder.list_content.setVisibility(View.GONE);
            }
        });
    }

    private void initListView() {
        mAdapter = new MultiAdapter(this, mData, new MultiAdapter.IAdpterListener() {
            @Override
            public void convert(AdapterHelper helper, MultiAdapter.ConvertViewInf data) {
                Size size = (Size) data.getData();
                TextView tv = (TextView) data.getView().findViewById(R.id.item_msg);
                helper.setText(R.id.item_msg, "" + size.width + "*" + size.height);
                if (mSize == null)
                    return;
                if (size.height == mSize.height && size.width == mSize.width) {
                    tv.setTextColor(getResources().getColor(R.color.color_blue));
                } else {
                    tv.setTextColor(getResources().getColor(R.color.white));
                }
            }
        });
        mAdapter.addType(R.layout.item_list_tool_layout);
        mViewHolder.list.setAdapter(mAdapter);
        mViewHolder.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSize = (Size) mData.get(position).getmData();
                createSurface();
                setupCamera();
                mViewHolder.list_content.setVisibility(View.GONE);
            }
        });
    }

    private void setSizeText(List<Size> sizes) {
        if (mSize == null) {
            mSize = sizes.get(sizes.size() - 1);//默认选择最低规格；
        }
        mViewHolder.tools_msg.setText("" + mSize.width + "*" + mSize.height);
    }

    private void setData(List<Size> sizes) {
        mData.clear();
        for (Size size : sizes) {
            mData.add(new ListItemEx(0, size));
        }
        mAdapter.notifyDataSetChanged();
    }

    private void createSurface() {
        mSurfaceContent.removeAllViews();
        mSurfaceview = null;
        mSurfaceview = new CameraSurfaceView(this);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mSurfaceview.setLayoutParams(lp);
        mSurfaceContent.addView(mSurfaceview);
        mSurfaceview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                int visibility = mViewHolder.control_layout.getVisibility();
                mViewHolder.control_layout.setVisibility(visibility == View.GONE ? View.VISIBLE : View.GONE);
                mViewHolder.list_content.setVisibility(View.GONE);
            }
        });
    }


    /* 停止录屏服务*/
    public void stopPhoneService() {
        try {
            Intent intent = new Intent(CameraPreview.this, SnapService.class);
            stopService(intent);
        } catch (Exception error) {
        }
    }


    /**
     * 设置camera；
     */
    private void setupCamera() {
        releaseCamera();
        try {
            mSurfaceHolder = mSurfaceview.getHolder(); // 绑定SurfaceView，取得SurfaceHolder对象
            mSurfaceHolder.addCallback(this); // SurfaceHolder加入回调接口
            mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);// 设置显示器类型，setType必须设置
            mCamera = Camera.open(VideoID);
            mCamera.setDisplayOrientation(mDisplayOrientation); // 设置方向；
            ALog.e("设置方向" + mDisplayOrientation);
            Camera.Parameters mParameters = mCamera.getParameters();
            List<Size> sizes = mParameters.getSupportedPictureSizes();
            setSizeText(sizes);
            mParameters.setPreviewSize(mSize.width, mSize.height);
            mCamera.setParameters(mParameters);
            startCameraPreview();
            setData(sizes);
            ALog.e(sizes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始预览
     */
    private void startCameraPreview() {
        try {
            if (mCamera != null) {
                mCamera.setPreviewDisplay(mSurfaceHolder);
                mCamera.startPreview();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放所有资源，才能重新进行初始化
     */
    private void releaseCamera() {
        try {
            if (mCamera != null) {
                mCamera.setPreviewCallback(null); // ！！这个必须在前，不然退出出错
                mCamera.stopPreview();
                mCamera.setPreviewDisplay(null);
                mCamera.release();
                mCamera = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ALog.e("正在释放摄像头资源");
    }

    /* 返回键*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        releaseCamera();
        mTimerEnable = false;
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimerEnable = false;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        if (mCamera == null) {
            return;
        }
        try {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(this);
            Camera.Parameters parameters = mCamera.getParameters();// 获取摄像头参数
            Size size = parameters.getPreviewSize();
            mImageModel.setVideoWidth(size.width);
            mImageModel.setVideoHeight(size.height);
            mImageModel.setVideoFormatIndex(parameters.getPreviewFormat());
            startCameraPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (tempPreRate < VideoPreRate) {
            tempPreRate++;
            return;
        }
        tempPreRate = 0;
        if (data != null) {
            TaskQueue.add(new TCPTaskCameraSender("CameraPreview", data,
                    mImageModel));
        }
    }

    public class ImageModel {

        public int VideoQuality = 20;//视频质量
        public float VideoWidthRatio = 1;//发送视频宽度比例
        public float VideoHeightRatio = 1;//发送视频高度比例
        public int VideoWidth = 240;//发送视频宽度
        public int VideoHeight = 240;//发送视频高度
        public int VideoFormatIndex = 0;//视频格式索引

        public int getVideoFormatIndex() {
            return VideoFormatIndex;
        }

        public void setVideoFormatIndex(int videoFormatIndex) {
            VideoFormatIndex = videoFormatIndex;
        }

        public int getVideoWidth() {
            return VideoWidth;
        }

        public void setVideoWidth(int videoWidth) {
            VideoWidth = videoWidth;
        }

        public int getVideoHeight() {
            return VideoHeight;
        }

        public void setVideoHeight(int videoHeight) {
            VideoHeight = videoHeight;
        }

        public float getVideoWidthRatio() {
            return VideoWidthRatio;
        }

        public void setVideoWidthRatio(float videoWidthRatio) {
            VideoWidthRatio = videoWidthRatio;
        }

        public float getVideoHeightRatio() {
            return VideoHeightRatio;
        }

        public void setVideoHeightRatio(float videoHeightRatio) {
            VideoHeightRatio = videoHeightRatio;
        }

        public ImageModel() {
        }
    }
}
