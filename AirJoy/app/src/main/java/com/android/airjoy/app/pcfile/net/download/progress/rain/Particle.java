package com.android.airjoy.app.pcfile.net.download.progress.rain;

/***
 * 粒子对象
 *
 * @author zhangjia
 *
 */
public class Particle {
	int color; // 颜色
	int r;// 半径
	double vertical_v;// 垂直速度
	double horizontal_v;// 水平速度
	int startX;
	int startY;
	int x;
	int y;
	double startTime;// 起始时间

	public Particle(int color, int r, double vertical_v, double horizontal_v,
					int x, int y, double startTime) {
		super();
		this.color = color;
		this.r = r;
		this.vertical_v = vertical_v;
		this.horizontal_v = horizontal_v;
		this.startX = x;
		this.startY = y;
		this.x = x;
		this.y = y;
		this.startTime = startTime;
	}

} 