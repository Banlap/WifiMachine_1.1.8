package com.bigun.wifioscilloscope.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

public class NetUtil {
	static TextView tv;

	/**
	 * 判断是否有网络
	 * 
	 * @param context
	 * @return boolean
	 */
	public static boolean checkNetworkState(Context context) {
		boolean flag = false;
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager != null && manager.getActiveNetworkInfo() != null) {
			flag = manager.getActiveNetworkInfo().isAvailable();
		}
		return flag;
	}

	public static String getIp(Context context) {
		// 获取wifi服务
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		// 判断wifi是否开启
		if (!wifiManager.isWifiEnabled()) {
			wifiManager.setWifiEnabled(true);
		}
		// 261623980
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();
		String ip = intToIp(ipAddress);
		return ip;
	}

	private static String intToIp(int i) {

		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
				+ "." + (i >> 24 & 0xFF);
	}

	/**
	 * 网络延时
	 * @param handler 
	 */
	public static void updateNetDelay(final Context context, final Handler handler) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				String delay = new String();
				Process p;
				try {
					p = Runtime.getRuntime()
							.exec("ping -c 4 " + getIp(context));

					BufferedReader buf = new BufferedReader(
							new InputStreamReader(p.getInputStream()));
					String str = new String();
					while ((str = buf.readLine()) != null) {
						if (str.contains("avg")) {
							int i = str.indexOf("/", 20);
							int j = str.indexOf(".", i);
							delay = str.substring(i + 1, j);
							delay = "延迟:" + delay + "ms";
//							System.out.println("延时："+delay);
							Message msg = Message.obtain(handler);
							msg.what = 10;
							msg.obj = delay;
							msg.sendToTarget();
						}

					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, 0, 2000);

	}

}
