package com.android.airjoy.app.pcfile.asynList;

import java.util.ArrayList;

import com.android.airjoy.R;
import com.android.airjoy.app.pcfile.listview.ListAdapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ListView;
import android.widget.RelativeLayout;
/**
 * 下载页面视图；
 *
 * @author 贾豆
 *
 */
public class DownloadView extends RelativeLayout {
	public Context _context;
	ListView listView;
	ArrayList<ListItem> list_GroupItem;
	ListAdapter listGroup;

	/* 构造函数 */
	public DownloadView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this._context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.app_pc_download, this);
		initCustomListView();
	}

	/* 初始化自定义ListView */
	private void initCustomListView() {
		final ListView taskList = (ListView)findViewById(R.id.downloadlist);
		taskList.setAdapter(new AsyncTaskAdapter(this._context, 1));
	}

}
