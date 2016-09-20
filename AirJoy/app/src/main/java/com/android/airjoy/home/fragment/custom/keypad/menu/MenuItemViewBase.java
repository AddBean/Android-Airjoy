package com.android.airjoy.home.fragment.custom.keypad.menu;

/**
 * Created by AddBean on 2016/3/21.
 */

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.addbean.autils.tools.ToolsUtils;
import com.addbean.autils.utils.ALog;
import com.android.airjoy.R;
import com.android.airjoy.home.fragment.custom.config.SelectorModel;
import com.android.airjoy.widget.ColorPickerView;
import com.android.airjoy.widget.anim.ScaleAnim;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.security.PublicKey;
import java.util.List;

/**
 * 自定义菜单主视图；
 */
public abstract class MenuItemViewBase extends RelativeLayout implements View.OnClickListener {
    private Context mContext;
    private LayoutInflater mInflater;
    private View mView;
    private boolean mIsVer = true;
    private int DP = 0;
    private IOnMenuClickListener mOnMenuClickListener;

    public class ViewHolder {

        @ViewInject(R.id.menu_content_view)
        private LinearLayout menu_content_view;
        @ViewInject(R.id.btn_cancel)
        private TextView btn_cancel;
        @ViewInject(R.id.btn_confirm)
        private TextView btn_confirm;
        @ViewInject(R.id.menu_content)
        private LinearLayout menu_content;

    }

    private ViewHolder mViewHolder = new ViewHolder();

    public MenuItemViewBase(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    protected void initView() {
        DP = ToolsUtils.dpConvertToPx(getContext(), 1);
        if (mView == null) {
            mInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
            mView = mInflater.inflate(R.layout.menu_base_dialog, null);
            this.addView(mView);
        }
        ViewUtils.inject(mViewHolder, mView);
        mViewHolder.menu_content_view.addView(getMenuContentView());
        mViewHolder.btn_confirm.setOnClickListener(this);
        mViewHolder.btn_cancel.setOnClickListener(this);
    }

    protected abstract View getMenuContentView();

    public void setmOnMenuClickListener(IOnMenuClickListener mOnMenuClickListener) {
        this.mOnMenuClickListener = mOnMenuClickListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                cancel();
                break;
            case R.id.btn_confirm:
                submit();
                break;
        }
    }

    protected void cancel() {
        if (mOnMenuClickListener != null)
            mOnMenuClickListener.onDialogCancel();
    }

    protected void submit() {
        if (mOnMenuClickListener != null)
            mOnMenuClickListener.onDialogConfirm(null);
        ALog.e("submit");
    }

    public void showColorMenu(final IOnSelectClickListener onSelectClickListener) {
        mViewHolder.menu_content.removeAllViews();
        if (mViewHolder.menu_content.getChildCount() == 0) {
            ColorPickerView pickerView = new ColorPickerView(getContext(), this.getWidth(), this.getHeight());
            mViewHolder.menu_content.addView(pickerView);
            pickerView.setOnColorChangedListener(new ColorPickerView.OnColorChangedListener() {
                @Override
                public void colorChanged(int color) {
                    startCollpseAnim(mViewHolder.menu_content, false);
                    onSelectClickListener.onSelected(color);
                }
            });
        }
        startCollpseAnim(mViewHolder.menu_content, true);
    }

    public void showGridMenu(List<SelectorModel> datas, final IOnSelectClickListener onSelectClickListener) {
        mViewHolder.menu_content.removeAllViews();
        if (mViewHolder.menu_content.getChildCount() == 0)
            mViewHolder.menu_content.addView(new SelectorGridView(getContext(), datas, new IOnSelectClickListener() {
                @Override
                public void onSelected(Object data) {
                    startCollpseAnim(mViewHolder.menu_content, false);
                    onSelectClickListener.onSelected(data);
                }
            }));
        startCollpseAnim(mViewHolder.menu_content, true);
    }

    private void startCollpseAnim(View view, boolean isDrawOut) {
        ScaleAnim anim = null;
        view.setVisibility(View.VISIBLE);
        if (isDrawOut) {
            //高度为0时，动画无法执行。原因尚不可知；
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
        anim.setDuration(300);
        view.startAnimation(anim);
    }
}