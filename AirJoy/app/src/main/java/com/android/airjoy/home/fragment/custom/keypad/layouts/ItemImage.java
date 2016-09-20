package com.android.airjoy.home.fragment.custom.keypad.layouts;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.addbean.autils.tools.ToolsUtils;
import com.addbean.autils.utils.ALog;
import com.addbean.autils.utils.ResultActivityListener;
import com.android.airjoy.R;
import com.android.airjoy.home.fragment.custom.config.ModelItem;
import com.android.airjoy.home.fragment.custom.config.ModelItemImage;
import com.android.airjoy.home.fragment.custom.keypad.core.ItemLayoutBase;
import com.android.airjoy.home.fragment.custom.keypad.menu.MenuItemViewBase;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;

/**
 * Created by AddBean on 2016/3/22.
 */
public class ItemImage extends ItemLayoutBase {
    private CustomImage mCustomImage;
    private ModelItemImage mModelItemImage;
    private int mWidth, mHeigh;
    BitmapUtils mBitmapUtils;

    public ItemImage(Context context, ModelItem mate) {
        super(context, mate);
    }

    @Override
    protected void initView(int mWidth, int mHeigh) {
        this.mWidth = mWidth;
        this.mHeigh = mHeigh;
        mBitmapUtils = new BitmapUtils(getContext());
        mBitmapUtils.configDefaultAutoRotation(true);
        mBitmapUtils.configDefaultLoadFailedImage(R.drawable.loading_fail_img);
        mBitmapUtils.configDefaultLoadingImage(R.drawable.loading_img);
        mCustomImage = new CustomImage(getContext());
        upadteUi();
        this.addView(mCustomImage);
    }

    public void upadteUi() {
        Gson gson = new Gson();
        mModelItemImage = gson.fromJson(getmModelItem().getmConfigDetail(), ModelItemImage.class);
        if (mModelItemImage == null) mModelItemImage = new ModelItemImage();
        if (mModelItemImage.getmImageSrc() != null) {
            mBitmapUtils.display(mCustomImage, mModelItemImage.getmImageSrc());
        } else {
            mCustomImage.setImageResource(R.drawable.loading_img);
        }
        mCustomImage.setScaleType(ImageView.ScaleType.FIT_XY);
    }


    @Override
    public void setPosition(Point position) {

    }

    @Override
    public void setMatrix(float[] scalex) {

    }

    @Override
    public MenuItemViewBase getMenuView() {
        return new ImageMenuView(getContext());
    }


    public class CustomImage extends ImageView {

        public CustomImage(Context context) {
            super(context);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public static ItemLayoutBase getInstance(Context context, ModelItem mate) {
        return new ItemImage(context, mate);
    }

    public void saveDate() {
        Gson gson = new Gson();
        String json = gson.toJson(mModelItemImage);
        getmModelItem().setmConfigDetail(json);
    }

    public class ImageMenuView extends MenuItemViewBase {
        private String path;

        public ImageMenuView(Context context) {
            super(context);
        }

        @Override
        protected View getMenuContentView() {
            path = null;
            LinearLayout ll = new LinearLayout(getContext());
            ll.setOrientation(VERTICAL);
            ll.setGravity(Gravity.CENTER);
            final ImageView iv = new ImageView(getContext());
            int DP = ToolsUtils.dpConvertToPx(getContext(), 1);
            iv.setPadding(16 * DP, 16 * DP, 16 * DP, 16 * DP);
            iv.setVisibility(GONE);
            iv.setLayoutParams(new LinearLayout.LayoutParams(100 * DP, 100 * DP));
            final TextView t1 = new TextView(getContext());
            t1.setText("选择背景");
            t1.setGravity(Gravity.CENTER);
            t1.setTextColor(getResources().getColor(R.color.color_gran_dark));
            t1.setTextSize(14);
            t1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    intent.putExtra("crop", true);
                    intent.putExtra("return-data", true);
                    startActivity(intent, new ResultActivityListener() {

                        @Override
                        public void onResult(int requestCode, int resultCode, Intent data) {
                            if (data == null) return;
                            Uri uri = data.getData();
                            iv.setVisibility(VISIBLE);
                            path = ToolsUtils.getRealFilePath(getContext(), uri);
                            mBitmapUtils.display(iv, path);
                        }
                    });
                }
            });

            ll.addView(t1);
            ll.addView(iv);
            return ll;
        }

        @Override
        public void submit() {
            super.submit();
            if (path == null) return;
            mModelItemImage.setmImageSrc(path);
            saveDate();
            upadteUi();
            ALog.e(path);
        }
    }

    public static String getType() {
        return "ItemImage";
    }

    public static String getNick() {
        return "图形";
    }

}
