package com.android.airjoy.home.fragment;

import android.content.Context;
import android.widget.TextView;

import com.addbean.aviews.views.dynamic_fragment.ADynamicBaseSubFragment;
import com.addbean.aviews.views.dynamic_fragment.TabTitleView;
import com.android.airjoy.R;

/**
 * Created by AddBean on 2016/3/20.
 */
public class TabView extends TabTitleView {
    private TextView mTitle;

    public TabView(Context context, Object tag) {
        super(context, tag);
    }

    @Override
    protected void initView() {
        mTitle = (TextView) findViewById(R.id.title_text);
        mTitle.setText((String) getmTag());
    }

    @Override
    protected int getContentView() {
        return R.layout.item_title_tab;
    }

    @Override
    protected void onPageSelected(Boolean isSelected, TabTitleView tag, ADynamicBaseSubFragment fragment) {
        if (!isSelected) {
            if (mTitle != null) {
                mTitle.setAlpha(0.6f);
            }
        } else {
            if (mTitle != null) {
                mTitle.setAlpha(0.8f);
            }
        }
    }
}
