package com.android.airjoy.home.fragment.custom.keypad.menu;

/**
 * Created by AddBean on 2016/3/21.
 */

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.addbean.autils.tools.ToolsUtils;
import com.android.airjoy.R;
import com.android.airjoy.home.fragment.custom.config.ModelItem;
import com.android.airjoy.home.fragment.custom.config.SelectorModel;
import com.android.airjoy.widget.anim.ScaleAnim;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 自定义菜单主视图；
 */
public class MenuAddView extends RelativeLayout implements View.OnClickListener {
    private Context mContext;
    private LayoutInflater mInflater;
    private View mView;
    private boolean mVerEnable = true;
    private int DP = 0;
    private IOnMenuClickListener mOnMenuClickListener;
    private ModelItem mModelItem = new ModelItem();

    public class ViewHolder {

        @ViewInject(R.id.menu_content)
        private LinearLayout menu_content;
        @ViewInject(R.id.layout_cmd)
        private LinearLayout layout_cmd;
        @ViewInject(R.id.text_cmd_msg)
        private TextView text_cmd_msg;
        @ViewInject(R.id.layout_type)
        private LinearLayout layout_type;
        @ViewInject(R.id.text_type_msg)
        private TextView text_type_msg;
        @ViewInject(R.id.layout_anim)
        private LinearLayout layout_anim;
        @ViewInject(R.id.text_anim_msg)
        private TextView text_anim_msg;
        @ViewInject(R.id.select_icon_vib)
        private ImageView select_icon_vib;
        @ViewInject(R.id.btn_cancel)
        private TextView btn_cancel;
        @ViewInject(R.id.btn_confirm)
        private TextView btn_confirm;

    }

    private ViewHolder mViewHolder = new ViewHolder();

    public MenuAddView(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    private void initView() {
        this.requestFocus();
        DP = ToolsUtils.dpConvertToPx(getContext(), 1);
        if (mView == null) {
            mInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
            mView = mInflater.inflate(R.layout.menu_dialog, null);
            this.addView(mView);
        }
        ViewUtils.inject(mViewHolder, mView);
        mViewHolder.btn_confirm.setOnClickListener(this);
        mViewHolder.btn_cancel.setOnClickListener(this);
        mViewHolder.layout_cmd.setOnClickListener(this);
        mViewHolder.layout_type.setOnClickListener(this);
        mViewHolder.layout_anim.setOnClickListener(this);
        mViewHolder.select_icon_vib.setOnClickListener(this);
        mViewHolder.select_icon_vib.setSelected(true);
    }

    public void setmOnMenuClickListener(IOnMenuClickListener mOnMenuClickListener) {
        this.mOnMenuClickListener = mOnMenuClickListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_icon_vib:
                mVerEnable = !mViewHolder.select_icon_vib.isSelected();
                mViewHolder.select_icon_vib.setSelected(mVerEnable);
                break;
            case R.id.btn_cancel:
                cancel();
                break;
            case R.id.btn_confirm:
                submit();
                break;
            case R.id.layout_cmd:
                showCmdMenu();
                break;
            case R.id.layout_type:
                showTypeMenu();
                break;
            case R.id.layout_anim:
                showAnimMenu();
                break;
        }
    }


    private void showCmdMenu() {
        mViewHolder.menu_content.removeAllViews();
        if (mViewHolder.menu_content.getChildCount() == 0)
            mViewHolder.menu_content.addView(new SelectorGridView(getContext(),SelectorModel.getKeyList(), new IOnSelectClickListener() {
                @Override
                public void onSelected(Object data) {
                    mViewHolder.text_cmd_msg.setText(((SelectorModel) data).getmName());
                    mModelItem.setmCmdName(((SelectorModel) data).getmName());
                    mModelItem.setmCmd(((SelectorModel) data).getmCode());
                    startCollpseAnim(mViewHolder.menu_content, false);
                }
            }));
        startCollpseAnim(mViewHolder.menu_content, true);
    }

    private void showTypeMenu() {
        mViewHolder.menu_content.removeAllViews();
        if (mViewHolder.menu_content.getChildCount() == 0)
            mViewHolder.menu_content.addView(new SelectorGridView(getContext(), SelectorModel.getTypeList(),new IOnSelectClickListener() {
                @Override
                public void onSelected(Object data) {
                    mViewHolder.text_type_msg.setText(((SelectorModel) data).getmName());
                    mModelItem.setmType(((SelectorModel) data).getmCode());
                    startCollpseAnim(mViewHolder.menu_content, false);
                }
            }));
        startCollpseAnim(mViewHolder.menu_content, true);
    }

    private void showAnimMenu() {
        mViewHolder.menu_content.removeAllViews();
        if (mViewHolder.menu_content.getChildCount() == 0)
            mViewHolder.menu_content.addView(new SelectorGridView(getContext(), SelectorModel.getAnimList(),new IOnSelectClickListener() {
                @Override
                public void onSelected(Object data) {
                    mViewHolder.text_anim_msg.setText(((SelectorModel) data).getmName());
                    mModelItem.setmAnim(((SelectorModel) data).getmCode());
                    startCollpseAnim(mViewHolder.menu_content, false);
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
        anim.setDuration(500);
        view.startAnimation(anim);
    }

    private void submit() {
        try {
            checkDate();
            if (mOnMenuClickListener != null)
                mOnMenuClickListener.onDialogConfirm(mModelItem);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void checkDate() throws Exception {
        mModelItem.setmIsVibrate(mVerEnable);
        if (TextUtils.isEmpty(mModelItem.getmCmd()))
            throw new Exception("未选择命令");
        if (TextUtils.isEmpty(mModelItem.getmType()))
            throw new Exception("未选择按键类型");
    }

    private void cancel() {
        if (mOnMenuClickListener != null)
            mOnMenuClickListener.onDialogCancel();
    }
}