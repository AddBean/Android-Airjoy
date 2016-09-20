package com.android.airjoy.app.pcfile.net.download;

import java.io.File;
import java.util.concurrent.Executors;

import com.android.airjoy.R;
import com.android.airjoy.app.anim.Rotate3dHelper;
import com.android.airjoy.app.pcfile.AppPcActivity;
import com.android.airjoy.app.pcfile.bean.ConvertValue;
import com.android.airjoy.app.pcfile.net.download.progress.WaveViewProgress;
import com.android.airjoy.constant.Config;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * socket访问pc公共类
 *
 * @author 贾豆
 *
 */
public class DownloadActivity extends Activity {

	public String _savePath = "";
	public String _saveName = "";
	public String TAG = "DownloadActivity";
	public WaveViewProgress _waveViewProgress;
	public Context _context;
	String _filePath;
	String _fileName;
	long _fileSize;
	TextView _fileNameText;
	TextView _sizeInfText;
	ImageView _downloadIcon;

	/* 入口函数 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置无标题
		setContentView(R.layout.app_pc_download_activity);
		this._context = this;

		// 查找控件；
		this._waveViewProgress = (WaveViewProgress) findViewById(R.id.waveViewProgress);
		this._fileNameText = (TextView) findViewById(R.id.fileName);
		this._sizeInfText = (TextView) findViewById(R.id.sizeInf);
		this._downloadIcon = (ImageView) findViewById(R.id.DownloadIcon);

		// 获取Intent传来的参数；
		this._filePath = this.getIntent().getStringExtra("filePath");
		this._fileSize = this.getIntent().getLongExtra("fileSize", 1000);
		this._fileName = this.getIntent().getStringExtra("fileName");
		Log.v(TAG, "FilePath:" + this._filePath + " FileSize:" + this._fileSize);

		// 初始化文件信息显示;
		this._fileNameText.setText("正在下载:"+this._fileName);
		this._sizeInfText.setText("文件大小: "+ ConvertValue.FormetFileSize(this._fileSize));
		this._downloadIcon.setBackgroundResource(AppPcActivity.getIdByEx(_fileName));

		// 开始下载；
		if (detectSdStateAndCreatePath() != null) {
			Log.v(TAG, detectSdStateAndCreatePath());
			this._savePath = detectSdStateAndCreatePath();
			this._saveName = this._fileName;
			DownloadTask downloadTask = new DownloadTask(Config.Ip, Config.Port,
					detectSdStateAndCreatePath(),
					"DOWNLOAD|" + _filePath + "|", this._fileName,
					this._fileSize);
			downloadTask.executeOnExecutor(Executors.newSingleThreadExecutor());// 一个接一个执行的的线程池
		} else {
			Log.v(TAG, "FilePath:" + this._filePath + " FileSize:"
					+ this._fileSize);
		}
	}

	/* 下载的异步任务线程 */
	class DownloadTask extends AsyncTask<Void, Integer, Void> {
		private String _ip;
		private String _downloadPath;
		private String _savePath;
		private String _fileName;
		private long _fileSize;
		private int _port;

		/* 初始化该异步任务参数 */
		public DownloadTask(String ip, int port, String savePath,
							String downloadPath, String fileName, long fileSize) {
			this._ip = ip;
			this._port = port;
			this._savePath = savePath;
			this._downloadPath = downloadPath;
			this._fileSize = fileSize;
			this._fileName = fileName;
		}

		/* 取消该异步任务 */
		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		/* 后台运行，此处负责与Pc进行网络通讯 */
		@Override
		protected Void doInBackground(Void... params) {
			DownloadThread.start(this._ip, this._port, this._downloadPath,
					this._savePath, this._fileName, this._fileSize,
					new DownloadThread.updataPrograss() {
						@Override
						public void updata(int index) {
							// TODO Auto-generated method stub
							_waveViewProgress.setProgress(index);
						}

					});
			return null;
		}

		/* 处理完成 */
		@Override
		protected void onPostExecute(Void result) {
			_waveViewProgress.setProgress(100);
			_fileNameText.setText("下载完成，点击打开!");
			_sizeInfText.setText("保存路径: "+_savePath  + _fileName);
			onChangeAnim(_downloadIcon);
			_downloadIcon.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					try {
						onChangeAnim(_downloadIcon);
						File file = new File(_savePath + "/" + _fileName);
						OpenFile.openFile(_context, file);
					} catch (Exception e) {
						Toast.makeText(_context, "无法打开！", Toast.LENGTH_SHORT)
								.show();
					}
				}
			});
		}

		/* 点击动画 */
		public void onChangeAnim(final ImageView imageView) {
			final Rotate3dHelper SendEmilAnim = new Rotate3dHelper(imageView);// 设置动画；
			SendEmilAnim.Rotate3dByX(200, 0, 180, 100, true,
					new Rotate3dHelper.OnAnimOverListener() {
						@Override
						public void AnimOver() {
							imageView.setBackgroundResource(AppPcActivity.getIdByEx(_fileName));
						}
					});

		}
	}

	/* 检测创建文件目录 */
	public String detectSdStateAndCreatePath() {
		String FirstFolder = "Airjoy";// 一级目录
		String SecondFolder = "Download";// 二级目录
		/* ALBUM_PATH取得机器的SD卡位置，File.separator为分隔符“/” */
		String ALBUM_PATH = Environment.getExternalStorageDirectory()
				+ File.separator + FirstFolder + File.separator;
		String Second_PATH = ALBUM_PATH + SecondFolder + File.separator;

		// 检查手机上是否有外部存储卡
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);

		if (!sdCardExist)

		{// 如果不存在SD卡，进行提示

			Toast.makeText(DownloadActivity.this, "请插入外部SD存储卡!",
					Toast.LENGTH_SHORT).show();
			return null;
		} else {// 如果存在SD卡，判断文件夹目录是否存在

			// 一级目录和二级目录必须分开创建
			File dirFirstFile = new File(ALBUM_PATH);// 新建一级主目录

			if (!dirFirstFile.exists()) {// 判断文件夹目录是否存在

				dirFirstFile.mkdir();// 如果不存在则创建

			}
			File dirSecondFile = new File(Second_PATH);// 新建二级主目录

			if (!dirSecondFile.exists()) {// 判断文件夹目录是否存在

				dirSecondFile.mkdir();// 如果不存在则创建

			}
			return "/sdcard/" + FirstFolder + "/" + SecondFolder + "/";
		}

	}

}
