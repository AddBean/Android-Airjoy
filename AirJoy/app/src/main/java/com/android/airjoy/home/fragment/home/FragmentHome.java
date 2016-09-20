package com.android.airjoy.home.fragment.home;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.addbean.aviews.utils.multiadapter.AdapterHelper;
import com.addbean.aviews.utils.multiadapter.ListItemEx;
import com.addbean.aviews.utils.multiadapter.MultiAdapter;
import com.addbean.aviews.views.dynamic_fragment.ADynamicBaseSubFragment;
import com.android.airjoy.R;
import com.android.airjoy.app.ap.AppAp;
import com.android.airjoy.app.camera.CameraPreview;
import com.android.airjoy.app.cmd.SystemCmd;
import com.android.airjoy.app.help.AppHelp;
import com.android.airjoy.app.joy.AppJoy;
import com.android.airjoy.app.makefun.Makefun;
import com.android.airjoy.app.mouse.MouseActivity;
import com.android.airjoy.app.pccamera.PcCameraActivity;
import com.android.airjoy.app.pcfile.AppPcActivity;
import com.android.airjoy.app.pcscreen.DeskShowActivity;
import com.android.airjoy.app.phone.AppPhone;
import com.android.airjoy.constant.Config;
import com.android.airjoy.home.fragment.custom.config.ModelModule;
import com.android.airjoy.others.umeng.UmengConfig;
import com.android.airjoy.widget.AppleStyleDialog;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AddBean on 2016/3/20.
 */
public class FragmentHome extends ADynamicBaseSubFragment implements AdapterView.OnItemClickListener {
    public static FragmentHome _fragmentHome;
    public MultiAdapter mAdapter;
    public GridView mGridView;
    public List<ListItemEx> mData = new ArrayList<ListItemEx>();

    public static FragmentHome getInstance(Object mTag) {
        if (_fragmentHome == null)
            _fragmentHome = new FragmentHome(mTag);
        return _fragmentHome;
    }

    public FragmentHome(Object mTag) {
        super(mTag);
    }

    @Override
    public int getFragmentView() {
        return R.layout.fragment_sub_home;
    }

    @Override
    public void initView() {
        mGridView = (GridView) getCurrentView().findViewById(R.id.grid_content);
        mAdapter = new MultiAdapter(getActivity(), mData, new MultiAdapter.IAdpterListener() {
            @Override
            public void convert(AdapterHelper helper, MultiAdapter.ConvertViewInf data) {
                ModelModule model = (ModelModule) data.getData();
                helper.setImageSrc(R.id.home_itemImage, model.getmResId());
                helper.setText(R.id.home_itemText, model.getmName());
            }
        });
        mAdapter.addType(R.layout.home_gridview);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(this);
        initData();
    }

    private void initData() {
        mData.clear();
        ModelModule model;
        model = new ModelModule(R.drawable.icon_mouse, "无线鼠标", ModelModule.EICON_TYPE.APP);
        mData.add(new ListItemEx(0, model));
        model = new ModelModule(R.drawable.icon_phone_camera, "手机摄像", ModelModule.EICON_TYPE.APP);
        mData.add(new ListItemEx(0, model));
        model = new ModelModule(R.drawable.icon_pcfils, "我的电脑", ModelModule.EICON_TYPE.APP);
        mData.add(new ListItemEx(0, model));
        model = new ModelModule(R.drawable.icon_phone, "监视手机", ModelModule.EICON_TYPE.APP);
        mData.add(new ListItemEx(0, model));
        model = new ModelModule(R.drawable.icon_pc, "监视电脑", ModelModule.EICON_TYPE.APP);
        mData.add(new ListItemEx(0, model));
        model = new ModelModule(R.drawable.icon_camera, "电脑摄像", ModelModule.EICON_TYPE.APP);
        mData.add(new ListItemEx(0, model));
        model = new ModelModule(R.drawable.icon_windows, "电脑命令", ModelModule.EICON_TYPE.APP);
        mData.add(new ListItemEx(0, model));
        model = new ModelModule(R.drawable.icon_hacker, "黑客工具", ModelModule.EICON_TYPE.APP);
        mData.add(new ListItemEx(0, model));
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResumeView() {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:// 鼠标；
                jumpToTargetActivity(MouseActivity.class, UmengConfig.EVENT_MOUSE);
                break;
            case 1:// 摄像头；
                jumpToTargetActivity(CameraPreview.class, UmengConfig.EVENT_CAMERA);
                break;
            case 2://我的电脑；
                jumpToTargetActivity(AppPcActivity.class, UmengConfig.EVENT_PCFILE);
                break;
            case 3:// 分享手机屏幕；
                jumpToTargetActivity(AppPhone.class, UmengConfig.EVENT_PHONESPY);
                break;
            case 4:// 分享电脑屏幕；
                jumpToTargetActivity(DeskShowActivity.class, UmengConfig.EVENT_PCSPY);
                break;
            case 5:// 分享电脑屏幕；
                jumpToTargetActivity(PcCameraActivity.class, UmengConfig.EVENT_PCCAMERA);
                break;
            case 6:// 系统命令；
                jumpToTargetActivity(SystemCmd.class, UmengConfig.EVENT_CMD);
                break;
            case 7:// 黑客工具；
                jumpToTargetActivity(Makefun.class, UmengConfig.EVENT_HACK);
                break;
        }
    }

    private Boolean jumpToTargetActivity(final Class<?> targetAvtivity, String event) {
        final Intent intent = new Intent();
        if (Config.Ip == null || Config.Port == 0) {
            final AppleStyleDialog dialog = new AppleStyleDialog(getActivity(), "连接失败，怎么办？","下载电脑端，并打开电脑端。","确认手机和电脑连接在同一路由器上。","打开手机端使用。");
            dialog.setOnBtnClickListener(new AppleStyleDialog.IOnBtnClickListener() {
                @Override
                public void onSubmit() {
                    Uri uri = Uri.parse(getString(R.string.DownloadUrl));
                    Intent it = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(it);
                    dialog.dismiss();
                }
                @Override
                public void onCancel() {
                    dialog.dismiss();
                }
            });
            dialog.setSubmitText("下载电脑端");
            dialog.setCancelText("放弃");
            dialog.show();
        } else {
            MobclickAgent.onEvent(getActivity(), event);
            intent.setClass(getActivity(), targetAvtivity);
            if (targetAvtivity == CameraPreview.class) {//传入默认摄像头；
                intent.putExtra("Camera", 0);//后置摄像头；
            }
            startActivity(intent);
            return true;
        }
        return true;

    }
}
