package com.android.airjoy.app.pcfile.net.download.progress.rain;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

//继承自SurfaceView并实现SurfaceHolder.Callback接口的类
public class ParticleView extends SurfaceView implements SurfaceHolder.Callback {
	public static int DIE_OUT_LINE = 420;// 粒子的Y坐标超过该值会从粒子集合移除
	DrawThread drawThread; // 后台刷新屏幕线程
	ParticleSet particleSet; // ParticleSet对象引用
	ParticleThread particleThread; // ParticleThread对象引用
	String fps = "FPS:N/A"; // 声明帧速率字符串

	// 构造器，初始化主要成员变量
	public ParticleView(Context context, int window_Width, int window_Height) {
		super(context); // 调用父类构造器
		DIE_OUT_LINE = window_Height;
		this.getHolder().addCallback(this); // 添加Callback接口
		drawThread = new DrawThread(this, getHolder()); // 创建DrawThread对象
		particleSet = new ParticleSet(); // 创建ParticleSet对象
		particleThread = new ParticleThread(this, window_Width); // 创建ParticleThread对象
	}

	// 方法：绘制屏幕
	public void doDraw(Canvas canvas) {
		canvas.drawColor(Color.BLACK); // 清屏
		ArrayList<Particle> particleSet = this.particleSet.particals; // 获得ParticleSet对象中的粒子集合对象
		Paint paint = new Paint(); // 创建画笔对象
		for (int i = 0; i < particleSet.size(); i++) { // 遍历粒子集合，绘制每个粒子
			Particle p = particleSet.get(i);
			paint.setColor(p.color); // 设置画笔颜色为粒子颜色
			int tempX = p.x; // 获得粒子X坐标
			int tempY = p.y; // 获得粒子Y坐标
			int tempRadius = p.r; // 获得粒子半径
			RectF oval = new RectF(tempX, tempY, tempX + 2 * tempRadius, tempY
					+ 2 * tempRadius);
			// canvas.drawOval(oval, paint); // 绘制椭圆粒子
			canvas.drawCircle(tempX, tempY, tempRadius, paint);
		}
		paint.setColor(Color.WHITE); // 设置画笔颜色
		paint.setTextSize(18); // 设置文字大小
		paint.setAntiAlias(true); // 设置抗锯齿
		canvas.drawText(fps, 15, 15, paint);// 画出帧速率字符串
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
							   int height) {// 重写surfaceChanged方法
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {// 从写surfaceCreated方法
		if (!drawThread.isAlive()) { // 如果DrawThread没有启动，就启动这个线程
			drawThread.start();
		}
		if (!particleThread.isAlive()) { // 如果ParticleThread没有启动，就启动这个线程
			particleThread.start();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {// 重写surfaceDestroyed方法
		drawThread.flag = false; // 停止线程的执行
		drawThread = null; // 将dt指向的对象声明为垃圾
	}
} 