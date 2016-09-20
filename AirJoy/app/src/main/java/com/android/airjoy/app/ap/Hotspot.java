package com.android.airjoy.app.ap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.ArrayList;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
/**
 * 热点操作类
 *
 * @author 贾豆
 *
 */
public  class Hotspot {
	public static WifiManager wifiManager;
	public boolean flag = false;
	/* 关闭热点 */
	public static  void stopHotspot(Context context) {
		wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		wifiManager.setWifiEnabled(false); // 关闭wifi;
	}

	/* 打开热点 */
	public static  boolean setWifiApEnabled(Context context,boolean enabled,String _preSharedKey,String _ssid) {
		wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if (enabled) {
			wifiManager.setWifiEnabled(false); // 关闭wifi;
		}
		try {
			WifiConfiguration apConfig = new WifiConfiguration(); // 热点的配置类
			apConfig.SSID = _ssid; // 配置热点的名称(可以在名字后面加点随机数什么的)
			apConfig.preSharedKey = _preSharedKey; // 配置热点的密码;
			Method method = wifiManager.getClass().getMethod( // 通过反射调用设置热点
					"setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);
			return (Boolean) method.invoke(wifiManager, apConfig, enabled); // 返回热点打开状态
		} catch (Exception e) {
			return false;
		}
	}


	/* 通过读arp获取当前连接的用户列表 */
	public static  ArrayList<String> getConnectedIP() {
		ArrayList<String> connectedIP = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					"/proc/net/arp"));
			String line;
			while ((line = br.readLine()) != null) {
				String[] splitted = line.split(" +");
				if (splitted != null && splitted.length >= 4) {
					String ip = splitted[0];
					connectedIP.add(ip);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connectedIP;
	}

	/* 获取wifi名称信息 */
	public static  String getWifiInf(Context context)
	{
		WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		int wifiState = wifiMgr.getWifiState();
		WifiInfo info = wifiMgr.getConnectionInfo();
		String wifiId = info != null ? info.getSSID() : null;
		if (wifiState != WifiManager.WIFI_STATE_ENABLED)
		{
			return "无连接";
		}else if(wifiState == WifiManager.WIFI_STATE_DISABLED){
			return "WifiDisable";
		}
		return wifiId;
	}

	/* 获取wifiAp状态*/
	public static boolean getWifiApState(Context context) {
		try {
			WifiManager localWifiManager = (WifiManager) context.getSystemService("wifi");
			int i = ((Integer) localWifiManager.getClass()
					.getMethod("getWifiApState", new Class[0])
					.invoke(localWifiManager, new Object[0])).intValue();
			return (3 == i) || (13 == i);
		} catch (Exception localException) {
		}
		return false;
	}
}
