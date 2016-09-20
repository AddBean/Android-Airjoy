package com.android.airjoy.app.pcfile.asynList;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.android.airjoy.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 异步任务listview适配器；
 *
 * @author 贾豆
 *
 */
public class AsyncTaskAdapter extends BaseAdapter {
	/** 每次只执行一个任务的线程池 */
	private static ExecutorService singleTaskExecutor = null;
	static {
		singleTaskExecutor = Executors.newSingleThreadExecutor();// 每次只执行一个线程任务的线程池
	};

	private Context mContext;

	private LayoutInflater mFactory;

	private int mTaskCount;

	private List<DownloadTask> mTaskList = null;

	public AsyncTaskAdapter(Context context, int taskCount) {
		mContext = context;
		mFactory = LayoutInflater.from(mContext);
		mTaskCount = taskCount;
		mTaskList = new ArrayList<DownloadTask>(taskCount);
	}

	@Override
	public int getCount() {
		return mTaskCount;
	}

	@Override
	public Object getItem(int position) {
		return mTaskList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mFactory.inflate(R.layout.app_pc_download_list, null);
		}
		return convertView;
	}

}