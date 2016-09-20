package com.android.airjoy.app.pcfile.asynList;

import android.os.AsyncTask;
import android.os.SystemClock;
/**
 * 下载文件任务类；
 *
 * @author 贾豆
 *
 */
class DownloadTask extends AsyncTask<Void, Integer, Void> {
	private ListItem mTaskItem;

	private String id = "id";

	private String _ip;
	private int _port;
	private String _downloadPath;
	private String _savePath;
	public DownloadTask(ListItem item,String ip,int port,String savePath,String downloadPath) {
		mTaskItem = item;
		this._ip=ip;
		this._port=port;
		this._savePath=savePath;
		this._downloadPath=downloadPath;
	}

	@Override
	protected void onPreExecute() {
		mTaskItem.setTitle(id);
	}

	/**
	 * Overriding methods
	 */
	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	/* 后台运行，此处负责与Pc进行网络通讯*/
	@Override
	protected Void doInBackground(Void... params) {
		int prog = 0;
		while (prog < 101) {
			if (prog > 0 || prog == 0) // 小于70%时，加快进度条更新
			{
				SystemClock.sleep(30);
			}
			publishProgress(prog); // 更新进度条
			prog++;
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		mTaskItem.setProgress(values[0]); // 设置进度
	}
}