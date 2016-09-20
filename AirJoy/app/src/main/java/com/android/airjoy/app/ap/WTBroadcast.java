package com.android.airjoy.app.ap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

/**
 * AddBean
 */
public class WTBroadcast extends BroadcastReceiver {
	public static ArrayList<EventHandler> ehList = new ArrayList();

	public void onReceive(Context paramContext, Intent paramIntent) {
		if (paramIntent.getAction().equals(
				"android.net.wifi.WIFI_STATE_CHANGED")) {
			for (int j = 0; j < ehList.size(); j++)
				((EventHandler) ehList.get(j)).wifiStatusNotification();
		}
	}

	public static abstract interface EventHandler {
		public abstract void wifiStatusNotification();
	}
}
