package com.bigun.wifioscilloscope;

import com.bigun.wifioscilloscope.util.ScreenHelper;

import android.app.Application;
import android.graphics.Point;

public class BigunApp extends Application {
	public static Point screenPoint;
	public static int ProgressW;
	public static float ProgressRealW;
	public static int XTimes = 1;
	public static int MAIN_X;
	public static int MAIN_H;
	public static Boolean isUpdataT = true;
	public static Boolean isFullPoint = true;

	public static Boolean isRestart = false;
	public static int startIndex = 0;
	// »¬¶¯²ÎÊý
	public static float SLIDE_X = 0;

	@Override
	public void onCreate() {
		super.onCreate();
		screenPoint = ScreenHelper.appSize(this);
	}

}
