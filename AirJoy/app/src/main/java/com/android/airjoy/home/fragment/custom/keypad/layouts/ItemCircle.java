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
import com.android.airjoy.home.fragment.custom.config.ModelItemCircle;
import com.android.airjoy.home.fragment.custom.keypad.core.ItemLayoutBase;
import com.android.airjoy.home.fragment.custom.keypad.menu.IOnSelectClickListener;
import com.android.airjoy.home.fragment.custom.keypad.menu.MenuItemViewBase;
import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by AddBean on 2016/3/21.
 */
public class ItemCircle extends ItemLayoutBase {
    public ItemCircle(Context context, ModelItem mate) {
        super(context, mate);
    }

    private static final int DEF_COLOR = 0xFF000000;
    private static final int DEF_WIDTH = 2;
    private static final int DEF_ALPHA = 100;
    private int DP = 1;
    private ModelItemCircle mModelItemCorner;


    @Override
    public void setPosition(Point position) {

    }

    @Override
    public void setMatrix(float[] scalex) {

    }

    @Override
    public MenuItemViewBase getMenuView() {
        return new CustomCircleMenu(getContext());
    }

    @Override
    protected void initView(int mWidth, int mHeigh) {
        DP = ToolsUtils.dpConvertToPx(getContext(), 1);
        mModelItemCorner = new Gson().fromJson(getmModelItem().getmConfigDetail(), ModelItemCircle.class);
        if (mModelItemCorner == null) {
            mModelItemCorner = new ModelItemCircle();
            mModelItemCorner.setmColor(DEF_COLOR);
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
        drawCircle(canvas);
    }

    private void drawCircle(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(mModelItemCorner.ismIsFill() ? Paint.Style.FILL : Paint.Style.STROKE);
        paint.setColor(mModelItemCorner.getmColor());
        paint.setAlpha(mModelItemCorner.getmAlpha());
        int width = mModelItemCorner.getmStrickWidth() * DP;
        paint.setStrokeWidth(width);
        RectF rect = new RectF();
        rect.set(width / 2, width, getMeasuredWidth() - width / 2, getMeasuredHeight() - width / 2);
        int r = Math.min(getMeasuredWidth(), getMeasuredHeight())/2;
        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, r - mModelItemCorner.getmStrickWidth() * DP / 2, paint);
    }

    public class CustomCircleMenu extends MenuItemViewBase {
        private Context mContext;
        private ViewGroup mView;

        public class ViewHolder {
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

        public CustomCircleMenu(Context context) {
            super(context);
            if (mView != null)
                ViewUtils.inject(mViewHodler, mView);
            setupView();
        }

        private void setupView() {
            mViewHodler.check_isfill.setSelected(mModelItemCorner.ismIsFill());
            mViewHodler.alpha_seek_bar.setMaxCount(255);
            mViewHodler.width_seek_bar.setMaxCount(50);
            mViewHodler.alpha_seek_bar.setCurrentCount(mModelItemCorner.getmAlpha());
            mViewHodler.width_seek_bar.setCurrentCount(mModelItemCorner.getmStrickWidth());
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
            mView = (ViewGroup) mInflater.inflate(R.layout.item_menu_circle, null);
            return mView;
        }

        @Override
        protected void submit() {
            super.submit();
            mModelItemCorner.setmAlpha((int) mViewHodler.alpha_seek_bar.getCurrentCount());
            mModelItemCorner.setmStrickWidth((int) mViewHodler.width_seek_bar.getCurrentCount());
            saveDate();
            upadateUi();
        }
    }

    private void upadateUi() {
        postInvalidate();
    }

    public static ItemLayoutBase getInstance(Context context, ModelItem mate) {
        return new ItemCircle(context,mate);
    }

    public static String getType() {
        return "ItemCircle";
    }

    public static String getNick() {
        return "圆形";
    }
}
