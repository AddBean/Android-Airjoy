package com.android.airjoy.app.phone;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;

import com.android.airjoy.R;
import com.android.airjoy.constant.Config;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
/**
 * Created by AddBean on 2016/3/28.
 */
public class SnapService extends Service {
	private static final String TAG = "SnapService";
	private static boolean bThreadRunning = true;
	private static Context _context;
	private final IBinder binder = new MyBinder();
	private static Notification notification;
	private static int time=200;
	public String Msg;
	private byte byteBuffer[] = new byte[1024];
	private OutputStream outsocket;
	public class MyBinder extends Binder {
		SnapService getService() {
			return SnapService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	@Override
	public void onCreate() {
		CreateInform();
		_context = this;

	}

	@Override
	public void onDestroy() {
		bThreadRunning = false;
		NotificationManager nm = (NotificationManager) getApplicationContext()
				.getSystemService(Context.NOTIFICATION_SERVICE);
		nm.cancel(100);
		super.onDestroy();
	}

	/* �������� */
	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stubm
		super.onStart(intent, startId);
		bThreadRunning=true;
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (bThreadRunning) {
					try {
						ScreenCapture(_context);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}).start();
	}

	@SuppressWarnings("deprecation")
	public void CreateInform() {

		Intent intent = new Intent(getApplicationContext(), AppPhone.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(
				getApplicationContext(), 0, intent, 0);
		notification = new Notification(R.drawable.ic_launcher, "后台服务开启中……",
				System.currentTimeMillis());
		notification.setLatestEventInfo(getApplicationContext(), "电脑助手后台服务",
				"正在进行屏幕录制同步服务……", pendingIntent);
		notification.flags = Notification.FLAG_NO_CLEAR;
		NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		nManager.notify(100, notification);
	}

	public void ScreenCapture(Context context) throws IOException {
		Log.i(TAG, "ScreenCapture");
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager WM = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = WM.getDefaultDisplay();
		display.getMetrics(metrics);
		int height = metrics.heightPixels;
		int width = metrics.widthPixels;
		int pixelformat = display.getPixelFormat();
		PixelFormat localPixelFormat1 = new PixelFormat();
		PixelFormat.getPixelFormatInfo(pixelformat, localPixelFormat1);
		int deepth = localPixelFormat1.bytesPerPixel;
		File mydir = new File("/dev/graphics/");
		File fbfile = new File(mydir, "fb0");
		FileInputStream inStream = new FileInputStream(fbfile);
		byte[] piex = new byte[height * width * deepth];
		DataInputStream dStream = new DataInputStream(inStream);
		int[] colors = new int[height * width];
		if (dStream.read(piex, 0, height * width * deepth) != -1) {
			for (int m = 0; m < piex.length; m++) {
				if (m % 4 == 0) {
					int r = (piex[m] & 0xFF);
					int g = (piex[m + 1] & 0xFF);
					int b = (piex[m + 2] & 0xFF);
					int a = (piex[m + 3] & 0xFF);
					colors[m / 4] = (a << 24) + (r << 16) + (g << 8) + b;
				}
			}
			Bitmap bitmap = Bitmap.createBitmap(colors, width, height,
					Bitmap.Config.ARGB_8888);
			try {
				if (bitmap != null) {
					ByteArrayOutputStream outstream = new ByteArrayOutputStream();
					bitmap.compress(Bitmap.CompressFormat.JPEG, 20, outstream);
					outstream.flush();
					sendToPc(outstream);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void sendToPc(ByteArrayOutputStream stream){
		try{
			Socket tempSocket = new Socket(Config.Ip, Config.Port);
			outsocket = tempSocket.getOutputStream();
			String msg=null;
			msg=java.net.URLEncoder.encode("PHONESCREEN|"+"jiadou"+"|","utf-8");
			byte[] buffer= msg.getBytes();
			outsocket.write(buffer);
			ByteArrayInputStream inputstream = new ByteArrayInputStream(stream.toByteArray());
			int amount;
			while ((amount = inputstream.read(byteBuffer)) != -1) {
				outsocket.write(byteBuffer, 0, amount);
			}
			stream.flush();
			stream.close();
			tempSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
