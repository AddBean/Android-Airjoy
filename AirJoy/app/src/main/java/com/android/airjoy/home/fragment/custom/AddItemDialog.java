package com.android.airjoy.home.fragment.custom;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.addbean.autils.tools.ToolsUtils;
import com.addbean.autils.utils.ALog;
import com.addbean.autils.utils.PreferencesTools;
import com.addbean.autils.utils.ResultActivityListener;
import com.android.airjoy.R;
import com.android.airjoy.constant.Config;
import com.android.airjoy.home.HomeActivity;
import com.android.airjoy.home.fragment.custom.config.ModelModule;
import com.android.airjoy.home.fragment.custom.config.MateModule;
import com.android.airjoy.widget.ColorPickerView;
import com.android.airjoy.widget.RoundLayout;
import com.android.airjoy.widget.anim.ScaleAnim;
import com.lidroid.xutils.BitmapUtils;

/**
 * Created by AddBean on 2016/3/20.
 */
public class AddItemDialog extends Dialog {
    private OnMenuClickListener mOnMenuClickListener;
    public MenuView mMenuView;
    private Context mContext;
    private HomeActivity mHomeActivity;

    public AddItemDialog(Context context,OnMenuClickListener mOnMenuClickListener) {
        super(context, R.style.transparent_dialog_add);
        this.mContext = context;
        this.mMenuView = new MenuView(context);
        this.setContentView(this.mMenuView);
        this.mOnMenuClickListener = mOnMenuClickListener;
        mHomeActivity = (HomeActivity) context;
    }

    @Override
    public void show() {
        Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.menu_slide_in);
        this.mMenuView.startAnimation(anim);
        super.show();
        Window window = this.getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = ((Activity) mContext).getWindowManager().getDefaultDisplay().getHeight();
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        this.onWindowAttributesChanged(wl);

    }

    @Override
    public void dismiss() {
        Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.menu_slide_out);
        this.mMenuView.startAnimation(anim);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dismissView();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

    private void dismissView() {
        super.dismiss();
    }


    public class MenuView extends FrameLayout implements View.OnClickListener {
        private Context mContext;
        private LayoutInflater mInflater;
        private View mView;
        private EditText mEdit;
        private View mHiz;
        private View mVer;
        private ImageView mImageHiz;
        private ImageView mImageVer;
        private TextView mConfirm;
        private TextView mCancel;
        private ImageView mBgImage;
        private LinearLayout mMenuContent;
        private RoundLayout mColorLayout;
        private BitmapUtils mBitmapUtils;
        private String mBgPath;
        private boolean mIsVer = true;
        private int mColor = 0;

        public MenuView(Context context) {
            super(context);
            this.mContext = context;
            initView();
        }

        private void initView() {
            if (mView == null) {
                mInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
                mView = mInflater.inflate(R.layout.custom_menu_dialog, null);
                this.addView(mView);
            }
            mImageHiz = (ImageView) findViewById(R.id.select_icon_hiz);
            mImageVer = (ImageView) findViewById(R.id.select_icon_ver);
            mHiz = findViewById(R.id.layout_hiz);
            mVer = findViewById(R.id.layout_ver);
            mEdit = (EditText) findViewById(R.id.edit_text);
            mCancel = (TextView) findViewById(R.id.btn_cancel);
            mConfirm = (TextView) findViewById(R.id.btn_confirm);
            mMenuContent = (LinearLayout) findViewById(R.id.menu_menu_content);
            mColorLayout = (RoundLayout) findViewById(R.id.color_layout);
            mBgImage = (ImageView) findViewById(R.id.background_image);
            mMenuContent.setOnClickListener(this);
            mHiz.setOnClickListener(this);
            mVer.setOnClickListener(this);
            mBgImage.setOnClickListener(this);
            mColorLayout.setOnClickListener(this);
            mConfirm.setOnClickListener(this);
            mCancel.setOnClickListener(this);
            mImageVer.setSelected(true);
            mImageHiz.setSelected(false);
            mBitmapUtils = new BitmapUtils(getContext());
            mBitmapUtils.configDefaultAutoRotation(true);
            mBitmapUtils.configDefaultLoadFailedImage(R.drawable.loading_fail_img);
            mBitmapUtils.configDefaultLoadingImage(R.drawable.loading_img);
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.layout_hiz:
                    mImageVer.setSelected(false);
                    mImageHiz.setSelected(true);
                    mIsVer = false;
                    break;
                case R.id.layout_ver:
                    mImageVer.setSelected(true);
                    mImageHiz.setSelected(false);
                    mIsVer = true;
                    break;
                case R.id.btn_cancel:
                    dismiss();
                    break;
                case R.id.btn_confirm:
                    submit();
                    break;
                case R.id.color_layout:
                    collpseLayout(mMenuContent);
                    break;
                case R.id.background_image:
                    changeBgImage();
                    break;
            }
        }

        private void changeBgImage() {

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.putExtra("crop", true);
            intent.putExtra("return-data", true);
            if (mHomeActivity != null)
                mHomeActivity.startActivityWithCallback(intent, new ResultActivityListener() {

                    @Override
                    public void onResult(int requestCode, int resultCode, Intent data) {
                        if (data == null) return;
                        Uri uri = data.getData();
                        mBgPath = ToolsUtils.getRealFilePath(getContext(), uri);
                        mBitmapUtils.display(mBgImage, mBgPath);
                    }
                });
        }

        private void submit() {
            String name = mEdit.getText().toString().trim();
            try {
                if (TextUtils.isEmpty(name))
                    throw new Exception("名称不能为空！");
                if (name.length() > 10)
                    throw new Exception("名称不能大于10个字符！");
                if (isNameRepeat(name))
                    throw new Exception("名称不能重复！");
                ModelModule model = new ModelModule(mIsVer ? R.drawable.custom_hiz : R.drawable.custom_or, name, ModelModule.EICON_TYPE.CUSTOM);
                model.setmIsVertical(mIsVer);
                model.setmBackgroundColor(mColor);
                model.setmBackgroundSrc(mBgPath);
                mOnMenuClickListener.onConfirm(model);
                ALog.e(model);
                dismiss();
            } catch (Exception e) {
                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        private void collpseLayout(View view) {
            ScaleAnim anim;
            view.setVisibility(View.VISIBLE);
            if (view.getHeight() == 0) {
                mMenuContent.removeAllViews();
                ColorPickerView pickerView = new ColorPickerView(getContext(), this.getWidth(), this.getHeight());
                pickerView.setOnColorChangedListener(new ColorPickerView.OnColorChangedListener() {
                    @Override
                    public void colorChanged(int color) {
                        mColor = color;
                        mColorLayout.setBLBackground(mColor, true);
                        collpseLayout(mMenuContent);
                    }
                });
                mMenuContent.addView(pickerView);
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
                lp.gravity = Gravity.BOTTOM;
                view.setLayoutParams(lp);
                view.requestLayout();
                anim = new ScaleAnim(view, 0, this.getMeasuredHeight());
            } else {
                anim = new ScaleAnim(view, this.getMeasuredHeight(), 0);
            }
            if (anim == null) return;
            anim.setInterpolator(new AccelerateDecelerateInterpolator());
            anim.setDuration(400);
            view.startAnimation(anim);
        }
    }


    private boolean isNameRepeat(String name) {
        MateModule list = PreferencesTools.getObjList(mContext, Config.ITEM_CONFIG_NAME, MateModule.class, false);
        if (list == null)
            return false;
        for (ModelModule model : list.getmConfigs()) {
            if (model.getmName().equals(name))
                return true;
        }
        return false;
    }

    public interface OnMenuClickListener {
        public void onConfirm(ModelModule model);
    }

}
