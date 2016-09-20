package com.android.airjoy.app.pcfile.net.download.progress.rain;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/***
 * 用于重绘屏幕和计算帧率
 *
 * @author zhangjia
 *
 */
public class DrawThread extends Thread {
	ParticleView particalView;// 自定义View
	SurfaceHolder surfaceHolder;
	boolean flag = false;// 线程标识
	int sleepSpan = 15;// 线程休眠
	long start = System.nanoTime();// 其实时间，用于计算帧速率
	int count = 0;// 计算帧率

	public DrawThread(ParticleView particalView, SurfaceHolder surfaceHolder) {
		super();
		this.particalView = particalView;
		this.surfaceHolder = surfaceHolder;
		this.flag = true;
	}

	@Override
	public void run() {
		Canvas canvas = null;
		while (flag) {
			try {
				canvas = surfaceHolder.lockCanvas();// 获取canvas.
				synchronized (surfaceHolder) {
					particalView.doDraw(canvas);// 进行绘制ballView.

				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (canvas != null) {
					surfaceHolder.unlockCanvasAndPost(canvas);// 解锁
				}
			}
			this.count++;
			if (count == 20) { // 如果计满20帧
				count = 0; // 清空计数器
				long tempStamp = System.nanoTime();// 获取当前时间
				long span = tempStamp - start; // 获取时间间隔
				start = tempStamp; // 为start重新赋值
				double fps = Math.round(100000000000.0 / span * 20) / 100.0;// 计算帧速率
				particalView.fps = "FPS:" + fps;// 将计算出的帧速率设置到BallView的相应字符串对象中
			}
			try {
				Thread.sleep(sleepSpan); // 线程休眠一段时间
			} catch (Exception e) {
				e.printStackTrace(); // 捕获并打印异常
			}
		}
	}

} 
