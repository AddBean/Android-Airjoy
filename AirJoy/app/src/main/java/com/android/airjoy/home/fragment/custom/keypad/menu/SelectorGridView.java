package com.android.airjoy.home.fragment.custom.keypad.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.addbean.aviews.utils.multiadapter.AdapterHelper;
import com.addbean.aviews.utils.multiadapter.ListItemEx;
import com.addbean.aviews.utils.multiadapter.MultiAdapter;
import com.android.airjoy.R;
import com.android.airjoy.home.fragment.custom.config.SelectorModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AddBean on 2016/3/23.
 */
public class SelectorGridView extends LinearLayout implements AdapterView.OnItemClickListener {
    private Context mContext;
    private View mView;
    private LayoutInflater mInflater;
    private GridView mGrid;
    private MultiAdapter mAdapter;
    private List<ListItemEx> mData = new ArrayList<ListItemEx>();
    private TextView mTitle;
    private IOnSelectClickListener mOnSelectClickListener;
    private List<SelectorModel> mModel;
    public SelectorGridView(Context context,  List<SelectorModel> data,IOnSelectClickListener onSelectClickListener) {
        super(context);
        this.mContext = context;
        mOnSelectClickListener=onSelectClickListener;
        mModel=data;
        initView();
    }

    private void initView() {
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = mInflater.inflate(R.layout.menu_item_add_cmd, null);
        mGrid = (GridView) mView.findViewById(R.id.grid);
        mTitle = (TextView) mView.findViewById(R.id.title);
        this.addView(mView);
        mTitle.setText("选择");
        mAdapter = new MultiAdapter(getContext(), mData, new MultiAdapter.IAdpterListener() {

            @Override
            public void convert(AdapterHelper helper, MultiAdapter.ConvertViewInf data) {
                SelectorModel key = (SelectorModel) data.getData();
                helper.setText(R.id.text, key.getmName());
            }
        });
        mAdapter.addType(R.layout.menu_item_add_cmd_item);
        this.mGrid.setAdapter(mAdapter);
        this.mGrid.setOnItemClickListener(this);
        getData();
    }

    private void getData() {
        mData.clear();
        for (SelectorModel key : mModel) {
            mData.add(new ListItemEx(0, key));
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mOnSelectClickListener.onSelected(mData.get(position).getmData());
    }
}
