package com.android.airjoy.home.fragment.custom.keypad.layouts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.addbean.autils.tools.ToolsUtils;
import com.android.airjoy.R;
import com.android.airjoy.home.fragment.custom.config.ModelItem;
import com.android.airjoy.home.fragment.custom.config.ModelItemCorner;
import com.android.airjoy.home.fragment.custom.keypad.core.ItemLayoutBase;
import com.android.airjoy.home.fragment.custom.keypad.menu.IOnSelectClickListener;
import com.android.airjoy.home.fragment.custom.keypad.menu.MenuItemViewBase;
import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by AddBean on 2016/3/22.
 */
public class ItemCorner extends ItemLayoutBase {
    private static final int DEF_COLOR = 0xFF000000;
    private static final int DEF_SIZE = 12;
    private static final int DEF_WIDTH = 2;
    private static final int DEF_ALPHA = 100;
    private int DP = 1;
    private ModelItemCorner mModelItemCorner;

    public ItemCorner(Context context, ModelItem mate) {
        super(context, mate);
    }

    public static ItemLayoutBase getInstance(Context context, ModelItem mate) {
        return new ItemCorner(context, mate);
    }

    @Override
    public MenuItemViewBase getMenuView() {
        return new CustomCornerMenu(getContext());
    }

    @Override
    protected void initView(int mWidth, int mHeigh) {
        DP = ToolsUtils.dpConvertToPx(getContext(), 1);
        mModelItemCorner = new Gson().fromJson(getmModelItem().getmConfigDetail(), ModelItemCorner.class);
        if (mModelItemCorner == null) {
            mModelItemCorner = new ModelItemCorner();
            mModelItemCorner.setmColor(DEF_COLOR);
            mModelItemCorner.setmCornerSize(DEF_SIZE * DP);
            mModelItemCorner.setmStrickWidth(DEF_WIDTH * DP);
            mModelItemCorner.setmAlpha(DEF_ALPHA);
            mModelItemCorner.setmIsFill(true);
        }
        upadateUi();
    }

    public void saveDate() {
        Gson gson = new Gson();
        String json = gson.toJson(mModelItemCorner);
        getmModelItem().setmConfigDetail(json);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        drawCroner(canvas);
    }

    private void drawCroner(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(mModelItemCorner.ismIsFill() ? Paint.Style.FILL : Paint.Style.STROKE);
        paint.setColor(mModelItemCorner.getmColor());
        paint.setAlpha(mModelItemCorner.getmAlpha());
        int width = mModelItemCorner.getmStrickWidth() * DP;
        paint.setStrokeWidth(width);
        RectF rect = new RectF();
        rect.set(width / 2, width, getMeasuredWidth() - width / 2, getMeasuredHeight() - width / 2);
        canvas.drawRoundRect(rect, mModelItemCorner.getmCornerSize(), mModelItemCorner.getmCornerSize(), paint);
    }

    public class CustomCornerMenu extends MenuItemViewBase {
        private Context mContext;
        private ViewGroup mView;

        public class ViewHolder {

            @ViewInject(R.id.size_seek_bar)
            private com.android.airjoy.widget.SpringProgressView size_seek_bar;
            @ViewInject(R.id.width_seek_bar)
            private com.android.airjoy.widget.SpringProgressView width_seek_bar;
            @ViewInject(R.id.alpha_seek_bar)
            private com.android.airjoy.widget.SpringProgressView alpha_seek_bar;
            @ViewInject(R.id.color_layout)
            private com.android.airjoy.widget.RoundLayout color_layout;
            @ViewInject(R.id.layout_ver)
            private LinearLayout layout_ver;
            @ViewInject(R.id.check_isfill)
            private ImageView check_isfill;

        }

        private ViewHolder mViewHodler = new ViewHolder();

        public CustomCornerMenu(Context context) {
            super(context);
            if (mView != null)
                ViewUtils.inject(mViewHodler, mView);
            setupView();
        }

        private void setupView() {
            mViewHodler.check_isfill.setSelected(mModelItemCorner.ismIsFill());
            mViewHodler.alpha_seek_bar.setMaxCount(255);
            mViewHodler.width_seek_bar.setMaxCount(50);
            mViewHodler.size_seek_bar.setMaxCount(Math.min(getmModelItem().getmHeight(), getmModelItem().getmWidth()));
            mViewHodler.alpha_seek_bar.setCurrentCount(mModelItemCorner.getmAlpha());
            mViewHodler.width_seek_bar.setCurrentCount(mModelItemCorner.getmStrickWidth());
            mViewHodler.size_seek_bar.setCurrentCount(mModelItemCorner.getmCornerSize());
            mViewHodler.color_layout.setBLBackground(mModelItemCorner.getmColor(), true);
            mViewHodler.color_layout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    showColorMenu(new IOnSelectClickListener() {
                        @Override
                        public void onSelected(Object data) {
                            mModelItemCorner.setmColor((Integer) data);
                            mViewHodler.color_layout.setBLBackground(mModelItemCorner.getmColor(), true);
                        }
                    });
                }
            });
            mViewHodler.check_isfill.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHodler.check_isfill.setSelected(!mViewHodler.check_isfill.isSelected());
                    mModelItemCorner.setmIsFill(mViewHodler.check_isfill.isSelected());
                }
            });
        }

        @Override
        protected View getMenuContentView() {
            mContext = getContext();
            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
            mView = (ViewGroup) mInflater.inflate(R.layout.item_menu_corner, null);
            return mView;
        }

        @Override
        protected void submit() {
            super.submit();
            mModelItemCorner.setmAlpha((int) mViewHodler.alpha_seek_bar.getCurrentCount());
            mModelItemCorner.setmStrickWidth((int) mViewHodler.width_seek_bar.getCurrentCount());
            mModelItemCorner.setmCornerSize((int) mViewHodler.size_seek_bar.getCurrentCount());
            saveDate();
            upadateUi();
        }
    }

    private void upadateUi() {
        postInvalidate();
    }


    @Override
    public void setPosition(Point position) {

    }

    @Override
    public void setMatrix(float[] scalex) {

    }

    public static String getType() {
        return "ItemCorner";
    }

    public static String getNick() {
        return "圆角矩形";
    }
}
