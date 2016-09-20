package com.android.airjoy.home.fragment.custom.keypad;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.addbean.autils.tools.ToolsUtils;
import com.addbean.autils.utils.ALog;
import com.addbean.autils.utils.AnimUtils;
import com.addbean.autils.utils.PreferencesTools;
import com.addbean.aviews.views.BaseFragment;
import com.android.airjoy.R;
import com.android.airjoy.home.fragment.custom.config.MateItem;
import com.android.airjoy.home.fragment.custom.config.ModelItem;
import com.android.airjoy.home.fragment.custom.config.ModelModule;
import com.android.airjoy.home.fragment.custom.keypad.core.ItemFactory;
import com.android.airjoy.home.fragment.custom.keypad.core.ItemLayoutBase;
import com.android.airjoy.home.fragment.custom.keypad.core.ItemLayoutEdit;
import com.android.airjoy.home.fragment.custom.keypad.menu.MenuAddDialog;
import com.android.airjoy.home.fragment.custom.keypad.menu.MenuItemDialog;
import com.android.airjoy.home.fragment.custom.keypad.menu.IOnMenuClickListener;
import com.android.airjoy.others.umeng.UmengConfig;
import com.lidroid.xutils.BitmapUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by AddBean on 2016/3/21.
 */
public class FragmentPadEdit extends BaseFragment implements ItemLayoutEdit.OnItemClickListener {
    private ModelModule mConfig;
    private ImageView mAddButton;
    private ImageView mEditButton;
    private ImageView mSaveButton;
    private ItemLayoutEdit mCurrentEditButton;
    private FrameLayout mContentView;
    private ViewGroup mView;
    private MateItem mMateItem;
    private int DP = 1;
    private MenuAddDialog menuAddDialog;
    private ControlEditLayout mControlEditLayout;
    private ImageView mBackgroundImage;
    public static boolean mEditEnable = false;
    private BitmapUtils mBitmapUtils;

    public static final FragmentPadEdit getInstance(ModelModule mModel) {
        FragmentPadEdit _fragmentPadEdit = new FragmentPadEdit(mModel);
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", mModel);
        _fragmentPadEdit.setArguments(bundle);
        return _fragmentPadEdit;
    }

    public FragmentPadEdit() {
    }

    public FragmentPadEdit(ModelModule mModel) {
        this.mConfig = mModel;
    }

    @Override
    public int getFragmentView() {
        return R.layout.fragment_pad_edit;
    }

    @Override
    public void initView() {
        this.mConfig = (ModelModule) getArguments().get("data");
        DP = ToolsUtils.dpConvertToPx(getContext(), 1);
        mView = (ViewGroup) getCurrentView();
        mBitmapUtils = new BitmapUtils(getContext());
        mBitmapUtils.configDiskCacheEnabled(true);
        mControlEditLayout = (ControlEditLayout) getCurrentView().findViewById(R.id.control_edit_layout);
        mBackgroundImage = (ImageView) getCurrentView().findViewById(R.id.background_image);
        mContentView = (FrameLayout) getCurrentView().findViewById(R.id.content_main);
        mAddButton = (ImageView) getCurrentView().findViewById(R.id.add_button);
        mEditButton = (ImageView) getCurrentView().findViewById(R.id.edit_button);
        mSaveButton = (ImageView) getCurrentView().findViewById(R.id.save_button);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimUtils.ScaleAnim(v);
                addButton();
            }
        });
        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AnimUtils.ScaleAnim(v);
                setEditEnable(true);
            }
        });
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AnimUtils.ScaleAnim(v);
                setEditEnable(false);
            }
        });
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAllEdit();
                try {
                    if (!mEditEnable) return;
                    saveLayout();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        readLayout();
        if (mConfig.getmBackgroundColor() != 0) {
            mContentView.setBackgroundColor(mConfig.getmBackgroundColor());
        }
        if(!TextUtils.isEmpty(mConfig.getmBackgroundSrc())){
            mBackgroundImage.setScaleType(ImageView.ScaleType.FIT_XY);
            mBitmapUtils.display(mBackgroundImage,mConfig.getmBackgroundSrc());
        }
        setEditEnable(mEditEnable);
        mAddButton.setVisibility(mEditEnable ? View.VISIBLE : View.GONE);
        mControlEditLayout.setmEditEnable(mEditEnable);
        mEditButton.setVisibility(!mEditEnable ? View.VISIBLE : View.GONE);
        mSaveButton.setVisibility(mEditEnable ? View.VISIBLE : View.GONE);
    }

    private void readLayout() {
        removeAllViews();
        if (mConfig == null) return;
        mMateItem = PreferencesTools.getObj(getContext(), mConfig.getmConfigId(), MateItem.class, false);
        if (mMateItem == null) return;

        for (ModelItem mate : mMateItem.getmButtons()) {
            ItemLayoutEdit btnView = new ItemLayoutEdit(getActivity(), ItemFactory.getItemByMate(getContext(), mate));
            btnView.setOnItemClickListener(this);
            mContentView.addView(btnView);
        }
    }

    private void saveLayout() throws Exception {
        int count = mContentView.getChildCount();
        mMateItem.setmButtons(new ArrayList<ModelItem>());
        mMateItem.getmButtons().clear();
        PreferencesTools.saveObj(getContext(), mConfig.getmConfigId(), mMateItem, false);//先清空
        for (int i = 0; i < count; i++) {
            View view = mContentView.getChildAt(i);
            if (view instanceof ItemLayoutEdit) {
                ItemLayoutBase content = ((ItemLayoutEdit) view).getmContent();
                ModelItem model = content.getmModelItem();
                if (mMateItem == null) throw new Exception("编辑控件内容信息为空");
                mMateItem.getmButtons().add(model);
            }
        }
        PreferencesTools.saveObj(getContext(), mConfig.getmConfigId(), mMateItem, false);
//        Toast.makeText(getContext(), "保存成功", Toast.LENGTH_SHORT).show();
    }

    private void removeAllViews() {
        int count = mContentView.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = mContentView.getChildAt(i);
            if (view instanceof ItemLayoutEdit) {
                mContentView.removeView(view);
            }
        }
    }

    private void addButton() {
        clearAllEdit();
        menuAddDialog = new MenuAddDialog(getContext(), new IOnMenuClickListener() {
            @Override
            public void onDialogConfirm(ModelItem mate) {
                menuAddDialog.dismiss();
                mate.setmWidth(50 * DP);
                mate.setmHeight(50 * DP);
                ALog.e(mate);
                ItemLayoutBase base = ItemFactory.getItemByMate(getActivity(), mate);
                if (base == null) {
                    Toast.makeText(getContext(), "该控件不存在", Toast.LENGTH_SHORT).show();
                    return;
                }
                mCurrentEditButton = new ItemLayoutEdit(getActivity(), base);
                mContentView.addView(mCurrentEditButton);
                int specW = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                int specH = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                mCurrentEditButton.measure(specW, specH);
                mCurrentEditButton.setPosition(mContentView.getMeasuredWidth() / 2 - mCurrentEditButton.getMeasuredWidth() / 2, mContentView.getMeasuredHeight() / 2 - mCurrentEditButton.getMeasuredHeight() / 2);
                mCurrentEditButton.setOnItemClickListener(FragmentPadEdit.this);
                onClick(mCurrentEditButton, ItemLayoutEdit.EControlType.COONTENT);
            }

            @Override
            public void onDialogCancel() {
                menuAddDialog.dismiss();
            }
        });
        menuAddDialog.show();


    }



    @Override
    public void onClick(ItemLayoutEdit view, ItemLayoutEdit.EControlType type) {
        if (type == null) return;
        switch (type) {
            case TOP_LEFT://删除;
                if (view.isEditEnable()) {
                    mContentView.removeView(view);
                    mContentView.requestLayout();
                }
                break;
            case TOP_RIGHT://编辑；
                if (view.isEditEnable() && view.getmContent().getMenuView() != null) {
                    MenuItemDialog dialog = new MenuItemDialog(getContext(), view.getmContent());
                    dialog.show();
                }
                break;
            case COONTENT:
                clearAllEdit();
                view.setEditEnable(true);
                mContentView.bringChildToFront(view);
                mContentView.requestLayout();
                break;
        }
    }

    private void clearAllEdit() {
        int count = mContentView.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = mContentView.getChildAt(i);
            if (view instanceof ItemLayoutEdit) {
                ((ItemLayoutEdit) view).setEditEnable(false);
            }
        }
    }

    @Override
    public void onResumeView() {

    }

    public boolean isEditEnable() {
        return mEditEnable;
    }

    public void setEditEnable(boolean editEnable) {
        if (!editEnable) {
            if (!hasEdited()) {
                Toast.makeText(getContext(), "您还未添加任何控件！", Toast.LENGTH_SHORT).show();
                return;
            }
            clearAllEdit();
            try {
                saveLayout();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.mEditEnable = editEnable;
        mAddButton.setVisibility(editEnable ? View.VISIBLE : View.GONE);
        mControlEditLayout.setmEditEnable(editEnable);
        mEditButton.setVisibility(!editEnable ? View.VISIBLE : View.GONE);
        mSaveButton.setVisibility(editEnable ? View.VISIBLE : View.GONE);

    }

    private boolean hasEdited() {
        int count = mContentView.getChildCount();
        PreferencesTools.saveObj(getContext(), mConfig.getmConfigId(), mMateItem, false);//先清空
        for (int i = 0; i < count; i++) {
            View view = mContentView.getChildAt(i);
            if (view instanceof ItemLayoutEdit) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onStop() {
        super.onStop();
        int count = mContentView.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = mContentView.getChildAt(i);
            if (view instanceof ItemLayoutEdit) {
                ((ItemLayoutEdit) view).onFragmentStop();
            }
        }
    }
}