package com.android.airjoy.app.pcfile.net.download.progress.rain;

import java.util.ArrayList;

/***
 * 粒子物理引擎
 *
 * @author zhangjia
 *
 */
public class ParticleThread extends Thread {
	boolean flag;// 线程标识
	ParticleView particalView;
	int sleepSpan = 80;
	double time = 0;
	double span = 0.15;
	private int window_Width;

	public ParticleThread(ParticleView particleView, int window_Width) {
		this.particalView = particleView;
		this.flag = true; // 设置线程执行标志位为true
		this.window_Width = window_Width;
	}

	@Override
	public void run() {
		while (flag) {
			particalView.particleSet.addPartical(5, time, window_Width);// 每次添加5粒
			ArrayList<Particle> particals = particalView.particleSet.particals;// 获取粒子集合
			int count = particals.size();
			for (int i = 0; i < count; i++) {
				Particle partical = particals.get(i);
				double timeSpan = time - partical.startTime;// 计算程序开始到现在的时间差
				int tempX = (int) (partical.startX + partical.horizontal_v
						* timeSpan);// 计算此时粒子的X值
				// 4.9 * timeSpan * timeSpan=1/2*g*t*t;//物理公式，g=9.8m/s;
				int tempY = (int) (partical.startY + 4.9 * timeSpan * timeSpan);// 计算此时粒子的Y值
				if (tempY > ParticleView.DIE_OUT_LINE) {
					particals.remove(partical);// 删除该粒子
					count = particals.size();// 重新获取个数
				}
				partical.x = tempX;
				partical.y = tempY;
			}
			time += span;
			try {
				Thread.sleep(sleepSpan);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
} 