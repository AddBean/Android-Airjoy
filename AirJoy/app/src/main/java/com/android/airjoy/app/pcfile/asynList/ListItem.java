package com.android.airjoy.app.pcfile.asynList;

import com.android.airjoy.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 下载显示器；
 *
 * @author 贾豆
 *
 */
public class ListItem extends LinearLayout
{
    private TextView mTitle;

    private ProgressBar mProgress;

    public ListItem(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public ListItem(Context context)
    {
        super(context);
    }

    public void setTitle(String title)
    {
        if (mTitle == null)
        {
            mTitle = (TextView)findViewById(R.id.dlname);
        }
        mTitle.setText(title);
    }

    public void setProgress(int prog)
    {
        if (mProgress == null)
        {
            mProgress = (ProgressBar)findViewById(R.id.task_progress);
        }
        mProgress.setProgress(prog);
    }
}
