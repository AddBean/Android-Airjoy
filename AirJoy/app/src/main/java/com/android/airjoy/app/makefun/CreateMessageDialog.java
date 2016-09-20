package com.android.airjoy.app.makefun;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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

import com.addbean.autils.utils.ALog;
import com.addbean.autils.utils.PreferencesTools;
import com.android.airjoy.R;
import com.android.airjoy.constant.Config;
import com.android.airjoy.home.fragment.custom.AddItemDialog;
import com.android.airjoy.home.fragment.custom.config.MateModule;
import com.android.airjoy.home.fragment.custom.config.ModelModule;
import com.android.airjoy.widget.ColorPickerView;
import com.android.airjoy.widget.RoundLayout;
import com.android.airjoy.widget.anim.ScaleAnim;

/**
 * Created by AddBean on 2016/3/20.
 */
public class CreateMessageDialog extends Dialog {
    private OnMenuClickListener mOnMenuClickListener;
    public MenuView mMenuView;
    private Context mContext;

    public CreateMessageDialog(Context context, OnMenuClickListener mOnMenuClickListener) {
        super(context, R.style.menu_dialog);
        this.mContext = context;
        this.mMenuView = new MenuView(context);
        this.setContentView(this.mMenuView);
        this.mOnMenuClickListener = mOnMenuClickListener;
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
        private EditText mEdit2;
        private TextView mTextTips;
        private TextView mConfirm;
        private TextView mCancel;
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
                mView = mInflater.inflate(R.layout.custom_menu_dialog_message, null);
                this.addView(mView);
            }

            mEdit = (EditText) findViewById(R.id.edit_text);
            mEdit2 = (EditText) findViewById(R.id.edit_text_2);
            mTextTips=(TextView)findViewById(R.id.text_tips);
            mCancel = (TextView) findViewById(R.id.btn_cancel);
            mConfirm = (TextView) findViewById(R.id.btn_confirm);
            mConfirm.setOnClickListener(this);
            mCancel.setOnClickListener(this);
            mTextTips.setText("提示：请尽量使用英文，汉字有编码问题");
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_cancel:
                    dismiss();
                    break;
                case R.id.btn_confirm:
                    submit();
                    break;
            }
        }

        private void submit() {
            String title = mEdit.getText().toString().trim();
            String msg = mEdit2.getText().toString().trim();
            try {
                if (TextUtils.isEmpty(title))
                    throw new Exception("提示标题不能为空！");
                if (TextUtils.isEmpty(msg))
                    throw new Exception("提示消息不能为空！");
                mOnMenuClickListener.onConfirm(title,msg);
                dismiss();
            } catch (Exception e) {
                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public interface OnMenuClickListener {
        public void onConfirm(String title,String msg);
    }
}
