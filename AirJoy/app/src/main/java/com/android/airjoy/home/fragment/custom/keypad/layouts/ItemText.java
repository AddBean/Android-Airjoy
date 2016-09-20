package com.android.airjoy.home.fragment.custom.keypad.layouts;

import android.content.Context;
import android.graphics.Point;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.addbean.autils.tools.ToolsUtils;
import com.addbean.autils.utils.ALog;
import com.android.airjoy.R;
import com.android.airjoy.home.fragment.custom.config.ModelItemText;
import com.android.airjoy.home.fragment.custom.config.SelectorModel;
import com.android.airjoy.home.fragment.custom.keypad.core.ItemLayoutBase;
import com.android.airjoy.home.fragment.custom.config.ModelItem;
import com.android.airjoy.home.fragment.custom.keypad.menu.IOnSelectClickListener;
import com.android.airjoy.home.fragment.custom.keypad.menu.MenuItemViewBase;
import com.android.airjoy.widget.ColorPickerView;
import com.android.airjoy.widget.SpringProgressView;
import com.android.airjoy.widget.anim.ScaleAnim;
import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by AddBean on 2016/3/22.
 */
public class ItemText extends ItemLayoutBase {
    private CustomText mText;
    private int DP = 1;
    private ModelItemText mModelItemText;
    private final int MAX_TEXT_SIZE = 50;
    private final int TEXT_DEF_SIZE = 16;
    private CustomMenu mCustomMenu;

    public ItemText(Context context, ModelItem mate) {
        super(context, mate);
    }


    public CustomText getmText() {
        return mText;
    }


    @Override
    protected void initView(int mWidth, int mHeigh) {
        updateWidget();
        this.addView(mText);
    }

    private void updateWidget() {
        mModelItemText = new Gson().fromJson(getmModelItem().getmConfigDetail(), ModelItemText.class);
        if (mText == null)
            mText = new CustomText(getContext());
        mText.setPadding(4 * DP, 4 * DP, 4 * DP, 4 * DP);
        if (mModelItemText == null) {
            mText.setGravity(Gravity.CENTER);
            mText.setTextColor(getResources().getColor(R.color.white));
            mText.setTextSize(TEXT_DEF_SIZE);
            mText.setText("点击右上角编辑");
            mModelItemText = new ModelItemText();
            mModelItemText.setmTextColor(getResources().getColor(R.color.white));
            mModelItemText.setmTextSize(TEXT_DEF_SIZE);
            mModelItemText.setmText("点击右上角编辑");
            saveDate();
        } else {
            if (mModelItemText.getmGravity() == 0) {//左对齐
                mText.setGravity(Gravity.LEFT);
            } else if (mModelItemText.getmGravity() == 1) {//居中
                mText.setGravity(Gravity.CENTER);
            } else if (mModelItemText.getmGravity() == 2) {//右对齐;
                mText.setGravity(Gravity.RIGHT);
            }
            if (mModelItemText.getmTextColor() != 0) {
                mText.setTextColor(mModelItemText.getmTextColor());
            }
            if (mModelItemText.getmTextSize() > 0) {
                mText.setTextSize(mModelItemText.getmTextSize());
            }
            mText.setText(mModelItemText.getmText());
        }
        ALog.e(mModelItemText);
    }

    @Override
    public void setPosition(Point position) {

    }

    @Override
    public void setMatrix(float[] scalex) {

    }

    public static ItemLayoutBase getInstance(Context context, ModelItem mate) {
        return new ItemText(context, mate);
    }

    public class CustomText extends TextView {

        public CustomText(Context context) {
            super(context);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            this.setText(this.getText().toString());
        }
    }

    @Override
    public MenuItemViewBase getMenuView() {
        mCustomMenu = new CustomMenu(getContext());
        return mCustomMenu;
    }

    public class CustomMenu extends MenuItemViewBase {
        private CustomMenuContentView mCustomMenuContentView;

        public CustomMenu(Context context) {
            super(context);
        }

        @Override
        protected View getMenuContentView() {
            if (mCustomMenuContentView == null)
                mCustomMenuContentView = new CustomMenuContentView(getContext());
            return mCustomMenuContentView;
        }

        @Override
        protected void submit() {
            mCustomMenuContentView.submit();
            super.submit();
        }
    }

    public void saveDate() {
        Gson gson = new Gson();
        String json = gson.toJson(mModelItemText);
        getmModelItem().setmConfigDetail(json);
    }

    public class CustomMenuContentView extends FrameLayout {
        private Context mContext;
        private LayoutInflater mInflater;
        private View mView;
        private int mColor = 0;

        public class ViewHolder {

            @ViewInject(R.id.edit_content)
            private EditText edit_content;
            @ViewInject(R.id.seekBar)
            private SpringProgressView seekBar;
            @ViewInject(R.id.color_layout)
            private com.android.airjoy.widget.RoundLayout color_layout;
            @ViewInject(R.id.text_left)
            private TextView text_left;
            @ViewInject(R.id.text_mid)
            private TextView text_mid;
            @ViewInject(R.id.text_right)
            private TextView text_right;
            @ViewInject(R.id.menu_color_content)
            private LinearLayout menu_color_content;

        }

        private ViewHolder mViewHolder = new ViewHolder();

        public CustomMenuContentView(Context context) {
            super(context);
            this.mContext = context;
            DP = ToolsUtils.dpConvertToPx(getContext(), 1);
            if (mView == null) {
                mInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
                mView = mInflater.inflate(R.layout.menu_custom_content_view, null);
                this.addView(mView);
            }
            ViewUtils.inject(mViewHolder, mView);
            initMenuView();
            bindEvent();
        }

        private void clearAllSelect() {
            mViewHolder.text_left.setSelected(false);
            mViewHolder.text_mid.setSelected(false);
            mViewHolder.text_right.setSelected(false);
        }

        private void initMenuView() {
            mViewHolder.seekBar.setMaxCount(MAX_TEXT_SIZE);
            if (mModelItemText != null) {
                mColor=mModelItemText.getmTextColor();
                if (mModelItemText.getmGravity() == 0) {//左对齐
                    clearAllSelect();
                    mViewHolder.text_left.setSelected(true);
                } else if (mModelItemText.getmGravity() == 1) {//居中
                    clearAllSelect();
                    mViewHolder.text_mid.setSelected(true);
                } else if (mModelItemText.getmGravity() == 2) {//右对齐;
                    clearAllSelect();
                    mViewHolder.text_right.setSelected(true);
                }
                if (mModelItemText.getmTextColor() != 0) {
                    mViewHolder.color_layout.setBLBackground(mModelItemText.getmTextColor(), true);
                }
                if (mModelItemText.getmTextSize() > 0) {
                    mViewHolder.seekBar.setCurrentCount(mModelItemText.getmTextSize() );
                } else {
                    mViewHolder.seekBar.setCurrentCount(TEXT_DEF_SIZE );
                }
                if (!TextUtils.isEmpty(mModelItemText.getmText())) {
                    mViewHolder.edit_content.setText(mModelItemText.getmText());
                }
            }
        }

        private void bindEvent() {
            mViewHolder.color_layout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCustomMenu != null)
                        mCustomMenu.showColorMenu(new IOnSelectClickListener() {
                            @Override
                            public void onSelected(Object data) {
                                mColor = (Integer) data;
                                mViewHolder.color_layout.setBLBackground(mColor, true);
                            }
                        });
                }
            });
            mViewHolder.text_mid.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearAllSelect();
                    mViewHolder.text_mid.setSelected(true);
                    mModelItemText.setmGravity(1);
                }
            });
            mViewHolder.text_right.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearAllSelect();
                    mViewHolder.text_right.setSelected(true);
                    mModelItemText.setmGravity(2);
                }
            });
            mViewHolder.text_left.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearAllSelect();
                    mViewHolder.text_left.setSelected(true);
                    mModelItemText.setmGravity(0);
                }
            });
        }


        public void submit() {
            if (TextUtils.isEmpty(mViewHolder.edit_content.getText().toString())) {
                Toast.makeText(getContext(), "编辑内容不能为空", Toast.LENGTH_SHORT).show();
            } else {
                mModelItemText.setmText(mViewHolder.edit_content.getText().toString());
            }
            mModelItemText.setmTextColor(mColor);
            mModelItemText.setmTextSize((int) (mViewHolder.seekBar.getCurrentCount()));
            saveDate();
            ALog.e(mModelItemText);
            updateWidget();
        }
    }


    public static String getType() {
        return "ItemText";
    }

    public static String getNick() {
        return "文字";
    }
}
