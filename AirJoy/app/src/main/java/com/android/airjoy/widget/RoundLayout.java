package com.android.airjoy.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.addbean.autils.tools.ToolsUtils;
import com.android.airjoy.R;


/**
 * Created by Administrator on 2015/12/7.
 */
public class RoundLayout extends LinearLayout {
    private Context mContext;
    private Boolean mIsFill = false;
    private int mChildColor = -1;
    private int mColor;
    private float mWidth;

    public RoundLayout(Context context) {
        super(context);
        this.mContext = context;
    }

    public RoundLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initAttrs(attrs);
    }

    public RoundLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;

        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray attrsArray = mContext.obtainStyledAttributes(attrs,
                R.styleable.RoundLayout, 0, 0);
        mIsFill = attrsArray.getBoolean(
                R.styleable.RoundLayout_isFill, true);
        mColor = attrsArray.getColor(
                R.styleable.RoundLayout_defaultColor, mContext.getResources().getColor(R.color.white));
        mChildColor = attrsArray.getColor(
                R.styleable.RoundLayout_childTextViewColor, mContext.getResources().getColor(R.color.color_black_2));
        mWidth = attrsArray.getDimension(
                R.styleable.RoundLayout_lineWidth, (float) (ToolsUtils.dpConvertToPx(mContext, 3) / 2));
    }


    public void setmIsFill(Boolean mIsFill) {
        this.mIsFill = mIsFill;
    }

    public Boolean getmIsFill() {
        return this.mIsFill;
    }

    public void setmChildColor(int mChildColor) {
        this.mChildColor = mChildColor;
    }

    public void setmColor(int mColor) {
        this.mColor = mColor;
    }


    public void setmWidth(float mWidth) {
        this.mWidth = mWidth;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mChildColor > 0)
            setBLChildTextColor(mChildColor);
    }

    private void drawBackground(Canvas canvas) {
        Paint paint = new Paint();
        if (!mIsFill)
            paint.setStyle(Paint.Style.STROKE);
        paint.setColor(mColor);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(mWidth);
        RectF rectF = new RectF();
        rectF.set(mWidth / 2, mWidth / 2, getWidth() - mWidth / 2, getHeight() - mWidth / 2);
        canvas.drawRoundRect(rectF, getHeight() / 2, getHeight() / 2, paint);
    }

    public void setBLBackground(int color, Boolean isFill) {
        this.mColor = color;
        this.mIsFill = isFill;
        invalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        drawBackground(canvas);
        super.dispatchDraw(canvas);
    }

    /**
     * 设置所有子textview元素颜色
     */
    public void setBLChildTextColor(final int color) {
        traverseViewGroup(this, new IGetView() {
            @Override
            public void getView(View view) {
                if (view.getClass().equals(TextView.class)) {
                    ((TextView) view).setTextColor(color);
                }
            }
        });
    }

    /**
     * 遍历viewgroup；
     */
    public void traverseViewGroup(ViewGroup root, IGetView getView) {
        final int childCount = root.getChildCount();
        for (int i = 0; i < childCount; ++i) {
            final View child = root.getChildAt(i);
            if (child instanceof ViewGroup) {
                traverseViewGroup((ViewGroup) child, getView);
            } else {//找到末节点，调用回调
                getView.getView(child);
            }
        }
    }

    public interface IGetView {
        public void getView(View view);
    }
}
