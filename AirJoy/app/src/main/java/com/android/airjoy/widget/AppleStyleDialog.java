package com.android.airjoy.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.addbean.autils.tools.ToolsUtils;
import com.addbean.autils.utils.AnimUtils;
import com.android.airjoy.R;

/**
 * Created by AddBean on 2016/1/25.
 */
public class AppleStyleDialog extends Dialog {
    private ContentView mContentView;
    public IOnBtnClickListener mOnBtnClickListener;
    private String mTitle;

    public AppleStyleDialog(Context context, String title, String... msg) {
        super(context, R.style.menu_dialog);
        this.mTitle = title;
        mContentView = new ContentView(context);
        this.setContentView(mContentView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setTitle(title);
        mContentView.setMessage(msg);
        mContentView.mSubmitText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimUtils.ScaleAnim(v);
                if (mOnBtnClickListener != null)
                    mOnBtnClickListener.onSubmit();
            }
        });
        mContentView.mCancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimUtils.ScaleAnim(v);
                if (mOnBtnClickListener != null)
                    mOnBtnClickListener.onCancel();
            }
        });
    }

    /**
     * 设置确认键文字
     *
     * @param text
     */
    public void setSubmitText(String text) {
        mContentView.mSubmitText.setText(text);
    }

    /**
     * 设置取消键文件
     *
     * @param text
     */
    public void setCancelText(String text) {
        mContentView.mCancelText.setText(text);
    }

    /**
     * 设置title;
     *
     * @param title
     */
    public void setTitle(String title) {
        mContentView.mTitleText.setText(title);
    }


    /**
     * 设置点击按钮监听；
     */
    public void setOnBtnClickListener(IOnBtnClickListener onBtnClickListener) {
        this.mOnBtnClickListener = onBtnClickListener;

    }

    /**
     * 内容视图；
     */
    public class ContentView extends LinearLayout {
        public LayoutInflater mInflater;
        public View mView;
        private Context mContext;
        public TextView mTitleText;
        public LinearLayout mMsgLayout;
        public TextView mSubmitText;
        public TextView mCancelText;
        private int DP = 1;

        public ContentView(Context context) {
            super(context);
            this.mContext = context;
            DP = ToolsUtils.dpConvertToPx(mContext, 1);
            initView();
        }


        /**
         * 初始化视图；
         */
        private void initView() {
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = mInflater.inflate(R.layout.dialog_apple_style, null);
            mTitleText = (TextView) mView.findViewById(R.id.dialog_title);
            mMsgLayout = (LinearLayout) mView.findViewById(R.id.dialog_content);
            mSubmitText = (TextView) mView.findViewById(R.id.dialog_btn_submit);
            mCancelText = (TextView) mView.findViewById(R.id.dialog_btn_cancel);
            mMsgLayout.setGravity(Gravity.CENTER_VERTICAL);
            mMsgLayout.setPadding(25 * DP, 0, 16 * DP, 25 * DP);
            setMessage();
            this.addView(mView);

        }

        /**
         * 设置消息；
         *
         * @param msg
         */
        public void setMessage(String... msg) {
            for (String str : msg) {
                View view = getSingleMsgView(str);
                mMsgLayout.addView(view);

            }
        }

        private View getSingleMsgView(String msg) {

            LinearLayout ll = new LinearLayout(mContext);
            ll.setOrientation(HORIZONTAL);

            LinearLayout.LayoutParams dotlp = new LinearLayout.LayoutParams(6 * DP, 6 * DP);
//            dotlp.gravity = Gravity.CENTER_VERTICAL;
            dotlp.setMargins(0, 8 * DP, 0, 0);
            View dot = new View(mContext);
            dot.setBackgroundResource(R.drawable.white_dot);
            dot.setLayoutParams(dotlp);

            LinearLayout.LayoutParams msglp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            msglp.leftMargin = 8 * DP;
            TextView msgView = new TextView(mContext);
            msgView.setLayoutParams(msglp);
            msgView.setGravity(Gravity.LEFT);
            msgView.setText(msg);
            msgView.setTextColor(getResources().getColor(R.color.color_gran));
            msgView.setTextSize(15);

            ll.addView(dot);
            ll.addView(msgView);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 4 * DP, 0, 0);
            ll.setLayoutParams(lp);
            return ll;
        }
    }

    /**
     * 监听点击接口；
     */
    public interface IOnBtnClickListener {
        public void onSubmit();

        public void onCancel();
    }
}
