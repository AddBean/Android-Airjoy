package com.android.airjoy.app.pcfile.net.download;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

import android.R.integer;
import android.util.Log;
/**
 * 下载线程类；
 *
 * @author 贾豆
 *
 */
public class DownloadThread {
	private static DownloadSocket cs = null;
	private static  updataPrograss _updataPrograss;
	private static int _bufferSize=8192;//8k
	/* 开始下载 */
	public static void start(String ip, int port, String downloadPath,
							 String savePath,String fileName,long fileLength,updataPrograss updataPrograss) {
		_updataPrograss=updataPrograss;
		if (createConnection(ip, port)) {
			sendFileInf(downloadPath);
			downloadFile(savePath,fileLength,fileName);
		}
	}

	/* 下载文件:1,获取文件的长度；2，接受文件 */
	private static void downloadFile(String path,long fileLength,String fileName) {
		if (cs == null)
			return;
		DataInputStream inputStream = null;
		inputStream = cs.getDownloadStream();
		try {
			String savePath = path;
			int bufferSize = _bufferSize;
			byte[] buf = new byte[bufferSize];
			long passedlen = 0;
			DataOutputStream outputStream = new DataOutputStream(
					new BufferedOutputStream(new BufferedOutputStream(
							new FileOutputStream(savePath+fileName))));
			while (true) {
				int read = 0;
				if (inputStream != null) {
					read = inputStream.read(buf);
				}
				passedlen += read;
				if (passedlen == fileLength) {
					Log.d("Download", " 文件接收完成-1" +fileLength);
					outputStream.write(buf, 0, read);
					break;
				}
				//Log.d("Download", " 文件接收完成:" +(int)(passedlen * 100 / fileLength));
				outputStream.write(buf, 0, read);
				_updataPrograss.updata((int)(passedlen * 100 / fileLength));//回调更新进度条；
			}
			Log.d("Download", " 文件接收完成-2");
			outputStream.flush();
			outputStream.close();
		} catch (IOException e) {
			Log.d("Download", " 出错：" + e);
			e.printStackTrace();
		}finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
				cs.shutDownConnection();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	/* 发送文件信息，文件路径 */
	private static void sendFileInf(String fileInf) {
		if (cs == null)
			return;
		cs.sendFileInf(fileInf);
	}

	/* 创建socket连接 */
	public static boolean createConnection(String ip, int port) {
		cs = new DownloadSocket(ip, port);
		cs.createConnection();
		Log.d("Download", "连接服务器成功:");
		return true;
	}

	/* 回调函数接口*/
	public interface updataPrograss{
		public void updata(int index);
	}
}
