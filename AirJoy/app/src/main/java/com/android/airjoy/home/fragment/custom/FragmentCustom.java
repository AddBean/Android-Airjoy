package com.android.airjoy.home.fragment.custom;

import android.content.ComponentName;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.addbean.autils.utils.ALog;
import com.addbean.autils.utils.PreferencesTools;
import com.addbean.aviews.utils.multiadapter.AdapterHelper;
import com.addbean.aviews.utils.multiadapter.ListItemEx;
import com.addbean.aviews.utils.multiadapter.MultiAdapter;
import com.addbean.aviews.views.RoundImageView;
import com.addbean.aviews.views.dynamic_fragment.ADynamicBaseSubFragment;
import com.android.airjoy.R;
import com.android.airjoy.constant.Config;
import com.android.airjoy.home.fragment.custom.config.MateModule;
import com.android.airjoy.home.fragment.custom.config.ModelItem;
import com.android.airjoy.home.fragment.custom.config.MateItem;
import com.android.airjoy.home.fragment.custom.config.ModelModule;
import com.android.airjoy.home.fragment.custom.keypad.FragmentPadEdit;
import com.android.airjoy.home.fragment.custom.keypad.PadActivity;
import com.android.airjoy.others.umeng.UmengConfig;
import com.android.airjoy.widget.AppleStyleDialog;
import com.android.airjoy.widget.CustomMenuDialog;
import com.lidroid.xutils.BitmapUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by AddBean on 2016/3/20.
 */
public class FragmentCustom extends ADynamicBaseSubFragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    public static FragmentCustom _fragmentCustom;
    private Object mTag;
    public MultiAdapter mAdapter;
    public GridView mGridView;
    public List<ListItemEx> mData = new ArrayList<ListItemEx>();
    private BitmapUtils mBitmapUtils;

    public FragmentCustom(Object mTag) {
        super(mTag);
        this.mTag = mTag;
    }

    public static FragmentCustom getInstance(Object mTag) {
        if (_fragmentCustom == null)
            _fragmentCustom = new FragmentCustom(mTag);
        return _fragmentCustom;
    }

    @Override
    public int getFragmentView() {
        return R.layout.fragment_sub_custom;
    }


    @Override
    public void initView() {
        mBitmapUtils = new BitmapUtils(getActivity());
        mGridView = (GridView) getCurrentView().findViewById(R.id.grid_content);
        mAdapter = new MultiAdapter(getActivity(), mData, new MultiAdapter.IAdpterListener() {
            @Override
            public void convert(AdapterHelper helper, MultiAdapter.ConvertViewInf data) {
                ModelModule model = (ModelModule) data.getData();
                if (data.getLayoutId() == R.layout.home_custom_gridview_add)
                    return;
                RoundImageView image = (RoundImageView) data.getView().findViewById(R.id.home_itemImage);
                if (TextUtils.isEmpty(model.getmBackgroundSrc())) {
                    mBitmapUtils.display(image,"");
                    image.setBackgroundColor(model.getmBackgroundColor());
                } else {
                    mBitmapUtils.display(image, model.getmBackgroundSrc());
                }

                helper.setText(R.id.home_itemText, model.getmName());
            }
        });
        mAdapter.addType(R.layout.home_custom_gridview);
        mAdapter.addType(R.layout.home_custom_gridview_add);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(this);
        mGridView.setOnItemLongClickListener(this);
        initData();
    }

    @Override
    public void onResumeView() {
    }

    private void initData() {
        ModelModule model;
        mData.clear();
        MateModule list = PreferencesTools.getObjList(getActivity(), Config.ITEM_CONFIG_NAME, MateModule.class, false);
        if (list != null) {
            for (ModelModule item : list.getmConfigs()) {
                mData.add(new ListItemEx(0, item));
            }
        }
        model = new ModelModule(R.drawable.custom_add, "添加", ModelModule.EICON_TYPE.ADD);
        mData.add(new ListItemEx(1, model));
        sortAndSaveList(false);
    }

    private void sortAndSaveList(boolean isSave) {
        List<ListItemEx> temp = new ArrayList<ListItemEx>(mData);
        mData.clear();
        ListItemEx tempItem = null;
        for (ListItemEx item : temp) {
            ModelModule model = (ModelModule) item.getmData();
            if (model.getmType() == ModelModule.EICON_TYPE.ADD) {
                tempItem = item;
            } else {
                mData.add(item);
            }
        }
        if (isSave)
            PreferencesTools.saveObj(getActivity(), Config.ITEM_CONFIG_NAME, new MateModule(mData), false);
        if (tempItem != null)
            mData.add(tempItem);
        mAdapter.notifyDataSetChanged();
    }

    private void addItem(ModelModule model) {
        mData.add(new ListItemEx(0, model));
        sortAndSaveList(true);
        createItemMateConfig(model);
    }

    private void removeItem(int position) {
        ModelModule model = (ModelModule) mData.get(position).getmData();
        mData.remove(position);
        sortAndSaveList(true);
    }

    private void createItemMateConfig(ModelModule model) {
        MateItem data = new MateItem();
        data.setmButtons(new ArrayList<ModelItem>());
        data.setmName(model.getmName());
        data.setmBackgroundColor(model.getmBackgroundColor());
        data.setmBackgroundSrc(model.getmBackgroundSrc());
        data.setmIsVertical(model.ismIsVertical());
        sendEventToUmeng(UmengConfig.CREATE_SCENE, model.getmName());
        //创建该布局对应的配置文件；
        PreferencesTools.saveObj(getActivity(), model.getmConfigId(), data, false);
        Intent intent = new Intent(getActivity(), PadActivity.class);
        intent.putExtra("data", model);
        startActivity(intent);
    }

    private void sendEventToUmeng(String eventName, String name) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("scene_name", name);
        MobclickAgent.onEvent(getActivity(), eventName, map);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == mData.size() - 1) {
            AddItemDialog addItemDialog = new AddItemDialog(getActivity(), new AddItemDialog.OnMenuClickListener() {
                @Override
                public void onConfirm(ModelModule model) {
                    FragmentPadEdit.mEditEnable = true;
                    addItem(model);
                }
            });
            addItemDialog.show();
        } else {
            Intent intent = new Intent(getActivity(), PadActivity.class);
            ModelModule data = (ModelModule) mData.get(position).getmData();
            sendEventToUmeng(UmengConfig.EVENT_SCENE, data.getmName());
            intent.putExtra("data", data);
            FragmentPadEdit.mEditEnable = false;
            startActivity(intent);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        if (position != mData.size() - 1) {
            final CustomMenuDialog dialog = new CustomMenuDialog(getActivity(), "菜单");
            dialog.setSubmitVisiable(View.GONE);
            dialog.addItem("删除", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeItem(position);
                    dialog.dismiss();
                }
            });
//            dialog.addItem("发送到桌面", new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ModelModule data = (ModelModule) mData.get(position).getmData();
//                    addShortcut(data);
//                    sendEventToUmeng(UmengConfig.EVENT_CREATE_SHORTICON, data.getmName());
//                    dialog.dismiss();
//                }
//            });
            dialog.show();
        }
        return false;
    }

    /**
     * 为程序创建桌面快捷方式
     */
    private void addShortcut(ModelModule data) {
        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        //快捷方式的名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name));
        shortcut.putExtra("duplicate", false); //不允许重复创建
        //指定当前的Activity为快捷方式启动的对象;
        ComponentName comp = new ComponentName(getActivity().getPackageName(), "." + PadActivity.class.getName());
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setComponent(comp);
        intent.putExtra("data", data);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
        Intent.ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(getActivity(), R.drawable.ic_launcher);//快捷方式的图标
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
        getActivity().sendBroadcast(shortcut);
    }
}
