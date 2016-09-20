package com.android.airjoy.app.pcfile.net.download;
import java.io.BufferedInputStream;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;
/**
 * socket访问pc公共类
 *
 * @author 贾豆
 *
 */
public class DownloadSocket {
	private String ip;
	private int port;
	private Socket socket = null;
	DataOutputStream out = null;
	DataInputStream getDownloadStream = null;

	/* 构造函数*/
	public DownloadSocket(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	/* 创建socket链接*/
	public void createConnection() {
		try {
			socket = new Socket(ip, port);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		} finally {
		}
	}

	/* 发送要下载文件的信息*/
	public void sendFileInf(String fileInf) {
		try {
			out = new DataOutputStream(socket.getOutputStream());
			byte[] buffer= fileInf.getBytes();
			out.write(buffer);
//				out.writeUTF(fileInf);
//				out.flush();
			Log.v("inf", fileInf);
			return;
		} catch (IOException e) {
			e.printStackTrace();
			if (out != null) {
				try {
					out.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	/* 获取下载文件流*/
	public DataInputStream getDownloadStream() {
		try {
			getDownloadStream = new DataInputStream(new BufferedInputStream(
					socket.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (getDownloadStream != null) {
				try {
					getDownloadStream.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return getDownloadStream;
	}

	/* 关闭当前链接*/
	public void shutDownConnection() {
		try {
			if (out != null) {
				out.close();
			}
			if (getDownloadStream != null) {
				getDownloadStream.close();
			}
			if (socket != null) {
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
